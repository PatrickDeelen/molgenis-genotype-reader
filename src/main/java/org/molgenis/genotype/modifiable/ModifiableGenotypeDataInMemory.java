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
		return sourceGenotypeData.getSequenceByName(name);
	}

	@Override
	public Iterable<GeneticVariant> getVariantsByPos(String seqName, int startPos)
	{
		return sourceGenotypeData.getVariantsByPos(seqName, startPos);
	}

	@Override
	public GeneticVariant getSnpVariantByPos(String seqName, int startPos)
	{
		return sourceGenotypeData.getSnpVariantByPos(seqName, startPos);
	}

	@Override
	public Iterable<GeneticVariant> getSequenceGeneticVariants(String seqName)
	{
		return sourceGenotypeData.getSequenceGeneticVariants(seqName);
	}

	@Override
	public List<Annotation> getVariantAnnotations()
	{
		return sourceGenotypeData.getVariantAnnotations();
	}

	@Override
	public Annotation getVariantAnnotation(String annotationId)
	{
		return sourceGenotypeData.getVariantAnnotation(annotationId);
	}

	@Override
	public List<Sample> getSamples()
	{
		return sourceGenotypeData.getSamples();
	}

	@Override
	public Iterator<GeneticVariant> iterator()
	{
		return sourceGenotypeData.iterator();
	}

	@Override
	public synchronized GeneticVariantId getUpdatedId(GeneticVariant geneticVariant)
	{
		return idUpdates.get(geneticVariant);
	}

	@Override
	public synchronized Allele getUpdatedRef(GeneticVariant geneticVariant)
	{
		return refAlleleUpdate.get(geneticVariant);
	}

	@Override
	public synchronized SampleVariantsProvider getUpdatedSampleVariantProvider(GeneticVariant geneticVariant)
	{
		return variantProviderUpdates.get(geneticVariant);
	}

	@Override
	public synchronized void updateVariantId(GeneticVariant geneticVariant, GeneticVariantId newGeneticVariantId)
	{
		if (geneticVariant.getVariantId().equals(newGeneticVariantId))
		{
			idUpdates.remove(geneticVariant);
			return;
		}
		idUpdates.put(geneticVariant, newGeneticVariantId);
	}

	@Override
	public synchronized void updateVariantPrimaryId(GeneticVariant geneticVariant, String newPrimaryId)
	{

		if (idUpdates.containsKey(geneticVariant))
		{
			if (idUpdates.get(geneticVariant).getPrimairyId().equals(newPrimaryId))
			{
				return;
			}
		}
		else if (geneticVariant.getPrimaryVariantId().equals(newPrimaryId))
		{
			return;
		}

		GeneticVariantId oldId = geneticVariant.getVariantId();
		String oldPrimairyId = oldId.getPrimairyId();

		// Create alternative alleles based on old alternatives
		ArrayList<String> newAlternativeAlleles = new ArrayList<String>(oldId.getAlternativeIds());
		// Remove new primary if it is one of the old alternative
		newAlternativeAlleles.remove(newPrimaryId);
		// add the old primary ID to the alternative ID list
		newAlternativeAlleles.add(oldPrimairyId);

		updateVariantId(geneticVariant, GeneticVariantId.createVariantId(newPrimaryId, newAlternativeAlleles));

	}

	@Override
	public synchronized void swapGeneticVariant(GeneticVariant geneticVariant)
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

		allelesUpdate.put(geneticVariant, variantAlleles.getComplement());
		refAlleleUpdate.put(geneticVariant, refAllele.getComplement());
		variantProviderUpdates.put(geneticVariant, swappingSampleVariantsProvider);

	}

	@Override
	public synchronized void updateRefAllele(GeneticVariant geneticVariant, Allele newRefAllele)
	{

		// If no update do nothing expect if there was already a previous
		// update. Reverting back is complicated because that would require
		// recoding the alleles. Might undo intentional changes in ordering of
		// alternative alleles
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
			throw new GenotypeDataException("Can not update to reference allele (" + newRefAllele
					+ ") is not a found in supplied alleles " + variantAlleles.getAllelesAsString()
					+ " for variant with ID: " + geneticVariant.getPrimaryVariantId() + " at: "
					+ geneticVariant.getSequenceName() + ":" + geneticVariant.getStartPos());
		}

		// reference allele is changed so can never be the first allele so lets
		// do the reorder dance :)
		ArrayList<Allele> allelesWithoutRef = new ArrayList<Allele>(variantAlleles.getAlleles());
		allelesWithoutRef.remove(newRefAllele);
		allelesWithoutRef.add(0, newRefAllele);

		allelesUpdate.put(geneticVariant, Alleles.createAlleles(allelesWithoutRef));
		refAlleleUpdate.put(geneticVariant, newRefAllele);

	}

	@Override
	public synchronized Alleles getUpdatedAlleles(GeneticVariant geneticVariant)
	{
		return allelesUpdate.get(geneticVariant);
	}

	@Override
	public Iterable<ModifiableGeneticVariant> getModifiableSequenceGeneticVariants(String seqName)
	{
		Iterator<GeneticVariant> originalIterator = sourceGenotypeData.getSequenceGeneticVariants(seqName).iterator();
		return ModifiableGeneticVariantIterator.createModifiableGeneticVariantIterable(originalIterator, this);
	}

	@Override
	public Iterable<ModifiableGeneticVariant> getModifiableVariantsByPos(String seqName, int startPos)
	{
		Iterator<GeneticVariant> originalIterator = sourceGenotypeData.getVariantsByPos(seqName, startPos).iterator();
		return ModifiableGeneticVariantIterator.createModifiableGeneticVariantIterable(originalIterator, this);
	}

	@Override
	public ModifiableGeneticVariant getModifiableSnpVariantByPos(String seqName, int startPos)
	{
		return new ModifiableGeneticVariant(sourceGenotypeData.getSnpVariantByPos(seqName, startPos), this);
	}

	@Override
	public Iterable<ModifiableGeneticVariant> getModifiableGeneticVariants()
	{
		return ModifiableGeneticVariantIterator.createModifiableGeneticVariantIterable(this.iterator(), this);
	}

}
