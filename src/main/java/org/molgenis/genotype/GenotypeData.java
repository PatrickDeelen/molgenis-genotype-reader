package org.molgenis.genotype;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.VariantHandler;

/**
 * Interface that represents genomic data, can be backed by different data types
 * 
 * @author erwin
 * 
 */
public interface GenotypeData extends Closeable
{
	/**
	 * Get all sequencenames
	 * 
	 * @return List of String
	 */
	List<String> getSeqNames();

	/**
	 * Get all sequences in the data
	 * 
	 * @return List of Sequence
	 */
	List<Sequence> getSequences();

	/**
	 * Get a Sequence buy it's name. Name is case sensitive
	 * 
	 * @param name
	 * @return the Sequence or null if not found
	 */
	Sequence getSequenceByName(String name);

	/**
	 * Get all possible variant annotations
	 * 
	 * @return List of Annotation
	 */
	List<Annotation> getVariantAnnotations();

	/**
	 * Get a specific variant annotation
	 * 
	 * @param annotationId
	 * @return The Annotation or null if not found
	 * @throws IOException
	 */
	Annotation getVariantAnnotation(String annotationId);

	/**
	 * Get the variant at the specified position
	 * 
	 * @param seqName
	 * @param startPos
	 * @return the variant or null if not found
	 */
	GeneticVariant getVariant(String seqName, int startPos);

	/**
	 * Get all variants of a sequence For every variant
	 * VariantHandler.handle(variant) is called.
	 * 
	 * @param seqName
	 * @param handler
	 */
	void seqVariants(String seqName, VariantHandler handler);

	/**
	 * Get all sample variant values of a variant at the specified position
	 * 
	 * Returns a Map of sample variants by sampleId. The list of variants can
	 * contain null !!!! if unknown
	 */
	Map<String, List<String>> getSampleGeneticVariants(String seqName, int startPos);

	/**
	 * Get all samples
	 * 
	 * @return
	 */
	List<Sample> getSamples();

	List<Annotation> getSampleAnnotations();

	Annotation getSampleAnnotation(String annotationId);
}
