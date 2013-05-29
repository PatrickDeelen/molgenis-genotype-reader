package org.molgenis.genotype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.annotation.Annotation;

public abstract class AbstractGenotypeData implements GenotypeData
{
	@Override
	public List<Annotation> getVariantAnnotations()
	{
		return Collections.unmodifiableList(new ArrayList<Annotation>(getVariantAnnotationsMap().values()));
	}

	@Override
	public Annotation getVariantAnnotation(String annotationId)
	{
		return getVariantAnnotationsMap().get(annotationId);
	}

	/**
	 * Get variant annotations by id
	 */
	protected abstract Map<String, Annotation> getVariantAnnotationsMap();

}
