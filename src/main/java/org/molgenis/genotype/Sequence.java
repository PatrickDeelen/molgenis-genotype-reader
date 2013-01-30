package org.molgenis.genotype;

import org.molgenis.genotype.variant.SnpVariantHandler;
import org.molgenis.genotype.variant.VariantHandler;

/**
 * Represents a genetic sequence for example a chromosome
 * 
 * @author erwin
 * 
 */
public interface Sequence
{
	public String getName();

	public Integer getLength();

	/**
	 * Returns if this sequence represents a chromosome or not
	 */
	public boolean isChromosome();

	/**
	 * Get all variants in this sequence. For every variant in this sequence
	 * VariantHandler.handle(variant) is called.
	 * 
	 * @param handler
	 */
	public void variants(VariantHandler handler);

	/**
	 * Get all snp variants in this sequence. For every snp variant in this
	 * sequence SnpVariantHandler.handle(variant) is called.
	 * 
	 * @param handler
	 */
	public void snpVariants(SnpVariantHandler handler);
}
