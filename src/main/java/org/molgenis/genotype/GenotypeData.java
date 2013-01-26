package org.molgenis.genotype;

import java.io.IOException;
import java.util.List;

import org.molgenis.genotype.annotation.Annotation;

/**
 * Interface that represents genomic data, can be backed by different data types
 * 
 * @author erwin
 * 
 */
public interface GenotypeData
{
	/**
	 * Get all sequencenames
	 * 
	 * @return List of String
	 * @throws IOException
	 */
	List<String> getSeqNames() throws IOException;

	/**
	 * Get all possible variant annotations
	 * 
	 * @return List of Annotation
	 * @throws IOException
	 */
	List<Annotation> getVariantAnnotations() throws IOException;

	/**
	 * Get a specific variant annotation
	 * 
	 * @param annotationId
	 * @return The Annotation or null if not found
	 * @throws IOException
	 */
	Annotation getVariantAnnotation(String annotationId) throws IOException;

	List<Sequence> getSequences();

}
