package org.molgenis.genotype.variant;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GenericGeneticVariant extends AbstractGeneticVariant
{
	private List<String> alleles;
	private String refAllele;

	public GenericGeneticVariant(List<String> ids, String sequenceName, int startPos, List<String> alleles,
			String refAllele, Map<String, List<String>> sampleVariants)
	{
		super(ids, sequenceName, startPos, sampleVariants);
		this.alleles = alleles;
		this.refAllele = refAllele;
	}

	public List<String> getAlleles()
	{
		return Collections.unmodifiableList(alleles);
	}

	public String getRefAllele()
	{
		return refAllele;
	}

}
