package org.molgenis.genotype;

import java.io.IOException;
import java.util.List;

public interface VariantQuery
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
	 * @return VariantQueryResult
	 * 
	 * @throws IOException
	 */
	VariantQueryResult executeQuery(String sequence, int startPos, int stopPos);

	/**
	 * Gets all variants of a sequence
	 * 
	 * @param sequence
	 * @return
	 * @throws IOException
	 */
	VariantQueryResult executeQuery(String sequence);

	/**
	 * Find variants at the specified position
	 * 
	 * @param sequence
	 * @param startPos
	 * @return
	 */
	VariantQueryResult executeQuery(String sequence, int startPos);

	List<VariantAlleles> findSamplesForVariant(String sequence, int startPos, VariantAlleles alleles,
			List<String> columnNames, List<String> sampleNames);
}
