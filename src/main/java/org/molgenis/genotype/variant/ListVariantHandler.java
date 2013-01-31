package org.molgenis.genotype.variant;

import java.util.ArrayList;
import java.util.List;

/**
 * VariantHandeler implementation that simply stores all variants in a List. Be
 * cautious using this class because the List can become very long.
 * 
 * @author erwin
 * 
 */
public class ListVariantHandler implements VariantHandler
{
	private final List<GeneticVariant> variants = new ArrayList<GeneticVariant>();

	public List<GeneticVariant> getVariants()
	{
		return variants;
	}

	public boolean handle(GeneticVariant variant)
	{
		variants.add(variant);
		return true;
	}

}
