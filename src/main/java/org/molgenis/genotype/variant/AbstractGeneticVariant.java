package org.molgenis.genotype.variant;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractGeneticVariant implements GeneticVariant
{
	private final GeneticVariantId variantId;
	private final int startPos;
	private final String sequenceName;
	private final Map<String, ?> annotationValues;
	private final Integer stopPos;
	private final List<String> altDescriptions;
	private final List<String> altTypes;

	public AbstractGeneticVariant(List<String> ids, String sequenceName, int startPos, Map<String, ?> annotationValues,
			Integer stopPos, List<String> altDescriptions, List<String> altTypes)
	{
		
		if (sequenceName == null) throw new IllegalArgumentException("SequenceName is null");
		if (annotationValues == null) throw new IllegalArgumentException("AnnotationValues is null");
		if (altDescriptions == null) throw new IllegalArgumentException("AltDescriptions is null");
		if (altTypes == null) throw new IllegalArgumentException("AltTypes is null");

		if(ids == null){
			this.variantId = null;
		} else if(ids.size() == 1){
			this.variantId = new SingleGeneticVariantId(ids.get(0));
		} else {
			this.variantId = new ListGeneticVariantId(ids);
		}
		
		
		this.startPos = startPos;
		this.sequenceName = sequenceName;
		this.annotationValues = annotationValues;
		this.stopPos = stopPos;
		this.altDescriptions = altDescriptions;
		this.altTypes = altTypes;
	}

	public AbstractGeneticVariant(String id, String sequenceName, int startPos, Map<String, ?> annotationValues,
			Integer stopPos, List<String> altDescriptions, List<String> altTypes)
	{
		
		if (sequenceName == null) throw new IllegalArgumentException("SequenceName is null");
		if (annotationValues == null) throw new IllegalArgumentException("AnnotationValues is null");
		if (altDescriptions == null) throw new IllegalArgumentException("AltDescriptions is null");
		if (altTypes == null) throw new IllegalArgumentException("AltTypes is null");

		if(id == null){
			this.variantId = null;
		} else {
			this.variantId = new SingleGeneticVariantId(id);
		}
		
		this.startPos = startPos;
		this.sequenceName = sequenceName;
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
		if(variantId == null){
			return null;
		} else {
			return variantId.getPrimairyId();
		}
	}

	@Override
	public List<String> getAlternativeVariantIds()
	{
		return variantId.getAlternativeIds();
	}

	@Override
	public List<String> getAllIds()
	{
		return variantId.getVariantIds();
	}
	
	@Override
	public GeneticVariantId getVariantId(){
		return variantId;
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
	public abstract List<List<String>> getSampleVariants();

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
