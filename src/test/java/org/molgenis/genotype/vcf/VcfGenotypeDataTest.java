package org.molgenis.genotype.vcf;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.samtools.util.BlockCompressedInputStream;

import org.molgenis.genotype.GenotypeDataIndex;
import org.molgenis.genotype.ResourceTest;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.annotation.VcfAnnotation;
import org.molgenis.genotype.tabix.TabixIndex;
import org.molgenis.io.vcf.VcfReader;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class VcfGenotypeDataTest extends ResourceTest
{
	private VcfGenotypeData genotypeData;

	@BeforeClass
	public void beforeClass() throws IOException
	{
		GenotypeDataIndex index = new TabixIndex(getTestVcfGzTbi());
		VcfReader reader = new VcfReader(new BlockCompressedInputStream(getTestVcfGz()));
		genotypeData = new VcfGenotypeData(index, reader);
	}

	@Test
	public void getSeqNames()
	{
		List<String> seqNames = genotypeData.getSeqNames();
		assertNotNull(seqNames);
		assertEquals(seqNames.size(), 2);
		assertEquals(seqNames.get(0), "1");
		assertEquals(seqNames.get(1), "2");
	}

	@Test
	public void getVariantAnnotationsMap()
	{
		Map<String, Annotation> annotations = genotypeData.getVariantAnnotationsMap();
		assertNotNull(annotations);
		assertEquals(annotations.size(), 20);

		Annotation annotation = annotations.get("NS");
		assertNotNull(annotation);
		assertEquals(annotation.getId(), "NS");
		assertTrue(annotation instanceof VcfAnnotation);
	}
}
