package org.molgenis.genotype.variant;

import java.util.List;

/**
 * Loads the sample variants for a variant. Is used to enable lazy loading of
 * the sample variants
 * 
 * @author erwin
 * 
 */
public interface SampleVariantsProvider
{
	List<List<String>> getSampleVariants(GeneticVariant variant);
}
