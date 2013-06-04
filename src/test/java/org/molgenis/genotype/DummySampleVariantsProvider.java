package org.molgenis.genotype;

import java.util.List;

import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SampleVariantsProvider;

public class DummySampleVariantsProvider implements SampleVariantsProvider
{

	private final List<Alleles> variantAlleles;

	public DummySampleVariantsProvider(List<Alleles> variantAlleles)
	{
		super();
		this.variantAlleles = variantAlleles;
	}

	@Override
	public List<Alleles> getSampleVariants(GeneticVariant variant)
	{
		return variantAlleles;
	}

	@Override
	public int cacheSize()
	{
		return 0;
	}

	@Override
	public List<Boolean> getSamplePhasing(GeneticVariant variant)
	{
		return null;
	}

}
