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
		VariantAlleles alleles = VariantAlleles.create(Arrays.asList("A", "T"));
		assertNotNull(alleles.getAlleles());
		assertEquals(alleles.getAlleles().size(), 2);
		assertEquals(alleles.getAlleles().get(0), "A");
		assertEquals(alleles.getAlleles().get(1), "T");
		assertEquals(alleles.getAllelesAsChars()[0], 'A');
		assertEquals(alleles.getAllelesAsChars()[1], 'T');

		VariantAlleles alleles2 = VariantAlleles.create('A', 'T');
		assertEquals(alleles2, alleles);
	}

	@Test
	public void getAsChar()
	{
		VariantAlleles alleles = VariantAlleles.create(Arrays.asList("A", "T"));
		assertNotNull(alleles.getAllelesAsChars());
		assertEquals(alleles.getAllelesAsChars().length, 2);
		assertEquals(alleles.getAllelesAsChars()[0], 'A');
		assertEquals(alleles.getAllelesAsChars()[1], 'T');

		assertEquals(alleles == VariantAlleles.create('A', 'T'), true);

	}

	@Test(expectedExceptions = RuntimeException.class)
	public void getNonSnpAsChar()
	{
		VariantAlleles.create(Arrays.asList("A", "T", "CG")).getAllelesAsChars();
	}

	@Test
	public void createWithChars()
	{
		VariantAlleles variantAlleles = VariantAlleles.create('C', 'A');
		assertEquals(variantAlleles.getAllelesAsChars(), new char[]
		{ 'C', 'A' });
	}

	@Test
	public void swap()
	{
		List<String> alleles = Arrays.asList("A", "T", "C", "G");
		VariantAlleles variantAlleles = VariantAlleles.create(alleles);
		VariantAlleles swapped = variantAlleles.getComplement();
		assertEquals(swapped.getAlleles(), Arrays.asList("T", "A", "G", "C"));
		assertEquals(swapped.getAllelesAsChars(), new char[]
		{ 'T', 'A', 'G', 'C' });
	}

	@Test
	public void swapSnp()
	{
		VariantAlleles variantAlleles = VariantAlleles.create('A', 'G');
		VariantAlleles swapped = variantAlleles.getComplement();
		assertEquals(swapped.getAlleles(), Arrays.asList("T", "C"));
		assertEquals(swapped.getAllelesAsChars(), new char[]
		{ 'T', 'C' });
	}

	@Test
	public void sameAlleles()
	{
		VariantAlleles variantAlleles = VariantAlleles.create('A', 'G');
		VariantAlleles variantAlleles2 = VariantAlleles.create('T', 'G');
		VariantAlleles variantAlleles3 = VariantAlleles.create('G', 'A');
		VariantAlleles variantAlleles4 = VariantAlleles.create(new char[]
		{ 'A', 'G', 'T' });
		VariantAlleles variantAlleles5 = VariantAlleles.create(new char[]
		{ 'A' });
		VariantAlleles variantAlleles6 = VariantAlleles.create('A', 'G');

		assertEquals(variantAlleles.sameAlleles(variantAlleles2), false);
		assertEquals(variantAlleles.sameAlleles(variantAlleles3), true);
		assertEquals(variantAlleles.sameAlleles(variantAlleles4), false);
		assertEquals(variantAlleles.sameAlleles(variantAlleles5), false);
		assertEquals(variantAlleles.sameAlleles(variantAlleles6), true);
	}

	@Test
	public void isAtOrGcSnp()
	{

		VariantAlleles variantAlleles;

		variantAlleles = VariantAlleles.create('A', 'G');
		assertEquals(variantAlleles.isAtOrGcSnp(), false);

		variantAlleles = VariantAlleles.create(Arrays.asList("A", "G"));
		assertEquals(variantAlleles.isAtOrGcSnp(), false);

		variantAlleles = VariantAlleles.create(Arrays.asList("G", "C"));
		assertEquals(variantAlleles.isAtOrGcSnp(), true);

		variantAlleles = VariantAlleles.create('A', 'T');
		assertEquals(variantAlleles.isAtOrGcSnp(), true);

		variantAlleles = VariantAlleles.create(Arrays.asList("G", "C", "GC"));
		assertEquals(variantAlleles.isAtOrGcSnp(), false);

		variantAlleles = VariantAlleles.create(Arrays.asList("G", "C", "G"));
		assertEquals(variantAlleles.isAtOrGcSnp(), true);

		variantAlleles = VariantAlleles.create(Arrays.asList("G", "C", "T"));
		assertEquals(variantAlleles.isAtOrGcSnp(), false);
	}
}
