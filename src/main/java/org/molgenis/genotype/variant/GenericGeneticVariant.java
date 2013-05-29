package org.molgenis.genotype.variant;

import java.util.List;
import java.util.Map;

import org.molgenis.genotype.Alleles;

public class GenericGeneticVariant extends GeneticVariantOld
{

	public GenericGeneticVariant(List<String> ids, String sequenceName, int startPos, Alleles alleles,
			String refAllele, Map<String, ?> annotationValues, Integer stopPos, List<String> altDescriptions,
			List<String> altTypes, SampleVariantsProvider sampleVariantsProvider)
	{
		super(ids, sequenceName, startPos, alleles, refAllele, annotationValues, stopPos, altDescriptions, altTypes,
				sampleVariantsProvider, GeneticVariantOld.Type.GENERIC);
	}

	public GenericGeneticVariant(String id, String sequenceName, int startPos, Alleles alleles,
			String refAllele, Map<String, ?> annotationValues, Integer stopPos, List<String> altDescriptions,
			List<String> altTypes, SampleVariantsProvider sampleVariantsProvider)
	{
		super(id, sequenceName, startPos, alleles, refAllele, annotationValues, stopPos, altDescriptions, altTypes,
				sampleVariantsProvider, GeneticVariantOld.Type.GENERIC);
	}

}
