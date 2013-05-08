package org.molgenis.genotype.variant;

import java.util.List;

import org.molgenis.genotype.VariantAlleles;

public class SwappingSampleVariantsProvider implements SampleVariantsProvider
{
	private SampleVariantsProvider sampleVariantsProvider;

	public SwappingSampleVariantsProvider(SampleVariantsProvider sampleVariantsProvider)
	{
		this.sampleVariantsProvider = sampleVariantsProvider;
	}

	@Override
	public List<VariantAlleles> getSampleVariants(GeneticVariant variant)
	{
		List<VariantAlleles> alleles = sampleVariantsProvider.getSampleVariants(variant);
		for (int i = 0; i < alleles.size(); i++)
		{
			alleles.set(i, alleles.get(i).getComplement());
		}

		return alleles;
	}

}
