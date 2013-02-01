package org.molgenis.genotype.variant;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractGeneticVariant implements GeneticVariant
{
	private final List<String> ids;
	private final int startPos;
	private final String sequenceName;
	private final Map<String, List<String>> sampleVariants;
	private final Map<String, ?> annotationValues;
	private final Integer stopPos;
	private final List<String> altDescriptions;
	private final List<String> altTypes;

	public AbstractGeneticVariant(List<String> ids, String sequenceName, int startPos,
			Map<String, List<String>> sampleVariants, Map<String, ?> annotationValues, Integer stopPos,
			List<String> altDescriptions, List<String> altTypes)
	{
		if (ids == null) throw new IllegalArgumentException("Id list is null");
		if (sequenceName == null) throw new IllegalArgumentException("SequenceName is null");
		if (sampleVariants == null) throw new IllegalArgumentException("SampleVariants map is null");
		if (annotationValues == null) throw new IllegalArgumentException("AnnotationValues is null");
		if (altDescriptions == null) throw new IllegalArgumentException("AltDescriptions is null");
		if (altTypes == null) throw new IllegalArgumentException("AltTypes is null");

		this.ids = ids;
		this.startPos = startPos;
		this.sequenceName = sequenceName;
		this.sampleVariants = sampleVariants;
		this.annotationValues = annotationValues;
		this.stopPos = stopPos;
		this.altDescriptions = altDescriptions;
		this.altTypes = altTypes;
	}

	public abstract List<String> getAlleles();

	public abstract String getRefAllele();

	public String getCompoundId()
	{
		List<String> ids = getIds();
		if (ids.isEmpty())
		{
			return null;
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < ids.size(); i++)
		{
			if (i > 0)
			{
				sb.append(";");
			}
			sb.append(ids.get(i));
		}

		return sb.toString();
	}

	public List<String> getIds()
	{
		return Collections.unmodifiableList(ids);
	}

	public int getStartPos()
	{
		return startPos;
	}

	public String getSequenceName()
	{
		return sequenceName;
	}

	public Map<String, List<String>> getSampleVariants()
	{
		return Collections.unmodifiableMap(sampleVariants);
	}

	public Map<String, ?> getAnnotationValues()
	{
		return annotationValues;
	}

	public Integer getStopPos()
	{
		return stopPos;
	}

	public List<String> getAltDescriptions()
	{
		return Collections.unmodifiableList(altDescriptions);
	}

	public List<String> getAltTypes()
	{
		return Collections.unmodifiableList(altTypes);
	}

}
