package org.molgenis.genotype;

import java.io.File;
import java.net.URISyntaxException;

import org.molgenis.genotype.tabix.TabixIndexTest;

public class ResourceTest
{
	protected File getTestResourceFile(String name) throws URISyntaxException
	{
		return new File(this.getClass().getResource(name).toURI());
	}

	protected File getTestVcfGz() throws URISyntaxException
	{
		return getTestResourceFile("/test.vcf.gz");
	}

	protected File getTestVcfGzTbi() throws URISyntaxException
	{
		return getTestResourceFile("/test.vcf.gz.tbi");
	}
}
