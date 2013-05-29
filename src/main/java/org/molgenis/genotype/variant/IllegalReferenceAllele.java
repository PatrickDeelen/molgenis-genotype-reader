package org.molgenis.genotype.variant;

import org.molgenis.genotype.VariantAlleles;

public class IllegalReferenceAllele extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private IllegalReferenceAllele()
	{
	}

	public IllegalReferenceAllele(VariantAlleles variantAlleles)
	{
		super("Illigal reference allele. Only 1 allele can be ref but found " + variantAlleles.getAlleleCount()
				+ " alleles");
	}

}
