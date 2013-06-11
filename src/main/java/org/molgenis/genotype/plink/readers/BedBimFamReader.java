package org.molgenis.genotype.plink.readers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.plink.datatypes.Biallele;
import org.molgenis.genotype.plink.datatypes.BimEntry;
import org.molgenis.genotype.plink.datatypes.FamEntry;
import org.molgenis.genotype.plink.drivers.BedFileDriver;
import org.molgenis.genotype.plink.drivers.BimFileDriver;
import org.molgenis.genotype.plink.drivers.FamFileDriver;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.ReadOnlyGeneticVariant;
import org.molgenis.genotype.variant.SampleVariantUniqueIdProvider;
import org.molgenis.genotype.variant.SampleVariantsProvider;

/**
 * Plink binary reader/converter. See:
 * https://github.com/molgenis/molgenis/blob/standalone_tools
 * /src/plinkbintocsv/PlinkbinToCsv.java
 * 
 * @author joeri
 * 
 */
public class BedBimFamReader implements SampleVariantsProvider
{

	private BedFileDriver bedfd;
	private BimFileDriver bimfd;
	private FamFileDriver famfd;

	private long nrOfIndividuals;
	private long nrOfSnps;
	private long nrOfGenotypes;
	private int paddingPerSnp;
	private List<String> individualNames;
	private List<String> snpNames;
	private List<BimEntry> bimEntries;
	private List<FamEntry> famEntries;
	private List<String> sequences; //usually the chromosome numbers, unique within this list
	private HashMap<String, Biallele> snpCoding;
	
	//helper variables
	private Map<String, Integer> snpIndexById = new HashMap<String, Integer>();
	private final int sampleVariantProviderUniqueId;
	
	//first lookup is on sequence (usually chromosome, ie. "chr1" ), second on position (basepair, ie. 9345352)
	private Map<String, Map<Long, Integer>> snpIndexByPosition  = new HashMap<String, Map<Long, Integer>>();
	
	//sample phasing
	private Map<GeneticVariant, List<Boolean>> samplePhasing = new HashMap<GeneticVariant, List<Boolean>>();

	public BedBimFamReader(File bed, File bim, File fam) throws Exception
	{
		bedfd = new BedFileDriver(bed);
		bimfd = new BimFileDriver(bim);
		famfd = new FamFileDriver(fam);

		nrOfIndividuals = famfd.getNrOfElements();
		nrOfSnps = bimfd.getNrOfElements();
		nrOfGenotypes = nrOfIndividuals * nrOfSnps;
		paddingPerSnp = (int) ((bedfd.getNrOfElements() - nrOfGenotypes) / nrOfSnps);
		
		sampleVariantProviderUniqueId = SampleVariantUniqueIdProvider.getNextUniqueId();
	}

	public void setIndividuals() throws Exception
	{
		List<String> individualNames = new ArrayList<String>();
		List<FamEntry> famEntries = famfd.getAllEntries();
		if (famEntries.size() != nrOfIndividuals)
		{
			throw new Exception("Problem with FAM file: scanned number of elements (" + nrOfIndividuals
					+ ") does not match number of parsed elements (" + famEntries.size() + ")");
		}
		for (FamEntry fe : famEntries)
		{
			if (individualNames.contains(fe.getIndividual()))
			{
				throw new Exception("Problem with FAM file: Individual '" + fe.getIndividual() + "' is not unique!");
			}
			individualNames.add(fe.getIndividual());
		}
		this.individualNames = individualNames;
		this.famEntries = famEntries;
	}

	public void setSnps() throws Exception
	{
		nrOfSnps = bimfd.getNrOfElements();
		List<String> snpNames = new ArrayList<String>();
		List<String> uniqueChromosomes = new ArrayList<String>();
		List<BimEntry> bimEntries = bimfd.getAllEntries();
		if (bimEntries.size() != nrOfSnps)
		{
			throw new Exception(
					"Problem with BIM file: scanned number of elements does not match number of parsed elements");
		}
		this.bimEntries = bimEntries;
		snpCoding = new HashMap<String, Biallele>();
		int index = 0;
		for (BimEntry be : bimEntries)
		{
			if (snpCoding.containsKey(be.getSNP()))
			{
				throw new Exception("Problem with BIM file: SNP '" + be.getSNP() + "' is not unique!");
			}
			snpCoding.put(be.getSNP(), be.getBiallele());
			snpNames.add(be.getSNP());
			if(!uniqueChromosomes.contains(be.getChromosome()))
			{
				uniqueChromosomes.add(be.getChromosome());
			}
			snpIndexById.put(be.getSNP(), index);
			
			//add to sequence -> position map, create one if missing for this sequence (or 'chromosome')
			if(snpIndexByPosition.get(be.getChromosome()) == null){
				Map<Long, Integer> mapForThisSequence= new HashMap<Long, Integer>();
				snpIndexByPosition.put(be.getChromosome(), mapForThisSequence);
			}
			snpIndexByPosition.get(be.getChromosome()).put(be.getBpPos(), index);
			
			index++;
		}
		this.snpNames = snpNames;
		this.sequences = uniqueChromosomes;
	}
	
	public List<GeneticVariant> loadVariantsForSequence(String seq)
	{
		List<GeneticVariant> variants = new ArrayList<GeneticVariant>();
		
		int index = 0;
		for(BimEntry entry : this.bimEntries)
		{
			String sequenceName = entry.getChromosome();
			
			if(!sequenceName.equals(seq))
			{
				continue;
			}
			
			String id = entry.getSNP();
			int startPos = (int) entry.getBpPos();
			Biallele allele = bimEntries.get(index).getBiallele();	
			GeneticVariant snp = ReadOnlyGeneticVariant.createSnp(id, startPos, sequenceName, this, allele.getAllele1(), allele.getAllele2());
			variants.add(snp);
			index++;
		}
		
		return variants;
	}

