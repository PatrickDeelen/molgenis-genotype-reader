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
	 * The alleles of this genetic variants will be swapped for all future
	 * access
	 * 
	 * @return
	 */
	public void swapAlleles()
	{
		String swappedRef = this.getRefAllele() == null ? null : String.valueOf(Utils.getComplementNucleotide(this
				.getRefAllele().charAt(0)));
		this.setRefAllele(swappedRef);

		this.setSampleVariantsProvider(new SwappingSampleVariantsProvider(this.getSampleVariantsProvider()));
		this.setAlleles(this.getAlleles().getComplement());

		if (this.minorAllele != null)
		{
			this.setMinorAllele(String.valueOf(Utils.getComplementNucleotide(this.getMinorAllele().charAt(0))));
		}

	}

}
