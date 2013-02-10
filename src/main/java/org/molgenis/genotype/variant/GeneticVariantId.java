package org.molgenis.genotype.variant;

import java.util.List;

public abstract class GeneticVariantId implements Iterable<String>{

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
	 * Test is other variantId is the same ID. Two IDs are the same if they
	 * share one ID. Even if one or both have multiple other IDs that do not
	 * necessarily overlap
	 * 
	 * @param otherVariantId
	 */
	public boolean isSameId(GeneticVariantId otherVariantId) {

		if(this.onlyPrimairyId() && otherVariantId.onlyPrimairyId()) {
			
			return this.getPrimairyId().equals(otherVariantId.getPrimairyId());
			
		} else if (this.onlyPrimairyId()) {
			
			if(otherVariantId.isIdInVariantIds(this.getPrimairyId())){
				return true;
			}
						
		} else if(otherVariantId.onlyPrimairyId()){
			
			if(this.isIdInVariantIds(otherVariantId.getPrimairyId())){
				return true;
			}
			
		} else {
			for(String thisId : this){
				if(otherVariantId.isIdInVariantIds(thisId)){
					return true;
				}
			}
		}
		
		return false;
		
	}
}
