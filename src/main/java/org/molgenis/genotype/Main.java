package org.molgenis.genotype;

import java.io.File;
import java.util.Iterator;

import org.molgenis.genotype.variant.GeneticVariant;
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

		try
		{
			GenotypeData data = new VcfGenotypeData(vcfFile, indexFile);
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
			VariantQueryResult r = seq.getVariants();
			try
			{
				Iterator<GeneticVariant> it = r.iterator();
				while (it.hasNext())
				{
					GeneticVariant v = it.next();
					System.out.println(v.getSequenceName() + ":" + v.getPrimaryVariantId() + ":" + v.getStartPos());
				}
			}
			finally
			{
				r.close();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
