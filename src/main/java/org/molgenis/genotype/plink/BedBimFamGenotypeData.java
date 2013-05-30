package org.molgenis.genotype.plink;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.molgenis.genotype.AbstractRandomAccessGenotypeData;
import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.SimpleSequence;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.plink.datatypes.Biallele;
import org.molgenis.genotype.plink.datatypes.MapEntry;
import org.molgenis.genotype.plink.datatypes.PedEntry;
import org.molgenis.genotype.plink.drivers.PedFileDriver;
import org.molgenis.genotype.plink.readers.MapFileReader;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.ReadOnlyGeneticVariant;
import org.molgenis.genotype.variant.SampleVariantsProvider;

public class BedBimFamGenotypeData extends AbstractRandomAccessGenotypeData implements SampleVariantsProvider
{
	public static final String FATHER_SAMPLE_ANNOTATION_NAME = "father";
	public static final String MOTHER_SAMPLE_ANNOTATION_NAME = "mother";
	public static final String SEX_SAMPLE_ANNOTATION_NAME = "sex";
	public static final String PHENOTYPE_SAMPLE_ANNOTATION_NAME = "phenotype";
	private static final char NULL_VALUE = '0';
	private static final Logger LOG = Logger.getLogger(BedBimFamGenotypeData.class);

	private final File pedFile = null;
	private Map<Integer, List<Biallele>> sampleAllelesBySnpIndex = new HashMap<Integer, List<Biallele>>();

	private List<GeneticVariant> snps = new ArrayList<GeneticVariant>(1000000);
	private Map<String, Integer> snpIndexById = new HashMap<String, Integer>(1000000);
	private Map<String, List<GeneticVariant>> snpBySequence = new TreeMap<String, List<GeneticVariant>>();

	public BedBimFamGenotypeData(File bedFile, File bimFile, File famFile) throws FileNotFoundException, IOException
	{
	
	}

	@Override
	public List<Sequence> getSequences()
	{
		return null;
	}

	@Override
	public List<Sample> getSamples()
	{
		return null;
	}

	@Override
	public List<Alleles> getSampleVariants(GeneticVariant variant)

	{
		return null;
	}

	@Override
	protected Map<String, Annotation> getVariantAnnotationsMap()
	{
		return null;
	}

	@Override
	public List<String> getSeqNames()
	{
		return null;
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
	public Iterator<GeneticVariant> getSequenceGeneticVariants(String seqName)
	{
		return null;
	}
}
