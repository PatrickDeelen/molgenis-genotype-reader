package org.molgenis.genotype.variant;

import java.util.List;

import org.molgenis.genotype.Alleles;

/**
 * Loads the sample variants for a variant. Is used to enable lazy loading of the sample variants
 * 
 * @author erwin
 * 
 */
public interface SampleVariantsProvider
{
	List<Alleles> getSampleVariants(GeneticVariant variant);

	/**
	 * Returns for each sample if it phased or not
	 * 
	 * @return
	 */
	List<Boolean> getSamplePhasing(GeneticVariant variant);

	int cacheSize();
}
