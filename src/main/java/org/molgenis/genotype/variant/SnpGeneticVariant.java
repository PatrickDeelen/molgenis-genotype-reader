package org.molgenis.genotype.variant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SnpGeneticVariant extends AbstractGeneticVariant
{
	private final char refAllele;
	private final char[] snpAlleles;

	public SnpGeneticVariant(List<String> ids, String sequenceName, int startPos, char[] snpAlleles, char refAllele,
			Map<String, List<String>> sampleVariants, Map<String, ?> annotationValues)
	{
		super(ids, sequenceName, startPos, sampleVariants, annotationValues);
		this.refAllele = refAllele;
		this.snpAlleles = snpAlleles.clone();
	}

	@Override
	public List<String> getAlleles()
	{
		List<String> alleles = new ArrayList<String>(snpAlleles.length);

		for (char snpAllele : snpAlleles)
		{
			alleles.add(Character.toString(snpAllele));
		}

		return Collections.unmodifiableList(alleles);
	}

	@Override
	public String getRefAllele()
	{
		return Character.toString(refAllele);
	}

	public char getSnpRefAlelle()
	{
		return refAllele;
	}

	public char[] getSnpAlleles()
	{
		return snpAlleles.clone();
	}

}
