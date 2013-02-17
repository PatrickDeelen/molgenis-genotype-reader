package org.molgenis.genotype.variant;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class GeneticVariantTest
{
	@Test
	public void getPrimaryVatiantId()
	{
		GeneticVariant variant = createGeneticVariant(Arrays.asList("X"));
		assertNotNull(variant.getPrimaryVariantId());
		assertEquals("X", variant.getPrimaryVariantId());

		variant = createGeneticVariant(Arrays.asList("X", "Y"));
		assertNotNull(variant.getPrimaryVariantId());
		assertEquals("X", variant.getPrimaryVariantId());

		variant = createGeneticVariant(Arrays.asList("X", "Y", "Z"));
		assertNotNull(variant.getPrimaryVariantId());
		assertEquals("X", variant.getPrimaryVariantId());
	}

	@Test
	public void getAlternativeVariantIds()
	{
		GeneticVariant variant = createGeneticVariant(Arrays.asList("X"));
		assertNotNull(variant.getAlternativeVariantIds());
		assertTrue(variant.getAlternativeVariantIds().isEmpty());

		variant = createGeneticVariant(Arrays.asList("X", "Y"));
		assertNotNull(variant.getAlternativeVariantIds());
		assertEquals(variant.getAlternativeVariantIds().size(), 1);
		assertEquals(variant.getAlternativeVariantIds().get(0), "Y");

		variant = createGeneticVariant(Arrays.asList("X", "Y", "Z"));
		assertNotNull(variant.getAlternativeVariantIds());
		assertEquals(variant.getAlternativeVariantIds().size(), 2);
		assertEquals(variant.getAlternativeVariantIds().get(0), "Y");
		assertEquals(variant.getAlternativeVariantIds().get(1), "Z");
	}

	private GeneticVariant createGeneticVariant(List<String> ids)
	{
		return new GeneticVariant(ids, "sequenceName", 1, null, null, null, null, null, null, null,
				GeneticVariant.Type.SNP);
	}
}
