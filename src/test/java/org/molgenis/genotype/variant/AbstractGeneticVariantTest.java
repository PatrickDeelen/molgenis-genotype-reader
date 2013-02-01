package org.molgenis.genotype.variant;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.Test;

public class AbstractGeneticVariantTest
{
	@Test
	public void getCompoundId()
	{
		AbstractGeneticVariant variant = new TestGeneticVariant(Arrays.asList("X"));
		assertNotNull(variant.getCompoundId());
		assertEquals("X", variant.getCompoundId());

		variant = new TestGeneticVariant(Arrays.asList("X", "Y"));
		assertNotNull(variant.getCompoundId());
		assertEquals("X;Y", variant.getCompoundId());

		variant = new TestGeneticVariant(Arrays.asList("X", "Y", "Z"));
		assertNotNull(variant.getCompoundId());
		assertEquals("X;Y;Z", variant.getCompoundId());
	}

	private class TestGeneticVariant extends AbstractGeneticVariant
	{
		public TestGeneticVariant(List<String> ids)
		{
			super(ids, "1", 0, new HashMap<String, List<String>>(), new HashMap<String, Object>(), null, Collections
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

	}
}
