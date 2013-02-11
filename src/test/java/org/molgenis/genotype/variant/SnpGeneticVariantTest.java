package org.molgenis.genotype.variant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

public class SnpGeneticVariantTest {
	
	
	private static SnpGeneticVariant testSnpVariant;
	
	private final char refSnpAllele = 'A';
	private final char[] snpAlleles = {'A', 'C'};
	private final String refAllele = "A";
	private final List<String> alleles = new ArrayList<String>();
	private List<List<Character>> sampleSnpVariants;
	private List<List<String>> sampleVariants;
	
  @BeforeTest
  public void beforeTest() {
	  
	  
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
	  
	  testSnpVariant = new SnpGeneticVariant("SNP1", "1", 1, snpAlleles, refSnpAllele, sampleSnpVariants, Collections.<String, Object> emptyMap(), 1, Collections.<String> emptyList(), Collections.<String> emptyList());
	  
	  
  }

  @Test
  public void getAlleles() {
	  Assert.assertEquals(testSnpVariant.getAlleles(), alleles);
  }

  @Test
  public void getMinorAllele() {
    Assert.assertEquals(testSnpVariant.getMinorAllele(), "C");
  }

  @Test
  public void getMinorAlleleFrequency() {
    Assert.assertEquals(testSnpVariant.getMinorAlleleFrequency(), 0.4f);
  }

  @Test
  public void getMinorSnpAllele() {
	  Assert.assertEquals(testSnpVariant.getMinorSnpAllele(), 'C');
  }

  @Test
  public void getRefAllele() {
	  Assert.assertEquals(testSnpVariant.getRefAllele(), refAllele);
  }

  @Test
  public void getSampleSnpVariants() {
    Assert.assertEquals(testSnpVariant.getSampleSnpVariants(), sampleSnpVariants);
  }

  @Test
  public void getSampleVariants() {
    Assert.assertEquals(testSnpVariant.getSampleVariants(), sampleVariants);
  }

  @Test
  public void getSnpAlleles() {
	  Assert.assertEquals(testSnpVariant.getSnpAlleles(), snpAlleles);
  }

  @Test
  public void getSnpRefAlelle() {
	  Assert.assertEquals(testSnpVariant.getSnpRefAlelle(), refSnpAllele);
  }

  @Test
  public void getStopPos() {
	  Assert.assertEquals(testSnpVariant.getStopPos(), null);
  }
}
