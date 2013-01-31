package org.molgenis.genotype;

import java.io.File;

import org.apache.commons.io.IOUtils;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.VariantHandler;
import org.molgenis.genotype.vcf.VcfGenotypeData;

public class Main
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		File vcfFile = new File("/Users/erwin/Documents/data/vcf/chr17.vcf.gz");
		File indexFile = new File("/Users/erwin/Documents/data/vcf/chr17.vcf.gz.tbi");

		GenotypeData data = null;
		try
		{
			data = new VcfGenotypeData(vcfFile, indexFile);
			System.out.println(data.getSeqNames());

			// Name: rs8065600, start: 13676927, alleles: [C, T]
			// GeneticVariant variant = data.getVariant("17", 13676927);
			// System.out.println(variant.getCompoundId());
			// System.out.println(variant.getAlleles());
			// Map<String, List<String>> sampleVariants =
			// variant.getSampleVariants();
			//
			// for (String id : sampleVariants.keySet())
			// {
			// System.out.println(id + ":" + sampleVariants.get(id));
			// }

			// System.out.println("Sample count:" + sampleVariants.size());

			Sequence seq = data.getSequenceByName("17");
			CountingVariantHandler handler = new CountingVariantHandler();
			seq.variants(handler);
			System.out.println("Count: " + handler.getCount());

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(data);
		}

	}

	private static class CountingVariantHandler implements VariantHandler
	{
		private int count = 0;

		public int getCount()
		{
			return count;
		}

		public void handle(GeneticVariant variant)
		{
			System.out.printf("Name: %s, start: %s, alleles: %s\n", variant.getCompoundId(), variant.getStartPos(),
					variant.getAlleles());
			if (count % 1000 == 0)
			{
				System.out.println(count);
			}
			count++;
		}
	}
}
