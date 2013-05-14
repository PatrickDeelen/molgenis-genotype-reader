package org.molgenis.genotype.plink;

import java.io.IOException;
import java.net.URISyntaxException;

import org.molgenis.genotype.ResourceTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PedMapGenotypeWriterTest extends ResourceTest
{
	private PedMapGenotypeData genotypeData;

	@BeforeClass
	public void beforeClass() throws IOException, URISyntaxException
	{
		genotypeData = new PedMapGenotypeData(getTestMapGz(), getTestMapGzTbi(), getTestPed());
	}

	@Test
	public void write() throws IOException
	{
		/*
		 * new PedMapGenotypeWriter(genotypeData, FATHER_SAMPLE_ANNOTATION_NAME,
		 * MOTHER_SAMPLE_ANNOTATION_NAME, SEX_SAMPLE_ANNOTATION_NAME,
		 * PHENOTYPE_SAMPLE_ANNOTATION_NAME).write(new File(
		 * "/Users/erwin/Documents/testresult.ped"), new
		 * File("/Users/erwin/Documents/testresult.map"));
		 */
	}
}
