package org.molgenis.genotype.annotation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.molgenis.io.vcf.VcfInfo;
import org.molgenis.io.vcf.VcfInfo.InfoType;
import org.testng.annotations.Test;

public class VcfAnnotationTest
{
	@Test
	public void fromVcfInfo()
	{
		VcfInfo info = new VcfInfo("ID", InfoType.CHARACTER, "A", "Desc");
		VcfAnnotation annotation = VcfAnnotation.fromVcfInfo(info);
		assertNotNull(annotation);
		assertEquals(annotation.getId(), info.getId());
		assertEquals(annotation.getName(), info.getId());
		assertEquals(annotation.getDescription(), info.getDescription());
		assertEquals(annotation.getNumber(), -1);
		assertEquals(annotation.getType(), Annotation.Type.CHAR);
		assertTrue(annotation.isPerAltAllele());
		assertFalse(annotation.isPerGenotype());

		info = new VcfInfo("ID", InfoType.FLAG, "G", "Desc");
		annotation = VcfAnnotation.fromVcfInfo(info);
		assertNotNull(annotation);
		assertEquals(annotation.getId(), info.getId());
		assertEquals(annotation.getName(), info.getId());
		assertEquals(annotation.getDescription(), info.getDescription());
		assertEquals(annotation.getNumber(), -1);
		assertEquals(annotation.getType(), Annotation.Type.BOOLEAN);
		assertFalse(annotation.isPerAltAllele());
		assertTrue(annotation.isPerGenotype());

		info = new VcfInfo("ID", InfoType.STRING, ".", "Desc");
		annotation = VcfAnnotation.fromVcfInfo(info);
		assertNotNull(annotation);
		assertEquals(annotation.getId(), info.getId());
		assertEquals(annotation.getName(), info.getId());
		assertEquals(annotation.getDescription(), info.getDescription());
		assertEquals(annotation.getNumber(), -1);
		assertEquals(annotation.getType(), Annotation.Type.STRING);
		assertFalse(annotation.isPerAltAllele());
		assertFalse(annotation.isPerGenotype());

		info = new VcfInfo("ID", InfoType.UNKNOWN, "1", "Desc");
		annotation = VcfAnnotation.fromVcfInfo(info);
		assertNotNull(annotation);
		assertEquals(annotation.getId(), info.getId());
		assertEquals(annotation.getName(), info.getId());
		assertEquals(annotation.getDescription(), info.getDescription());
		assertEquals(annotation.getNumber(), 1);
		assertEquals(annotation.getType(), Annotation.Type.UNKOWN);
		assertFalse(annotation.isPerAltAllele());
		assertFalse(annotation.isPerGenotype());
	}
}
