package org.molgenis.genotype.variant;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GenericGeneticVariant extends AbstractGeneticVariant
{
	private final List<String> alleles;
	private final String refAllele;
	private String minorAllele = null;
	private float minorAlleleFreq = 0;
	private final List<List<String>> sampleVariants;

	public GenericGeneticVariant(List<String> ids, String sequenceName, int startPos, List<String> alleles,
			String refAllele, List<List<String>> sampleVariants, Map<String, ?> annotationValues, Integer stopPos,
			List<String> altDescriptions, List<String> altTypes)
	{
		super(ids, sequenceName, startPos, annotationValues, stopPos, altDescriptions, altTypes);
		this.alleles = alleles;
		this.refAllele = refAllele;

		if (sampleVariants == null) throw new IllegalArgumentException("SampleVariants map is null");
		this.sampleVariants = sampleVariants;
	}

	@Override
	public List<String> getAlleles()
	{
		return Collections.unmodifiableList(alleles);
	}

	@Override
	public String getRefAllele()
	{
		return refAllele;
	}

	@Override
	public String getMinorAllele()
	{
		if (minorAllele == null)
		{
			deterimeMinorAllele();
		}
		return minorAllele;
	}

	@Override
	public List<List<String>> getSampleVariants()
	{
		return sampleVariants;
	}

	@Override
	public float getMinorAlleleFrequency()
	{
		if (minorAllele == null)
		{
			deterimeMinorAllele();
		}
		return minorAlleleFreq;
	}

	/**
	 * Determine the minor allele and its frequency and fill in these values
	 */
	private void deterimeMinorAllele()
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}

}
