package org.molgenis.genotype.variant;

import org.molgenis.genotype.util.ChromosomeComparator;

abstract public class AbstractGeneticVariant implements GeneticVariant
{

	private static final ChromosomeComparator chrComparator = new ChromosomeComparator();

	@Override
	public int compareTo(GeneticVariant other)
	{
		if (other == null)
		{
			return 0;
		}

		if (this == other)
		{
			return 0;
		}

		if (!this.getSequenceName().equals(other.getSequenceName()))
		{
			return chrComparator.compare(this.getSequenceName(), other.getSequenceName());
		}
		else
		{
			// same sequence
			if (this.getStartPos() != other.getStartPos())
			{
				return this.getStartPos() - other.getStartPos();
			}
			else
			{
				// TODO FIX ME!!!!!!!!!!!!!!!!!!!!
				throw new RuntimeException(
						"Genetic variant comparotor can not compare variants with identical sequence and pos. This is a major TODO");

				// same sequence and same start

			}
		}
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
		int result = super.hashCode();
		result = prime * result + ((getVariantAlleles() == null) ? 0 : getVariantAlleles().hashCode());
		result = prime * result + ((getSampleVariantsProvider() == null) ? 0 : getSampleVariantsProvider().hashCode());
		result = prime * result + ((getSequenceName() == null) ? 0 : getSequenceName().hashCode());
		result = prime * result + getStartPos();
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
		if (!(obj instanceof GeneticVariant)) return false;
		GeneticVariant other = (GeneticVariant) obj;
		if (getVariantAlleles() == null)
		{
			if (other.getVariantAlleles() != null) return false;
		}
		else if (!getVariantAlleles().equals(other.getVariantAlleles())) return false;
		if (getSampleVariantsProvider() == null)
		{
			if (other.getSampleVariantsProvider() != null) return false;
		}
		else if (!getSampleVariantsProvider().equals(other.getSampleVariantsProvider())) return false;
		if (getSequenceName() == null)
		{
			if (other.getSequenceName() != null) return false;
		}
		else if (!getSequenceName().equals(other.getSequenceName())) return false;
		if (getStartPos() != other.getStartPos()) return false;
		return true;
	}
}
