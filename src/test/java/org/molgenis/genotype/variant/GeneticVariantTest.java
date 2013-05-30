package org.molgenis.genotype.variant;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.DummySampleVariantsProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GeneticVariantTest
{
	private GeneticVariant testInstance;

	@BeforeClass
	public void setUp()
	{

		Alleles alleles = Alleles.createBasedOnChars('A', 'C');

		ArrayList<Alleles> sampleAlleles = new ArrayList<Alleles>();
		sampleAlleles.add(Alleles.createBasedOnChars('A', 'C'));
		sampleAlleles.add(Alleles.createBasedOnChars('A', 'C'));
		sampleAlleles.add(Alleles.createBasedOnChars('C', 'C'));
		sampleAlleles.add(Alleles.createBasedOnChars('A', 'A'));
		sampleAlleles.add(Alleles.createBasedOnChars('A', 'A'));
		SampleVariantsProvider sampleAllelesProvider = new DummySampleVariantsProvider(sampleAlleles);

		testInstance = ReadOnlyGeneticVariant.createSnp("rs1", 1, "chr1", sampleAllelesProvider, 'A', 'C');

	}

	@Test
	public void getAllIds()
	{
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("rs1");
		assertEquals(testInstance.getAllIds(), expected);
	}

	@Test
	public void getAlleleCount()
	{
		assertEquals(testInstance.getAlleleCount(), 2);
	}

	@Test
	public void getCalledDosages()
	{
		byte[] expected =
		{ 1, 1, 0, 2, 2 };
		// TODO
		// assertEquals(testInstance.getSampleCalledDosage(), expected);
	}

	@Test
	public void getMinorAllele()
	{
		// TODO
		// assertEquals(testInstance.getMinorAllele(), "C");
	}

	@Test
	public void getMinorAlleleFrequency()
	{
		// TODO
		// assertEquals(testInstance.getMinorAlleleFrequency(), 0.4f);
	}

	@Test
	public void getRefAllele()
	{
		assertNull(testInstance.getRefAllele());
	}

	@Test
	public void getSequenceName()
	{
		assertEquals(testInstance.getSequenceName(), "chr1");
	}

	@Test
	public void getStartPos()
	{
		assertEquals(testInstance.getStartPos(), 1);
	}

	@Test
	public void isBiallelic()
	{
		// TODO
		// assertEquals(testInstance.isBiallelic(), true);
	}

	@Test
	public void isSnp()
	{
		// assertEquals(testInstance.isSnp(), true);
	}

	@Test
	public void getPrimaryVatiantId()
	{
		GeneticVariant variant = createGeneticVariant(Arrays.asList("X"));
		assertNotNull(variant.getPrimaryVariantId());
		assertEquals("X", variant.getPrimaryVariantId());

		variant = createGeneticVariant(Arrays.asList("X", "Y"));
		assertNotNull(variant.getPrimaryVariantId());
		assertEquals("X", variant.getPrimaryVariantId());

		variant = createGeneticVariant(Arrays.asList("X", "Y", "Z"));
		assertNotNull(variant.getPrimaryVariantId());
		assertEquals("X", variant.getPrimaryVariantId());
	}

	@Test
	public void getAlternativeVariantIds()
	{
		GeneticVariant variant = createGeneticVariant(Arrays.asList("X"));
		assertNotNull(variant.getAlternativeVariantIds());
		assertTrue(variant.getAlternativeVariantIds().isEmpty());

		variant = createGeneticVariant(Arrays.asList("X", "Y"));
		assertNotNull(variant.getAlternativeVariantIds());
		assertEquals(variant.getAlternativeVariantIds().size(), 1);
		assertEquals(variant.getAlternativeVariantIds().get(0), "Y");

		variant = createGeneticVariant(Arrays.asList("X", "Y", "Z"));
		assertNotNull(variant.getAlternativeVariantIds());
		assertEquals(variant.getAlternativeVariantIds().size(), 2);
		assertEquals(variant.getAlternativeVariantIds().get(0), "Y");
		assertEquals(variant.getAlternativeVariantIds().get(1), "Z");
	}

	private GeneticVariant createGeneticVariant(List<String> ids)
	{
		return ReadOnlyGeneticVariant.createSnp(ids, 1, "chr1", null, 'A', 'C');
	}
}
