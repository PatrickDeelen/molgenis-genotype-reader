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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((annotations == null) ? 0 : annotations.hashCode());
		result = prime * result + ((familyId == null) ? 0 : familyId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Sample other = (Sample) obj;
		if (annotations == null)
		{
			if (other.annotations != null) return false;
		}
		else if (!annotations.equals(other.annotations)) return false;
		if (familyId == null)
		{
			if (other.familyId != null) return false;
		}
		else if (!familyId.equals(other.familyId)) return false;
		if (id == null)
		{
			if (other.id != null) return false;
		}
		else if (!id.equals(other.id)) return false;
		return true;
	}

}
