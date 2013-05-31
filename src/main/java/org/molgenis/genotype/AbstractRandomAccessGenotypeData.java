package org.molgenis.genotype;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

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

	@Override
	public Iterator<GeneticVariant> iterator()
	{
		return new GeneticVariantsIterator(this);
	}

	private static class GeneticVariantsIterator implements Iterator<GeneticVariant>
	{
		private Iterator<String> seqNames;
		private Iterator<GeneticVariant> seqGeneticVariants;
		private RandomAccessGenotypeData randomAccessGenotypeData;

		public GeneticVariantsIterator(RandomAccessGenotypeData randomAccessGenotypeData)
		{
			seqNames = randomAccessGenotypeData.getSeqNames().iterator();
			seqGeneticVariants = randomAccessGenotypeData.getSequenceGeneticVariants(seqNames.next());
			this.randomAccessGenotypeData = randomAccessGenotypeData;
		}

		@Override
		public boolean hasNext()
		{
			return seqGeneticVariants.hasNext() || seqNames.hasNext();
		}

		@Override
		public GeneticVariant next()
		{
			if (seqGeneticVariants.hasNext())
			{
				return seqGeneticVariants.next();
			}

			if (seqNames.hasNext())
			{
				seqGeneticVariants = randomAccessGenotypeData.getSequenceGeneticVariants(seqNames.next());
				return seqGeneticVariants.next();
			}

			throw new NoSuchElementException();
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}

	}
}
