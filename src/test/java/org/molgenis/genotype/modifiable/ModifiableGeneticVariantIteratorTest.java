package org.molgenis.genotype.modifiable;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;

import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.ReadOnlyGeneticVariant;
import org.testng.annotations.Test;

public class ModifiableGeneticVariantIteratorTest
{

	@Test
	public void createModifiableGeneticVariantIterable()
	{
		GeneticVariant variant1 = ReadOnlyGeneticVariant.createSnp("Rs1", 1, "chr1", null, 'A', 'T');
		GeneticVariant variant2 = ReadOnlyGeneticVariant.createSnp("Rs2", 20, "chr1", null, 'G', 'C');
		ArrayList<GeneticVariant> variants = new ArrayList<GeneticVariant>(2);
		variants.add(variant1);
		variants.add(variant2);

		Iterable<ModifiableGeneticVariant> modifiableVariants = ModifiableGeneticVariantIterator
				.createModifiableGeneticVariantIterable(variants.iterator(), null);

		Iterator<ModifiableGeneticVariant> modifiableVariantsIterator = modifiableVariants.iterator();

		assertEquals(modifiableVariantsIterator.next().getOriginalVariant(), variant1);
		assertEquals(modifiableVariantsIterator.next().getOriginalVariant(), variant2);

		assertEquals(modifiableVariantsIterator.hasNext(), false);

	}
}
