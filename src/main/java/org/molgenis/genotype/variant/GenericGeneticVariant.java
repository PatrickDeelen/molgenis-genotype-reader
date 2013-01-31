package org.molgenis.genotype.variant;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GenericGeneticVariant extends AbstractGeneticVariant
{
	private final List<String> alleles;
	private final String refAllele;

	public GenericGeneticVariant(List<String> ids, String sequenceName, int startPos, List<String> alleles,
			String refAllele, Map<String, List<String>> sampleVariants, Map<String, ?> annotationValues)
	{
		super(ids, sequenceName, startPos, sampleVariants, annotationValues);
		this.alleles = alleles;
		this.refAllele = refAllele;
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

}
