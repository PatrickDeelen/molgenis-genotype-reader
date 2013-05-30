package org.molgenis.genotype.variant;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.Allele;
import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.util.Ld;
import org.molgenis.genotype.util.LdCalculatorException;
import org.molgenis.genotype.variant.id.GeneticVariantId;

public class ReadOnlyGeneticVariant implements GeneticVariant
{

	private final GeneticVariantId variantId;
	private final int startPos;
	private final String sequenceName;
	private final Map<String, ?> annotationValues;
	private final SampleVariantsProvider sampleVariantsProvider;
	private final Alleles alleles;
	private final Allele refAllele;

	private ReadOnlyGeneticVariant(GeneticVariantId variantId, int startPos, String sequenceName,
			Map<String, ?> annotationValues, SampleVariantsProvider sampleVariantsProvider, Alleles alleles,
			Allele refAllele)
	{
		super();
		this.variantId = variantId;
		this.startPos = startPos;
		this.sequenceName = sequenceName;
		this.annotationValues = annotationValues;
		this.sampleVariantsProvider = sampleVariantsProvider;
		this.alleles = alleles;
		this.refAllele = refAllele;
	}

	public static GeneticVariant createSnp(String snpId, int pos, String sequenceName,
			SampleVariantsProvider sampleVariantsProvider, char allele1, char allele2)
	{
		return new ReadOnlyGeneticVariant(GeneticVariantId.createVariantId(snpId), pos, sequenceName, null,
				sampleVariantsProvider, Alleles.createBasedOnChars(allele1, allele2), null);
	}

	public static GeneticVariant createSnp(String snpId, int pos, String sequenceName,
			SampleVariantsProvider sampleVariantsProvider, char allele1, char allele2, char refAllele)
	{
		return new ReadOnlyGeneticVariant(GeneticVariantId.createVariantId(snpId), pos, sequenceName, null,
				sampleVariantsProvider, Alleles.createBasedOnChars(allele1, allele2), Allele.create(refAllele));
	}

	public static GeneticVariant createSnp(List<String> snpIds, int pos, String sequenceName,
			SampleVariantsProvider sampleVariantsProvider, char allele1, char allele2)
	{
		return new ReadOnlyGeneticVariant(GeneticVariantId.createVariantId(snpIds), pos, sequenceName, null,
				sampleVariantsProvider, Alleles.createBasedOnChars(allele1, allele2), null);
	}

	public static GeneticVariant createSnp(List<String> snpIds, int pos, String sequenceName,
			SampleVariantsProvider sampleVariantsProvider, char allele1, char allele2, char refAllele)
	{
		return new ReadOnlyGeneticVariant(GeneticVariantId.createVariantId(snpIds), pos, sequenceName, null,
				sampleVariantsProvider, Alleles.createBasedOnChars(allele1, allele2), Allele.create(refAllele));
	}

	public static GeneticVariant createVariant(String variantId, int pos, String sequenceName,
			SampleVariantsProvider sampleVariantsProvider, String allele1, String allele2)
	{
		return new ReadOnlyGeneticVariant(GeneticVariantId.createVariantId(variantId), pos, sequenceName, null,
				sampleVariantsProvider, Alleles.create(allele1, allele2), null);
	}

	public static GeneticVariant createVariant(String variantId, int pos, String sequenceName,
			SampleVariantsProvider sampleVariantsProvider, String allele1, String allele2, String refAllele)
	{
		return new ReadOnlyGeneticVariant(GeneticVariantId.createVariantId(variantId), pos, sequenceName, null,
				sampleVariantsProvider, Alleles.create(allele1, allele2), Allele.create(refAllele));
	}

	public static GeneticVariant createVariant(List<String> variantIds, int pos, String sequenceName,
			SampleVariantsProvider sampleVariantsProvider, String allele1, String allele2)
	{
		return new ReadOnlyGeneticVariant(GeneticVariantId.createVariantId(variantIds), pos, sequenceName, null,
				sampleVariantsProvider, Alleles.create(allele1, allele2), null);
	}

	public static GeneticVariant createVariant(List<String> variantIds, int pos, String sequenceName,
			SampleVariantsProvider sampleVariantsProvider, String allele1, String allele2, String refAllele)
	{
		return new ReadOnlyGeneticVariant(GeneticVariantId.createVariantId(variantIds), pos, sequenceName, null,
				sampleVariantsProvider, Alleles.create(allele1, allele2), Allele.create(refAllele));
	}

	public static GeneticVariant createVariant(String variantId, int pos, String sequenceName,
			SampleVariantsProvider sampleVariantsProvider, List<String> alleles)
	{
		return new ReadOnlyGeneticVariant(GeneticVariantId.createVariantId(variantId), pos, sequenceName, null,
				sampleVariantsProvider, Alleles.createBasedOnString(alleles), null);
	}

	public static GeneticVariant createVariant(String variantId, int pos, String sequenceName,
			SampleVariantsProvider sampleVariantsProvider, List<String> alleles, String refAllele)
	{
		return new ReadOnlyGeneticVariant(GeneticVariantId.createVariantId(variantId), pos, sequenceName, null,
				sampleVariantsProvider, Alleles.createBasedOnString(alleles), Allele.create(refAllele));
	}

	public static GeneticVariant createVariant(List<String> variantIds, int pos, String sequenceName,
			SampleVariantsProvider sampleVariantsProvider, List<String> alleles)
	{
		return new ReadOnlyGeneticVariant(GeneticVariantId.createVariantId(variantIds), pos, sequenceName, null,
				sampleVariantsProvider, Alleles.createBasedOnString(alleles), null);
	}

	public static GeneticVariant createVariant(List<String> variantIds, int pos, String sequenceName,
			SampleVariantsProvider sampleVariantsProvider, List<String> alleles, String refAllele)
	{
		return new ReadOnlyGeneticVariant(GeneticVariantId.createVariantId(variantIds), pos, sequenceName, null,
				sampleVariantsProvider, Alleles.createBasedOnString(alleles), Allele.create(refAllele));
	}

	@Override
	public String getPrimaryVariantId()
	{
		return variantId.getPrimairyId();
	}

	@Override
	public List<String> getAlternativeVariantIds()
	{
		return variantId.getAlternativeIds();
	}

	@Override
	public List<String> getAllIds()
	{
		return variantId.getVariantIds();
	}

	@Override
	public GeneticVariantId getVariantId()
	{
		return variantId;
	}

	@Override
	public int getStartPos()
	{
		return startPos;
	}

	@Override
	public String getSequenceName()
	{
		return sequenceName;
	}

	@Override
	public Alleles getVariantAlleles()
	{
		return alleles;
	}

	@Override
	public int getAlleleCount()
	{
		return alleles.getAlleleCount();
	}

	@Override
	public Allele getRefAllele()
	{
		return refAllele;
	}

	@Override
	public char getSnpRefAllele() throws NotASnpException
	{
		if (!isSnp())
		{
			throw new NotASnpException(this);
		}
		return refAllele.getAlleleAsSnp();
	}

	@Override
	public List<Alleles> getSampleVariants()
	{
		return Collections.unmodifiableList(sampleVariantsProvider.getSampleVariants(this));
	}

	@Override
	public Map<String, ?> getAnnotationValues()
	{
		return Collections.unmodifiableMap(annotationValues);
	}

	@Override
	public float getMinorAlleleFrequency()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Allele getMinorAllele()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSnp()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAtOrGcSnp()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Ld calculateLd(GeneticVariant other) throws LdCalculatorException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBiallelic()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float[] getSampleDosages()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SampleVariantsProvider getSampleVariantsProvider()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getSampleCalledDosage()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
