package org.molgenis.genotype.util;

import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.molgenis.genotype.Allele;
import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.variant.AbstractGeneticVariant;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SampleVariantsProvider;
import org.molgenis.genotype.variant.id.GeneticVariantId;

public class GeneticVariantTreeSet<E extends GeneticVariant> extends TreeSet<E>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // TODO is this correct

	/**
	 * Get all variants on a sequence
	 * 
	 * @param sequenceName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public NavigableSet<E> getSequenceVariants(String sequenceName)
	{
		return subSet((E) new DummyGenticVariant(sequenceName, Integer.MIN_VALUE), true, (E) new DummyGenticVariant(
				sequenceName, Integer.MAX_VALUE), true);
	}

	/**
	 * Get all variants at a specific position
	 * 
	 * @param sequenceName
	 * @param startPos
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public NavigableSet<E> getSequencePosVariants(String sequenceName, int startPos)
	{
		// Do this -1 and +1 to disregard ordering of allele or sample variant
		// provider
		return subSet((E) new DummyGenticVariant(sequenceName, startPos - 1), true, (E) new DummyGenticVariant(
				sequenceName, startPos + 1), false);
	}

	/**
	 * Get all variants within a specific range
	 * 
	 * @param sequenceName
	 * @param rangeStart
	 * @param startInclusive
	 * @param rangeStop
	 * @param stopInclusive
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public NavigableSet<E> getSequenceRangeVariants(String sequenceName, int rangeStart, boolean startInclusive,
			int rangeStop, boolean stopInclusive)
	{
		// Do this -1 and +1 to disregard ordering of allele or sample variant
		// provider
		return subSet((E) new DummyGenticVariant(sequenceName, startInclusive ? rangeStart - 1 : rangeStart), true,
				(E) new DummyGenticVariant(sequenceName, stopInclusive ? rangeStop + 1 : rangeStop), false);
	}

	/**
	 * Dummy genetic variant class used tp specify a range in the treeset
	 * 
	 * @author Patrick Deelen
	 * 
	 */
	private class DummyGenticVariant extends AbstractGeneticVariant
	{

		private final String sequenceName;
		private final int startPos;

		public DummyGenticVariant(String sequenceName, int startPos)
		{
			super();
			this.sequenceName = sequenceName;
			this.startPos = startPos;
		}

		@Override
		public int getStartPos()
		{
			return startPos;
		}

		@Override
		public String getSequenceName()
		{
			return sequenceName;
		}

		@Override
		public String getPrimaryVariantId()
		{
			return null;
		}

		@Override
		public List<String> getAlternativeVariantIds()
		{
			return null;
		}

		@Override
		public List<String> getAllIds()
		{
			return null;
		}

		@Override
		public GeneticVariantId getVariantId()
		{
			return null;
		}

		@Override
		public Alleles getVariantAlleles()
		{
			return null;
		}

		@Override
		public int getAlleleCount()
		{
			return 0;
		}

		@Override
		public Allele getRefAllele()
		{
			return null;
		}

		@Override
		public List<Alleles> getSampleVariants()
		{
			return null;
		}

		@Override
		public Map<String, ?> getAnnotationValues()
		{
			return null;
		}

		@Override
		public double getMinorAlleleFrequency()
		{
			return 0;
		}

		@Override
		public Allele getMinorAllele()
		{

			return null;
		}

		@Override
		public boolean isSnp()
		{

			return false;
		}

		@Override
		public boolean isAtOrGcSnp()
		{

			return false;
		}

		@Override
		public Ld calculateLd(GeneticVariant other) throws LdCalculatorException
		{

			return null;
		}

		@Override
		public boolean isBiallelic()
		{

			return false;
		}

		@Override
		public float[] getSampleDosages()
		{

			return null;
		}

		@Override
		public byte[] getSampleCalledDosage()
		{

			return null;
		}

		@Override
		public SampleVariantsProvider getSampleVariantsProvider()
		{

			return null;
		}

	}

}
