package org.molgenis.genotype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.annotation.Annotation;

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

	/**
	 * Get variant annotations by id
	 */
	protected abstract Map<String, Annotation> getVariantAnnotationsMap();

}
