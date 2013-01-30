package org.molgenis.genotype.variant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SnpGeneticVariant extends AbstractGeneticVariant
{
	private char refAllele;
	private char[] snpAlleles;

	public SnpGeneticVariant(List<String> ids, String sequenceName, int startPos, char[] snpAlleles, char refAllele,
			Map<String, List<String>> sampleVariants)
	{
		super(ids, sequenceName, startPos, sampleVariants);
		this.refAllele = refAllele;
		this.snpAlleles = snpAlleles.clone();
	}

	public List<String> getAlleles()
	{
		List<String> alleles = new ArrayList<String>(snpAlleles.length);

		for (char snpAllele : snpAlleles)
		{
			alleles.add(Character.toString(snpAllele));
		}

		return Collections.unmodifiableList(alleles);
	}

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
