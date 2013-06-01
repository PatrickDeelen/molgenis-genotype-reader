package org.molgenis.genotype.modifiable;

import java.util.Iterator;

import org.molgenis.genotype.variant.GeneticVariant;

public class ModifiableGeneticVariantIterator<E extends GeneticVariant> implements Iterator<E>
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

	@SuppressWarnings("unchecked")
	@Override
	public E next()
	{
		return (E) new ModifiableGeneticVariant(originalIterator.next(), modifiableGenotypeData);
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("Not supported");
	}

	/**
	 * Wrap genetic variant iterator to return modifiable genetic variants
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
		return new ModifiableGeneticVariantIterable<ModifiableGeneticVariant>(
				new ModifiableGeneticVariantIterator<ModifiableGeneticVariant>(originalIterator, modifiableGenotypeData));
	}

	/**
	 * Wrap genetic variant iterable to return genetic variants that are
	 * modifiable
	 * 
	 * @param originalIterator
	 * @param modifiableGenotypeData
	 * @return
	 */
	public static Iterable<GeneticVariant> createGeneticVariantIterableBackByModifiable(
			Iterator<GeneticVariant> originalIterator, ModifiableGenotypeData modifiableGenotypeData)
	{
		return new ModifiableGeneticVariantIterable<GeneticVariant>(
				new ModifiableGeneticVariantIterator<GeneticVariant>(originalIterator, modifiableGenotypeData));
	}

	protected static class ModifiableGeneticVariantIterable<E extends GeneticVariant> implements Iterable<E>
	{

		private final Iterator<E> modifiableGeneticVariantIterator;

		public ModifiableGeneticVariantIterable(Iterator<E> modifiableGeneticVariantIterator)
		{
			super();
			this.modifiableGeneticVariantIterator = modifiableGeneticVariantIterator;
		}

		@Override
		public Iterator<E> iterator()
		{
			return modifiableGeneticVariantIterator;
		}

	}
}
