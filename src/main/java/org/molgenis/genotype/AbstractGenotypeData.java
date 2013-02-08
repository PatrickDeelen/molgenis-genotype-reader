package org.molgenis.genotype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.VariantHandler;

public abstract class AbstractGenotypeData implements GenotypeData
{
	@Override
	public abstract List<String> getSeqNames();

	@Override
	public List<Annotation> getVariantAnnotations()
	{
		return Collections.unmodifiableList(new ArrayList<Annotation>(getVariantAnnotationsMap().values()));
	}

	@Override
	public Annotation getVariantAnnotation(String annotaionId)
	{
		return getVariantAnnotationsMap().get(annotaionId);
	}

	@Override
	public Sequence getSequenceByName(String name)
	{
		for (Sequence sequence : getSequences())
		{
			if (sequence.getName().equals(name))
			{
				return sequence;
			}
		}

		return null;
	}

	@Override
	public void seqVariants(String seqName, VariantHandler handler)
	{
		Sequence sequence = getSequenceByName(seqName);
		if (sequence != null)
		{
			sequence.variants(handler);
		}
	}

	@Override
	public List<List<String>> getSampleGeneticVariants(String seqName, int startPos)
	{
		List<List<String>> sampleVariants = new ArrayList<List<String>>();

		List<GeneticVariant> variants = getVariantsByPos(seqName, startPos);
		for (GeneticVariant variant : variants)
		{
			sampleVariants.addAll(variant.getSampleVariants());
		}

		return sampleVariants;
	}

	/**
	 * Get variant annotations by id
	 */
	protected abstract Map<String, Annotation> getVariantAnnotationsMap();

}
