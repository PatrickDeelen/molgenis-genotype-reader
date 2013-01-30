package org.molgenis.genotype.variant;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractGeneticVariant implements GeneticVariant
{
	private List<String> ids;
	private int startPos;
	private String sequenceName;
	private Map<String, List<String>> sampleVariants;

	public AbstractGeneticVariant(List<String> ids, String sequenceName, int startPos,
			Map<String, List<String>> sampleVariants)
	{
		if (ids == null) throw new IllegalArgumentException("Id list is null");
		if (sequenceName == null) throw new IllegalArgumentException("SequenceName is null");
		if (sampleVariants == null) throw new IllegalArgumentException("SampleVariants map is null");

		this.ids = ids;
		this.startPos = startPos;
		this.sequenceName = sequenceName;
		this.sampleVariants = sampleVariants;
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
}
