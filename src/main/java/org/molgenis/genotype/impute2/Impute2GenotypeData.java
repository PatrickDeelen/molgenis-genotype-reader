package org.molgenis.genotype.impute2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.molgenis.genotype.GenotypeDataIndex;
import org.molgenis.genotype.IndexedGenotypeData;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.SimpleSequence;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.tabix.TabixIndex;
import org.molgenis.genotype.util.Utils;
import org.molgenis.io.csv.CsvReader;
import org.molgenis.util.tuple.Tuple;

/**
 * GenotypeData for haps/sample files see http://www.shapeit.fr/
 * 
 * First run <code>index-haps.sh yourfile.haps<code> to create the tabix index file
 * 
 * 
 * @author erwin
 * 
 */
public class Impute2GenotypeData extends IndexedGenotypeData
{
	private GenotypeDataIndex index;
	private File sampleFile;

	public Impute2GenotypeData(File bzipHapsFile, File tabixIndexFile, File sampleFile) throws IOException
	{
		if (bzipHapsFile == null) throw new IllegalArgumentException("bzipHapsFile is null");
		if (!bzipHapsFile.isFile()) throw new FileNotFoundException("bzipHapsFile file not found at "
				+ bzipHapsFile.getAbsolutePath());
		if (!bzipHapsFile.canRead()) throw new IOException("bzipHapsFile file not found at "
				+ bzipHapsFile.getAbsolutePath());

		if (tabixIndexFile == null) throw new IllegalArgumentException("tabixIndexFile is null");
		if (!tabixIndexFile.isFile()) throw new FileNotFoundException("tabixIndexFile file not found at "
				+ tabixIndexFile.getAbsolutePath());
		if (!tabixIndexFile.canRead()) throw new IOException("tabixIndexFile file not found at "
				+ tabixIndexFile.getAbsolutePath());

		if (sampleFile == null) throw new IllegalArgumentException("sampleFile is null");
		if (!sampleFile.isFile()) throw new FileNotFoundException("sampleFile file not found at "
				+ sampleFile.getAbsolutePath());
		if (!sampleFile.canRead()) throw new IOException("sampleFile file not found at " + sampleFile.getAbsolutePath());

		index = new TabixIndex(tabixIndexFile, bzipHapsFile, new Impute2VariantLineMapper());
		this.sampleFile = sampleFile;
	}

	@Override
	public Iterable<Sequence> getSequences()
	{
		List<Sequence> sequences = new ArrayList<Sequence>();
		for (String seqName : getSeqNames())
		{
			sequences.add(new SimpleSequence(seqName, null, this));
		}

		return sequences;
	}

	@Override
	public List<Sample> getSamples()
	{
		List<Sample> samples = new ArrayList<Sample>();

		CsvReader reader = null;
		try
		{
			reader = new CsvReader(sampleFile, ' ');

			List<String> colNames = Utils.iteratorToList(reader.colNamesIterator());

			Tuple dataTypes = null;
			boolean firstRow = true;

			for (Tuple tuple : reader)
			{
				if (firstRow)// First after the headers contains the datatype of a column
				{
					dataTypes = tuple;
					firstRow = false;
				}
				else
				{

					String familyId = tuple.getString(0);
					String sampleId = tuple.getString(1);
					double missing = tuple.getDouble(2);

					Map<String, Object> annotations = new HashMap<String, Object>();
					annotations.put("missing", missing);

					for (int i = 3; i < tuple.getNrCols(); i++)
					{
						if (dataTypes.getString(i).equalsIgnoreCase("D"))
						{
							annotations.put(colNames.get(i), tuple.getInt(i));
						}
						else if (dataTypes.getString(i).equalsIgnoreCase("C")
								|| dataTypes.getString(i).equalsIgnoreCase("P"))
						{
							annotations.put(colNames.get(i), tuple.getDouble(i));
						}
						else if (dataTypes.getString(i).equalsIgnoreCase("B"))
						{
							annotations.put(colNames.get(i), tuple.getBoolean(i));
						}
						else
						{
							annotations.put(colNames.get(i), tuple.getString(i));
						}
					}

					samples.add(new Sample(sampleId, familyId, annotations));
				}

			}

		}
		catch (FileNotFoundException e)
		{
			throw new RuntimeException("File [" + sampleFile.getAbsolutePath() + "] does not exists", e);
		}
		catch (IOException e)
		{
			throw new RuntimeException("IOException parsing [" + sampleFile.getAbsolutePath() + "]", e);
		}
		finally
		{
			IOUtils.closeQuietly(reader);
		}
		return samples;
	}

	@Override
	protected GenotypeDataIndex getIndex()
	{
		return index;
	}

	@Override
	protected Map<String, Annotation> getVariantAnnotationsMap()
	{
		return Collections.emptyMap();
	}

}
