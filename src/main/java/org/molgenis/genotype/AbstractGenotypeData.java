package org.molgenis.genotype;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.annotation.Annotation;

public abstract class AbstractGenotypeData implements GenotypeData
{
	public abstract List<String> getSeqNames() throws IOException;

	public List<Annotation> getVariantAnnotations() throws IOException
	{
		return Collections.unmodifiableList(new ArrayList<Annotation>(getVariantAnnotationsMap().values()));
	}

	public Annotation getVariantAnnotation(String annotaionId) throws IOException
	{
		return getVariantAnnotationsMap().get(annotaionId);
	}

	/**
	 * Get variant annotations by id
	 */
	protected abstract Map<String, Annotation> getVariantAnnotationsMap();

}
