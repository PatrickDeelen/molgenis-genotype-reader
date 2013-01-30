package org.molgenis.genotype.tabix;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;

import org.molgenis.genotype.GenotypeQuery;
import org.molgenis.genotype.ResourceTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TabixQueryTest extends ResourceTest
{
	private GenotypeQuery query;
	private TabixIndex index;

	@BeforeClass
	public void beforeClass() throws IOException
	{
		index = new TabixIndex(getTestVcfGzTbi(), getTestVcfGz());
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
		Iterator<String> it = query.executeQuery("1", 565286, 6097450);
		int i = 0;
		while (it.hasNext())
		{
			i++;
			String s = it.next();
			assertNotNull(s);
			assertTrue(s.startsWith("1"));
		}

		// 565286 is excluded, 6097450 is included
		assertEquals(i, 5);
	}

	@Test
	public void querySeq1() throws IOException
	{
		Iterator<String> it = query.executeQuery("1");
		int i = 0;
		while (it.hasNext())
		{
			i++;
			String s = it.next();
			assertNotNull(s);
			assertTrue(s.startsWith("1"));
		}
		assertEquals(i, 6);
	}

	@Test
	public void querySeq2() throws IOException
	{
		Iterator<String> it = query.executeQuery("2");
		int i = 0;
		while (it.hasNext())
		{
			i++;
			String s = it.next();
			assertNotNull(s);
			assertTrue(s.startsWith("2"));
		}
		assertEquals(i, 1);
	}

}
