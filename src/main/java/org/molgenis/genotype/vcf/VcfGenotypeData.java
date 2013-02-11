package org.molgenis.genotype.vcf;

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
import org.molgenis.genotype.annotation.VcfAnnotation;
import org.molgenis.genotype.tabix.TabixIndex;
import org.molgenis.genotype.tabix.TabixSequence;
import org.molgenis.genotype.variant.VariantLineMapper;
import org.molgenis.io.vcf.VcfAlt;
import org.molgenis.io.vcf.VcfContig;
import org.molgenis.io.vcf.VcfInfo;
import org.molgenis.io.vcf.VcfReader;

public class VcfGenotypeData extends IndexedGenotypeData
{
	private final GenotypeDataIndex index;
	private final VcfReader reader;
	private Map<String, Annotation> sampleAnnotationsMap;
	private Map<String, String> altDescriptions;

	public VcfGenotypeData(File bzipVcfFile, File tabixIndexFile)
	{
		try
		{
			reader = new VcfReader(new BlockCompressedInputStream(bzipVcfFile));

			try
			{
				VariantLineMapper variantLineMapper = new VcfVariantLineMapper(reader.getColNames(),
						reader.getSampleNames(), getVariantAnnotations(), getAltDescriptions());
				index = new TabixIndex(tabixIndexFile, bzipVcfFile, variantLineMapper);
			}
			finally
			{
				IOUtils.closeQuietly(reader);
			}
		}
		catch (IOException e)
		{
			throw new GenotypeDataException(e);
		}
	}

	@Override
	protected Map<String, Annotation> getVariantAnnotationsMap()
	{
		if (sampleAnnotationsMap == null)
		{
			List<VcfInfo> infos = reader.getInfos();
			sampleAnnotationsMap = new LinkedHashMap<String, Annotation>(infos.size());

			for (VcfInfo info : infos)
			{
				sampleAnnotationsMap.put(info.getId(), VcfAnnotation.fromVcfInfo(info));
			}
		}

		return sampleAnnotationsMap;
	}

	@Override
	public List<Sequence> getSequences()
	{
		List<String> seqNames = getSeqNames();
		Map<String, Integer> seqLengths = getSequenceLengths();

		List<Sequence> sequences = new ArrayList<Sequence>(seqNames.size());
		for (String seqName : seqNames)
		{
			sequences.add(new TabixSequence(seqName, seqLengths.get(seqName), index));
		}

		return sequences;
	}

	@Override
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
			Sample sample = new Sample(sampleName, null, Collections.<String, Object> emptyMap());
			samples.add(sample);
		}

		return samples;
	}

	@Override
	public List<Annotation> getSampleAnnotations()
	{
		// TODO
		return Collections.emptyList();
	}

	@Override
	public Annotation getSampleAnnotation(String annotationId)
	{
		// TODO
		return null;
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

	private Map<String, String> getAltDescriptions()
	{
		if (altDescriptions == null)
		{
			List<VcfAlt> alts = reader.getAlts();
			altDescriptions = new HashMap<String, String>(alts.size());

			for (VcfAlt alt : alts)
			{
				altDescriptions.put(alt.getId(), alt.getDescription());
			}
		}

		return altDescriptions;
	}

	@Override
	protected GenotypeDataIndex getIndex()
	{
		return index;
	}

}
