package org.molgenis.genotype.vcf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.variant.GenericGeneticVariant;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SnpGeneticVariant;
import org.molgenis.genotype.variant.VariantLineMapper;
import org.molgenis.io.vcf.VcfRecord;
import org.molgenis.io.vcf.VcfSampleGenotype;

public class VcfVariantLineMapper implements VariantLineMapper
{
	private final List<String> colNames;
	private final List<String> sampleNames;
	private final List<Annotation> variantAnnotations;

	public VcfVariantLineMapper(List<String> colNames, List<String> sampleNames, List<Annotation> variantAnnotations)
	{
		this.colNames = colNames;
		this.sampleNames = sampleNames;
		this.variantAnnotations = variantAnnotations;
	}

	public GeneticVariant mapLine(String line)
	{
		VcfRecord record = new VcfRecord(line, colNames);

		List<String> ids = record.getId();
		String sequenceName = record.getChrom();
		Integer startPos = record.getPos();
		List<String> alleles = record.getAlleles();
		String refAllele = record.getRef();

		// Get the GT format values (example: 0/0/1)
		Map<String, List<String>> sampleVariantsBySampleId = new LinkedHashMap<String, List<String>>();
		for (String sampleName : sampleNames)
		{
			VcfSampleGenotype geno = record.getSampleGenotype(sampleName);
			if (geno == null) throw new GenotypeDataException("Missing GT format value for sample [" + sampleName + "]");

			List<String> sampleVariants = geno.getSamleVariants(alleles);
			sampleVariantsBySampleId.put(sampleName, Collections.unmodifiableList(sampleVariants));
		}

		// Get the annotation values
		Map<String, Object> annotationValues = new HashMap<String, Object>();
		for (Annotation annotation : variantAnnotations)
		{
			String annoId = annotation.getId();
			Object annoValue = null;

			List<String> values = record.getInfo(annotation.getId());
			if ((values != null) && !values.isEmpty())
			{
				switch (annotation.getType())
				{
					case INTEGER:
						if (annotation.isList())
						{
							List<Integer> ints = new ArrayList<Integer>();
							for (String value : values)
							{
								ints.add(Integer.valueOf(value));
							}
							annoValue = ints;
						}
						else
						{
							annoValue = Integer.valueOf(values.get(0));
						}
						break;
					case BOOLEAN:
						if (annotation.isList())
						{
							List<Boolean> bools = new ArrayList<Boolean>();
							for (String value : values)
							{
								bools.add(Boolean.parseBoolean(value));
							}
							annoValue = bools;
						}
						else
						{
							annoValue = Boolean.parseBoolean(values.get(0));
						}
						break;
					case FLOAT:
						if (annotation.isList())
						{
							List<Float> floats = new ArrayList<Float>();
							for (String value : values)
							{
								floats.add(Float.parseFloat(value));
							}
							annoValue = floats;
						}
						else
						{
							annoValue = Float.parseFloat(values.get(0));
						}
						break;
					case CHAR:
						if (annotation.isList())
						{
							List<Character> chars = new ArrayList<Character>();
							for (String value : values)
							{
								chars.add(value.charAt(0));
							}
							annoValue = chars;
						}
						else
						{
							annoValue = Character.valueOf(values.get(0).charAt(0));
						}
						break;
					default:
						if (annotation.isList())
						{
							annoValue = values;
						}
						else
						{
							annoValue = values.get(0);
						}
				}

				if (annoValue != null)
				{
					annotationValues.put(annoId, annoValue);
				}
			}
		}

		GeneticVariant variant;
		if (isSnp(alleles))
		{
			variant = new SnpGeneticVariant(ids, sequenceName, startPos, toCharArray(alleles), refAllele.charAt(0),
					sampleVariantsBySampleId, annotationValues);
		}
		else
		{
			variant = new GenericGeneticVariant(ids, sequenceName, startPos, alleles, refAllele,
					sampleVariantsBySampleId, annotationValues);
		}

		return variant;
	}

	// Variant is a SNP if all alleles are one nucleotide long
	private boolean isSnp(List<String> alleles)
	{
		if (alleles.size() < 2)
		{
			// This is a monomorphic reference (i.e. with no alternate alleles)
			return false;
		}

		for (String allele : alleles)
		{
			if (allele.length() != 1)
			{
				return false;
			}
		}

		return true;
	}

	private char[] toCharArray(List<String> alleles)
	{
		char[] result = new char[alleles.size()];
		for (int i = 0; i < alleles.size(); i++)
		{
			result[i] = alleles.get(i).charAt(0);
		}

		return result;
	}

}
