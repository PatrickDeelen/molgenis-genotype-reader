package org.molgenis.genotype.plink;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.GenotypeDataIndex;
import org.molgenis.genotype.IndexedGenotypeData;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.tabix.TabixIndex;
import org.molgenis.genotype.tabix.TabixSequence;
import org.molgenis.util.plink.datatypes.PedEntry;
import org.molgenis.util.plink.drivers.PedFileDriver;

public class PedMapGenotypeData extends IndexedGenotypeData
{
	private static final char DEFAULT_SEPARATOR = '	';
	private final GenotypeDataIndex index;
	private final File pedFile;
	private char separator;

	public PedMapGenotypeData(File bzipMapFile, File mapIndexFile, File pedFile)
	{
		this(bzipMapFile, mapIndexFile, pedFile, DEFAULT_SEPARATOR);
	}

	public PedMapGenotypeData(File bzipMapFile, File mapIndexFile, File pedFile, char separator)
	{
		try
		{
			index = new TabixIndex(mapIndexFile, bzipMapFile, new PedMapVariantLineMapper(separator + ""));
		}
		catch (IOException e)
		{
			throw new GenotypeDataException(e);
		}

		this.pedFile = pedFile;
		this.separator = separator;
	}

	@Override
	public List<Sequence> getSequences()
	{
		List<String> seqNames = getSeqNames();

		List<Sequence> sequences = new ArrayList<Sequence>(seqNames.size());
		for (String seqName : seqNames)
		{
			sequences.add(new TabixSequence(seqName, null, index));
		}

		return sequences;
	}

	@Override
	public List<Sample> getSamples()
	{
		PedFileDriver pedFileDriver = new PedFileDriver(pedFile, separator);
		try
		{
			List<Sample> samples = new ArrayList<Sample>();
			for (PedEntry pedEntry : pedFileDriver)
			{
				samples.add(new Sample(pedEntry.getIndividual(), pedEntry.getFamily(), Collections
						.<String, Object> emptyMap()));
			}

			return samples;
		}
		finally
		{
			IOUtils.closeQuietly(pedFileDriver);
		}

	}

	@Override
	public List<Annotation> getSampleAnnotations()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Annotation getSampleAnnotation(String annotationId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected GenotypeDataIndex getIndex()
	{
		return index;
	}

	@Override
	protected Map<String, Annotation> getVariantAnnotationsMap()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
