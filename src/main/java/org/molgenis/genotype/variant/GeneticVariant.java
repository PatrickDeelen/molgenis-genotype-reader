package org.molgenis.genotype.variant;

import java.util.List;
import java.util.Map;

public interface GeneticVariant
{
	/**
	 * A Variant can have multiple id's (it's known under different names). The
	 * compoundId is a concatination of these ids with ';' as separator.
	 * 
	 * @return the concatinated String
	 */
	String getCompoundId();

	/**
	 * Gets all the id's (names) by wich this variant is known.
	 * 
	 * @return List of String
	 */
	List<String> getIds();

	/**
	 * Gets the starting position on the sequence
	 * 
	 * @return int
	 */
	int getStartPos();

	/**
	 * Get the Sequence this variant is lying on
	 * 
	 * @return the Sequence
	 */
	String getSequenceName();

	/**
	 * Get all posible alleles (including the reference) The first value is the
	 * reference value
	 * 
	 * @return List of String
	 */
	List<String> getAlleles();

	/**
	 * Gets the reference allele
	 * 
	 * @return String
	 */
	String getRefAllele();

	/**
	 * Returns a Map of sample variants by sampleId.
	 */
	Map<String, List<String>> getSampleVariants();
}
