package org.molgenis.genotype;

import java.util.Collections;
import java.util.Map;

public class Sample
{
	private final String id;
	private final String familyId;
	private final Map<String, SampleAnnotation> annotations;

	public Sample(String id, String familyId, Map<String, SampleAnnotation> annotations)
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

	public Map<String, SampleAnnotation> getAnnotations()
	{
		return Collections.unmodifiableMap(annotations);
	}

	public static class SampleAnnotation
	{
		public enum Type
		{
			COVARIATE, PHENOTYPE, OTHER
		}

		private String name;
		private Object value;
		private Type type;

		public SampleAnnotation(String name, Object value, Type type)
		{
			super();
			this.name = name;
			this.value = value;
			this.type = type;
		}

		public String getName()
		{
			return name;
		}

		public Object getValue()
		{
			return value;
		}

		public Type getType()
		{
			return type;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			SampleAnnotation other = (SampleAnnotation) obj;
			if (name == null)
			{
				if (other.name != null) return false;
			}
			else if (!name.equals(other.name)) return false;
			if (type != other.type) return false;
			if (value == null)
			{
				if (other.value != null) return false;
			}
			else if (!value.equals(other.value)) return false;
			return true;
		}

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
