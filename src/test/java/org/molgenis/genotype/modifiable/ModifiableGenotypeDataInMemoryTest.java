package org.molgenis.genotype.modifiable;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import org.molgenis.genotype.Allele;
import org.molgenis.genotype.Alleles;
import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.RandomAccessGenotypeData;
import org.molgenis.genotype.ResourceTest;
import org.molgenis.genotype.plink.PedMapGenotypeData;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.id.GeneticVariantId;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ModifiableGenotypeDataInMemoryTest extends ResourceTest
{

	private RandomAccessGenotypeData originalGenotypeData;
	private ModifiableGenotypeDataInMemory modifiableGenotypeData;

	@BeforeMethod
	public void setup() throws FileNotFoundException, IOException, URISyntaxException
	{
		originalGenotypeData = new PedMapGenotypeData(getTestPed(), getTestMap());
		modifiableGenotypeData = new ModifiableGenotypeDataInMemory(originalGenotypeData);
	}

	@Test
	public void getModifiableGeneticVariants()
	{
		Iterator<GeneticVariant> originalGeneticVariants = originalGenotypeData.iterator();
		Iterator<ModifiableGeneticVariant> modifiableGeneticVariants = modifiableGenotypeData
				.getModifiableGeneticVariants().iterator();

		assertEqualsVariantIterators(originalGeneticVariants, modifiableGeneticVariants);

	}

	@Test
	public void getModifiableSequenceGeneticVariants()
	{
		Iterator<GeneticVariant> originalGeneticVariants = originalGenotypeData.getSequenceGeneticVariants("22")
				.iterator();
		Iterator<ModifiableGeneticVariant> modifiableGeneticVariants = modifiableGenotypeData
				.getModifiableSequenceGeneticVariants("22").iterator();

		assertEqualsVariantIterators(originalGeneticVariants, modifiableGeneticVariants);
	}

	@Test
	public void getModifiableSnpVariantByPos()
	{
		GeneticVariant originalVariant = originalGenotypeData.getSnpVariantByPos("22", 14433624);
		ModifiableGeneticVariant modifiableGeneticVariant = modifiableGenotypeData.getModifiableSnpVariantByPos("22",
				14433624);

		assertEquals(modifiableGeneticVariant.getSequenceName(), originalVariant.getSequenceName());
		assertEquals(modifiableGeneticVariant.getStartPos(), originalVariant.getStartPos());

		originalVariant = modifiableGenotypeData.getModifiableSnpVariantByPos("22", 1);
		assertNull(originalVariant);

	}

	@Test
	public void getModifiableVariantsByPos()
	{
		Iterator<GeneticVariant> originalGeneticVariants = originalGenotypeData.getVariantsByPos("22", 14433624)
				.iterator();
		Iterator<ModifiableGeneticVariant> modifiableGeneticVariants = modifiableGenotypeData
				.getModifiableVariantsByPos("22", 14433624).iterator();

		assertEqualsVariantIterators(originalGeneticVariants, modifiableGeneticVariants);
	}

	@Test
	public void getSamples()
	{
		assertEquals(modifiableGenotypeData.getSamples(), originalGenotypeData.getSamples());
	}

	@Test
	public void getSeqNames()
	{
		assertEquals(modifiableGenotypeData.getSeqNames(), originalGenotypeData.getSeqNames());
	}

	@Test
	public void getSequenceByName()
	{
		assertEquals(modifiableGenotypeData.getSequenceByName("22"), originalGenotypeData.getSequenceByName("22"));
	}

	@Test
	public void getSequenceGeneticVariants()
	{
		Iterator<GeneticVariant> originalGeneticVariants = originalGenotypeData.getSequenceGeneticVariants("22")
				.iterator();
		Iterator<GeneticVariant> modifiableGeneticVariants = modifiableGenotypeData.getSequenceGeneticVariants("22")
				.iterator();

		assertEqualsVariantIterators(originalGeneticVariants, modifiableGeneticVariants);
	}

	@Test
	public void getSequences()
	{
		assertEquals(modifiableGenotypeData.getSequences(), originalGenotypeData.getSequences());
	}

	@Test
	public void getSnpVariantByPos()
	{
		GeneticVariant original = originalGenotypeData.getSnpVariantByPos("22", 14433624);
		GeneticVariant modifiable = modifiableGenotypeData.getSnpVariantByPos("22", 14433624);

		assertEquals(modifiable.getSequenceName(), original.getSequenceName());
		assertEquals(modifiable.getStartPos(), original.getStartPos());

		original = modifiableGenotypeData.getSnpVariantByPos("22", 1);
		assertNull(original);
	}

	@Test
	public void getVariantsByPos()
	{
		Iterator<GeneticVariant> originalGeneticVariants = originalGenotypeData.getVariantsByPos("22", 14433624)
				.iterator();
		Iterator<GeneticVariant> modifiableGeneticVariants = modifiableGenotypeData.getVariantsByPos("22", 14433624)
				.iterator();

		assertEqualsVariantIterators(originalGeneticVariants, modifiableGeneticVariants);
	}

	@Test(expectedExceptions = GenotypeDataException.class)
	public void testIllegalRefAllele()
	{

		ModifiableGeneticVariant modifiableGeneticVariant = modifiableGenotypeData.getModifiableSnpVariantByPos("22",
				14431347);

		modifiableGeneticVariant.updateRefAllele(Allele.T);

	}

	@Test
	public void updateRefAllele()
	{

		ModifiableGeneticVariant modifiableGeneticVariant = modifiableGenotypeData.getModifiableSnpVariantByPos("22",
				14431347);

		modifiableGeneticVariant.updateRefAllele(Allele.C);

		assertEquals(modifiableGeneticVariant.getRefAllele(), Allele.C);

		assertEquals(modifiableGenotypeData.getSnpVariantByPos("22", 14431347).getRefAllele(), Allele.C);

		for (GeneticVariant variant : modifiableGenotypeData)
		{
			if (variant.getStartPos() == 14431347)
			{
				assertEquals(variant.getRefAllele(), Allele.C);
			}
		}

		for (GeneticVariant variant : modifiableGenotypeData.getSequenceGeneticVariants("22"))
		{
			if (variant.getStartPos() == 14431347)
			{
				assertEquals(variant.getRefAllele(), Allele.C);
			}
		}

		for (GeneticVariant variant : modifiableGenotypeData.getVariantsByPos("22", 14431347))
		{
			if (variant.getStartPos() == 14431347)
			{
				assertEquals(variant.getRefAllele(), Allele.C);
			}
		}

	}

	@Test
	public void updatePrimaryId()
	{

		ModifiableGeneticVariant modifiableGeneticVariant = modifiableGenotypeData.getModifiableSnpVariantByPos("22",
				14432618);

		// First overwrite the primaryId with original ID nothing should happen
		modifiableGeneticVariant.updatePrimaryId("rs738829");
		assertNull(modifiableGenotypeData.getUpdatedAlleles(modifiableGeneticVariant));

		modifiableGeneticVariant.updatePrimaryId("testId1");
		assertEquals(modifiableGeneticVariant.getPrimaryVariantId(), "testId1");
		assertEquals(modifiableGeneticVariant.getAlternativeVariantIds().size(), 1);
		assertEquals(modifiableGeneticVariant.getAlternativeVariantIds().get(0), "rs738829");

		modifiableGeneticVariant.updatePrimaryId("testId2");
		assertEquals(modifiableGeneticVariant.getPrimaryVariantId(), "testId2");
		assertEquals(modifiableGeneticVariant.getAlternativeVariantIds().size(), 2);
		assertEquals(modifiableGeneticVariant.getAlternativeVariantIds().contains("rs738829"), true);
		assertEquals(modifiableGeneticVariant.getAlternativeVariantIds().contains("testId1"), true);

		modifiableGeneticVariant.updatePrimaryId("testId1");
		assertEquals(modifiableGeneticVariant.getVariantId().getPrimairyId(), "testId1");
		assertEquals(modifiableGeneticVariant.getVariantId().getAlternativeIds().size(), 2);
		assertEquals(modifiableGeneticVariant.getVariantId().getAlternativeIds().contains("rs738829"), true);
		assertEquals(modifiableGeneticVariant.getVariantId().getAlternativeIds().contains("testId2"), true);

		GeneticVariantId expectedId = modifiableGeneticVariant.getVariantId();

		assertEquals(modifiableGenotypeData.getSnpVariantByPos("22", 14432618).getVariantId(), expectedId);

		for (GeneticVariant variant : modifiableGenotypeData)
		{
			if (variant.getStartPos() == 14432618)
			{
				assertEquals(variant.getVariantId(), expectedId);
			}
		}

		for (GeneticVariant variant : modifiableGenotypeData.getSequenceGeneticVariants("22"))
		{
			if (variant.getStartPos() == 14432618)
			{
				assertEquals(variant.getVariantId(), expectedId);
			}
		}

		for (GeneticVariant variant : modifiableGenotypeData.getVariantsByPos("22", 14432618))
		{
			if (variant.getStartPos() == 14432618)
			{
				assertEquals(variant.getVariantId(), expectedId);
			}
		}

	}

	@Test
	public void testSwap()
	{

		int posOfTestVariant = 14433624;

		ModifiableGeneticVariant modifiableGeneticVariant = modifiableGenotypeData.getModifiableSnpVariantByPos("22",
				posOfTestVariant);

		ArrayList<Alleles> expectedSwappedSampleAlleles = new ArrayList<Alleles>(9);
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('C', 'C'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('T', 'C'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('C', 'C'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('C', 'C'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('C', 'C'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('T', 'C'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('C', 'C'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('C', 'C'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('T', 'C'));

		byte[] expectedCalledDosage = new byte[]
		{ 2, 1, 2, 2, 2, 1, 2, 2, 1 };

		modifiableGeneticVariant.swap();

		assertEquals(modifiableGeneticVariant.getSampleVariants(), expectedSwappedSampleAlleles);
		assertEquals(modifiableGeneticVariant.getVariantAlleles(), Alleles.createBasedOnChars('C', 'T'));
		assertEquals(modifiableGeneticVariant.getSampleCalledDosage(), expectedCalledDosage);
		assertNull(modifiableGeneticVariant.getRefAllele());

	}

	@Test
	public void testSwapWithRef()
	{

		int posOfTestVariant = 14433659;

		ModifiableGeneticVariant modifiableGeneticVariant = modifiableGenotypeData.getModifiableSnpVariantByPos("22",
				posOfTestVariant);

		ArrayList<Alleles> expectedSwappedSampleAlleles = new ArrayList<Alleles>(9);
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('G', 'G'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('G', 'G'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('G', 'G'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('G', 'G'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('G', 'G'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('G', 'G'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('G', 'G'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('T', 'G'));
		expectedSwappedSampleAlleles.add(Alleles.createBasedOnChars('G', 'G'));

		byte[] expectedCalledDosage = new byte[]
		{ 0, 0, 0, 0, 0, 0, 0, 1, 0 };

		modifiableGeneticVariant.updateRefAllele(Allele.A);

		modifiableGeneticVariant.swap();

		assertEquals(modifiableGeneticVariant.getSampleVariants(), expectedSwappedSampleAlleles);
		assertEquals(modifiableGeneticVariant.getVariantAlleles(), Alleles.createBasedOnChars('T', 'G'));
		assertEquals(modifiableGeneticVariant.getSampleCalledDosage(), expectedCalledDosage);
		assertEquals(modifiableGeneticVariant.getRefAllele(), Allele.T);

		modifiableGeneticVariant.updateRefAllele(Allele.G);

		byte[] expectedCalledDosage2 = new byte[]
		{ 2, 2, 2, 2, 2, 2, 2, 1, 2 };

		assertEquals(modifiableGeneticVariant.getSampleVariants(), expectedSwappedSampleAlleles);
		assertEquals(modifiableGeneticVariant.getVariantAlleles(), Alleles.createBasedOnChars('G', 'T'));
		assertEquals(modifiableGeneticVariant.getSampleCalledDosage(), expectedCalledDosage2);
		assertEquals(modifiableGeneticVariant.getRefAllele(), Allele.G);

		assertEquals(modifiableGenotypeData.getModifiableSnpVariantByPos("22", posOfTestVariant).getSampleVariants(),
				expectedSwappedSampleAlleles);

		// Make sure swap is present in all ways of accessing

		assertEquals(modifiableGenotypeData.getSnpVariantByPos("22", posOfTestVariant).getSampleVariants(),
				expectedSwappedSampleAlleles);

		for (GeneticVariant variant : modifiableGenotypeData)
		{
			if (variant.getStartPos() == posOfTestVariant)
			{
				assertEquals(variant.getSampleVariants(), expectedSwappedSampleAlleles);
				assertEquals(variant.getVariantAlleles(), Alleles.createBasedOnChars('G', 'T'));
				assertEquals(variant.getSampleCalledDosage(), expectedCalledDosage2);
				assertEquals(variant.getRefAllele(), Allele.G);
			}
		}

		int counter = 0;
		for (GeneticVariant variant : modifiableGenotypeData.getSequenceGeneticVariants("22"))
		{
			if (variant.getStartPos() == posOfTestVariant)
			{
				assertEquals(variant.getSampleVariants(), expectedSwappedSampleAlleles);
				assertEquals(variant.getVariantAlleles(), Alleles.createBasedOnChars('G', 'T'));
				assertEquals(variant.getSampleCalledDosage(), expectedCalledDosage2);
				assertEquals(variant.getRefAllele(), Allele.G);
			}
			++counter;
		}
		assertEquals(counter, 9);

		for (GeneticVariant variant : modifiableGenotypeData.getVariantsByPos("22", 14432618))
		{
			if (variant.getStartPos() == posOfTestVariant)
			{
				assertEquals(variant.getSampleVariants(), expectedSwappedSampleAlleles);
				assertEquals(variant.getVariantAlleles(), Alleles.createBasedOnChars('G', 'T'));
				assertEquals(variant.getSampleCalledDosage(), expectedCalledDosage2);
				assertEquals(variant.getRefAllele(), Allele.G);
			}
		}

		for (GeneticVariant variant : modifiableGenotypeData.getModifiableGeneticVariants())
		{
			if (variant.getStartPos() == posOfTestVariant)
			{
				assertEquals(variant.getSampleVariants(), expectedSwappedSampleAlleles);
				assertEquals(variant.getVariantAlleles(), Alleles.createBasedOnChars('G', 'T'));
				assertEquals(variant.getSampleCalledDosage(), expectedCalledDosage2);
				assertEquals(variant.getRefAllele(), Allele.G);
			}
		}

		counter = 0;
		for (GeneticVariant variant : modifiableGenotypeData.getModifiableSequenceGeneticVariants("22"))
		{
			if (variant.getStartPos() == posOfTestVariant)
			{
				assertEquals(variant.getSampleVariants(), expectedSwappedSampleAlleles);
				assertEquals(variant.getVariantAlleles(), Alleles.createBasedOnChars('G', 'T'));
				assertEquals(variant.getSampleCalledDosage(), expectedCalledDosage2);
				assertEquals(variant.getRefAllele(), Allele.G);
			}
			++counter;
		}
		assertEquals(counter, 9);

		for (GeneticVariant variant : modifiableGenotypeData.getModifiableVariantsByPos("22", 14432618))
		{
			if (variant.getStartPos() == posOfTestVariant)
			{
				assertEquals(variant.getSampleVariants(), expectedSwappedSampleAlleles);
				assertEquals(variant.getVariantAlleles(), Alleles.createBasedOnChars('G', 'T'));
				assertEquals(variant.getSampleCalledDosage(), expectedCalledDosage2);
				assertEquals(variant.getRefAllele(), Allele.G);
			}
		}

	}

	@Test
	public void filterVariants()
	{

		int removePos1 = 14434713;
		String removeChr1 = "22";
		int removePos2 = 14434960;
		String removeChr2 = "23";

		ModifiableGeneticVariant removedVariant1 = modifiableGenotypeData.getModifiableSnpVariantByPos(removeChr1,
				removePos1);
		ModifiableGeneticVariant removedVariant2 = modifiableGenotypeData.getModifiableSnpVariantByPos(removeChr2,
				removePos2);

		modifiableGenotypeData.excludeVariant(removedVariant1);
		removedVariant2.exclude();

		assertNull(modifiableGenotypeData.getModifiableSnpVariantByPos(removeChr1, removePos1));
		assertNull(modifiableGenotypeData.getModifiableSnpVariantByPos(removeChr2, removePos2));

		assertNull(modifiableGenotypeData.getSnpVariantByPos(removeChr1, removePos1));
		assertNull(modifiableGenotypeData.getSnpVariantByPos(removeChr2, removePos2));

		int counter;

		counter = 0;
		for (GeneticVariant variant : modifiableGenotypeData)
		{
			assertEquals(variant.getStartPos() == removePos1, false);
			assertEquals(variant.getStartPos() == removePos2, false);
			++counter;
		}
		assertEquals(counter, 8);

		counter = 0;
		for (GeneticVariant variant : modifiableGenotypeData.getSequenceGeneticVariants("22"))
		{
			assertEquals(variant.getStartPos() == removePos1, false);
			assertEquals(variant.getStartPos() == removePos2, false);
			++counter;
		}
		assertEquals(counter, 8);

		counter = 0;
		for (GeneticVariant variant : modifiableGenotypeData.getVariantsByPos("22", removePos1))
		{
			System.err.println(variant.isSnp());
			System.err.println(variant.getStartPos());
			assertEquals(variant.getStartPos() == removePos1, false);
			assertEquals(variant.getStartPos() == removePos2, false);
			++counter;
		}
		assertEquals(counter, 0);

		counter = 0;
		for (GeneticVariant variant : modifiableGenotypeData.getModifiableGeneticVariants())
		{
			assertEquals(variant.getStartPos() == removePos1, false);
			assertEquals(variant.getStartPos() == removePos2, false);
			++counter;
		}
		assertEquals(counter, 8);

		counter = 0;
		for (GeneticVariant variant : modifiableGenotypeData.getModifiableSequenceGeneticVariants("22"))
		{
			assertEquals(variant.getStartPos() == removePos1, false);
			assertEquals(variant.getStartPos() == removePos2, false);
			++counter;
		}
		assertEquals(counter, 8);

		counter = 0;
		for (GeneticVariant variant : modifiableGenotypeData.getModifiableVariantsByPos("22", removePos1))
		{
			System.err.println(variant.isSnp());
			System.err.println(variant.getStartPos());
			assertEquals(variant.getStartPos() == removePos1, false);
			assertEquals(variant.getStartPos() == removePos2, false);
			++counter;
		}
		assertEquals(counter, 0);

	}

	public static void assertEqualsVariantIterators(Iterator<GeneticVariant> originalGeneticVariants,
			Iterator<? extends GeneticVariant> modifiableGeneticVariants)
	{

		while (originalGeneticVariants.hasNext() && modifiableGeneticVariants.hasNext())
		{
			GeneticVariant originalVariant = originalGeneticVariants.next();
			GeneticVariant modifiableVariant = modifiableGeneticVariants.next();

			assertEquals(modifiableVariant.getSequenceName(), originalVariant.getSequenceName());
			assertEquals(modifiableVariant.getStartPos(), originalVariant.getStartPos());
		}

		if (originalGeneticVariants.hasNext() || modifiableGeneticVariants.hasNext())
		{
			throw new AssertionError("Number of original genetic variatiants is not identical to modifiable variants");
		}

	}
}
