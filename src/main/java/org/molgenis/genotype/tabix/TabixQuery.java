package org.molgenis.genotype.tabix;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import net.sf.samtools.util.BlockCompressedInputStream;

import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.VariantQuery;
import org.molgenis.genotype.tabix.TabixIndex.TabixIterator;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.VariantLineMapper;

/**
 * Execute a query on the tabix iondex to get a subset of the data
 * 
 * @author erwin
 * 
 */
public class TabixQuery implements VariantQuery
{
	private BlockCompressedInputStream inputStream;;
	private TabixIndex index;
	private VariantLineMapper variantLineMapper;

	public TabixQuery(File bzipFile, TabixIndex index, VariantLineMapper variantLineMapper)
	{
		if (bzipFile == null) throw new IllegalArgumentException("BzipFile is null");
		if (index == null) throw new IllegalArgumentException("Index is null");
		if (variantLineMapper == null) throw new IllegalArgumentException("VariantLineMapper is null");
		try
		{
			inputStream = new BlockCompressedInputStream(bzipFile);
		}
		catch (IOException e)
		{
			throw new GenotypeDataException("IOExcption creating BlockCompressedInputStream for " + bzipFile.getName(),
					e);
		}
		this.index = index;
		this.variantLineMapper = variantLineMapper;
	}

	/**
	 * Get a subset of the data, returns the raw data lines from the gziped data
	 * file
	 * 
	 * @param sequence
	 * @param startPos
	 *            , exclusive
	 * @param stopPos
	 *            , inclusive
	 * @return Iterator over the requested data
	 * 
	 * @throws IOException
	 */
	public Iterator<GeneticVariant> executeQuery(String sequence, int startPos, int stopPos)
	{
		if (startPos < 0) throw new IllegalArgumentException("StartPos must be bigger then 0");
		if (stopPos <= startPos) throw new IllegalArgumentException("StopPos must be bigger then startPos ");

		try
		{
			TabixIterator tabixIterator = index.queryTabixIndex(sequence, startPos, stopPos, inputStream);
			return new TabixQueryIterator(tabixIterator, variantLineMapper);
		}
		catch (IOException e)
		{
			throw new GenotypeDataException(e);
		}
	}

	/**
	 * Gets all variants of a sequence
	 * 
	 * @param sequence
	 * @return
	 * @throws IOException
	 */
	public Iterator<GeneticVariant> executeQuery(String sequence)
	{
		return executeQuery(sequence, 0, Integer.MAX_VALUE);
	}

	public GeneticVariant executeQuery(String sequence, int startPos)
	{
		Iterator<GeneticVariant> lines = executeQuery(sequence, startPos - 1, startPos);
		if (lines.hasNext())
		{
			return lines.next();
		}

		return null;
	}

	public void close() throws IOException
	{
		inputStream.close();
	}

	private static class TabixQueryIterator implements Iterator<GeneticVariant>
	{
		private TabixIterator tabixIterator;
		private VariantLineMapper variantLineMapper;
		private String line;

		public TabixQueryIterator(TabixIterator tabixIterator, VariantLineMapper variantLineMapper) throws IOException
		{
			this.tabixIterator = tabixIterator;
			this.variantLineMapper = variantLineMapper;
			line = tabixIterator == null ? null : tabixIterator.next();
		}

		public boolean hasNext()
		{
			return line != null;
		}

		public GeneticVariant next()
		{
			GeneticVariant variant = variantLineMapper.mapLine(line);

			try
			{
				line = tabixIterator.next();
			}
			catch (IOException e)
			{
				throw new RuntimeException("Exception calling next on TabixIndex.TabixIterator", e);
			}

			return variant;
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}

	}
}
