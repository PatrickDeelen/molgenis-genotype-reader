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
	private List<GeneticVariant> variants = new ArrayList<GeneticVariant>();

	public List<GeneticVariant> getVariants()
	{
		return variants;
	}

	public void handle(GeneticVariant variant)
	{
		variants.add(variant);
	}

}
