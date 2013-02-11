package org.molgenis.genotype;

import java.io.IOException;
import java.util.List;

import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SnpGeneticVariant;

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
	 * Get the variants at the specified position
	 * 
	 * @param seqName
	 * @param startPos
	 * @return all variants found at startPos, can be empty if none found
	 */
	List<GeneticVariant> getVariantsByPos(String seqName, int startPos);

	/**
	 * Get the snp variants at the specified position
	 * 
	 * @param seqName
	 * @param startPos
	 * @return all snp variants found at startPos, can be empty if none found
	 */
	List<SnpGeneticVariant> getSnpVariantsByPos(String seqName, int startPos);

	/**
	 * Get all variants of a sequence Close the VariantQueryResult after use
	 * 
	 * @param seqName
	 * @throws GenotypeDataException
	 *             if the seqName does not exist
	 */
	VariantQueryResult getSeqVariants(String seqName);

	/**
	 * Get all sample variant values of the variants at the specified position
	 * 
	 * The list of variants can contain null !!!! if unknown
	 */
	List<List<String>> getSampleGeneticVariants(String seqName, int startPos);

	/**
	 * Get all samples
	 * 
	 * @return
	 */
	List<Sample> getSamples();

	List<Annotation> getSampleAnnotations();

	Annotation getSampleAnnotation(String annotationId);
}
