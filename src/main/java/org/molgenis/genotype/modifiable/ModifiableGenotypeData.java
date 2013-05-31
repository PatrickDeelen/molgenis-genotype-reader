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
	 * @param newPrimaryId
	 *            the new primary ID
	 */
	void updateVariantPrimaryId(GeneticVariant geneticVariant, String newPrimaryId);

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
	 * Updates the reference allele. Also changes the order of the alleles to
	 * make reference allele first
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @param newRefAllele
	 * @throws GenotypeModificationException
	 *             if reference allele is not one of the variant alleles
	 */
	void updateRefAllele(GeneticVariant geneticVariant, Allele newRefAllele) throws GenotypeModificationException;

	/**
	 * Get the updated alleles
	 * 
	 * @param geneticVariant
	 *            the original genetic variant
	 * @return the updated alleles
	 */
	Alleles getUpdatedAlleles(GeneticVariant geneticVariant);

}
