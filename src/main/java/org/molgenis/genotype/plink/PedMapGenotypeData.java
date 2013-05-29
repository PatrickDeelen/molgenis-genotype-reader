package org.molgenis.genotype.plink;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.samtools.util.BlockCompressedInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.GenotypeDataIndex;
import org.molgenis.genotype.IndexedGenotypeData;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.VariantAlleles;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.tabix.TabixIndex;
import org.molgenis.genotype.tabix.TabixSequence;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SampleVariantsProvider;
import org.molgenis.genotype.variant.SnpGeneticVariant;
import org.molgenis.util.plink.datatypes.Biallele;
import org.molgenis.util.plink.datatypes.MapEntry;
import org.molgenis.util.plink.datatypes.PedEntry;
import org.molgenis.util.plink.drivers.PedFileDriver;
import org.molgenis.util.plink.readers.MapFileReader;

public class PedMapGenotypeData extends IndexedGenotypeData implements SampleVariantsProvider
{
	public static final String FATHER_SAMPLE_ANNOTATION_NAME = "father";
	public static final String MOTHER_SAMPLE_ANNOTATION_NAME = "mother";
	public static final String SEX_SAMPLE_ANNOTATION_NAME = "sex";
	public static final String PHENOTYPE_SAMPLE_ANNOTATION_NAME = "phenotype";

	private static final char NULL_VALUE = '0';
	private static final Logger LOG = Logger.getLogger(PedMapGenotypeData.class);
	private final GenotypeDataIndex dataIndex;
	private final File pedFile;
	private Map<Integer, List<Biallele>> sampleAllelesBySnpIndex = new HashMap<Integer, List<Biallele>>();
	private List<GeneticVariant> snps = new ArrayList<GeneticVariant>(1000000);
	private Map<String, GeneticVariant> snpById = new HashMap<String, GeneticVariant>(1000000);
	private Map<String, Integer> snpIndexById = new HashMap<String, Integer>(1000000);

	public PedMapGenotypeData(File bzipMapFile, File mapIndexFile, File pedFile) throws FileNotFoundException,
			IOException
	{

		if (!bzipMapFile.isFile())
		{
			throw new FileNotFoundException("MAP file not found at " + bzipMapFile.getAbsolutePath());
		}

		if (!bzipMapFile.canRead())
		{
			throw new IOException("MAP file not found at " + bzipMapFile.getAbsolutePath());
		}

		if (!mapIndexFile.isFile())
		{
			throw new FileNotFoundException("MAP index file not found at " + mapIndexFile.getAbsolutePath());
		}

		if (!mapIndexFile.canRead())
		{
			throw new IOException("MAP index file not found at " + mapIndexFile.getAbsolutePath());
		}

		if (!pedFile.isFile())
		{
			throw new FileNotFoundException("PED file not found at " + pedFile.getAbsolutePath());
		}

		if (!pedFile.canRead())
		{
			throw new IOException("PED file not found at " + pedFile.getAbsolutePath());
		}

		this.pedFile = pedFile;

		MapFileReader mapFileReader = null;
		PedFileDriver pedFileDriver = null;
		try
		{
			pedFileDriver = new PedFileDriver(pedFile);
			loadSampleBialleles(pedFileDriver);

			mapFileReader = new MapFileReader(new BlockCompressedInputStream(bzipMapFile));
			loadSnps(mapFileReader);
			dataIndex = new TabixIndex(mapIndexFile, bzipMapFile, new PedMapVariantLineMapper(this));
		}
		catch (IOException e)
		{
			throw new GenotypeDataException("IOException creating TabixIndex", e);
		}
		finally
		{
			IOUtils.closeQuietly(pedFileDriver);
			IOUtils.closeQuietly(mapFileReader);
		}

	}

	private void loadSampleBialleles(PedFileDriver pedFileDriver)
	{
		int count = 0;
		for (PedEntry entry : pedFileDriver)
		{
			int index = 0;
			for (Biallele biallele : entry)
			{
				List<Biallele> biallelesForSnp = sampleAllelesBySnpIndex.get(index);
				if (biallelesForSnp == null)
				{
					biallelesForSnp = new ArrayList<Biallele>();
					sampleAllelesBySnpIndex.put(index, biallelesForSnp);
				}

				biallelesForSnp.add(biallele);
				index++;
			}

			LOG.info("Loaded [" + (++count) + "] samples");
		}

		LOG.info("Total [" + count + "] samples");
	}

