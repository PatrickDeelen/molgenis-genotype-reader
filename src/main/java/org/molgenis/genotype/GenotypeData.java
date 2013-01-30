package org.molgenis.genotype;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import org.molgenis.genotype.annotation.Annotation;

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
}
