package org.molgenis.genotype.modifiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.molgenis.genotype.Allele;
import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.RandomAccessGenotypeData;
import org.molgenis.genotype.Sample;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.annotation.Annotation;
import org.molgenis.genotype.variant.CachedSampleVariantProvider;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SampleVariantsProvider;
import org.molgenis.genotype.variant.SwappingSampleVariantsProvider;
import org.molgenis.genotype.variant.id.GeneticVariantId;

public class ModifiableGenotypeDataInMemory implements ModifiableGenotypeData
{

	private final RandomAccessGenotypeData sourceGenotypeData;
	private final HashMap<GeneticVariant, GeneticVariantId> idUpdates;
	private final HashMap<GeneticVariant, Allele> refAlleleUpdate;
	private final HashMap<GeneticVariant, Alleles> allelesUpdate;
	private final HashMap<GeneticVariant, SampleVariantsProvider> variantProviderUpdates;

	private final HashMap<SampleVariantsProvider, SampleVariantsProvider> swappingSampleVariantProviders;

	public ModifiableGenotypeDataInMemory(RandomAccessGenotypeData sourceGenotypeData)
	{
		super();
		this.sourceGenotypeData = sourceGenotypeData;
		this.idUpdates = new HashMap<GeneticVariant, GeneticVariantId>();
		this.refAlleleUpdate = new HashMap<GeneticVariant, Allele>();
		this.allelesUpdate = new HashMap<GeneticVariant, Alleles>();
		this.variantProviderUpdates = new HashMap<GeneticVariant, SampleVariantsProvider>();
		this.swappingSampleVariantProviders = new HashMap<SampleVariantsProvider, SampleVariantsProvider>();
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
		Alleles variantAlleles = getUpdatedAlleles(geneticVariant);
		if (variantAlleles == null)
		{
			variantAlleles = geneticVariant.getVariantAlleles();
		}

		Allele refAllele = getUpdatedRef(geneticVariant);
		if (refAllele == null)
		{
			refAllele = geneticVariant.getRefAllele();
		}

		SampleVariantsProvider sampleVariantProvider = getUpdatedSampleVariantProvider(geneticVariant);
		if (sampleVariantProvider == null)
		{
			sampleVariantProvider = geneticVariant.getSampleVariantsProvider();
		}

		SampleVariantsProvider swappingSampleVariantsProvider = swappingSampleVariantProviders
				.get(sampleVariantProvider);
		if (swappingSampleVariantsProvider == null)
		{
			swappingSampleVariantsProvider = new SwappingSampleVariantsProvider(sampleVariantProvider);
			if (sampleVariantProvider.cacheSize() > 0)
			{
				swappingSampleVariantsProvider = new CachedSampleVariantProvider(swappingSampleVariantsProvider,
						sampleVariantProvider.cacheSize());
			}
			swappingSampleVariantProviders.put(sampleVariantProvider, swappingSampleVariantsProvider);
		}

		// TODO is this correct???
		synchronized (this)
		{
			allelesUpdate.put(geneticVariant, variantAlleles.getComplement());
			refAlleleUpdate.put(geneticVariant, refAllele.getComplement());
			variantProviderUpdates.put(geneticVariant, swappingSampleVariantsProvider);
		}

	}

	@Override
	public void updateRefAllele(GeneticVariant geneticVariant, Allele newRefAllele)
	{

		// If no update do nothing expect if there was already a previous
		// update. Reverting back is complicated because that would require
		// recoding the alleles. Might undo intentional changes
		if (geneticVariant.getRefAllele() == newRefAllele && !refAlleleUpdate.containsKey(geneticVariant))
		{
			return;
		}

		Alleles variantAlleles = getUpdatedAlleles(geneticVariant);
		if (variantAlleles == null)
		{
			variantAlleles = geneticVariant.getVariantAlleles();
		}

		if (!variantAlleles.contains(newRefAllele))
		{
			throw new GenotypeDataException("Can not update to refernece allele (" + newRefAllele
					+ ") is not a found in supplied alleles " + variantAlleles.getAllelesAsString()
					+ " for variant with ID: " + geneticVariant.getPrimaryVariantId() + " at: "
					+ geneticVariant.getSequenceName() + ":" + geneticVariant.getStartPos());
		}

		// reference allele is changed so can never be the first allele so lets
		// do the reorder dance :)
		ArrayList<Allele> allelesWithoutRef = new ArrayList<Allele>(variantAlleles.getAlleles());
		allelesWithoutRef.remove(newRefAllele);
		allelesWithoutRef.add(0, newRefAllele);

		// TODO is this correct???
		synchronized (this)
		{
			allelesUpdate.put(geneticVariant, Alleles.createAlleles(allelesWithoutRef));
			refAlleleUpdate.put(geneticVariant, newRefAllele);
		}

	}

	@Override
	public Alleles getUpdatedAlleles(GeneticVariant geneticVariant)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
