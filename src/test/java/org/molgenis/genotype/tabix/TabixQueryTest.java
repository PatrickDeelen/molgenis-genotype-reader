package org.molgenis.genotype.tabix;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import net.sf.samtools.util.BlockCompressedInputStream;

import org.molgenis.genotype.ResourceTest;
import org.molgenis.genotype.VariantQuery;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.VariantLineMapper;
import org.molgenis.genotype.vcf.VcfVariantLineMapper;
import org.molgenis.io.vcf.VcfReader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TabixQueryTest extends ResourceTest
{
	private VariantQuery query;
	private TabixIndex index;

	@BeforeClass
	public void beforeClass() throws IOException
	{
		VcfReader reader = new VcfReader(new BlockCompressedInputStream(getTestVcfGz()));

		try
		{
			VariantLineMapper variantLineMapper = new VcfVariantLineMapper(reader.getColNames(),
					reader.getSampleNames(), Collections.<Annotation> emptyList(),
					Collections.<String, String> emptyMap());
			index = new TabixIndex(getTestVcfGzTbi(), getTestVcfGz(), variantLineMapper);
		}
		finally
		{
			reader.close();
		}

	}

	@BeforeMethod
	public void beforeMethod() throws IOException
	{
		query = index.createQuery();
	}

	@AfterMethod
	public void afterMethod() throws IOException
	{
		query.close();
	}

	@Test
	public void queryPos() throws IOException
	{
		Iterator<GeneticVariant> it = query.executeQuery("1", 565286, 6097450);
		int i = 0;
		while (it.hasNext())
		{
			i++;
			GeneticVariant variant = it.next();
			assertNotNull(variant);
			assertEquals(variant.getSequenceName(), "1");
		}

		// 565286 is excluded, 6097450 is included
		assertEquals(i, 5);
	}

	@Test
	public void querySeq1() throws IOException
	{
		Iterator<GeneticVariant> it = query.executeQuery("1");
		int i = 0;
		while (it.hasNext())
		{
			i++;
			GeneticVariant variant = it.next();
			assertNotNull(variant);
			assertEquals(variant.getSequenceName(), "1");
		}
		assertEquals(i, 6);
	}

	@Test
	public void querySeq2() throws IOException
	{
		Iterator<GeneticVariant> it = query.executeQuery("2");
		int i = 0;
		while (it.hasNext())
		{
			i++;
			GeneticVariant variant = it.next();
			assertNotNull(variant);
			assertEquals(variant.getSequenceName(), "2");
		}
		assertEquals(i, 1);
	}

	@Test
	public void queryStartPos()
	{
		Iterator<GeneticVariant> variants = query.executeQuery("1", 3172273);
		assertNotNull(variants);
		assertTrue(variants.hasNext());
		assertEquals(variants.next().getPrimaryVariantId(), "rs2455100");
		assertFalse(variants.hasNext());

		assertNotNull(query.executeQuery("x", 3172273));
		assertFalse(query.executeQuery("x", 3172273).hasNext());
		assertNotNull(query.executeQuery("1", 31722730));
		assertFalse(query.executeQuery("1", 31722730).hasNext());
	}
}
