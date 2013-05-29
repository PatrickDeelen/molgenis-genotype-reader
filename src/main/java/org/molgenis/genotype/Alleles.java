package org.molgenis.genotype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.util.Utils;

public class Alleles
{
	private static Map<List<String>, Alleles> cache = new HashMap<List<String>, Alleles>();
	private static Map<CharArrayWrapper, Alleles> snpCache = new HashMap<CharArrayWrapper, Alleles>();

	private final List<String> alleles;
	private final char[] allelesAsChar;
	private final boolean snp;
	private Alleles complement;
	private final boolean isAtOrGcSnp;

	private Alleles(List<String> alleles, char[] allelesAsChar, boolean snp)
	{
		this.alleles = Collections.unmodifiableList(alleles);
		this.allelesAsChar = allelesAsChar;
		this.snp = snp;

		this.isAtOrGcSnp = areAlleleCharsAtOrGc(allelesAsChar);

	}

	private Alleles(char[] allelesAsChar)
	{

		this.allelesAsChar = allelesAsChar;
		this.snp = true;

		ArrayList<String> allelesBuilder = new ArrayList<String>();
		for (char allele : allelesAsChar)
		{
			allelesBuilder.add(String.valueOf(allele));
		}
		this.alleles = Collections.unmodifiableList(allelesBuilder);

		this.isAtOrGcSnp = areAlleleCharsAtOrGc(allelesAsChar);

	}

	private static boolean areAlleleCharsAtOrGc(char[] allelesAsChar)
	{
		if (allelesAsChar == null)
		{
			return false;
		}
		if (allelesAsChar.length == 0)
		{
			return false;
		}

		boolean onlyAt = true;
		boolean onlyGc = true;
		for (char allele : allelesAsChar)
		{
			if (allele == 'A' || allele == 'T')
			{
				onlyGc = false;
			}
			if (allele == 'C' || allele == 'G')
			{
				onlyAt = false;
			}
		}
		return onlyAt || onlyGc;

	}

	public static Alleles create(List<String> alleles)
	{
		Alleles variantAlleles = cache.get(alleles);
		if (variantAlleles == null)
		{
			if (Utils.isSnp(alleles))
			{
				char[] allelesAsChar = new char[alleles.size()];
				for (int i = 0; i < alleles.size(); i++)
				{
					allelesAsChar[i] = alleles.get(i) == null ? '0' : alleles.get(i).charAt(0);
				}
				variantAlleles = new Alleles(alleles, allelesAsChar, true);

			}
			else
			{
				variantAlleles = new Alleles(alleles, null, false);
			}

			addToCache(variantAlleles);
			variantAlleles.addComplement();

		}

		return variantAlleles;
	}

	public static Alleles create(String allele1, String allele2)
	{
		ArrayList<String> alleles = new ArrayList<String>(2);
		alleles.add(allele1);
		alleles.add(allele2);
		return create(alleles);
	}

	public static Alleles create(String allele)
	{
		ArrayList<String> alleles = new ArrayList<String>(1);
		alleles.add(allele);
		return create(alleles);
	}

	public static Alleles create(char allele1, char allele2)
	{

		return create(new char[]
		{ allele1, allele2 });

	}

	public static Alleles create(char allele)
	{

		return create(new char[]
		{ allele });

	}

	public static Alleles create(char[] alleles)
	{
		Alleles variantAlleles = snpCache.get(new CharArrayWrapper(alleles));
		if (variantAlleles == null)
		{
			variantAlleles = new Alleles(alleles);
			addToCache(variantAlleles);
			variantAlleles.addComplement();
		}

		return variantAlleles;
	}

	private static void addToCache(Alleles variantAlleles)
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
			this.complement = Alleles.create(Utils.swapSnpStrand(allelesAsChar));
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
	public Alleles getComplement()
	{

		if (!isSnp())
		{
			throw new RuntimeException("Complement currenlty only supported for SNPs");
		}

		return complement;
	}

	/**
	 * Assess if two variantAllele instances have same alleles regardless of
	 * order. Only true if also identical number of alleles
	 * 
	 * @param other
	 * @return
	 */
	public boolean sameAlleles(Alleles other)
	{
		if (this == other)
		{
			return true;
		}
		if (this.alleles.size() != other.alleles.size())
		{
			return false;
		}
		return this.alleles.containsAll(other.alleles);
	}

	public boolean isAtOrGcSnp()
	{
		return isAtOrGcSnp;
	}

}
