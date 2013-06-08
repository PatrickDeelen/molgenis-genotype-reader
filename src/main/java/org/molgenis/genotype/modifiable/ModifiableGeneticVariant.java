package org.molgenis.genotype.modifiable;

import java.util.List;
import java.util.Map;

import org.molgenis.genotype.Allele;
import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.util.Ld;
import org.molgenis.genotype.util.LdCalculator;
import org.molgenis.genotype.util.LdCalculatorException;
import org.molgenis.genotype.util.MafCalculator;
import org.molgenis.genotype.util.MafResult;
import org.molgenis.genotype.variant.AbstractGeneticVariant;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SampleVariantsProvider;
import org.molgenis.genotype.variant.id.GeneticVariantId;

public class ModifiableGeneticVariant extends AbstractGeneticVariant
{

	private final GeneticVariant originalVariant;

	private final ModifiableGenotypeData modifiableGenotypeData;

	public ModifiableGeneticVariant(GeneticVariant originalVariant, ModifiableGenotypeData modifiableGenotypeData)
	{
		super();
		this.originalVariant = originalVariant;
		this.modifiableGenotypeData = modifiableGenotypeData;
	}

	@Override
	public String getPrimaryVariantId()
	{
		return getVariantId().getPrimairyId();
	}

	@Override
	public List<String> getAlternativeVariantIds()
	{
		return getVariantId().getAlternativeIds();
	}

	@Override
	public List<String> getAllIds()
	{
		return getVariantId().getVariantIds();
	}

	@Override
	public GeneticVariantId getVariantId()
	{
		GeneticVariantId variantId = modifiableGenotypeData.getUpdatedId(this);
		if (variantId != null)
		{
			return variantId;
		}
		else
		{
			return originalVariant.getVariantId();
		}
	}

	@Override
	public int getStartPos()
	{
		return originalVariant.getStartPos();
	}

	@Override
	public String getSequenceName()
	{
		return originalVariant.getSequenceName();
	}

	@Override
	public Alleles getVariantAlleles()
	{
		Alleles alleles = modifiableGenotypeData.getUpdatedAlleles(this);
		if (alleles != null)
		{
			return alleles;
		}
		else
		{
			return originalVariant.getVariantAlleles();
		}
	}

	@Override
	public int getAlleleCount()
	{
		return getVariantAlleles().getAlleleCount();
	}

	@Override
	public Allele getRefAllele()
	{
		Allele refAllele = modifiableGenotypeData.getUpdatedRef(this);
		if (refAllele != null)
		{
			return refAllele;
		}
		else
		{
			return originalVariant.getRefAllele();
		}
	}

	@Override
	public List<Alleles> getSampleVariants()
	{
		return getSampleVariantsProvider().getSampleVariants(originalVariant);
	}

	@Override
	public Map<String, ?> getAnnotationValues()
	{
		return originalVariant.getAnnotationValues();
	}

	@Override
	public double getMinorAlleleFrequency()
	{
		// Do not cache MAF results since modifications to alleles need to be
		// reflected
		try
		{
			MafResult mafResult = MafCalculator.calculateMaf(getVariantAlleles(), getRefAllele(), getSampleVariants());
			return mafResult.getFreq();
		}
		catch (NullPointerException e)
		{
			throw new GenotypeDataException("NullPointerException in maf caculation. " + getVariantAlleles() + " ref: "
					+ getRefAllele(), e);
		}

	}

	@Override
	public Allele getMinorAllele()
	{
		// Do not cache MAF results since modifications to alleles need to be
		// reflected
		try
		{
			MafResult mafResult = MafCalculator.calculateMaf(getVariantAlleles(), getRefAllele(), getSampleVariants());
			return mafResult.getMinorAllele();
		}
		catch (NullPointerException e)
		{
			throw new GenotypeDataException("NullPointerException in maf caculation. " + getVariantAlleles() + " ref: "
					+ getRefAllele(), e);
		}
	}

	@Override
	public boolean isSnp()
	{
		return getVariantAlleles().isSnp();
	}

