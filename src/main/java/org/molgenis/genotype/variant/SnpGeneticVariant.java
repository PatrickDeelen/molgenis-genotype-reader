package org.molgenis.genotype.variant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.VariantAlleles;
import org.molgenis.genotype.util.Utils;

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

	/**
	 * The new SnpGeneticVariant will return a new SnpGeneticVariant that has
	 * for all alleles the complement of the alleles in this SnpGeneticVariant
	 * 
	 * @return
	 */
	public SnpGeneticVariant swapAlleles()
	{
		String swappedRef = getRefAllele() == null ? null : String.valueOf(Utils.getComplementNucleotide(getRefAllele()
				.charAt(0)));

		SampleVariantsProvider provider = new SwappingSampleVariantsProvider(sampleVariantsProvider);
		List<String> ids = getVariantId().getVariantIds();

		return new SnpGeneticVariant(ids, getSequenceName(), getStartPos(), getVariantAlleles().swap(), swappedRef,
				getAnnotationValues(), getStopPos(), getAltDescriptions(), getAltTypes(), provider);
	}

}
