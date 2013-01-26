package org.molgenis.genotype.tabix;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.molgenis.genotype.ResourceTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TabixIndexTest extends ResourceTest
{
	private TabixIndex index;

	@BeforeClass
	private void setUp() throws IOException
	{
		index = new TabixIndex(getTestVcfGzTbi());
	}

	@Test
	public void getSeqNames()
	{
		List<String> seqNames = index.getSeqNames();
		assertNotNull(seqNames);
		assertEquals(seqNames.size(), 2);
		assertEquals(seqNames.get(0), "1");
		assertEquals(seqNames.get(1), "2");
	}
}
