package org.molgenis.genotype;

import java.util.Collections;
import java.util.Map;

public class Sample
{
	private final String id;
	private final String familyId;
	private final Map<String, ?> annotations;

	public Sample(String id, String familyId, Map<String, ?> annotations)
	{
		this.id = id;
		this.familyId = familyId;
		this.annotations = annotations;
	}

	public String getId()
	{
		return id;
	}

	public String getFamilyId()
	{
		return familyId;
	}

	public Map<String, ?> getAnnotations()
	{
		return Collections.unmodifiableMap(annotations);
	}

	@Override
	public String toString()
	{
		return "Sample [id=" + id + ", familyId=" + familyId + ", annotations=" + annotations + "]";
	}

}
