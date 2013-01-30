package org.molgenis.genotype.vcf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.molgenis.genotype.AbstractGenotypeData;
import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.GenotypeDataIndex;
import org.molgenis.genotype.GenotypeQuery;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.annotation.VcfAnnotation;
import org.molgenis.genotype.tabix.TabixSequence;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.VariantLineMapper;
import org.molgenis.io.vcf.VcfContig;
import org.molgenis.io.vcf.VcfInfo;
import org.molgenis.io.vcf.VcfReader;

public class VcfGenotypeData extends AbstractGenotypeData
{
	private GenotypeDataIndex index;
	private VcfReader reader;
	private VariantLineMapper variantLineMapper;

	public VcfGenotypeData(GenotypeDataIndex index, VcfReader reader)
	{
		this.index = index;
		this.reader = reader;
	}

	public List<String> getSeqNames()
	{
		return index.getSeqNames();
	}

	@Override
	protected Map<String, Annotation> getVariantAnnotationsMap()
	{
		List<VcfInfo> infos = reader.getInfos();
		Map<String, Annotation> annotationMap = new LinkedHashMap<String, Annotation>(infos.size());

		for (VcfInfo info : infos)
		{
			annotationMap.put(info.getId(), VcfAnnotation.fromVcfInfo(info));
		}

		return annotationMap;
	}

	public List<Sequence> getSequences()
	{
		List<String> seqNames = getSeqNames();
		Map<String, Integer> seqLengths = getSequenceLengths();

		List<Sequence> sequences = new ArrayList<Sequence>(seqNames.size());
		for (String seqName : seqNames)
		{

			sequences.add(new TabixSequence(seqName, seqLengths.get(seqName), index, getVariantLineMapper()));
		}

		return sequences;
	}

	public GeneticVariant getVariant(String seqName, int startPos)
	{
		GeneticVariant variant = null;

		GenotypeQuery q = index.createQuery();
		try
		{
			String line = q.executeQuery(seqName, startPos);
			if (line != null)
			{
				variant = getVariantLineMapper().mapLine(line);
			}
		}
		finally
		{
			IOUtils.closeQuietly(q);
		}

		return variant;
	}

	public List<Sample> getSamples()
	{
		List<String> sampleNames;
		try
		{
			sampleNames = reader.getSampleNames();
		}
		catch (IOException e)
		{
			throw new GenotypeDataException("IOException getting samplenames from VcfReader", e);
		}

		List<Sample> samples = new ArrayList<Sample>(sampleNames.size());
		for (String sampleName : sampleNames)
		{
			Sample sample = new Sample(sampleName);
			samples.add(sample);
		}

		return samples;
	}

	private VariantLineMapper getVariantLineMapper()
	{
		if (variantLineMapper == null)
		{
			try
			{
				variantLineMapper = new VcfVariantLineMapper(getVcfColNames(), reader.getSampleNames());
			}
			catch (IOException e)
			{
				throw new GenotypeDataException("IOException VcfReader.getSampleNames()", e);
			}
		}

		return variantLineMapper;
	}

	private List<String> getVcfColNames()
	{
		List<String> colNames = new ArrayList<String>();

		try
		{
			Iterator<String> it = reader.colNamesIterator();
			while (it.hasNext())
			{
				colNames.add(it.next());
			}
		}
		catch (IOException e)
		{
			throw new GenotypeDataException(e);
		}

		return colNames;
	}

	/**
	 * Get sequence length by sequence name
	 */
	private Map<String, Integer> getSequenceLengths()
	{
		List<VcfContig> contigs = reader.getContigs();
		Map<String, Integer> sequenceLengthById = new HashMap<String, Integer>(contigs.size());

		for (VcfContig contig : contigs)
		{
			sequenceLengthById.put(contig.getId(), contig.getLength());
		}

		return sequenceLengthById;
	}

	public void close() throws IOException
	{
		reader.close();
	}

}
