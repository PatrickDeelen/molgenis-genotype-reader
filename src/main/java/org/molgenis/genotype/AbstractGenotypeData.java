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
	public abstract List<String> getSeqNames();

	public List<Annotation> getVariantAnnotations()
	{
		return Collections.unmodifiableList(new ArrayList<Annotation>(getVariantAnnotationsMap().values()));
	}

	public Annotation getVariantAnnotation(String annotaionId)
	{
		return getVariantAnnotationsMap().get(annotaionId);
	}

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

	public void seqVariants(String seqName, VariantHandler handler)
	{
		Sequence sequence = getSequenceByName(seqName);
		if (sequence != null)
		{
			sequence.variants(handler);
		}
	}

	public Map<String, List<String>> getSampleGeneticVariants(String seqName, int startPos)
	{
		GeneticVariant variant = getVariant(seqName, startPos);
		if (variant == null)
		{
			return Collections.emptyMap();
		}

		return variant.getSampleVariants();
	}

	/**
	 * Get variant annotations by id
	 */
	protected abstract Map<String, Annotation> getVariantAnnotationsMap();

}
