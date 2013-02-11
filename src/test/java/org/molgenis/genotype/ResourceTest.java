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

	protected File getTestMapGz() throws URISyntaxException
	{
		return getTestResourceFile("/test.map.gz");
	}

	protected File getTestMapGzTbi() throws URISyntaxException
	{
		return getTestResourceFile("/test.map.gz.tbi");
	}

	protected File getTestPed() throws URISyntaxException
	{
		return getTestResourceFile("/test.ped");
	}
}
