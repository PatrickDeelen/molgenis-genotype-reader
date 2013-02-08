package org.molgenis.genotype;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

import org.molgenis.genotype.variant.GeneticVariant;

public interface VariantQuery extends Closeable
{
	/**
	 * Get a subset of the data in the index
	 * 
	 * @param sequence
	 * @param startPos
	 *            , exclusive
	 * @param stopPos
	 *            , inclusive
	 * 
	 * @return Iterator over the requested data
	 * 
	 * @throws IOException
	 */
	Iterator<GeneticVariant> executeQuery(String sequence, int startPos, int stopPos);

	/**
	 * Gets all variants of a sequence
	 * 
	 * @param sequence
	 * @return
	 * @throws IOException
	 */
	Iterator<GeneticVariant> executeQuery(String sequence);

	/**
	 * Find variants at the specified position
	 * 
	 * @param sequence
	 * @param startPos
	 * @return
	 */
	Iterator<GeneticVariant> executeQuery(String sequence, int startPos);
}
