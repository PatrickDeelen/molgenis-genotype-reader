package org.molgenis.genotype.vcf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.AbstractGenotypeData;
import org.molgenis.genotype.GenotypeDataIndex;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.annotation.VcfAnnotation;
import org.molgenis.io.vcf.VcfContig;
import org.molgenis.io.vcf.VcfInfo;
import org.molgenis.io.vcf.VcfReader;

public class VcfGenotypeData extends AbstractGenotypeData
{
	private GenotypeDataIndex index;
	private VcfReader reader;

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
		Map<String, Long> seqLengths = getSequenceLengths();

		List<Sequence> sequences = new ArrayList<Sequence>(seqNames.size());
		for (String seqName : seqNames)
		{
			sequences.add(new Sequence(seqName, seqLengths.get(seqName)));
		}

		return sequences;
	}

	/**
	 * Get sequence length by sequence name
	 */
	private Map<String, Long> getSequenceLengths()
	{
		List<VcfContig> contigs = reader.getContigs();
		Map<String, Long> sequenceLengthById = new HashMap<String, Long>(contigs.size());

		for (VcfContig contig : contigs)
		{
			sequenceLengthById.put(contig.getId(), contig.getLength());
		}

		return sequenceLengthById;
	}

}
