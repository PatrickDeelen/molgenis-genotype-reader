package org.molgenis.genotype;

import java.util.Iterator;
import java.util.List;

import org.molgenis.genotype.variant.GeneticVariant;

public interface RandomAccessGenotypeData extends GenotypeData
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
	 * @return Iterable of Sequences
	 */
	Iterable<Sequence> getSequences();

	/**
	 * Get a Sequence buy it's name. Name is case sensitive
	 * 
	 * @param name
	 * @return the Sequence or null if not found
	 */
	Sequence getSequenceByName(String name);

	/**
	 * Get the variants at the specified position
	 * 
	 * @param seqName
	 * @param startPos
	 * @return all variants found at startPos, can be empty if none found
	 */
	List<GeneticVariant> getVariantsByPos(String seqName, int startPos);

	/**
	 * Get the SNP variant at the specified position. Only one SNP possible per
	 * position.
	 * 
	 * @param seqName
	 * @param startPos
	 * @return The SNP found at this startPos, will be null if not present
	 */
	GeneticVariant getSnpVariantByPos(String seqName, int startPos);

	Iterator<GeneticVariant> getSequenceGeneticVariants(String seqName);

}
