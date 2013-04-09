package org.molgenis.genotype;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.util.Utils;

public class VariantAlleles
{
	private static Map<List<String>, VariantAlleles> cache = new HashMap<List<String>, VariantAlleles>();
	private List<String> alleles;
	private boolean snp;

	private VariantAlleles(List<String> alleles, boolean snp)
	{
		this.alleles = alleles;
		this.snp = snp;
		cache.put(alleles, this);
	}

	public static VariantAlleles create(List<String> alleles)
	{
		VariantAlleles variantAlleles = cache.get(alleles);
		if (variantAlleles == null)
		{
			variantAlleles = new VariantAlleles(alleles, Utils.isSnp(alleles));
			cache.put(alleles, variantAlleles);
		}

		return variantAlleles;
	}

	public static VariantAlleles create(char allele1, char allele2)
	{
		String strAllele1 = allele1 == '0' ? null : String.valueOf(allele1);
		String strAllele2 = allele2 == '0' ? null : String.valueOf(allele2);

		return create(Arrays.asList(strAllele1, strAllele2));
	}

	/**
	 * List of the posible alleles, can contain null if not known!!!!!
	 * 
	 * @return
	 */
	public List<String> getAlleles()
	{
		return alleles;
	}

	public boolean isSnp()
	{
		return snp;
	}

	public char[] getAllelesAsChars()
	{
		if (!isSnp())
		{
			throw new RuntimeException("Not a snp");
		}

		return new char[]
		{ alleles.get(0).charAt(0), alleles.get(1).charAt(0) };
	}

	@Override
	public String toString()
	{
		return "VariantAlleles [alleles=" + alleles + ", snp=" + snp + "]";
	}

}
