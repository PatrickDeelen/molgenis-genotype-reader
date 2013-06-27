package org.molgenis.genotype.util;

import org.molgenis.genotype.variant.GeneticVariant;

public class LdCalculatorCache
{

	private final Cache<VariantPiar, Ld> ldCache;

	public LdCalculatorCache()
	{
		ldCache = new Cache<LdCalculatorCache.VariantPiar, Ld>(10000);
	}

	public void addToCache(GeneticVariant variant1, GeneticVariant variant2, Ld ld)
	{
		ldCache.put(new VariantPiar(variant1, variant2), ld);
	}

	public Ld getFromCache(GeneticVariant variant1, GeneticVariant variant2)
	{
		return ldCache.get(new VariantPiar(variant1, variant2));
	}

	public static class VariantPiar
	{

		private final GeneticVariant variant1;
		private final GeneticVariant variant2;

		public VariantPiar(GeneticVariant variant1, GeneticVariant variant2)
		{
			super();
			this.variant1 = variant1;
			this.variant2 = variant2;
		}

		@Override
		public boolean equals(Object arg0)
		{
			if (variant1.equals(variant1) && variant2.equals(variant2))
			{
				return true;
			}
			else if (variant1.equals(variant2) && variant2.equals(variant1))
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		@Override
		public int hashCode()
		{
			return variant1.hashCode() + variant2.hashCode();
		}

	}

}
