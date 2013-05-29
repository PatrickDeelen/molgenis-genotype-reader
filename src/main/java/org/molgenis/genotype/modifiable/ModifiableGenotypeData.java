package org.molgenis.genotype.modifiable;

import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.RandomAccessGenotypeData;
import org.molgenis.genotype.variant.GeneticVariantOld;
import org.molgenis.genotype.variant.IllegalReferenceAlleleException;
import org.molgenis.genotype.variant.SampleVariantsProvider;
import org.molgenis.genotype.variant.id.GeneticVariantId;

/**
 * GenotypeData of which some variants properties can be updated
 * 
 * @author Patrick Deelen
 * 
 */
public interface ModifiableGenotypeData extends RandomAccessGenotypeData
{

	/**
	 * Get the updated ID for a genetic variant. Return null if not updated.
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @return
	 */
	GeneticVariantId getUpdatedId(GeneticVariantOld geneticVariant);

	/**
	 * Get the updated reference allele for a genetic variant. Return null if
	 * not updated.
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @return
	 */
	Alleles getUpdatedRef(GeneticVariantOld geneticVariant);

	/**
	 * Get the updated sample variant provider
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @return
	 */
	SampleVariantsProvider getUpdatedSampleVariantProvider(GeneticVariantOld geneticVariant);

	/**
	 * Overwrite the original genetic variant ID.
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @param geneticVariantId
	 *            the new genetic variant ID
	 */
	void updateVariantId(GeneticVariantOld geneticVariant, GeneticVariantId newGeneticVariantId);

	/**
	 * Updates the original genetic variant ID to make the new key primary. Old
	 * keys will be made alternative IDs.
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @param newPrimairyId
	 *            the new primary ID
	 */
	void updateVariantId(GeneticVariantOld geneticVariant, String newPrimairyId);

	/**
	 * Updates the the sample variant provider and the ref allele. The sample
	 * variant provider will be the swapping sample variant provider and the
	 * reference allele will be made complement
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 */
	void swapGeneticVariant(GeneticVariantOld geneticVariant);

	/**
	 * Update the sample variant provider
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @param newSampleVariantProvider
	 *            the new sample variant provider
	 */
	void updateSamplVariantsProvider(GeneticVariantOld geneticVariant, SampleVariantsProvider newSampleVariantProvider);

	/**
	 * Update the variants reference allele
	 * 
	 * @param variantAlleles
	 *            the original genetic variant
	 * @param newRefAllele
	 *            the new reference allele
	 * @throws IllegalReferenceAlleleException
	 *             if a reference allele is provided that contains multiple
	 *             alleles
	 */
	void updateRefAllele(Alleles variantAlleles, Alleles newRefAllele) throws IllegalReferenceAlleleException;

}
