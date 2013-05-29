package org.molgenis.genotype;

import java.util.List;

import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SnpGeneticVariant;

public abstract class AbstractRandomAccessGenotypeData extends AbstractGenotypeData implements RandomAccessGenotypeData
{
	@Override
	public Sequence getSequenceByName(String name)
	{
		for (Sequence sequence : getSequences())
		{
			if (sequence.getName().equals(name))
			{
				return sequence;
			}
		}

		return null;
	}

	@Override
	public SnpGeneticVariant getSnpVariantByPos(String seqName, int startPos)
	{
		List<GeneticVariant> variants = getVariantsByPos(seqName, startPos);

		for (GeneticVariant variant : variants)
		{
			if (variant.getType() == GeneticVariant.Type.SNP)
			{
				// only one SNP possible per position. Returning this SNP only
				return (SnpGeneticVariant) variant;
			}
		}

		return null;

	}
}
