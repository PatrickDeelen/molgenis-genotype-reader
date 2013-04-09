package org.molgenis.genotype;

import java.util.Arrays;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

public class VariantAllelesTest
{

	@Test
	public void getAlleles()
	{
		VariantAlleles alleles = VariantAlleles.create(Arrays.asList("A", "T"));
		AssertJUnit.assertNotNull(alleles.getAlleles());
		AssertJUnit.assertEquals(alleles.getAlleles().size(), 2);
		AssertJUnit.assertEquals(alleles.getAlleles().get(0), "A");
		AssertJUnit.assertEquals(alleles.getAlleles().get(1), "T");

		VariantAlleles alleles2 = VariantAlleles.create('A', 'T');
		AssertJUnit.assertEquals(alleles2, alleles);
	}

	@Test
	public void getAsChar()
	{
		VariantAlleles alleles = VariantAlleles.create(Arrays.asList("A", "T"));
		AssertJUnit.assertNotNull(alleles.getAllelesAsChars());
		AssertJUnit.assertEquals(alleles.getAllelesAsChars().length, 2);
		AssertJUnit.assertEquals(alleles.getAllelesAsChars()[0], 'A');
		AssertJUnit.assertEquals(alleles.getAllelesAsChars()[1], 'T');
	}

	@Test(expectedExceptions = RuntimeException.class)
	public void getNonSnpAsChar()
	{
		VariantAlleles.create(Arrays.asList("A", "T", "CG")).getAllelesAsChars();
	}
}
