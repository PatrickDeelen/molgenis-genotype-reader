package org.molgenis.genotype.modifiable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.molgenis.genotype.Allele;
import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.RandomAccessGenotypeData;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SampleVariantsProvider;
import org.molgenis.genotype.variant.SwappingSampleVariantsProvider;
import org.molgenis.genotype.variant.id.GeneticVariantId;

public class ModifiableGenotypeDataInMemory implements ModifiableGenotypeData
{

	private final RandomAccessGenotypeData sourceGenotypeData;
	private final HashMap<GeneticVariant, GeneticVariantId> idUpdates;
	private final HashMap<GeneticVariant, Allele> refAlleleUpdate;
	private final HashMap<GeneticVariant, Alleles> AllelesUpdate;
	private final HashMap<GeneticVariant, SampleVariantsProvider> variantProviderUpdates;

	private final HashMap<SampleVariantsProvider, SwappingSampleVariantsProvider> swappingSampleVariantProviders;

	public ModifiableGenotypeDataInMemory(RandomAccessGenotypeData sourceGenotypeData)
	{
		super();
		this.sourceGenotypeData = sourceGenotypeData;
		this.idUpdates = new HashMap<GeneticVariant, GeneticVariantId>();
		this.refAlleleUpdate = new HashMap<GeneticVariant, Allele>();
		this.AllelesUpdate = new HashMap<GeneticVariant, Alleles>();
		this.variantProviderUpdates = new HashMap<GeneticVariant, SampleVariantsProvider>();
		this.swappingSampleVariantProviders = new HashMap<SampleVariantsProvider, SwappingSampleVariantsProvider>();
	}

	@Override
	public List<String> getSeqNames()
	{
		return sourceGenotypeData.getSeqNames();
	}

	@Override
	public Iterable<Sequence> getSequences()
	{
		return sourceGenotypeData.getSequences();
	}

	@Override
	public Sequence getSequenceByName(String name)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public List<GeneticVariant> getVariantsByPos(String seqName, int startPos)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneticVariant getSnpVariantByPos(String seqName, int startPos)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<GeneticVariant> getSequenceGeneticVariants(String seqName)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Annotation> getVariantAnnotations()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Annotation getVariantAnnotation(String annotationId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sample> getSamples()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<GeneticVariant> iterator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneticVariantId getUpdatedId(GeneticVariant geneticVariant)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Allele getUpdatedRef(GeneticVariant geneticVariant)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SampleVariantsProvider getUpdatedSampleVariantProvider(GeneticVariant geneticVariant)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateVariantId(GeneticVariant geneticVariant, GeneticVariantId newGeneticVariantId)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updateVariantId(GeneticVariant geneticVariant, String newPrimairyId)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void swapGeneticVariant(GeneticVariant geneticVariant)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSamplVariantsProvider(GeneticVariant geneticVariant,
			SampleVariantsProvider newSampleVariantProvider)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRefAllele(GeneticVariant geneticVariant, Allele newRefAllele)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAlleles(GeneticVariant geneticVariant, Alleles newAlleles)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Alleles getUpdatedAlleles(GeneticVariant geneticVariant)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
