package org.molgenis.genotype.variant;

import java.util.List;

import org.molgenis.genotype.Alleles;
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
	private final Cache<GeneticVariantOld, List<Alleles>> cache;

	public CachedSampleVariantProvider(SampleVariantsProvider sampleVariantProvider, int cacheSize)
	{
		this.sampleVariantProvider = sampleVariantProvider;
		this.cache = new Cache<GeneticVariantOld, List<Alleles>>(cacheSize);
	}

	@Override
	public List<Alleles> getSampleVariants(GeneticVariantOld variant)
	{
		if (cache.containsKey(variant))
		{
			return cache.get(variant);
		}
		else
		{
			List<Alleles> variantAlleles = sampleVariantProvider.getSampleVariants(variant);
			cache.put(variant, variantAlleles);
			return variantAlleles;
		}
	}

}
