package org.molgenis.genotype;

import java.util.List;

import org.molgenis.genotype.variant.GeneticVariantOld;
import org.molgenis.genotype.variant.SampleVariantsProvider;

public class DummySampleVariantsProvider implements SampleVariantsProvider
{

	private final List<Alleles> variantAlleles;

	public DummySampleVariantsProvider(List<Alleles> variantAlleles)
	{
		super();
		this.variantAlleles = variantAlleles;
	}

	@Override
	public List<Alleles> getSampleVariants(GeneticVariantOld variant)
	{
		return variantAlleles;
	}

}
