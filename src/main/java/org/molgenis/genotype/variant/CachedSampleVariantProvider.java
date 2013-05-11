package org.molgenis.genotype.variant;

import java.util.List;

import org.molgenis.genotype.VariantAlleles;
import org.molgenis.genotype.util.Cache;

/**
 * Cached sample variant provider to prevent reloading a SNPs that is accessed
 * multiple times in a sort periode.
 * 
 * @author Patrick Deelen
 * 
 */
public class CachedSampleVariantProvider implements SampleVariantsProvider
{

	private final SampleVariantsProvider sampleVariantProvider;
	private final Cache<GeneticVariant, List<VariantAlleles>> cache;

	public CachedSampleVariantProvider(SampleVariantsProvider sampleVariantProvider, int cacheSize)
	{
		this.sampleVariantProvider = sampleVariantProvider;
		this.cache = new Cache<GeneticVariant, List<VariantAlleles>>(cacheSize);
	}

	@Override
	public List<VariantAlleles> getSampleVariants(GeneticVariant variant)
	{
		if (cache.containsKey(variant))
		{
			return cache.get(variant);
		}
		else
		{
			List<VariantAlleles> variantAlleles = sampleVariantProvider.getSampleVariants(variant);
			cache.put(variant, variantAlleles);
			return variantAlleles;
		}
	}

}
