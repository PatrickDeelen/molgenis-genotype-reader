package org.molgenis.genotype.tabix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.samtools.util.BlockCompressedInputStream;

import org.apache.commons.io.IOUtils;
import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.VariantQuery;
import org.molgenis.genotype.VariantQueryResult;
import org.molgenis.genotype.tabix.TabixIndex.TabixIterator;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.VariantLineMapper;
import org.molgenis.io.vcf.VcfRecord;
import org.molgenis.io.vcf.VcfSampleGenotype;

/**
 * Execute a query on the tabix iondex to get a subset of the data
 * 
 * @author erwin
 * 
 */
public class TabixQuery implements VariantQuery
{
	private BlockCompressedInputStream inputStream;;
	private final TabixIndex index;
	private final VariantLineMapper variantLineMapper;

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
	 * Get a subset of the data, returns the raw data lines from the gziped data file
	 * 
	 * @param sequence
	 * @param startPos
	 *            , exclusive
	 * @param stopPos
	 *            , inclusive
	 * 
	 * @throws IOException
	 */
	@Override
	public VariantQueryResult executeQuery(String sequence, int startPos, int stopPos)
	{
		if (startPos < 0) throw new IllegalArgumentException("StartPos must be bigger then 0");
		if (stopPos <= startPos) throw new IllegalArgumentException("StopPos must be bigger then startPos ");

		try
		{
			TabixIterator tabixIterator = index.queryTabixIndex(sequence, startPos, stopPos, inputStream);
			return new TabixQueryResult(inputStream, new TabixQueryIterator(tabixIterator, variantLineMapper));
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
	@Override
	public VariantQueryResult executeQuery(String sequence)
	{
		return executeQuery(sequence, 0, Integer.MAX_VALUE);
	}

	@Override
	public VariantQueryResult executeQuery(String sequence, int startPos)
	{
		return executeQuery(sequence, startPos - 1, startPos);
	}

	private static class TabixQueryIterator implements Iterator<GeneticVariant>
	{
		private final TabixIterator tabixIterator;
		private final VariantLineMapper variantLineMapper;
		private String line;

		public TabixQueryIterator(TabixIterator tabixIterator, VariantLineMapper variantLineMapper) throws IOException
		{
			this.tabixIterator = tabixIterator;
			this.variantLineMapper = variantLineMapper;
			line = tabixIterator == null ? null : tabixIterator.next();
		}

		@Override
		public boolean hasNext()
		{
			return line != null;
		}

		@Override
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

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}

	}

	// TODO remove this, is vcf specific
	@Override
	public List<Alleles> findSamplesForVariant(String sequence, int startPos, List<String> alleles,
			List<String> columnNames, List<String> sampleNames)
	{
		try
		{
			TabixIterator tabixIterator = index.queryTabixIndex(sequence, startPos - 1, startPos, inputStream);

			if (tabixIterator != null)
			{
				String line = tabixIterator.next();
				while (line != null)
				{
					VcfRecord record = new VcfRecord(line, columnNames);
					if (record.getChrom().equalsIgnoreCase(sequence) && (record.getPos() == startPos)
							&& record.getAlleles().equals(alleles))
					{
						List<Alleles> sampleVariants = new ArrayList<Alleles>(sampleNames.size());
						for (String sampleName : sampleNames)
						{
							VcfSampleGenotype geno = record.getSampleGenotype(sampleName);
							if (geno == null) throw new GenotypeDataException("Missing GT format value for sample ["
									+ sampleName + "]");
							sampleVariants.add(Alleles.createBasedOnString(geno.getSamleVariants(alleles)));
						}

						return sampleVariants;
					}

					line = tabixIterator.next();
				}
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			IOUtils.closeQuietly(inputStream);
		}

		return null;
	}
}
