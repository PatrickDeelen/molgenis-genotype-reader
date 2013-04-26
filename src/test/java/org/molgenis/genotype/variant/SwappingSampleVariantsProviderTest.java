package org.molgenis.genotype.variant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.molgenis.genotype.VariantAlleles;
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
		List<VariantAlleles> variantAlleles = Arrays.asList(VariantAlleles.create('A', 'T'),
				VariantAlleles.create('C', 'G'));
		when(mockSampleVariantsProvider.getSampleVariants(mockSnpGeneticVariant)).thenReturn(variantAlleles);

		List<VariantAlleles> result = swappingSampleVariantsProvider.getSampleVariants(mockSnpGeneticVariant);
		assertEquals(result.size(), 2);
		assertEquals(result.get(0).getAllelesAsChars(), new char[]
		{ 'T', 'A' });
		assertEquals(result.get(1).getAllelesAsChars(), new char[]
		{ 'G', 'C' });
	}
}
