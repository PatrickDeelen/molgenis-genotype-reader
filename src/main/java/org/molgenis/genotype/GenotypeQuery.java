package org.molgenis.genotype;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

public interface GenotypeQuery extends Closeable
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
	public Iterator<String> executeQuery(String sequence, int startPos, int stopPos);

	/**
	 * Gets all variants of a sequence
	 * 
	 * @param sequence
	 * @return
	 * @throws IOException
	 */
	public Iterator<String> executeQuery(String sequence);

	/**
	 * Find a variant at the specified position
	 * 
	 * @param sequence
	 * @param startPos
	 * @return The variant or null is not found
	 */
	public String executeQuery(String sequence, int startPos);
}
