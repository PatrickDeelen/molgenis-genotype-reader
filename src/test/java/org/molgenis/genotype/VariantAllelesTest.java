package org.molgenis.genotype;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.Arrays;

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
}
