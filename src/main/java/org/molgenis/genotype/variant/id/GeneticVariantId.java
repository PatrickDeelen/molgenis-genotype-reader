package org.molgenis.genotype.variant.id;

import java.util.List;

public abstract class GeneticVariantId implements Iterable<String>
{

	public static final String DEFAULT_ID_SEPARATOR = ";";

	/**
	 * Get the Id as string
	 * 
	 * @return
	 */
	public abstract String getPrimairyId();

	/**
	 * Get all variant IDs
	 * 
	 * @return
	 */
	public abstract List<String> getVariantIds();

	/**
	 * Get alternative IDs
	 * 
	 * @return
	 */
	public abstract List<String> getAlternativeIds();

	/**
	 * Get all Ids as one string using the default separator (;)
	 * 
	 * @return
	 */
	public abstract String getConcatenatedId();

	/**
	 * Get all Ids as one string using a custom separator
	 * 
	 * @param separator
	 * @return
	 */
	public abstract String getConcatenatedId(String separator);

	/**
	 * Test if queried ID is this variants ID
	 * 
	 * @param queryId
	 * @return if query ID is one of the variants IDs
	 */
	public abstract boolean isIdInVariantIds(String queryId);

	/**
	 * Test if this ID only contains a primary ID
	 * 
	 * @return true if only one ID
	 */
	public abstract boolean onlyPrimairyId();

	/**
	 * Does this variant have an ID
	 * 
	 * @return true if the variant has an id
	 */
	public abstract boolean containsId();

	/**
	 * Test is other variantId is the same ID. Two IDs are the same if they
	 * share one ID. Even if one or both have multiple other IDs that do not
	 * necessarily overlap. Two empty variants are not identical.
	 * 
	 * @param otherVariantId
	 */
	public boolean isSameId(GeneticVariantId otherVariantId)
	{

		if (!this.containsId() || !otherVariantId.containsId())
		{

			return false;

		}
		if (this.onlyPrimairyId() && otherVariantId.onlyPrimairyId())
		{

			return this.getPrimairyId().equals(otherVariantId.getPrimairyId());

		}
		else if (this.onlyPrimairyId())
		{

			if (otherVariantId.isIdInVariantIds(this.getPrimairyId()))
			{
				return true;
			}

		}
		else if (otherVariantId.onlyPrimairyId())
		{

			if (this.isIdInVariantIds(otherVariantId.getPrimairyId()))
			{
				return true;
			}

		}
		else
		{
			for (String thisId : this)
			{
				if (otherVariantId.isIdInVariantIds(thisId))
				{
					return true;
				}
			}
		}

		return false;

	}

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract int hashCode();

	public static GeneticVariantId getEmptyGeneticVariantId()
	{
		return new BlankGeneticVariantId();
	}

}
