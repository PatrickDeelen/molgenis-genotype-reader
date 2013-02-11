package org.molgenis.genotype.plink;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.samtools.util.BlockCompressedInputStream;

import org.apache.commons.io.IOUtils;
import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.GenotypeDataIndex;
import org.molgenis.genotype.IndexedGenotypeData;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.tabix.TabixIndex;
import org.molgenis.genotype.tabix.TabixSequence;
import org.molgenis.util.plink.datatypes.Biallele;
import org.molgenis.util.plink.datatypes.MapEntry;
import org.molgenis.util.plink.datatypes.PedEntry;
import org.molgenis.util.plink.drivers.PedFileDriver;
import org.molgenis.util.plink.readers.MapFileReader;

public class PedMapGenotypeData extends IndexedGenotypeData
{
	private static final char DEFAULT_SEPARATOR = '	';
	private final GenotypeDataIndex index;
	private final PedFileDriver pedFileDriver;
	private final File bzipMapFile;
	private char separator;
	private Map<String, List<Biallele>> sampleGenotypesBySnp;

	public PedMapGenotypeData(File bzipMapFile, File mapIndexFile, File pedFile)
	{
		this(bzipMapFile, mapIndexFile, pedFile, DEFAULT_SEPARATOR);
	}

	public PedMapGenotypeData(File bzipMapFile, File mapIndexFile, File pedFile, char separator)
	{
		try
		{
			index = new TabixIndex(mapIndexFile, bzipMapFile, new PedMapVariantLineMapper(this));
		}
		catch (IOException e)
		{
			throw new GenotypeDataException(e);
		}

		this.separator = separator;
		this.pedFileDriver = new PedFileDriver(pedFile, separator);
		this.bzipMapFile = bzipMapFile;
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

	public List<Biallele> getSampleGenotypes(String snp)
	{
		if (sampleGenotypesBySnp == null)
		{
			Map<Integer, String> idByPosition = new HashMap<Integer, String>();
			MapFileReader mapFileReader;

			try
			{
				mapFileReader = new MapFileReader(new BlockCompressedInputStream(bzipMapFile), separator);
			}
			catch (IOException e)
			{
				throw new GenotypeDataException(e);
			}
			try
			{
				int index = 0;
				for (MapEntry entry : mapFileReader)
				{
					idByPosition.put(index++, entry.getSNP());
				}
			}
			finally
			{
				IOUtils.closeQuietly(mapFileReader);
			}

			sampleGenotypesBySnp = new LinkedHashMap<String, List<Biallele>>();

			for (PedEntry pedEntry : pedFileDriver)
			{
				int index = 0;
				for (Biallele biallele : pedEntry)
				{
					String snpName = idByPosition.get(index);
					if (snpName == null)
					{
						throw new GenotypeDataException("Missing snp at index [" + index + "]");
					}

					List<Biallele> bialleles = sampleGenotypesBySnp.get(snpName);
					if (bialleles == null)
					{
						bialleles = new ArrayList<Biallele>();
						sampleGenotypesBySnp.put(snpName, bialleles);
					}

					bialleles.add(biallele);
					index++;
				}
			}
		}

		return sampleGenotypesBySnp.get(snp);
	}

	protected char getSeparator()
	{
		return separator;
	}
}
