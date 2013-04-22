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
	private char[] allelesAsChar;
	private boolean snp;

	private VariantAlleles(List<String> alleles, char[] allelesAsChar, boolean snp)
	{
		this.alleles = alleles;
		this.allelesAsChar = allelesAsChar;
		this.snp = snp;
	}

	public static VariantAlleles create(List<String> alleles)
	{
		VariantAlleles variantAlleles = cache.get(alleles);
		if (variantAlleles == null)
		{
			if (Utils.isSnp(alleles))
			{
				char[] allelesAsChar = new char[alleles.size()];
				for (int i = 0; i < alleles.size(); i++)
				{
					allelesAsChar[i] = alleles.get(i) == null ? '0' : alleles.get(i).charAt(0);
				}
				variantAlleles = new VariantAlleles(alleles, allelesAsChar, true);
			}
			else
			{
				variantAlleles = new VariantAlleles(alleles, null, false);
			}

			cache.put(alleles, variantAlleles);
		}

		return variantAlleles;
	}

	public static VariantAlleles create(char allele1, char allele2)
	{
		String strAllele1 = allele1 == '0' ? null : String.valueOf(allele1);
		String strAllele2 = allele2 == '0' ? null : String.valueOf(allele2);
		List<String> alleles = Arrays.asList(strAllele1, strAllele2);

		VariantAlleles variantAlleles = cache.get(alleles);
		if (variantAlleles == null)
		{
			variantAlleles = new VariantAlleles(alleles, new char[]
			{ allele1, allele2 }, true);
			cache.put(alleles, variantAlleles);
		}

		return variantAlleles;
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

	public int getAlleleCount()
	{
		return alleles.size();
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

		return allelesAsChar;
	}

	@Override
	public String toString()
	{
		return "VariantAlleles [alleles=" + alleles + ", snp=" + snp + "]";
	}

}
