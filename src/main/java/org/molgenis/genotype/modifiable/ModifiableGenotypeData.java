package org.molgenis.genotype.modifiable;

import org.molgenis.genotype.Allele;
import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.RandomAccessGenotypeData;
import org.molgenis.genotype.variant.GeneticVariant;
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
	GeneticVariantId getUpdatedId(GeneticVariant geneticVariant);

	/**
	 * Get the updated reference allele for a genetic variant. Return null if
	 * not updated.
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @return
	 */
	Allele getUpdatedRef(GeneticVariant geneticVariant);

	/**
	 * Get the updated sample variant provider
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @return
	 */
	SampleVariantsProvider getUpdatedSampleVariantProvider(GeneticVariant geneticVariant);

	/**
	 * Overwrite the original genetic variant ID.
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @param geneticVariantId
	 *            the new genetic variant ID
	 */
	void updateVariantId(GeneticVariant geneticVariant, GeneticVariantId newGeneticVariantId);

	/**
	 * Updates the original genetic variant ID to make the new key primary. Old
	 * keys will be made alternative IDs.
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @param newPrimairyId
	 *            the new primary ID
	 */
	void updateVariantId(GeneticVariant geneticVariant, String newPrimairyId);

	/**
	 * Updates the the sample variant provider and the ref allele. The sample
	 * variant provider will be the swapping sample variant provider and the
	 * reference allele will be made complement
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 */
	void swapGeneticVariant(GeneticVariant geneticVariant);

	/**
	 * Update the sample variant provider
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @param newSampleVariantProvider
	 *            the new sample variant provider
	 */
	void updateSamplVariantsProvider(GeneticVariant geneticVariant, SampleVariantsProvider newSampleVariantProvider);

	/**
	 * Update the reference allele
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @param newRefAllele
	 */
	void updateRefAllele(GeneticVariant geneticVariant, Allele newRefAllele);

	/**
	 * Update the alleles
	 * 
	 * @param geneticVariant
	 * @param newAlleles
	 */
	void updateAlleles(GeneticVariant geneticVariant, Alleles newAlleles);

	/**
	 * Get the updated alleles
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @return the updated alleles
	 */
	Alleles getUpdatedAlleles(GeneticVariant geneticVariant);

}
