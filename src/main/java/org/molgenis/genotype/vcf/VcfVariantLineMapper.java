package org.molgenis.genotype.vcf;

import java.util.ArrayList;
import java.util.HashMap;
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
	private static final String END_INFO_ID = "END";
	private final List<String> colNames;
	private final List<String> sampleNames;
	private final List<Annotation> infoAnnotations;
	private final Map<String, String> altDescriptions;

	public VcfVariantLineMapper(List<String> colNames, List<String> sampleNames, List<Annotation> infoAnnotations,
			Map<String, String> altDescriptions)
	{
		this.colNames = colNames;
		this.sampleNames = sampleNames;
		this.infoAnnotations = infoAnnotations;
		this.altDescriptions = altDescriptions;
	}

	@Override
	public GeneticVariant mapLine(String line)
	{
		VcfRecord record = new VcfRecord(line, colNames);

		List<String> ids = record.getId();
		String sequenceName = record.getChrom();
		Integer startPos = record.getPos();
		List<String> alleles = record.getAlleles();
		String refAllele = record.getRef();

		// Get the GT format values (example: 0/0/1)
		List<String> sampleVariants = new ArrayList<String>();
		for (String sampleName : sampleNames)
		{
			VcfSampleGenotype geno = record.getSampleGenotype(sampleName);
			if (geno == null) throw new GenotypeDataException("Missing GT format value for sample [" + sampleName + "]");

			sampleVariants.addAll(geno.getSamleVariants(alleles));
		}

		Map<String, Object> annotationValues = getAnnotationValues(record, infoAnnotations);
		Integer stopPos = (Integer) annotationValues.get(END_INFO_ID);

		// Check if the alt alleles contain references to alt annotaions
		List<String> altTypes = new ArrayList<String>();
		List<String> altDescriptions = new ArrayList<String>();

		if (alleles.size() > 0)
		{
			// First allele is ref
			for (int i = 1; i < alleles.size(); i++)
			{
				String alt = alleles.get(i);
				if ((alt != null) && alt.startsWith("<") && alt.endsWith(">"))
				{
					String altType = alt.substring(1, alt.length() - 1);
					altTypes.add(altType);
					String altDescription = this.altDescriptions.get(altType);
					if (altDescription != null)
					{
						altDescriptions.add(altDescription);
					}
				}
			}
		}

		GeneticVariant variant;
		if (isSnp(alleles))
		{
			variant = new SnpGeneticVariant(ids, sequenceName, startPos, toCharArray(alleles), refAllele.charAt(0),
					sampleVariants, annotationValues, stopPos, altDescriptions, altTypes);
		}
		else
		{

			variant = new GenericGeneticVariant(ids, sequenceName, startPos, alleles, refAllele, sampleVariants,
					annotationValues, stopPos, altDescriptions, altTypes);
		}

		return variant;
	}

	private Map<String, Object> getAnnotationValues(VcfRecord record, List<Annotation> annotations)
	{
		Map<String, Object> annotationValues = new HashMap<String, Object>();

		for (Annotation annotation : annotations)
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

		return annotationValues;
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
