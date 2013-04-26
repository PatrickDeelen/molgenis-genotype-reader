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
		VariantAlleles swapped = variantAlleles.swap();
		assertEquals(swapped.getAlleles(), Arrays.asList("T", "A", "G", "C"));
		assertEquals(swapped.getAllelesAsChars(), new char[]
		{ 'T', 'A', 'G', 'C' });
	}
}