	private void loadSnps(MapFileReader reader)
	{
		int index = 0;
		for (MapEntry entry : reader)
		{
			List<String> ids = Collections.singletonList(entry.getSNP());
			String sequenceName = entry.getChromosome();
			int startPos = (int) entry.getBpPos();
			String refAllele = null;// Unknown for ped/map
			Map<String, ?> annotationValues = Collections.emptyMap();
			List<String> altDescriptions = Collections.emptyList();
			List<String> altTypes = Collections.emptyList();

			List<Biallele> sampleAlleles = sampleAllelesBySnpIndex.get(index);
			List<String> alleles = new ArrayList<String>(2);
			for (Biallele biallele : sampleAlleles)
			{
				String allele1 = biallele.getAllele1() == NULL_VALUE ? null : biallele.getAllele1() + "";
				if ((allele1 != null) && !alleles.contains(allele1))
				{
					alleles.add(allele1);
				}

				String allele2 = biallele.getAllele2() == NULL_VALUE ? null : biallele.getAllele2() + "";
				if ((allele2 != null) && !alleles.contains(allele2))
				{
					alleles.add(allele2);
				}
			}

			GeneticVariant snp = new SnpGeneticVariant(ids, sequenceName, startPos, VariantAlleles.create(alleles),
					refAllele, annotationValues, altDescriptions, altTypes, this);

			snps.add(snp);
			snpById.put(snp.getPrimaryVariantId(), snp);
			snpIndexById.put(snp.getPrimaryVariantId(), index);
			index++;

			if ((index % 1000) == 0)
			{
				LOG.info("Loaded [" + index + "] snps");
			}
		}

		LOG.info("Total [" + index + "] snps");
	}

	@Override
	public List<Sequence> getSequences()
	{
		List<String> seqNames = getSeqNames();

		List<Sequence> sequences = new ArrayList<Sequence>(seqNames.size());
		for (String seqName : seqNames)
		{
			sequences.add(new TabixSequence(seqName, null, dataIndex));
		}

		return sequences;
	}

	@Override
	public List<Sample> getSamples()
	{
		PedFileDriver pedFileDriver = null;

		try
		{
			pedFileDriver = new PedFileDriver(pedFile);
			List<Sample> samples = new ArrayList<Sample>();
			for (PedEntry pedEntry : pedFileDriver)
			{
				Map<String, Object> annotations = new HashMap<String, Object>(4);
				annotations.put(FATHER_SAMPLE_ANNOTATION_NAME, pedEntry.getFather());
				annotations.put(MOTHER_SAMPLE_ANNOTATION_NAME, pedEntry.getMother());
				annotations.put(SEX_SAMPLE_ANNOTATION_NAME, pedEntry.getSex());
				annotations.put(PHENOTYPE_SAMPLE_ANNOTATION_NAME, pedEntry.getPhenotype());

				samples.add(new Sample(pedEntry.getIndividual(), pedEntry.getFamily(), annotations));
			}

			return samples;
		}
		finally
		{
			IOUtils.closeQuietly(pedFileDriver);
		}
	}

	@Override
	public List<GeneticVariant> getVariants()
	{
		return snps;
	}

	@Override
	public GeneticVariant getVariantById(String primaryVariantId)
	{
		return snpById.get(primaryVariantId);
	}

	@Override
	public SnpGeneticVariant getSnpVariantById(String primaryVariantId)
	{
		return (SnpGeneticVariant) getVariantById(primaryVariantId);
	}

	@Override
	public List<VariantAlleles> getSampleVariants(GeneticVariant variant)
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

		List<Biallele> bialleles = sampleAllelesBySnpIndex.get(index);
		List<VariantAlleles> sampleVariants = new ArrayList<VariantAlleles>(bialleles.size());
		for (Biallele biallele : bialleles)
		{
			sampleVariants.add(VariantAlleles.create(biallele.getAllele1(), biallele.getAllele2()));
		}

		return sampleVariants;
	}

	@Override
	protected GenotypeDataIndex getIndex()
	{
		return dataIndex;
	}

	@Override
	protected Map<String, Annotation> getVariantAnnotationsMap()
	{
		return Collections.emptyMap();
	}

	@Override
	public int getVariantCount()
	{
		return snps.size();
	}

}
