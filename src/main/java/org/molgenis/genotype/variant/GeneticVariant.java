package org.molgenis.genotype.variant;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.molgenis.genotype.VariantAlleles;
import org.molgenis.genotype.util.Ld;
import org.molgenis.genotype.util.LdCalculator;
import org.molgenis.genotype.util.LdCalculatorException;
import org.molgenis.genotype.variant.id.BlankGeneticVariantId;
import org.molgenis.genotype.variant.id.GeneticVariantId;
import org.molgenis.genotype.variant.id.ListGeneticVariantId;
import org.molgenis.genotype.variant.id.SingleGeneticVariantId;

public class GeneticVariant
{
	public enum Type
	{
		GENERIC, SNP
	}

	private final GeneticVariantId variantId;
	private final int startPos;
	private final String sequenceName;
	private final Map<String, ?> annotationValues;
	private final Integer stopPos;
	private final List<String> altDescriptions;
	private final List<String> altTypes;
	private final SampleVariantsProvider sampleVariantsProvider;
	private final VariantAlleles alleles;
	private final String refAllele;
	private String minorAllele = null;
	private float minorAlleleFreq = 0;
	private final GeneticVariant.Type type;

	public GeneticVariant(List<String> ids, String sequenceName, int startPos, VariantAlleles alleles,
			String refAllele, Map<String, ?> annotationValues, Integer stopPos, List<String> altDescriptions,
			List<String> altTypes, SampleVariantsProvider sampleVariantsProvider, GeneticVariant.Type type)
	{
		if ((ids == null) || ids.isEmpty())
		{
			this.variantId = new BlankGeneticVariantId();
		}
		else if (ids.size() == 0)
		{
			this.variantId = new BlankGeneticVariantId();
		}
		else if (ids.size() == 1)
		{
			this.variantId = new SingleGeneticVariantId(ids.get(0));
		}
		else
		{
			this.variantId = new ListGeneticVariantId(ids);
		}

		this.startPos = startPos;
		this.sequenceName = sequenceName;
		this.annotationValues = annotationValues;
		this.stopPos = stopPos;
		this.altDescriptions = altDescriptions;
		this.altTypes = altTypes;
		this.sampleVariantsProvider = sampleVariantsProvider;
		this.alleles = alleles;
		this.refAllele = refAllele;
		this.type = type;
	}

	public GeneticVariant(String id, String sequenceName, int startPos, VariantAlleles alleles, String refAllele,
			Map<String, ?> annotationValues, Integer stopPos, List<String> altDescriptions, List<String> altTypes,
			SampleVariantsProvider sampleVariantsProvider, GeneticVariant.Type type)
	{
		if (id == null)
		{
			this.variantId = new BlankGeneticVariantId();
		}
		else
		{
			this.variantId = new SingleGeneticVariantId(id);
		}

		this.startPos = startPos;
		this.sequenceName = sequenceName;
		this.annotationValues = annotationValues;
		this.stopPos = stopPos;
		this.altDescriptions = altDescriptions;
		this.altTypes = altTypes;
		this.sampleVariantsProvider = sampleVariantsProvider;
		this.alleles = alleles;
		this.refAllele = refAllele;
		this.type = type;
	}

	/**
	 * A Variant can have multiple id's (it's known under different names). The
	 * compoundId is a concatination of these ids with ';' as separator. This is
	 * the first in the list
	 * 
	 * @return String
	 */
	public String getPrimaryVariantId()
	{
		return variantId.getPrimairyId();
	}

	/**
	 * Gets all the other id's (names) besides the primaryVariantId by which
	 * this variant is known.
	 * 
	 * @return List of String
	 */
	public List<String> getAlternativeVariantIds()
	{
		return variantId.getAlternativeIds();
	}

	/**
	 * Get all IDs for this variant
	 * 
	 * @return List of String
	 */
	public List<String> getAllIds()
	{
		return variantId.getVariantIds();
	}

	/**
	 * Get the variant ID object for this variant
	 * 
	 * @return
	 */
	public GeneticVariantId getVariantId()
	{
		return variantId;
	}

	/**
	 * Gets the starting position on the sequence
	 * 
	 * @return int
	 */
	public int getStartPos()
	{
		return startPos;
	}

	/**
	 * Get the rnd position of the variant, returns null if unknown
	 * 
	 * @return
	 */
	public Integer getStopPos()
	{
		return stopPos;
	}

