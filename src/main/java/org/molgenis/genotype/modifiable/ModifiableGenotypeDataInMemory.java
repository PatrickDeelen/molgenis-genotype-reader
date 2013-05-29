package org.molgenis.genotype.modifiable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.GenotypeData;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.GeneticVariantOld;
import org.molgenis.genotype.variant.IllegalReferenceAlleleException;
import org.molgenis.genotype.variant.SampleVariantsProvider;
import org.molgenis.genotype.variant.SnpGeneticVariant;
import org.molgenis.genotype.variant.SwappingSampleVariantsProvider;
import org.molgenis.genotype.variant.id.GeneticVariantId;

public class ModifiableGenotypeDataInMemory implements ModifiableGenotypeData
{

	private final GenotypeData sourceGenotypeData;
	private final HashMap<GeneticVariantOld, GeneticVariantId> idUpdates;
	private final HashMap<GeneticVariantOld, Alleles> refAlleleUpdate;
	private final HashMap<GeneticVariantOld, SampleVariantsProvider> variantProviderUpdates;

	private final SwappingSampleVariantsProvider swappingSampleVariantProvider;

	public ModifiableGenotypeDataInMemory(GenotypeData sourceGenotypeData)
	{
		super();
		this.sourceGenotypeData = sourceGenotypeData;
		this.swappingSampleVariantProvider = null; // TODO use
													// sourceGenotypeData.getSampleVariantProvider
		this.idUpdates = new HashMap<GeneticVariantOld, GeneticVariantId>();
		this.refAlleleUpdate = new HashMap<GeneticVariantOld, Alleles>();
		this.variantProviderUpdates = new HashMap<GeneticVariantOld, SampleVariantsProvider>();
	}

	@Override
	public List<String> getSeqNames()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Sequence> getSequences()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sequence getSequenceByName(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GeneticVariant> getVariantsByPos(String seqName, int startPos)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SnpGeneticVariant getSnpVariantByPos(String seqName, int startPos)
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
	public GeneticVariantId getUpdatedId(GeneticVariantOld geneticVariant)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Alleles getUpdatedRef(GeneticVariantOld geneticVariant)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SampleVariantsProvider getUpdatedSampleVariantProvider(GeneticVariantOld geneticVariant)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateVariantId(GeneticVariantOld geneticVariant, GeneticVariantId newGeneticVariantId)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updateVariantId(GeneticVariantOld geneticVariant, String newPrimairyId)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void swapGeneticVariant(GeneticVariantOld geneticVariant)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSamplVariantsProvider(GeneticVariantOld geneticVariant,
			SampleVariantsProvider newSampleVariantProvider)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRefAllele(Alleles variantAlleles, Alleles newRefAllele) throws IllegalReferenceAlleleException
	{
		// TODO Auto-generated method stub

	}

}
