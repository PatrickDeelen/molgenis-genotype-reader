package org.molgenis.genotype.util;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.molgenis.genotype.DummySampleVariantsProvider;
import org.molgenis.genotype.VariantAlleles;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SampleVariantsProvider;
import org.molgenis.genotype.variant.SnpGeneticVariant;
import org.testng.annotations.Test;

public class LdCalculatorTest
{

	@Test
	public void calculateLd() throws LdCalculatorException
	{

		VariantAlleles alleles = VariantAlleles.create('A', 'C');

		ArrayList<VariantAlleles> sampleAlleles = new ArrayList<VariantAlleles>();
		sampleAlleles.add(VariantAlleles.create('A', 'C'));
		sampleAlleles.add(VariantAlleles.create('A', 'C'));
		sampleAlleles.add(VariantAlleles.create('C', 'C'));
		sampleAlleles.add(VariantAlleles.create('A', 'A'));
		sampleAlleles.add(VariantAlleles.create('A', 'A'));
		SampleVariantsProvider sampleAllelesProvider = new DummySampleVariantsProvider(sampleAlleles);

		GeneticVariant testInstance = new SnpGeneticVariant("rs1", "chr1", 1, alleles, "A", null, null, null,
				sampleAllelesProvider);

		Ld ld = LdCalculator.calculateLd(testInstance, testInstance);

		assertEquals(ld.getR2(), 1, 0.1);
		assertEquals(ld.getDPrime(), 1, 0.1);
	}

	@Test
	public void calculateLd2() throws LdCalculatorException
	{

		VariantAlleles alleles = VariantAlleles.create('A', 'C');

		ArrayList<VariantAlleles> sampleAlleles = new ArrayList<VariantAlleles>();
		sampleAlleles.add(VariantAlleles.create('A', 'C'));
		sampleAlleles.add(VariantAlleles.create('A', 'C'));
		sampleAlleles.add(VariantAlleles.create('C', 'C'));
		sampleAlleles.add(VariantAlleles.create('A', 'A'));
		sampleAlleles.add(VariantAlleles.create('A', 'A'));
		SampleVariantsProvider sampleAllelesProvider = new DummySampleVariantsProvider(sampleAlleles);

		GeneticVariant testInstance = new SnpGeneticVariant("rs1", "chr1", 1, alleles, "A", null, null, null,
				sampleAllelesProvider);

		alleles = VariantAlleles.create('T', 'C');

		sampleAlleles = new ArrayList<VariantAlleles>();
		sampleAlleles.add(VariantAlleles.create('T', 'C'));
		sampleAlleles.add(VariantAlleles.create('T', 'T'));
		sampleAlleles.add(VariantAlleles.create('C', 'C'));
		sampleAlleles.add(VariantAlleles.create('T', 'T'));
		sampleAlleles.add(VariantAlleles.create('C', 'C'));
		sampleAllelesProvider = new DummySampleVariantsProvider(sampleAlleles);

		GeneticVariant testInstance2 = new SnpGeneticVariant("rs2", "chr1", 1, alleles, "C", null, null, null,
				sampleAllelesProvider);

		Ld ld = LdCalculator.calculateLd(testInstance, testInstance2);

		assertEquals(ld.getR2(), 0.1, 0.1, "R2");
		assertEquals(ld.getDPrime(), 0.4, 0.1, "D-prime");

		ArrayList<Double> hapFreqExpect = new ArrayList<Double>(Arrays.asList(0.1198269630761759d,
				0.38017303692382415d, 0.2801730369238241d, 0.2801730369238241d));
		ArrayList<String> haps = new ArrayList<String>(Arrays.asList("CT", "CC", "AT", "AC"));

		assertEquals(ld.getHaplotypesFreq().keySet(), haps);
		assertEquals(ld.getHaplotypesFreq().values(), hapFreqExpect);

	}
}
