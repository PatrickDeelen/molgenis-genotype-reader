package org.molgenis.genotype;

import java.io.File;
import java.net.URISyntaxException;

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

	protected File getTestMap() throws URISyntaxException
	{
		return getTestResourceFile("/test.map");
	}

	protected File getTestPed() throws URISyntaxException
	{
		return getTestResourceFile("/test.ped");
	}
}
