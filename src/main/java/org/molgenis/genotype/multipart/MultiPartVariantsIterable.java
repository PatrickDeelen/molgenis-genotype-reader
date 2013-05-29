package org.molgenis.genotype.multipart;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.molgenis.genotype.GenotypeData;
import org.molgenis.genotype.variant.GeneticVariant;

public class MultiPartVariantsIterable implements Iterable<GeneticVariant>
{

	private final Iterable<GenotypeData> datasets;

	public MultiPartVariantsIterable(Iterable<GenotypeData> collection)
	{
		super();
		this.datasets = collection;

	}

	@Override
	public Iterator<GeneticVariant> iterator()
	{
		return new MultiPartVariantIterator(datasets);
	}

	private class MultiPartVariantIterator implements Iterator<GeneticVariant>
	{

		private Iterator<GenotypeData> datasetIterator;
		private Iterator<GeneticVariant> datasetVariantIterator;
		private boolean done = false;

		public MultiPartVariantIterator(Iterable<GenotypeData> datasets)
		{
			super();
			this.datasetIterator = datasets.iterator();
			if (datasetIterator.hasNext())
			{
				this.datasetVariantIterator = datasetIterator.next().getVariants().iterator();
			}
			else
			{
				done = true;
			}

		}

		@Override
		public boolean hasNext()
		{
			if (done)
			{
				return false;
			}

			while (!datasetVariantIterator.hasNext())
			{
				if (datasetIterator.hasNext())
				{
					datasetVariantIterator = datasetIterator.next().getVariants().iterator();
				}
				else
				{
					done = true;
					return false;
				}
			}
			return true;

		}

		@Override
		public GeneticVariant next()
		{
			if (hasNext())
			{
				return datasetVariantIterator.next();
			}
			else
			{
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}

	}

}