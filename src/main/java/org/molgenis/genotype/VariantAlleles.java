package org.molgenis.genotype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.util.Utils;

public class VariantAlleles
{
	private static Map<List<String>, VariantAlleles> cache = new HashMap<List<String>, VariantAlleles>();
	private static Map<CharArrayWrapper, VariantAlleles> snpCache = new HashMap<CharArrayWrapper, VariantAlleles>();
	private final List<String> alleles;
	private final char[] allelesAsChar;
	private final boolean snp;
	private VariantAlleles complement;

	private VariantAlleles(List<String> alleles, char[] allelesAsChar, boolean snp)
	{
		this.alleles = Collections.unmodifiableList(alleles);
		this.allelesAsChar = allelesAsChar;
		this.snp = snp;
	}

	private VariantAlleles(char[] allelesAsChar)
	{
		this.allelesAsChar = allelesAsChar;
		this.snp = true;

		ArrayList<String> allelesBuilder = new ArrayList<String>();
		for (char allele : allelesAsChar)
		{
			allelesBuilder.add(String.valueOf(allele));
		}
		this.alleles = Collections.unmodifiableList(allelesBuilder);

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

			addToCache(variantAlleles);
			variantAlleles.addComplement();

		}

		return variantAlleles;
	}

	public static VariantAlleles create(char allele1, char allele2)
	{

		return create(new char[]
		{ allele1, allele2 });

	}

	public static VariantAlleles create(char[] alleles)
	{
		VariantAlleles variantAlleles = snpCache.get(new CharArrayWrapper(alleles));
		if (variantAlleles == null)
		{
			variantAlleles = new VariantAlleles(alleles);
			addToCache(variantAlleles);
			variantAlleles.addComplement();
		}

		return variantAlleles;
	}

	private static void addToCache(VariantAlleles variantAlleles)
	{
		cache.put(variantAlleles.getAlleles(), variantAlleles);
		if (variantAlleles.isSnp())
		{
			snpCache.put(new CharArrayWrapper(variantAlleles.getAllelesAsChars()), variantAlleles);
		}
	}

	/**
	 * Add complement. Not done in constructor to prevent infinite loop. Cache
	 * must be up to date before this is called
	 */
	private void addComplement()
	{
		if (snp)
		{
			this.complement = VariantAlleles.create(Utils.swapSnpStrand(allelesAsChar));
		}
		else
		{
			this.complement = null;
		}

	}

	/**
	 * List of the possible alleles, can contain null if not known!!!!!
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

	/**
	 * Returns the complements of this variant alleles. Currently only works for
	 * SNPs
	 * 
	 * @return complement of current variant alleles
	 */
	public VariantAlleles getComplement()
	{

		if (!isSnp())
		{
			throw new RuntimeException("Complement currenlty only supported for SNPs");
		}

		return complement;
	}

	public boolean sameAlleles(VariantAlleles other)
	{
		if (this.alleles.size() != other.alleles.size())
		{
			return false;
		}
		return this.alleles.containsAll(other.alleles);
	}

}
