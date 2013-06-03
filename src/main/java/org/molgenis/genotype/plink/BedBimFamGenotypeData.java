package org.molgenis.genotype.plink;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.molgenis.genotype.AbstractRandomAccessGenotypeData;
import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.SimpleSequence;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.plink.datatypes.Biallele;
import org.molgenis.genotype.plink.datatypes.FamEntry;
import org.molgenis.genotype.plink.readers.BedBimFamReader;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SampleVariantsProvider;

public class BedBimFamGenotypeData extends AbstractRandomAccessGenotypeData
{
	public static final String FATHER_SAMPLE_ANNOTATION_NAME = "father";
	public static final String MOTHER_SAMPLE_ANNOTATION_NAME = "mother";
	public static final String SEX_SAMPLE_ANNOTATION_NAME = "sex";
	public static final String PHENOTYPE_SAMPLE_ANNOTATION_NAME = "phenotype";
	private static final char NULL_VALUE = '0';
	private static final Logger LOG = Logger.getLogger(BedBimFamGenotypeData.class);
	private final BedBimFamReader reader;

	public BedBimFamGenotypeData(File bedFile, File bimFile, File famFile) throws Exception
	{

		if (bedFile == null) throw new IllegalArgumentException("BedFile is null");
		if (bimFile == null) throw new IllegalArgumentException("BimFile is null");
		if (famFile == null) throw new IllegalArgumentException("FamFile is null");
		
		if (!bedFile.isFile()) throw new FileNotFoundException("BED index file not found at " + bedFile.getAbsolutePath());
		if (!bedFile.canRead()) throw new IOException("BED index file not found at " + bedFile.getAbsolutePath());
		
		if (!bimFile.isFile()) throw new FileNotFoundException("BIM file not found at " + bimFile.getAbsolutePath());
		if (!bimFile.canRead()) throw new IOException("BIM file not found at " + bimFile.getAbsolutePath());

		if (!famFile.isFile()) throw new FileNotFoundException("FAM file not found at " + famFile.getAbsolutePath());
		if (!famFile.canRead()) throw new IOException("FAM file not found at " + famFile.getAbsolutePath());
		
		this.reader = new BedBimFamReader(bedFile, bimFile, famFile);
		
		reader.setIndividuals();
		reader.setSnps();
	}

	@Override
	public List<Sequence> getSequences()
	{
		List<String> seqNames = getSeqNames();
		
		List<Sequence> sequences = new ArrayList<Sequence>(seqNames.size());
		for (String seqName : seqNames)
		{
			sequences.add(new SimpleSequence(seqName, null, this));
		}
		
		return sequences;
	}

	@Override
	public List<Sample> getSamples()
	{
		List<Sample> samples = new ArrayList<Sample>();
		for (FamEntry famEntry : reader.getFamEntries())
		{
			Map<String, Object> annotations = new HashMap<String, Object>(4);
			annotations.put(FATHER_SAMPLE_ANNOTATION_NAME, famEntry.getFather());
			annotations.put(MOTHER_SAMPLE_ANNOTATION_NAME, famEntry.getMother());
			annotations.put(SEX_SAMPLE_ANNOTATION_NAME, famEntry.getSex());
			annotations.put(PHENOTYPE_SAMPLE_ANNOTATION_NAME, famEntry.getPhenotype());
			samples.add(new Sample(famEntry.getIndividual(), famEntry.getFamily(), annotations));
		}
		return samples;
	}

	@Override
	protected Map<String, Annotation> getVariantAnnotationsMap()
	{
		return null;
	}

	@Override
	public List<String> getSeqNames()
	{
		return this.reader.getSequences();
	}

	@Override
	public List<GeneticVariant> getVariantsByPos(String seqName, int startPos)
	{
		return null;
	}

	@Override
	public Iterator<GeneticVariant> iterator()
	{
		return null;
	}
	
	@Override
	public Iterable<GeneticVariant> getSequenceGeneticVariants(String seqName)
	{
		List<GeneticVariant> variants = reader.loadVariantsForSequence(seqName);
		if (variants == null)
		{
			throw new IllegalArgumentException("Unknown sequence [" + seqName + "]");
		}
		return variants;
	}

}
