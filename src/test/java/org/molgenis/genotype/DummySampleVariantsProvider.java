package org.molgenis.genotype;

import java.util.List;

import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SampleVariantUniqueIdProvider;
import org.molgenis.genotype.variant.SampleVariantsProvider;

public class DummySampleVariantsProvider implements SampleVariantsProvider
{

	private final List<Alleles> variantAlleles;
	private final int sampleVariantProviderUniqueId;

	public DummySampleVariantsProvider(List<Alleles> variantAlleles)
	{
		super();
		this.variantAlleles = variantAlleles;
		sampleVariantProviderUniqueId = SampleVariantUniqueIdProvider.getNextUniqueId();
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
	
	public int getSampleVariantProviderUniqueId()
	{
		return sampleVariantProviderUniqueId;
	}

}
