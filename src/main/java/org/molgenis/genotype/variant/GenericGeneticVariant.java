package org.molgenis.genotype.variant;

import java.util.List;
import java.util.Map;

import org.molgenis.genotype.VariantAlleles;

public class GenericGeneticVariant extends GeneticVariant
{

	public GenericGeneticVariant(List<String> ids, String sequenceName, int startPos, VariantAlleles alleles,
			String refAllele, Map<String, ?> annotationValues, Integer stopPos, List<String> altDescriptions,
			List<String> altTypes, SampleVariantsProvider sampleVariantsProvider)
	{
		super(ids, sequenceName, startPos, alleles, refAllele, annotationValues, stopPos, altDescriptions, altTypes,
				sampleVariantsProvider, GeneticVariant.Type.GENERIC);
	}

}
