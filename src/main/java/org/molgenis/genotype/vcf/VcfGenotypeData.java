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
import org.apache.log4j.Logger;
import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.GenotypeDataIndex;
import org.molgenis.genotype.IndexedGenotypeData;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.VariantAlleles;
import org.molgenis.genotype.VariantQueryResult;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.annotation.VcfAnnotation;
import org.molgenis.genotype.tabix.TabixIndex;
import org.molgenis.genotype.tabix.TabixSequence;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SampleVariantsProvider;
import org.molgenis.genotype.variant.SnpGeneticVariant;
import org.molgenis.genotype.variant.VariantLineMapper;
import org.molgenis.io.vcf.VcfAlt;
import org.molgenis.io.vcf.VcfContig;
import org.molgenis.io.vcf.VcfInfo;
import org.molgenis.io.vcf.VcfReader;

public class VcfGenotypeData extends IndexedGenotypeData implements SampleVariantsProvider
{
	private static final Logger LOG = Logger.getLogger(VcfGenotypeData.class);
	private final GenotypeDataIndex index;
	private final VcfReader reader;
	private Map<String, Annotation> sampleAnnotationsMap;
	private Map<String, String> altDescriptions;
	private List<GeneticVariant> variants = new ArrayList<GeneticVariant>(1000000);
	private Map<String, Integer> variantIndexByPrimaryId = new HashMap<String, Integer>(1000000);

	public VcfGenotypeData(File bzipVcfFile, File tabixIndexFile)
	{
		try
		{
			reader = new VcfReader(new BlockCompressedInputStream(bzipVcfFile));

			try
			{
				VariantLineMapper variantLineMapper = new VcfVariantLineMapper(reader.getColNames(),
						getVariantAnnotations(), getAltDescriptions(), this);
				index = new TabixIndex(tabixIndexFile, bzipVcfFile, variantLineMapper);
			}
			finally
			{
				IOUtils.closeQuietly(reader);
			}

			int index = 0;
			for (Sequence sequence : getSequences())
			{
				VariantQueryResult vqr = sequence.getVariants();
				try
				{
					for (GeneticVariant variant : vqr)
					{
						variants.add(variant);

						if (variant.getPrimaryVariantId() != null)
						{
							variantIndexByPrimaryId.put(variant.getPrimaryVariantId(), index);
						}

						if ((++index % 1000) == 0)
						{
							LOG.info("Loaded [" + index + "] variants");
						}
					}
				}
				catch (Exception e)
				{
					throw new GenotypeDataException("Exception caching variants", e);
				}
				finally
				{
					IOUtils.closeQuietly(vqr);
				}
			}

		}
		catch (IOException e)
		{
			throw new GenotypeDataException(e);
		}
	}

	@Override
	public GeneticVariant getVariantById(String primaryVariantId)
	{
		if (primaryVariantId == null) throw new IllegalArgumentException("PrimaryVariantId can not be null");

		Integer index = variantIndexByPrimaryId.get(primaryVariantId);
		if (index != null)
		{
			return variants.get(index);
		}

		return null;
	}

	@Override
	public SnpGeneticVariant getSnpVariantById(String primaryVariantId)
	{
		GeneticVariant variant = getVariantById(primaryVariantId);
		if (variant == null)
		{
			return null;
		}
		else if (variant.isSnp())
		{

			return (SnpGeneticVariant) variant;

		}
		else
		{
			return null;
		}
	}

	@Override
	public List<GeneticVariant> getVariants()
	{
		return variants;
	}

	@Override
	public List<VariantAlleles> getSampleVariants(GeneticVariant variant)
	{
		try
		{
			return index.createQuery().findSamplesForVariant(variant.getSequenceName(), variant.getStartPos(),
					variant.getVariantAlleles(), reader.getColNames(), reader.getSampleNames());
		}
		catch (IOException e)
		{
			throw new GenotypeDataException("IOException getSampleVariants for variant with id [" + variant.getAllIds()
					+ "]", e);
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
