package org.molgenis.genotype.vcf;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.ResourceTest;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.annotation.VcfAnnotation;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.ListVariantHandler;
import org.molgenis.genotype.variant.SnpGeneticVariant;
import org.molgenis.genotype.variant.VariantHandler;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class VcfGenotypeDataTest extends ResourceTest
{
	private VcfGenotypeData genotypeData;

	@BeforeClass
	public void beforeClass() throws IOException
	{
		genotypeData = new VcfGenotypeData(getTestVcfGz(), getTestVcfGzTbi());
	}

	@AfterClass
	public void afterClass() throws IOException
	{
		genotypeData.close();
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

	@Test
	public void testGetSequences()
	{
		List<Sequence> sequences = genotypeData.getSequences();
		assertNotNull(sequences);
		assertEquals(sequences.size(), 2);
	}

	@Test
	public void testGetSequenceByName()
	{
		Sequence sequence = genotypeData.getSequenceByName("2");
		assertNotNull(sequence);
		assertEquals(sequence.getName(), "2");

		ListVariantHandler handler = new ListVariantHandler();
		sequence.variants(handler);

		List<GeneticVariant> variants = handler.getVariants();
		assertNotNull(variants);
		assertEquals(variants.size(), 1);
		GeneticVariant variant = variants.get(0);
		assertEquals(variant.getCompoundId(), "rs4908464");
		assertEquals(variant.getStartPos(), 7569187);
		assertEquals(variant.getRefAllele(), "G");
		assertEquals(variant.getSequenceName(), "2");
		assertTrue(variant instanceof SnpGeneticVariant);

		List<String> alleles = variant.getAlleles();
		assertNotNull(alleles);
		assertEquals(alleles.size(), 2);
		assertEquals(alleles.get(0), "G");
		assertEquals(alleles.get(1), "C");

		Map<String, List<String>> sampleVariantsBySampleId = variant.getSampleVariants();
		assertNotNull(sampleVariantsBySampleId);
		assertEquals(sampleVariantsBySampleId.size(), 1);
		List<String> sampleVariantList = sampleVariantsBySampleId
				.get("test_S0_L001_R1_001_converted_Unique_Output.pjt");
		assertNotNull(sampleVariantList);
		assertEquals(sampleVariantList.size(), 2);
		assertEquals(sampleVariantList.get(0), "C");
		assertEquals(sampleVariantList.get(0), "C");
	}

	@Test
	public void testGetSequenceByNameNotExisting()
	{
		assertNull(genotypeData.getSequenceByName("Bogus"));
	}

	@Test
	public void testGetVariant()
	{
		GeneticVariant variant = genotypeData.getVariant("1", 565286);
		assertNotNull(variant);
		assertEquals(variant.getCompoundId(), "rs1578391");

		assertNull(genotypeData.getVariant("bogus", 8));
	}

	@Test
	public void testSeqVariants()
	{
		CountingVariantHandler handler = new CountingVariantHandler();
		genotypeData.seqVariants("1", handler);
		assertEquals(handler.count, 6);

		handler = new CountingVariantHandler();
		genotypeData.seqVariants("2", handler);
		assertEquals(handler.count, 1);

		handler = new CountingVariantHandler();
		genotypeData.seqVariants("x", handler);
		assertEquals(handler.count, 0);
	}

	@Test
	public void testGetSampleGeneticVariants()
	{
		Map<String, List<String>> variantMap = genotypeData.getSampleGeneticVariants("1", 3172273);
		assertNotNull(variantMap);
		assertEquals(variantMap.size(), 1);

		List<String> variants = variantMap.values().iterator().next();
		assertEquals(variants.size(), 2);
		assertEquals(variants.get(0), "C");
		assertEquals(variants.get(1), "C");

		variantMap = genotypeData.getSampleGeneticVariants("1", 1);
		assertNotNull(variantMap);
		assertTrue(variantMap.isEmpty());
	}

	private static class CountingVariantHandler implements VariantHandler
	{
		private int count = 0;

		public void handle(GeneticVariant variant)
		{
			count++;
		}
	}
}