	/**
	 * Get the Sequence this variant is located on
	 * 
	 * @return the Sequence
	 */
	public String getSequenceName()
	{
		return sequenceName;
	}

	/**
	 * Get all possible alleles (including the reference) The first value is the
	 * reference value
	 * 
	 * @return VariantAlleles
	 */
	public VariantAlleles getVariantAlleles()
	{
		return alleles;
	}

	public int getAlleleCount()
	{
		return alleles.getAlleleCount();
	}

	/**
	 * Gets the reference allele
	 * 
	 * @return String
	 */
	public String getRefAllele()
	{
		return refAllele;
	}

	/**
	 * Returns list sample variants. The list of variants can contain null !!!!
	 * if unknown
	 */
	public List<VariantAlleles> getSampleVariants()
	{
		return Collections.unmodifiableList(sampleVariantsProvider.getSampleVariants(this));
	}

	/**
	 * Get the annotations for this variant. The key is the annotationId, the
	 * value is of the type defined by the Annotation (see
	 * GenotypeData.getVariantAnnotations()
	 * 
	 * @return
	 */
	public Map<String, ?> getAnnotationValues()
	{
		return Collections.unmodifiableMap(annotationValues);
	}

	public List<String> getAltDescriptions()
	{
		return Collections.unmodifiableList(altDescriptions);
	}

	public List<String> getAltTypes()
	{
		return Collections.unmodifiableList(altTypes);
	}

	/**
	 * Get the frequency of the minor allele
	 * 
	 * @return the minor allele frequency
	 */
	public float getMinorAlleleFrequency()
	{
		if (minorAllele == null)
		{
			deterimeMinorAllele();
		}
		return minorAlleleFreq;
	}

	/**
	 * Get the minor allele
	 * 
	 * @return the minor allele
	 */
	public String getMinorAllele()
	{
		if (minorAllele == null)
		{
			deterimeMinorAllele();
		}
		return minorAllele;
	}

	public GeneticVariant.Type getType()
	{
		return type;
	}

	/**
	 * Determine the minor allele and its frequency and fill in these values
	 */
	private void deterimeMinorAllele()
	{

		HashMap<String, AtomicInteger> alleleCounts = new HashMap<String, AtomicInteger>(alleles.getAlleles().size());
		for (String allele : alleles.getAlleles())
		{
			alleleCounts.put(allele, new AtomicInteger());
		}

		for (VariantAlleles sampleAlleles : getSampleVariants())
		{
			if (sampleAlleles != null)
			{
				for (String sampleAllele : sampleAlleles.getAlleles())
				{
					if (sampleAllele != null)
					{
						alleleCounts.get(sampleAllele).incrementAndGet();
					}
				}
			}
		}

		String provisionalMinorAllele = this.getRefAllele();
		int provisionalMinorAlleleCount = alleleCounts.get(this.getRefAllele()).get();
		int totalAlleleCount = 0;

		for (String allele : alleles.getAlleles())
		{

			int alleleCount = alleleCounts.get(allele).get();
			totalAlleleCount += alleleCount;

			if (alleleCount < provisionalMinorAlleleCount)
			{
				provisionalMinorAlleleCount = alleleCounts.get(allele).get();
				provisionalMinorAllele = allele;
			}
		}

		this.minorAllele = provisionalMinorAllele;
		this.minorAlleleFreq = provisionalMinorAlleleCount / (float) totalAlleleCount;
	}

	public boolean isSnp()
	{
		return type == GeneticVariant.Type.SNP ? true : false;
	}

	public Ld calculateLd(GeneticVariant other) throws LdCalculatorException
	{
		return LdCalculator.calculateLd(this, other);
	}

	public boolean isBiallelic()
	{
		return getAlleleCount() == 2 ? true : false;
	}

	/**
	 * 
	 * @return dosage based on called genotypes (count of ref allele)
	 */
	public byte[] getCalledDosages()
	{
		List<VariantAlleles> sampleVariants = getSampleVariants();

		byte[] dosages = new byte[getSampleVariants().size()];

		for (int i = 0; i < dosages.length; ++i)
		{
			VariantAlleles sampleVariant = sampleVariants.get(i);
			boolean missing = false;
			byte dosage = 0;

			for (String allele : sampleVariant.getAlleles())
			{
				if (allele == null)
				{
					missing = true;
				}
				else if (allele.equals(refAllele))
				{
					++dosage;
				}
			}

			dosages[i] = missing ? -1 : dosage;
		}

		return dosages;
	}
}