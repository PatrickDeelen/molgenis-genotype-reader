package org.molgenis.genotype.tabix;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.samtools.util.BlockCompressedInputStream;

import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.ResourceTest;
import org.molgenis.genotype.VariantQuery;
import org.molgenis.genotype.VariantQueryResult;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SampleVariantsProvider;
import org.molgenis.genotype.variant.VariantLineMapper;
import org.molgenis.genotype.vcf.VcfVariantLineMapper;
import org.molgenis.io.vcf.VcfReader;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TabixQueryTest extends ResourceTest implements SampleVariantsProvider
{
	private VariantQuery query;
	private TabixIndex index;
	private VariantQueryResult result;

	@BeforeClass
	public void beforeClass() throws IOException, URISyntaxException
	{
		VcfReader reader = new VcfReader(new BlockCompressedInputStream(getTestVcfGz()));

		try
		{
			VariantLineMapper variantLineMapper = new VcfVariantLineMapper(reader.getColNames(),
					Collections.<Annotation> emptyList(), Collections.<String, String> emptyMap(), this);
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
		if (result != null)
		{
			result.close();
		}
	}

	@Test
	public void queryPos() throws IOException
	{
		result = query.executeQuery("1", 565286, 6097450);
		Iterator<GeneticVariant> it = result.iterator();

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

	/*
	 * @Test public void testGetRawLines() { result = query.executeQuery("1", 565286, 6097450); List<String> rawLines =
	 * Utils.iteratorToList(result.getRawLines()); assertEquals( rawLines, Arrays.asList(
	 * "1	565286	rs1578391	C	T	.	flt	NS=1;DP=5;AF=1.000;ANNOT=INT;GI=LOC100131754	GT:DP:EC:CONFS	1/1:5:5:5.300,5.300,1.000,1.000,1.000,1.000,1.000"
	 * ,
	 * "1	2243618	rs35434908	A	GTTTCA	.	flt	NS=1;DP=6;AF=1.000;ANNOT=ING	GT:DP:EC:CONFS	1/1:6:6:6.500,6.560,1.000,1.000,1.000,1.000,1.000"
	 * ,
	 * "1	3171929	rs4648464	G	A	.	flt	NS=1;DP=4;AF=1.000;ANNOT=INT;GI=PRDM16;TI=NM_022114.3;PI=NP_071397.3	GT:DP:EC:CONFS	1/1:4:4:1.400,4.450,0.330,1.000,1.000,1.000,1.000"
	 * ,
	 * "1	3172062	rs4648465	G	A	.	flt	NS=1;DP=11;AF=1.000;ANNOT=INT;GI=PRDM16;TI=NM_022114.3;PI=NP_071397.3	GT:DP:EC:CONFS	1/1:11:11:6.700,8.600,0.780,1.000,1.000,1.000,1.000"
	 * ,
	 * "1	3172273	rs2455100	T	C	.	flt	NS=1;DP=7;AF=1.000;ANNOT=INT;GI=PRDM16;TI=NM_022114.3;PI=NP_071397.3	GT:DP:EC:CONFS	1/1:7:7:3.400,7.310,0.470,1.000,1.000,1.000,1.000"
	 * ,
	 * "1	6097450	rs1295089	G	A	.	flt	NS=1;DP=5;AF=1.000;ANNOT=INT;GI=KCNAB2;TI=NM_003636.2;PI=NP_003627.1	GT:DP:EC:CONFS	1/1:5:5:5.000,5.070,1.000,1.000,1.000,1.000,1.000"
	 * )); }
	 */
	@Test
	public void querySeq1() throws IOException
	{
		result = query.executeQuery("1");
		Iterator<GeneticVariant> it = result.iterator();

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
		result = query.executeQuery("2");
		Iterator<GeneticVariant> it = result.iterator();

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
		result = query.executeQuery("1", 3172273);
		Iterator<GeneticVariant> variants = result.iterator();

		assertNotNull(variants);
		assertTrue(variants.hasNext());
		assertEquals(variants.next().getPrimaryVariantId(), "rs2455100");
		assertFalse(variants.hasNext());

		assertNotNull(query.executeQuery("x", 3172273));
		assertFalse(query.executeQuery("x", 3172273).iterator().hasNext());
		assertNotNull(query.executeQuery("1", 31722730));
		assertFalse(query.executeQuery("1", 31722730).iterator().hasNext());
	}

	@Override
	public List<Alleles> getSampleVariants(GeneticVariant variant)
	{
		return Collections.emptyList();
	}

	@Override
	public int cacheSize()
	{
		return 0;
	}

	@Override
	public List<Boolean> getSamplePhasing(GeneticVariant variant)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
