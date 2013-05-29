//package org.molgenis.genotype.util;
//
//import static org.testng.Assert.assertEquals;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import org.molgenis.genotype.Alleles;
//import org.molgenis.genotype.DummySampleVariantsProvider;
//import org.molgenis.genotype.variant.GeneticVariant;
//import org.molgenis.genotype.variant.ReadOnlyGeneticVariant;
//import org.molgenis.genotype.variant.SampleVariantsProvider;
//import org.testng.annotations.Test;
//
//public class LdCalculatorTest
//{
//
//	@Test
//	public void calculateLd() throws LdCalculatorException
//	{
//
//		Alleles alleles = Alleles.createBasedOnChars('A', 'C');
//
//		ArrayList<Alleles> sampleAlleles = new ArrayList<Alleles>();
//		sampleAlleles.add(Alleles.createBasedOnChars('A', 'C'));
//		sampleAlleles.add(Alleles.createBasedOnChars('A', 'C'));
//		sampleAlleles.add(Alleles.createBasedOnChars('C', 'C'));
//		sampleAlleles.add(Alleles.createBasedOnChars('A', 'A'));
//		sampleAlleles.add(Alleles.createBasedOnChars('A', 'A'));
//		SampleVariantsProvider sampleAllelesProvider = new DummySampleVariantsProvider(sampleAlleles);
//
//		GeneticVariant testInstance = ReadOnlyGeneticVariant.createSnp("rs1", 1, "chr1", sampleAllelesProvider, 'A',
//				'C');
//
//		Ld ld = LdCalculator.calculateLd(testInstance, testInstance);
//
//		assertEquals(ld.getR2(), 1, 0.1);
//		assertEquals(ld.getDPrime(), 1, 0.1);
//	}
//
//	@Test
//	public void calculateLd2() throws LdCalculatorException
//	{
//
//		Alleles alleles = Alleles.createBasedOnChars('A', 'C');
//
//		ArrayList<Alleles> sampleAlleles = new ArrayList<Alleles>();
//		sampleAlleles.add(Alleles.createBasedOnChars('A', 'C'));
//		sampleAlleles.add(Alleles.createBasedOnChars('A', 'C'));
//		sampleAlleles.add(Alleles.createBasedOnChars('C', 'C'));
//		sampleAlleles.add(Alleles.createBasedOnChars('A', 'A'));
//		sampleAlleles.add(Alleles.createBasedOnChars('A', 'A'));
//		SampleVariantsProvider sampleAllelesProvider = new DummySampleVariantsProvider(sampleAlleles);
//
//		GeneticVariant testInstance = ReadOnlyGeneticVariant.createSnp("rs1", 1, "chr1", sampleAllelesProvider, 'A',
//				'C');
//		// GeneticVariant testInstance = new SnpGeneticVariant(, "chr1", 1,
//		// alleles, "A", null, null, null,
//
//		alleles = Alleles.createBasedOnChars('T', 'C');
//
//		sampleAlleles = new ArrayList<Alleles>();
//		sampleAlleles.add(Alleles.createBasedOnChars('T', 'C'));
//		sampleAlleles.add(Alleles.createBasedOnChars('T', 'T'));
//		sampleAlleles.add(Alleles.createBasedOnChars('C', 'C'));
//		sampleAlleles.add(Alleles.createBasedOnChars('T', 'T'));
//		sampleAlleles.add(Alleles.createBasedOnChars('C', 'C'));
//		sampleAllelesProvider = new DummySampleVariantsProvider(sampleAlleles);
//
//		GeneticVariant testInstance2 = ReadOnlyGeneticVariant.createSnp("rs2", 1, "chr1", sampleAllelesProvider, 'T',
//				'C');
//
//		Ld ld = LdCalculator.calculateLd(testInstance, testInstance2);
//
//		assertEquals(ld.getR2(), 0.1, 0.1, "R2");
//		assertEquals(ld.getDPrime(), 0.4, 0.1, "D-prime");
//
//		ArrayList<Double> hapFreqExpect = new ArrayList<Double>(Arrays.asList(0.1198269630761759d,
//				0.38017303692382415d, 0.2801730369238241d, 0.2801730369238241d));
//		ArrayList<String> haps = new ArrayList<String>(Arrays.asList("CT", "CC", "AT", "AC"));
//
//		assertEquals(ld.getHaplotypesFreq().keySet(), haps);
//		assertEquals(ld.getHaplotypesFreq().values(), hapFreqExpect);
//
//	}
// }