	@Override
	public boolean isAtOrGcSnp()
	{
		return getVariantAlleles().isAtOrGcSnp();
	}

	@Override
	public Ld calculateLd(GeneticVariant other) throws LdCalculatorException
	{
		return LdCalculator.calculateLd(this, other);
	}

	@Override
	public boolean isBiallelic()
	{
		return getVariantAlleles().getAlleleCount() == 2;
	}

	@Override
	public float[] getSampleDosages()
	{
		// TODO duplicate from read only variant. Should be provided by sample
		// genotype provider

		byte[] calledDosage = getSampleCalledDosage();
		float[] dosage = new float[calledDosage.length];

		for (int i = 0; i < calledDosage.length; ++i)
		{
			dosage[i] = calledDosage[i];
		}

		return dosage;

	}

	@Override
	public byte[] getSampleCalledDosage()
	{
		// TODO duplicate from read only variant. Should be provided by sample
		// genotype provider

		Allele dosageRef = getRefAllele() == null ? getVariantAlleles().get(0) : getRefAllele();

		List<Alleles> sampleVariants = getSampleVariants();

		byte[] dosages = new byte[getSampleVariants().size()];

		for (int i = 0; i < dosages.length; ++i)
		{
			Alleles sampleVariant = sampleVariants.get(i);
			boolean missing = false;
			byte dosage = 0;

			for (Allele allele : sampleVariant)
			{
				if (allele == null)
				{
					missing = true;
				}
				else if (allele == dosageRef)
				{
					++dosage;
				}
			}

			dosages[i] = missing ? -1 : dosage;
		}

		return dosages;
	}

	@Override
	public SampleVariantsProvider getSampleVariantsProvider()
	{
		SampleVariantsProvider sampleVariantProvider = modifiableGenotypeData.getUpdatedSampleVariantProvider(this);
		if (sampleVariantProvider != null)
		{
			return sampleVariantProvider;
		}
		else
		{
			return originalVariant.getSampleVariantsProvider();
		}
	}

	/**
	 * @return the originalVariant
	 */
	protected GeneticVariant getOriginalVariant()
	{
		return originalVariant;
	}

	/**
	 * Updates reference allele
	 * 
	 * @param newRefAllele
	 * @throws GenotypeModificationException
	 *             if reference allele is not one of the variant alleles
	 */
	public void updateRefAllele(Allele newRefAllele)
	{
		modifiableGenotypeData.updateRefAllele(this, newRefAllele);
	}

	/**
	 * Updates reference allele
	 * 
	 * @param newRefAllele
	 * @throws GenotypeModificationException
	 *             if reference allele is not one of the variant alleles
	 */
	public void updateRefAllele(String newRefAllele)
	{
		updateRefAllele(Allele.create(newRefAllele));
	}

	/**
	 * Updates reference allele
	 * 
	 * @param newRefAllele
	 * @throws GenotypeModificationException
	 *             if reference allele is not one of the variant alleles
	 */
	public void updateRefAllele(char newRefAllele)
	{
		updateRefAllele(Allele.create(newRefAllele));
	}

	/**
	 * Sets new primary ID. Old ID will made an alternative ID
	 * 
	 * @param newPrimaryId
	 */
	public void updatePrimaryId(String newPrimaryId)
	{
		modifiableGenotypeData.updateVariantPrimaryId(this, newPrimaryId);
	}

	/**
	 * Overwrite old variant ID
	 * 
	 * @param newVariantId
	 */
	public void updateId(GeneticVariantId newVariantId)
	{
		modifiableGenotypeData.updateVariantId(this, newVariantId);
	}

	/**
	 * Swap the alleles from this variants. Variant Alleles, Reference Allele
	 * and Sample Alleles will be updated
	 */
	public void swap()
	{
		modifiableGenotypeData.swapGeneticVariant(this);
	}

	/**
	 * Exclude this variant from the modifiable genotype data it belongs to.
	 */
	public void exclude()
	{
		modifiableGenotypeData.excludeVariant(this);
	}
}
