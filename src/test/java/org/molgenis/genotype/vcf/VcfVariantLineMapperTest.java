package org.molgenis.genotype.vcf;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SnpGeneticVariant;
import org.molgenis.genotype.variant.VariantLineMapper;
import org.testng.annotations.Test;

public class VcfVariantLineMapperTest
{
	private static final List<String> COL_NAMES = Arrays.asList("#CHROM", "POS", "ID", "REF", "ALT", "QUAL", "FILTER",
			"INFO", "FORMAT", "sample1");
	private static final List<String> SAMPLE_NAMES = Arrays.asList("sample1");

	@Test
	public void mapLineSnp()
	{
		String line = "1	565286	rs1578391	C	T	.	flt	NS=1;DP=5;AF=1.000;ANNOT=INT;GI=LOC100131754	GT:DP:EC:CONFS	1/1:5:5:5.300,5.300,1.000,1.000,1.000,1.000,1.000";

		VariantLineMapper mapper = new VcfVariantLineMapper(COL_NAMES, SAMPLE_NAMES,
				Collections.<Annotation> emptyList(), Collections.<String, String> emptyMap());
		GeneticVariant var = mapper.mapLine(line);
		assertNotNull(var);
		assertTrue(var instanceof SnpGeneticVariant);
		SnpGeneticVariant variant = (SnpGeneticVariant) var;

		assertEquals(variant.getSequenceName(), "1");
		assertEquals(variant.getStartPos(), 565286);
		assertEquals(variant.getPrimaryVariantId(), "rs1578391");

		List<String> ids = variant.getAllIds();
		assertNotNull(ids);
		assertEquals(ids.size(), 1);
		assertEquals(ids.get(0), "rs1578391");

		assertEquals(variant.getRefAllele(), "C");

		List<String> alleles = variant.getAlleles();
		assertNotNull(alleles);
		assertEquals(alleles.size(), 2);
		assertEquals(alleles.get(0), "C");
		assertEquals(alleles.get(1), "T");

		assertEquals(variant.getSnpRefAlelle(), 'C');
		char[] snpAlleles = variant.getSnpAlleles();
		assertNotNull(snpAlleles);
		assertEquals(snpAlleles.length, 2);
		assertEquals(snpAlleles[0], 'C');
		assertEquals(snpAlleles[1], 'T');
	}

	@Test
	public void mapLineInsert()
	{
		String line = "1	565286	.	C	CTA,CA	.	flt	NS=1;DP=5;AF=1.000;ANNOT=INT;GI=LOC100131754	GT:DP:EC:CONFS	1/1:5:5:5.300,5.300,1.000,1.000,1.000,1.000,1.000";

		VariantLineMapper mapper = new VcfVariantLineMapper(COL_NAMES, SAMPLE_NAMES,
				Collections.<Annotation> emptyList(), Collections.<String, String> emptyMap());
		GeneticVariant variant = mapper.mapLine(line);
		assertNotNull(variant);

		assertEquals(variant.getSequenceName(), "1");
		assertEquals(variant.getStartPos(), 565286);
		assertNull(variant.getPrimaryVariantId());

		List<String> ids = variant.getAlternativeVariantIds();
		assertNotNull(ids);
		assertTrue(ids.isEmpty());

		assertEquals(variant.getRefAllele(), "C");

		List<String> alleles = variant.getAlleles();
		assertNotNull(alleles);
		assertEquals(alleles.size(), 3);
		assertTrue(alleles.contains("C"));
		assertTrue(alleles.contains("CTA"));
		assertTrue(alleles.contains("CA"));
	}
}
