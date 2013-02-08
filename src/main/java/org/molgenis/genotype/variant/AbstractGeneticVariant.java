package org.molgenis.genotype.variant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractGeneticVariant implements GeneticVariant
{
	private final List<String> ids;
	private final int startPos;
	private final String sequenceName;
	private final List<String> sampleVariants;
	private final Map<String, ?> annotationValues;
	private final Integer stopPos;
	private final List<String> altDescriptions;
	private final List<String> altTypes;

	public AbstractGeneticVariant(List<String> ids, String sequenceName, int startPos, List<String> sampleVariants,
			Map<String, ?> annotationValues, Integer stopPos, List<String> altDescriptions, List<String> altTypes)
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

	@Override
	public abstract List<String> getAlleles();

	@Override
	public abstract String getRefAllele();

	@Override
	public String getPrimaryVariantId()
	{
		if (ids.isEmpty()) return null;
		return ids.get(0);
	}

	@Override
	public List<String> getAlternativeVariantIds()
	{
		List<String> alternativeIds = new ArrayList<String>();

		String primary = getPrimaryVariantId();
		if (primary != null)
		{
			alternativeIds.addAll(ids);
			alternativeIds.remove(primary);
		}

		return Collections.unmodifiableList(alternativeIds);
	}

	public List<String> getIds()
	{
		return Collections.unmodifiableList(ids);
	}

	@Override
	public int getStartPos()
	{
		return startPos;
	}

	@Override
	public String getSequenceName()
	{
		return sequenceName;
	}

	@Override
	public List<String> getSampleVariants()
	{
		return Collections.unmodifiableList(sampleVariants);
	}

	@Override
	public Map<String, ?> getAnnotationValues()
	{
		return annotationValues;
	}

	@Override
	public Integer getStopPos()
	{
		return stopPos;
	}

	@Override
	public List<String> getAltDescriptions()
	{
		return Collections.unmodifiableList(altDescriptions);
	}

	@Override
	public List<String> getAltTypes()
	{
		return Collections.unmodifiableList(altTypes);
	}

}
