package org.molgenis.genotype.impute2;

import java.util.ArrayList;
import java.util.List;

import org.molgenis.genotype.Allele;
import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SampleVariantsProvider;

/**
 * SampleVariantsProvider that returns the sample alleles of a HapsEntry
 * 
 * @author erwin
 * 
 */
public class HapsEntrySampleVariantsProvider implements SampleVariantsProvider
{
	private HapsEntry hapsEntry;

	public HapsEntrySampleVariantsProvider(HapsEntry hapsEntry)
	{
		this.hapsEntry = hapsEntry;
	}

	@Override
	public List<Alleles> getSampleVariants(GeneticVariant variant)
	{
		List<Alleles> allelesList = new ArrayList<Alleles>();
		for (String[] sampleAlleles : hapsEntry.getSampleAlleles())
		{
			Allele allele1 = createAllele(sampleAlleles[0]);
			Allele allele2 = createAllele(sampleAlleles[1]);
			Alleles alleles = Alleles.createAlleles(allele1, allele2);
			allelesList.add(alleles);
		}

		return allelesList;
	}

	private Allele createAllele(String sample)
	{
		if (sample.equalsIgnoreCase("?"))
		{
			return Allele.create(null);
		}

		if (sample.equalsIgnoreCase("0"))
		{
			return Allele.create(hapsEntry.getFirstAllele());
		}

		if (sample.equalsIgnoreCase("1"))
		{
			return Allele.create(hapsEntry.getSecondAllele());
		}

		throw new IllegalArgumentException("[" + sample + "] is an invalid value for a haps sample value");
	}
}
