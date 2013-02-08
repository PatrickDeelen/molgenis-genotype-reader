package org.molgenis.genotype.variant;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.Test;

public class AbstractGeneticVariantTest
{
	@Test
	public void getPrimaryVatiantId()
	{
		AbstractGeneticVariant variant = new TestGeneticVariant(Arrays.asList("X"));
		assertNotNull(variant.getPrimaryVariantId());
		assertEquals("X", variant.getPrimaryVariantId());

		variant = new TestGeneticVariant(Arrays.asList("X", "Y"));
		assertNotNull(variant.getPrimaryVariantId());
		assertEquals("X", variant.getPrimaryVariantId());

		variant = new TestGeneticVariant(Arrays.asList("X", "Y", "Z"));
		assertNotNull(variant.getPrimaryVariantId());
		assertEquals("X", variant.getPrimaryVariantId());
	}

	@Test
	public void getAlternativeVariantIds()
	{
		AbstractGeneticVariant variant = new TestGeneticVariant(Arrays.asList("X"));
		assertNotNull(variant.getAlternativeVariantIds());
		assertTrue(variant.getAlternativeVariantIds().isEmpty());

		variant = new TestGeneticVariant(Arrays.asList("X", "Y"));
		assertNotNull(variant.getAlternativeVariantIds());
		assertEquals(variant.getAlternativeVariantIds().size(), 1);
		assertEquals(variant.getAlternativeVariantIds().get(0), "Y");

		variant = new TestGeneticVariant(Arrays.asList("X", "Y", "Z"));
		assertNotNull(variant.getAlternativeVariantIds());
		assertEquals(variant.getAlternativeVariantIds().size(), 2);
		assertEquals(variant.getAlternativeVariantIds().get(0), "Y");
		assertEquals(variant.getAlternativeVariantIds().get(1), "Z");
	}

	private class TestGeneticVariant extends AbstractGeneticVariant
	{
		public TestGeneticVariant(List<String> ids)
		{
			super(ids, "1", 0, new ArrayList<String>(), new HashMap<String, Object>(), null, Collections
					.<String> emptyList(), Collections.<String> emptyList());
		}

		@Override
		public List<String> getAlleles()
		{
			return null;
		}

		@Override
		public String getRefAllele()
		{
			return null;
		}

		@Override
		public float getMinorAlleleFrequency() {
			return 0;
		}

		@Override
		public String getMinorAllele() {
			return null;
		}

	}
}
