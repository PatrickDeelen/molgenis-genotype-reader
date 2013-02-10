package org.molgenis.genotype.variant;

import java.util.List;
import java.util.Map;

public interface GeneticVariant
{
	/**
	 * A Variant can have multiple id's (it's known under different names). The
	 * compoundId is a concatination of these ids with ';' as separator. This is
	 * the first in the list
	 * 
	 * @return String
	 */
	String getPrimaryVariantId();

	/**
	 * Gets all the other id's (names) besides the primaryVariantId by wich this
	 * variant is known.
	 * 
	 * @return List of String
	 */
	List<String> getAlternativeVariantIds();

	/**
	 * Gets the starting position on the sequence
	 * 
	 * @return int
	 */
	int getStartPos();

	/**
	 * Get the rnd position of the variant, returns null if unknown
	 * 
	 * @return
	 */
	Integer getStopPos();

	/**
	 * Get the Sequence this variant is located on
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
	 * Returns list sample variants. The list of variants can contain null !!!!
	 * if unknown
	 */
	List<List<String>> getSampleVariants();

	/**
	 * Get the annotations for this variant. The key is the annotationId, the
	 * value is of the type defined by the Annotation (see
	 * GenotypeData.getVariantAnnotations()
	 * 
	 * @return
	 */
	Map<String, ?> getAnnotationValues();

	List<String> getAltDescriptions();

	List<String> getAltTypes();

	/**
	 * Get the frequency of the minor allele
	 * 
	 * @return the minor allele frequency
	 */
	float getMinorAlleleFrequency();

	/**
	 * Get the minor allele
	 * 
	 * @return the minor allele
	 */
	String getMinorAllele();

}
