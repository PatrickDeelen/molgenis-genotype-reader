package org.molgenis.genotype;

import java.util.List;

import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SampleVariantsProvider;

public class DummySampleVariantsProvider implements SampleVariantsProvider
{

	private final List<VariantAlleles> variantAlleles;

	public DummySampleVariantsProvider(List<VariantAlleles> variantAlleles)
	{
		super();
		this.variantAlleles = variantAlleles;
	}

	@Override
	public List<VariantAlleles> getSampleVariants(GeneticVariant variant)
	{
		return variantAlleles;
	}

}
