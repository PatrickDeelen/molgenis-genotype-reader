package org.molgenis.genotype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Alleles implements Iterable<Allele>
{
	private static final Map<List<Allele>, Alleles> pool = new HashMap<List<Allele>, Alleles>();

	private final List<Allele> alleles;
	private final boolean snp;
	private Alleles complement;
	private final boolean isAtOrGcSnp;
	private final List<String> allelesAsString;
	private final char[] allelesAsChar;

	private Alleles(List<Allele> alleles)
	{
		this.alleles = Collections.unmodifiableList(alleles);

		boolean isSnp = true;
		ArrayList<String> allelesAsStringBuilder = new ArrayList<String>(alleles.size());
		for (Allele allele : alleles)
		{
			if (!allele.isSnpAllele())
			{
				isSnp = false;
			}
			allelesAsStringBuilder.add(allele.getAlleleAsString());
		}
		this.allelesAsString = Collections.unmodifiableList(allelesAsStringBuilder);

		this.snp = isSnp;
		if (snp)
		{
			allelesAsChar = new char[alleles.size()];
			int i = 0;
			for (Allele allele : alleles)
			{
				allelesAsChar[i] = allele.getAlleleAsSnp();
				++i;
			}
		}
		else
		{
			allelesAsChar = null;
		}

		this.isAtOrGcSnp = areAlleleCharsAtOrGc(alleles);

	}

	private static boolean areAlleleCharsAtOrGc(List<Allele> alleles)
	{

		if (alleles.size() == 0)
		{
			return false;
		}

		boolean onlyAt = true;
		boolean onlyGc = true;
		for (Allele allele : alleles)
		{
			if (!allele.isSnpAllele())
			{
				return false;
			}
			if (allele == Allele.A_ALLELE || allele == Allele.T_ALLELE)
			{
				onlyGc = false;
			}
			if (allele == Allele.C_ALLELE || allele == Allele.G_ALLELE)
			{
				onlyAt = false;
			}
		}
		return onlyAt || onlyGc;

	}

	public static Alleles createAlleles(List<Allele> alleles)
	{

		if (pool.containsKey(alleles))
		{
			return pool.get(alleles);
		}
		else
		{
			Alleles newAlleles = new Alleles(alleles);
			pool.put(alleles, newAlleles);
			newAlleles.addComplement();
			return newAlleles;
		}
	}

	public static Alleles createAlleles(Allele allele1, Allele allele2)
	{
		ArrayList<Allele> alleles = new ArrayList<Allele>(2);
		alleles.add(allele1);
		alleles.add(allele2);
		return createAlleles(alleles);
	}

	public static Alleles createBasedOnString(List<String> stringAlleles)
	{
		ArrayList<Allele> alleles = new ArrayList<Allele>(stringAlleles.size());

		for (String stringAllele : stringAlleles)
		{
			alleles.add(Allele.create(stringAllele));
		}

		return createAlleles(alleles);
	}

	public static Alleles createBasedOnString(String allele1, String allele2)
	{

		return createAlleles(Allele.create(allele1), Allele.create(allele2));

	}

	public static Alleles createBasedOnChars(char allele1, char allele2)
	{

		return createAlleles(Allele.create(allele1), Allele.create(allele2));

	}

	public static Alleles createBasedOnChars(char[] charAlleles)
	{
		ArrayList<Allele> alleles = new ArrayList<Allele>(charAlleles.length);
		for (char charAllele : charAlleles)
		{
			alleles.add(Allele.create(charAllele));
		}
		return createAlleles(alleles);
	}

	/**
	 * Add complement. Not done in constructor to prevent infinite loop. Pool
	 * must be up to date before this is called
	 */
	private void addComplement()
	{
		if (snp)
		{
			ArrayList<Allele> complementAlleles = new ArrayList<Allele>(alleles.size());
			for (Allele allele : alleles)
			{
				complementAlleles.add(allele.getComplement());
			}
			this.complement = Alleles.createAlleles(complementAlleles);
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
	public List<Allele> getAlleles()
	{
		return alleles;
	}

	public List<String> getAllelesAsString()
	{
		return allelesAsString;
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
		return "VariantAlleles [alleles=" + getAllelesAsString() + ", snp=" + snp + "]";
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

	@Override
	public Iterator<Allele> iterator()
	{
		return alleles.iterator();
	}

	public Allele get(int alleleIndex)
	{
		return alleles.get(alleleIndex);
	}

}
