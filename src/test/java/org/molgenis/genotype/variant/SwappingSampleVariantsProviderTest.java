package org.molgenis.genotype.variant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.molgenis.genotype.Alleles;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SwappingSampleVariantsProviderTest
{
	private SampleVariantsProvider mockSampleVariantsProvider;
	private SnpGeneticVariant mockSnpGeneticVariant;
	private SwappingSampleVariantsProvider swappingSampleVariantsProvider;

	@BeforeMethod
	public void beforeMethod()
	{
		mockSampleVariantsProvider = mock(SampleVariantsProvider.class);
		mockSnpGeneticVariant = mock(SnpGeneticVariant.class);
		swappingSampleVariantsProvider = new SwappingSampleVariantsProvider(mockSampleVariantsProvider);
	}

	@Test
	public void getSampleVariants()
	{
		List<Alleles> variantAlleles = Arrays.asList(Alleles.create('A', 'T'),
				Alleles.create('C', 'G'));
		when(mockSampleVariantsProvider.getSampleVariants(mockSnpGeneticVariant)).thenReturn(variantAlleles);

		List<Alleles> result = swappingSampleVariantsProvider.getSampleVariants(mockSnpGeneticVariant);
		assertEquals(result.size(), 2);
		assertEquals(result.get(0).getAllelesAsChars(), new char[]
		{ 'T', 'A' });
		assertEquals(result.get(1).getAllelesAsChars(), new char[]
		{ 'G', 'C' });
	}
}
