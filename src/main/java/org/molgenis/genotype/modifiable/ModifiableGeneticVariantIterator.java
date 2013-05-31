package org.molgenis.genotype.modifiable;

import java.util.Iterator;

import org.molgenis.genotype.variant.GeneticVariant;

public class ModifiableGeneticVariantIterator implements Iterator<ModifiableGeneticVariant>
{

	private final Iterator<GeneticVariant> originalIterator;
	private final ModifiableGenotypeData modifiableGenotypeData;

	public ModifiableGeneticVariantIterator(Iterator<GeneticVariant> originalIterator,
			ModifiableGenotypeData modifiableGenotypeData)
	{
		super();
		this.originalIterator = originalIterator;
		this.modifiableGenotypeData = modifiableGenotypeData;
	}

	@Override
	public boolean hasNext()
	{
		return originalIterator.hasNext();
	}

	@Override
	public ModifiableGeneticVariant next()
	{
		return new ModifiableGeneticVariant(originalIterator.next(), modifiableGenotypeData);
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("Not supported");
	}

	/**
	 * 
	 * 
	 * @param originalIterator
	 *            the original iterator
	 * @param modifiableGenotypeData
	 *            the modifiable genotype data that stores the changes.
	 * @return
	 */
	public static Iterable<ModifiableGeneticVariant> createModifiableGeneticVariantIterable(
			Iterator<GeneticVariant> originalIterator, ModifiableGenotypeData modifiableGenotypeData)
	{
		return new ModifiableGeneticVariantIterable(new ModifiableGeneticVariantIterator(originalIterator,
				modifiableGenotypeData));
	}

	protected static class ModifiableGeneticVariantIterable implements Iterable<ModifiableGeneticVariant>
	{

		private final Iterator<ModifiableGeneticVariant> modifiableGeneticVariantIterator;

		public ModifiableGeneticVariantIterable(Iterator<ModifiableGeneticVariant> modifiableGeneticVariantIterator)
		{
			super();
			this.modifiableGeneticVariantIterator = modifiableGeneticVariantIterator;
		}

		@Override
		public Iterator<ModifiableGeneticVariant> iterator()
		{
			return modifiableGeneticVariantIterator;
		}

	}
}
