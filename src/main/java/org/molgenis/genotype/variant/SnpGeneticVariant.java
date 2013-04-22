package org.molgenis.genotype.variant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.VariantAlleles;

public class SnpGeneticVariant extends GeneticVariant
{

	public SnpGeneticVariant(List<String> ids, String sequenceName, int startPos, VariantAlleles alleles,
			String refAllele, Map<String, ?> annotationValues, List<String> altDescriptions, List<String> altTypes,
			SampleVariantsProvider sampleVariantsProvider)
	{
		super(ids, sequenceName, startPos, alleles, refAllele, annotationValues, null, altDescriptions, altTypes,
				sampleVariantsProvider, GeneticVariant.Type.SNP);
	}

	public SnpGeneticVariant(String id, String sequenceName, int startPos, VariantAlleles alleles, String refAllele,
			Map<String, ?> annotationValues, List<String> altDescriptions, List<String> altTypes,
			SampleVariantsProvider sampleVariantsProvider)
	{
		super(id, sequenceName, startPos, alleles, refAllele, annotationValues, null, altDescriptions, altTypes,
				sampleVariantsProvider, GeneticVariant.Type.SNP);
	}

	public List<char[]> getSampleSnpVariants()
	{
		List<char[]> sampleSnpVariants = new ArrayList<char[]>();
		for (VariantAlleles variantAlleles : getSampleVariants())
		{
			sampleSnpVariants.add(variantAlleles.getAllelesAsChars());
		}

		return sampleSnpVariants;
	}

	public char[] getSnpAlleles()
	{
		return getVariantAlleles().getAllelesAsChars();
	}
}
