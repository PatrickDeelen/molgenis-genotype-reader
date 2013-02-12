package org.molgenis.genotype.variant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SnpGeneticVariantTest
{

	private static SnpGeneticVariant testSnpVariant;

	private final char refSnpAllele = 'A';
	private final char[] snpAlleles =
	{ 'A', 'C' };
	private final String refAllele = "A";
	private final List<String> alleles = new ArrayList<String>();
	private List<List<Character>> sampleSnpVariants;
	private List<List<String>> sampleVariants;

	@BeforeTest
	public void beforeTest()
	{

		alleles.add("A");
		alleles.add("C");

		sampleSnpVariants = new ArrayList<List<Character>>();
		sampleVariants = new ArrayList<List<String>>();

		ArrayList<Character> sampleSnp;
		ArrayList<String> sample;

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('C');
		sampleSnpVariants.add(sampleSnp);
		sample = new ArrayList<String>();
		sample.add("A");
		sample.add("C");
		sampleVariants.add(sample);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('C');
		sampleSnpVariants.add(sampleSnp);
		sample = new ArrayList<String>();
		sample.add("A");
		sample.add("C");
		sampleVariants.add(sample);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('C');
		sampleSnp.add('C');
		sampleSnpVariants.add(sampleSnp);
		sample = new ArrayList<String>();
		sample.add("C");
		sample.add("C");
		sampleVariants.add(sample);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants.add(sampleSnp);
		sample = new ArrayList<String>();
		sample.add("A");
		sample.add("A");
		sampleVariants.add(sample);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants.add(sampleSnp);
		sample = new ArrayList<String>();
		sample.add("A");
		sample.add("A");
		sampleVariants.add(sample);

		testSnpVariant = new SnpGeneticVariant("SNP1", "1", 1, snpAlleles, refSnpAllele, sampleSnpVariants,
				Collections.<String, Object> emptyMap(), 1, Collections.<String> emptyList(),
				Collections.<String> emptyList());

	}

	@Test
	public void getAlleles()
	{
		Assert.assertEquals(testSnpVariant.getAlleles(), alleles);
	}

	@Test
	public void getMinorAllele()
	{
		Assert.assertEquals(testSnpVariant.getMinorAllele(), "C");
	}

	@Test
	public void getMinorAlleleFrequency()
	{
		Assert.assertEquals(testSnpVariant.getMinorAlleleFrequency(), 0.4f);
	}

	@Test
	public void getMinorSnpAllele()
	{
		Assert.assertEquals(testSnpVariant.getMinorSnpAllele(), 'C');
	}

	@Test
	public void getRefAllele()
	{
		Assert.assertEquals(testSnpVariant.getRefAllele(), refAllele);
	}

	@Test
	public void getSampleSnpVariants()
	{
		Assert.assertEquals(testSnpVariant.getSampleSnpVariants(), sampleSnpVariants);
	}

	@Test
	public void getSampleVariants()
	{
		Assert.assertEquals(testSnpVariant.getSampleVariants(), sampleVariants);
	}

	@Test
	public void getSnpAlleles()
	{
		Assert.assertEquals(testSnpVariant.getSnpAlleles(), snpAlleles);
	}

	@Test
	public void getSnpRefAlelle()
	{
		Assert.assertEquals(testSnpVariant.getSnpRefAlelle(), refSnpAllele);
	}

	@Test
	public void getStopPos()
	{
		Assert.assertEquals(testSnpVariant.getStopPos(), null);
	}

	@Test
	public void getMinorAlleleFrequency2()
	{

		char[] snpAlleles2;
		List<List<Character>> sampleSnpVariants2;
		ArrayList<Character> sampleSnp;
		SnpGeneticVariant testSnpVariant2;

		// Test MAF 0 for bi-allelic SNP

		snpAlleles2 = new char[2];
		snpAlleles2[0] = 'A';
		snpAlleles2[1] = 'C';
		sampleSnpVariants2 = new ArrayList<List<Character>>();

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		testSnpVariant2 = new SnpGeneticVariant("SNP", "1", 1, snpAlleles2, snpAlleles2[0], sampleSnpVariants2,
				Collections.<String, Object> emptyMap(), 1, Collections.<String> emptyList(),
				Collections.<String> emptyList());

		Assert.assertEquals(testSnpVariant2.getMinorSnpAllele(), 'C');
		Assert.assertEquals(testSnpVariant2.getMinorAlleleFrequency(), 0f);

		// Test MAF 0 for mono-morphic

		snpAlleles2 = new char[1];
		snpAlleles2[0] = 'A';
		sampleSnpVariants2 = new ArrayList<List<Character>>();

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		testSnpVariant2 = new SnpGeneticVariant("SNP", "1", 1, snpAlleles2, snpAlleles2[0], sampleSnpVariants2,
				Collections.<String, Object> emptyMap(), 1, Collections.<String> emptyList(),
				Collections.<String> emptyList());

		Assert.assertEquals(testSnpVariant2.getMinorSnpAllele(), '\0');
		Assert.assertEquals(testSnpVariant2.getMinorAllele(), "\0");
		Assert.assertEquals(testSnpVariant2.getMinorAlleleFrequency(), 0f);

		// Test MAF 0.5

		snpAlleles2 = new char[2];
		snpAlleles2[0] = 'A';
		snpAlleles2[1] = 'T';
		sampleSnpVariants2 = new ArrayList<List<Character>>();

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('T');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('T');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('T');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('T');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('T');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		testSnpVariant2 = new SnpGeneticVariant("SNP", "1", 1, snpAlleles2, snpAlleles2[0], sampleSnpVariants2,
				Collections.<String, Object> emptyMap(), 1, Collections.<String> emptyList(),
				Collections.<String> emptyList());

		// first allele in list of alleles is listed as minor in case of tie. So
		// expect A
		Assert.assertEquals(testSnpVariant2.getMinorSnpAllele(), 'A');
		Assert.assertEquals(testSnpVariant2.getMinorAlleleFrequency(), 0.5f);

		// Test MAF with non defined missing genotypes

		snpAlleles2 = new char[2];
		snpAlleles2[0] = 'A';
		snpAlleles2[1] = 'G';
		sampleSnpVariants2 = new ArrayList<List<Character>>();

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('G');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('G');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		testSnpVariant2 = new SnpGeneticVariant("SNP", "1", 1, snpAlleles2, snpAlleles2[0], sampleSnpVariants2,
				Collections.<String, Object> emptyMap(), 1, Collections.<String> emptyList(),
				Collections.<String> emptyList());

		Assert.assertEquals(testSnpVariant2.getMinorSnpAllele(), 'G');
		Assert.assertEquals(testSnpVariant2.getMinorAlleleFrequency(), 0.25f);

		// Test MAF with non defined missing genotypes

		snpAlleles2 = new char[2];
		snpAlleles2[0] = 'T';
		snpAlleles2[1] = 'G';
		sampleSnpVariants2 = new ArrayList<List<Character>>();

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('G');
		sampleSnp.add('\0');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('G');
		sampleSnp.add('\0');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('T');
		sampleSnp.add('G');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('G');
		sampleSnp.add('T');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('G');
		sampleSnp.add('G');
		sampleSnpVariants2.add(sampleSnp);

		testSnpVariant2 = new SnpGeneticVariant("SNP", "1", 1, snpAlleles2, snpAlleles2[0], sampleSnpVariants2,
				Collections.<String, Object> emptyMap(), 1, Collections.<String> emptyList(),
				Collections.<String> emptyList());

		Assert.assertEquals(testSnpVariant2.getMinorSnpAllele(), 'T');
		Assert.assertEquals(testSnpVariant2.getMinorAlleleFrequency(), 0.25f);

		// Test MAF with three possible alleles

		snpAlleles2 = new char[3];
		snpAlleles2[0] = 'T';
		snpAlleles2[1] = 'G';
		snpAlleles2[2] = 'A';
		sampleSnpVariants2 = new ArrayList<List<Character>>();

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('G');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('G');
		sampleSnp.add('T');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('T');
		sampleSnp.add('G');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('G');
		sampleSnp.add('T');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('G');
		sampleSnp.add('G');
		sampleSnpVariants2.add(sampleSnp);

		testSnpVariant2 = new SnpGeneticVariant("SNP", "1", 1, snpAlleles2, snpAlleles2[0], sampleSnpVariants2,
				Collections.<String, Object> emptyMap(), 1, Collections.<String> emptyList(),
				Collections.<String> emptyList());

		Assert.assertEquals(testSnpVariant2.getMinorSnpAllele(), 'A');
		Assert.assertEquals(testSnpVariant2.getMinorAlleleFrequency(), 0.1f);

		// Test MAF with four possible alleles

		snpAlleles2 = new char[4];
		snpAlleles2[0] = 'T';
		snpAlleles2[1] = 'G';
		snpAlleles2[2] = 'A';
		snpAlleles2[3] = 'C';
		sampleSnpVariants2 = new ArrayList<List<Character>>();

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('A');
		sampleSnp.add('A');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('C');
		sampleSnp.add('T');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('T');
		sampleSnp.add('G');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('G');
		sampleSnp.add('T');
		sampleSnpVariants2.add(sampleSnp);

		sampleSnp = new ArrayList<Character>();
		sampleSnp.add('G');
		sampleSnp.add('G');
		sampleSnpVariants2.add(sampleSnp);

		testSnpVariant2 = new SnpGeneticVariant("SNP", "1", 1, snpAlleles2, snpAlleles2[0], sampleSnpVariants2,
				Collections.<String, Object> emptyMap(), 1, Collections.<String> emptyList(),
				Collections.<String> emptyList());

		Assert.assertEquals(testSnpVariant2.getMinorSnpAllele(), 'C');
		Assert.assertEquals(testSnpVariant2.getMinorAlleleFrequency(), 0.1f);

	}

}