	public void extractGenotypes(File writeTo) throws Exception
	{
		setIndividuals();
		setSnps();

		Writer genotypesOut = new OutputStreamWriter(new FileOutputStream(writeTo), "UTF-8");

		// /header: all individual names
		for (String indvName : individualNames)
		{
			genotypesOut.write("\t" + indvName);
		}
		genotypesOut.write("\n");

		// elements: snp name + genotypes
		int snpCounter = 0;
		for (int genotypeStart = 0; genotypeStart < nrOfGenotypes; genotypeStart += nrOfIndividuals)
		{
			System.out.println((int) ((genotypeStart / (double) nrOfGenotypes) * 100) + "% of genotypes done");

			String snpName = snpNames.get(snpCounter);
			String[] allIndividualsForThisSNP = bedfd.getElements(genotypeStart, genotypeStart + nrOfIndividuals,
					paddingPerSnp, snpCounter);
			String a1 = Character.toString(snpCoding.get(snpName).getAllele1());
			String a2 = Character.toString(snpCoding.get(snpName).getAllele2());
			String hom1 = a1 + a1;
			String hom2 = a2 + a2;
			String hetr = a1 + a2;

			StringBuilder lineOfGenotypesBuilder = new StringBuilder(snpName);
			for (String s : allIndividualsForThisSNP)
			{
				lineOfGenotypesBuilder.append('\t').append(bedfd.convertGenoCoding(s, hom1, hom2, hetr, ""));
			}
			lineOfGenotypesBuilder.append('\n');
			genotypesOut.write(lineOfGenotypesBuilder.toString());

			snpCounter++;
		}
		genotypesOut.close();
	}

	public List<FamEntry> getFamEntries()
	{
		return famEntries;
	}


	public List<String> getSequences()
	{
		return sequences;
	}
	
	public int getSnpIndexByPosition(String seq, long pos)
	{
		return snpIndexByPosition.get(seq).get(pos);
	}

	public static void main(String[] args) throws Exception
	{
		File bed = new File(Biallele.class.getResource("../testfiles/test.bed").getFile());
		File bim = new File(Biallele.class.getResource("../testfiles/test.bim").getFile());
		File fam = new File(Biallele.class.getResource("../testfiles/test.fam").getFile());
		BedBimFamReader bbfr = new BedBimFamReader(bed, bim, fam);
		File out = new File("geno_tmp.txt");
		System.out.println("going to write to: " + out.getAbsolutePath());
		bbfr.extractGenotypes(out);
	}

	@Override
	public List<Alleles> getSampleVariants(GeneticVariant variant)
	{
		if (variant.getPrimaryVariantId() == null)
		{
			throw new IllegalArgumentException("Not a snp, missing primaryVariantId");
		}

		Integer index = snpIndexById.get(variant.getPrimaryVariantId());

		if (index == null)
		{
			throw new IllegalArgumentException("Unknown primaryVariantId [" + variant.getPrimaryVariantId() + "]");
		}
		
		List<Biallele> bialleles = new ArrayList<Biallele>();
		
		try
		{
			String[] allIndividualsForThisSNP = bedfd.getSNPs(index.longValue(), (int)nrOfIndividuals);
			
			String a1 = Character.toString(snpCoding.get(snpNames.get(index)).getAllele1());
			String a2 = Character.toString(snpCoding.get(snpNames.get(index)).getAllele2());

			for(int i = 0; i < this.nrOfIndividuals; i++)
			{
				Biallele b;
				if(allIndividualsForThisSNP[i].equals("00"))
				{
					b = new Biallele(a1, a1);
				}
				else if(allIndividualsForThisSNP[i].equals("01"))
				{
					b = new Biallele(a1, a2);
				}
				else if(allIndividualsForThisSNP[i].equals("11"))
				{
					b = new Biallele(a2, a2);
				}
				else
				{
					b = null;
				}
				bialleles.add(b);
			}
			
		}
		catch (Exception e)
		{
			throw new GenotypeDataException(e);
		}

		List<Alleles> sampleVariants = new ArrayList<Alleles>(bialleles.size());
		for (Biallele biallele : bialleles)
		{
			//weird: first allele 2 then allele 1 ?
			sampleVariants.add(Alleles.createBasedOnChars(biallele.getAllele2(), biallele.getAllele1()));
		}
		return sampleVariants;
	}

	@Override
	public int cacheSize()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSampleVariantProviderUniqueId()
	{
		return sampleVariantProviderUniqueId;
	}

	@Override
	public List<Boolean> getSamplePhasing(GeneticVariant variant)
	{
		if (samplePhasing.containsKey(variant))
		{
			return samplePhasing.get(variant);
		}

		List<Boolean> phasing = Collections.nCopies(getSampleVariants(variant).size(), false);
		samplePhasing.put(variant, phasing);
		return phasing;
	}

	public List<GeneticVariant> loadVariantsForIndex(int index)
	{
		BimEntry be = bimEntries.get(index);
		List<GeneticVariant> variants = new ArrayList<GeneticVariant>();
		Biallele allele = be.getBiallele();	
		GeneticVariant snp = ReadOnlyGeneticVariant.createSnp(be.getSNP(), (int) be.getBpPos(), be.getChromosome(), this, allele.getAllele1(), allele.getAllele2());
		variants.add(snp);
		return variants;
	}
}
