package org.molgenis.genotype.vcf;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.variant.GenericGeneticVariant;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SnpGeneticVariant;
import org.molgenis.genotype.variant.VariantLineMapper;
import org.molgenis.io.vcf.VcfRecord;
import org.molgenis.io.vcf.VcfSampleGenotype;

public class VcfVariantLineMapper implements VariantLineMapper
{
	private List<String> colNames;
	private List<String> sampleNames;

	public VcfVariantLineMapper(List<String> colNames, List<String> sampleNames)
	{
		this.colNames = colNames;
		this.sampleNames = sampleNames;
	}

	public GeneticVariant mapLine(String line)
	{
		VcfRecord record = new VcfRecord(line, colNames);

		List<String> ids = record.getId();
		String sequenceName = record.getChrom();
		Integer startPos = record.getPos();
		List<String> alleles = record.getAlleles();
		String refAllele = record.getRef();

		// Get the GT format values (example: 0/0/1)
		Map<String, List<String>> sampleVariantsBySampleId = new LinkedHashMap<String, List<String>>();
		for (String sampleName : sampleNames)
		{
			VcfSampleGenotype geno = record.getSampleGenotype(sampleName);
			if (geno == null) throw new GenotypeDataException("Missing GT format value for sample [" + sampleName + "]");

			List<String> sampleVariants = geno.getSamleVariants(alleles);
			sampleVariantsBySampleId.put(sampleName, Collections.unmodifiableList(sampleVariants));
		}

		GeneticVariant variant;
		if (isSnp(alleles))
		{
			variant = new SnpGeneticVariant(ids, sequenceName, startPos, toCharArray(alleles), refAllele.charAt(0),
					sampleVariantsBySampleId);
		}
		else
		{
			variant = new GenericGeneticVariant(ids, sequenceName, startPos, alleles, refAllele,
					sampleVariantsBySampleId);
		}

		return variant;
	}

	// Variant is a SNP if all alleles are one nucleotide long
	private boolean isSnp(List<String> alleles)
	{
		if (alleles.size() < 2)
		{
			// This is a monomorphic reference (i.e. with no alternate alleles)
			return false;
		}

		for (String allele : alleles)
		{
			if (allele.length() != 1)
			{
				return false;
			}
		}

		return true;
	}

	private char[] toCharArray(List<String> alleles)
	{
		char[] result = new char[alleles.size()];
		for (int i = 0; i < alleles.size(); i++)
		{
			result[i] = alleles.get(i).charAt(0);
		}

		return result;
	}

}
