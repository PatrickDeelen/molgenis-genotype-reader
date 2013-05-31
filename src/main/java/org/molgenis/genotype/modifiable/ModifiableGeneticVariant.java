package org.molgenis.genotype.modifiable;

import java.util.List;
import java.util.Map;

import org.molgenis.genotype.Allele;
import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.util.Ld;
import org.molgenis.genotype.util.LdCalculator;
import org.molgenis.genotype.util.LdCalculatorException;
import org.molgenis.genotype.util.MafResult;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.MafCalculator;
import org.molgenis.genotype.variant.SampleVariantsProvider;
import org.molgenis.genotype.variant.id.GeneticVariantId;

public class ModifiableGeneticVariant implements GeneticVariant
{

	private final GeneticVariant originalVariant;

	private final ModifiableGenotypeData modifiableGenotypeData;
	private MafResult mafResult = null;

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
		GeneticVariantId variantId = modifiableGenotypeData.getUpdatedId(originalVariant);
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
		Alleles alleles = modifiableGenotypeData.getUpdatedAlleles(originalVariant);
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
		Allele refAllele = modifiableGenotypeData.getUpdatedRef(originalVariant);
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
		if (mafResult == null)
		{
			mafResult = MafCalculator.calculateMaf(getVariantAlleles(), getRefAllele(), getSampleVariants());
		}
		return mafResult.getFreq();
	}

	@Override
	public Allele getMinorAllele()
	{
		if (mafResult == null)
		{
			mafResult = MafCalculator.calculateMaf(getVariantAlleles(), getRefAllele(), getSampleVariants());
		}
		return mafResult.getMinorAllele();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getSampleCalledDosage()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SampleVariantsProvider getSampleVariantsProvider()
	{
		SampleVariantsProvider sampleVariantProvider = modifiableGenotypeData
				.getUpdatedSampleVariantProvider(originalVariant);
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

}
