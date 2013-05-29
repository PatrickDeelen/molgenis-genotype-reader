package org.molgenis.genotype;

import java.util.List;

import org.molgenis.genotype.variant.GeneticVariant;

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
	public GeneticVariant getSnpVariantByPos(String seqName, int startPos)
	{
		List<GeneticVariant> variants = getVariantsByPos(seqName, startPos);

		for (GeneticVariant variant : variants)
		{
			if (variant.isSnp())
			{
				// only one SNP possible per position. Returning this SNP only
				return variant;
			}
		}

		return null;

	}
}
