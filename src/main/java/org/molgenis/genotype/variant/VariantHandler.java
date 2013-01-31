package org.molgenis.genotype.variant;

/**
 * Callback interface for iterating over the VariantQuery results. Return false
 * if you want to quit iterating
 * 
 * @author erwin
 * 
 */
public interface VariantHandler
{
	boolean handle(GeneticVariant variant);
}
