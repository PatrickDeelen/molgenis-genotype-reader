package org.molgenis.genotype.variantFilter;

import java.util.HashSet;
import java.util.List;

import org.molgenis.genotype.GenotypeData;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.VariantQueryResult;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SnpGeneticVariant;

public class VariantFilterGenotypeData implements GenotypeData
{

	private final GenotypeData originalGenotypeData;
	private final HashSet<GeneticVariant> includeVariants;
	private final HashSet<GeneticVariant> excludeVariants;

	/**
	 * View of originalGenotypeData with a subset of the genetic variants. Data
	 * is not copied. Modifications in the original data are reflected here
	 * 
	 * If both an include list as an exclude list is specified the exclude list
	 * takes presidency. i.e. other words all variants in the exclude list are
	 * removed from the include list. Only variants remaining in the include
	 * list will be visible via this view.
	 * 
	 * Either include or exclude must other than null.
	 * 
	 * @param originalGenotypeData
	 * @param includeVariants
	 *            list with variants to include. If null all but the excluded
	 *            variants are selected
	 * @param excludeVariants
	 *            list with variants to exclude. Can be null
	 */
	public VariantFilterGenotypeData(GenotypeData originalGenotypeData, HashSet<GeneticVariant> includeVariants,
			HashSet<GeneticVariant> excludeVariants)
	{
		super();

		if (includeVariants == null && excludeVariants == null)
		{
			throw new IllegalArgumentException("Include and exclude list are null for variantFilterGenotypeData");
		}

		this.originalGenotypeData = originalGenotypeData;
		this.includeVariants = includeVariants;
		this.excludeVariants = excludeVariants;
	}

	@Override
	public List<String> getSeqNames()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Sequence> getSequences()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sequence getSequenceByName(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Annotation> getVariantAnnotations()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Annotation getVariantAnnotation(String annotationId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GeneticVariant> getVariantsByPos(String seqName, int startPos)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SnpGeneticVariant getSnpVariantByPos(String seqName, int startPos)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariantQueryResult getSeqVariants(String seqName)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> getSamples()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<GeneticVariant> getVariants()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneticVariant getVariantById(String primaryVariantId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneticVariant getSnpVariantById(String primaryVariantId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getVariantCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
