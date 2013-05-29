package org.molgenis.genotype;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class VariantAllelesTest
{
	@Test
	public void getAlleles()
	{
		Alleles alleles = Alleles.create(Arrays.asList("A", "T"));
		assertNotNull(alleles.getAlleles());
		assertEquals(alleles.getAlleles().size(), 2);
		assertEquals(alleles.getAlleles().get(0), "A");
		assertEquals(alleles.getAlleles().get(1), "T");
		assertEquals(alleles.getAllelesAsChars()[0], 'A');
		assertEquals(alleles.getAllelesAsChars()[1], 'T');

		Alleles alleles2 = Alleles.create('A', 'T');
		assertEquals(alleles2, alleles);
	}

	@Test
	public void getAsChar()
	{
		Alleles alleles = Alleles.create(Arrays.asList("A", "T"));
		assertNotNull(alleles.getAllelesAsChars());
		assertEquals(alleles.getAllelesAsChars().length, 2);
		assertEquals(alleles.getAllelesAsChars()[0], 'A');
		assertEquals(alleles.getAllelesAsChars()[1], 'T');

		assertEquals(alleles == Alleles.create('A', 'T'), true);

	}

	@Test(expectedExceptions = RuntimeException.class)
	public void getNonSnpAsChar()
	{
		Alleles.create(Arrays.asList("A", "T", "CG")).getAllelesAsChars();
	}

	@Test
	public void createWithChars()
	{
		Alleles variantAlleles = Alleles.create('C', 'A');
		assertEquals(variantAlleles.getAllelesAsChars(), new char[]
		{ 'C', 'A' });
	}

	@Test
	public void swap()
	{
		List<String> alleles = Arrays.asList("A", "T", "C", "G");
		Alleles variantAlleles = Alleles.create(alleles);
		Alleles swapped = variantAlleles.getComplement();
		assertEquals(swapped.getAlleles(), Arrays.asList("T", "A", "G", "C"));
		assertEquals(swapped.getAllelesAsChars(), new char[]
		{ 'T', 'A', 'G', 'C' });
	}

	@Test
	public void swapSnp()
	{
		Alleles variantAlleles = Alleles.create('A', 'G');
		Alleles swapped = variantAlleles.getComplement();
		assertEquals(swapped.getAlleles(), Arrays.asList("T", "C"));
		assertEquals(swapped.getAllelesAsChars(), new char[]
		{ 'T', 'C' });
	}

	@Test
	public void sameAlleles()
	{
		Alleles variantAlleles = Alleles.create('A', 'G');
		Alleles variantAlleles2 = Alleles.create('T', 'G');
		Alleles variantAlleles3 = Alleles.create('G', 'A');
		Alleles variantAlleles4 = Alleles.create(new char[]
		{ 'A', 'G', 'T' });
		Alleles variantAlleles5 = Alleles.create(new char[]
		{ 'A' });
		Alleles variantAlleles6 = Alleles.create('A', 'G');

		assertEquals(variantAlleles.sameAlleles(variantAlleles2), false);
		assertEquals(variantAlleles.sameAlleles(variantAlleles3), true);
		assertEquals(variantAlleles.sameAlleles(variantAlleles4), false);
		assertEquals(variantAlleles.sameAlleles(variantAlleles5), false);
		assertEquals(variantAlleles.sameAlleles(variantAlleles6), true);
	}

	@Test
	public void isAtOrGcSnp()
	{

		Alleles variantAlleles;

		variantAlleles = Alleles.create('A', 'G');
		assertEquals(variantAlleles.isAtOrGcSnp(), false);

		variantAlleles = Alleles.create(Arrays.asList("A", "G"));
		assertEquals(variantAlleles.isAtOrGcSnp(), false);

		variantAlleles = Alleles.create(Arrays.asList("G", "C"));
		assertEquals(variantAlleles.isAtOrGcSnp(), true);

		variantAlleles = Alleles.create('A', 'T');
		assertEquals(variantAlleles.isAtOrGcSnp(), true);

		variantAlleles = Alleles.create(Arrays.asList("G", "C", "GC"));
		assertEquals(variantAlleles.isAtOrGcSnp(), false);

		variantAlleles = Alleles.create(Arrays.asList("G", "C", "G"));
		assertEquals(variantAlleles.isAtOrGcSnp(), true);

		variantAlleles = Alleles.create(Arrays.asList("G", "C", "T"));
		assertEquals(variantAlleles.isAtOrGcSnp(), false);
	}
}
