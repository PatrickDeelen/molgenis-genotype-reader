//package org.molgenis.genotype.variant;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.molgenis.genotype.Alleles;
//import org.molgenis.genotype.util.Utils;
//
//public class SnpGeneticVariant extends GeneticVariantOld
//{
//
//	public SnpGeneticVariant(List<String> ids, String sequenceName, int startPos, Alleles alleles,
//			String refAllele, Map<String, ?> annotationValues, List<String> altDescriptions, List<String> altTypes,
//			SampleVariantsProvider sampleVariantsProvider)
//	{
//		super(ids, sequenceName, startPos, alleles, refAllele, annotationValues, null, altDescriptions, altTypes,
//				sampleVariantsProvider, GeneticVariantOld.Type.SNP);
//	}
//
//	public SnpGeneticVariant(String id, String sequenceName, int startPos, Alleles alleles, String refAllele,
//			Map<String, ?> annotationValues, List<String> altDescriptions, List<String> altTypes,
//			SampleVariantsProvider sampleVariantsProvider)
//	{
//		super(id, sequenceName, startPos, alleles, refAllele, annotationValues, null, altDescriptions, altTypes,
//				sampleVariantsProvider, GeneticVariantOld.Type.SNP);
//	}
//
//	public List<char[]> getSampleSnpVariants()
//	{
//		List<char[]> sampleSnpVariants = new ArrayList<char[]>();
//		for (Alleles variantAlleles : getSampleVariants())
//		{
//			sampleSnpVariants.add(variantAlleles.getAllelesAsChars());
//		}
//
//		return sampleSnpVariants;
//	}
//
//	public char[] getSnpAlleles()
//	{
//		return getVariantAlleles().getAllelesAsChars();
//	}
//
//	/**
//	 * The alleles of this genetic variants will be swapped for all future
//	 * access
//	 * 
//	 * @return
//	 */
//	public void swapAlleles()
//	{
//		String swappedRef = this.getRefAllele() == null ? null : String.valueOf(Utils.getComplementNucleotide(this
//				.getRefAllele().charAt(0)));
//		this.setRefAllele(swappedRef);
//
//		this.setSampleVariantsProvider(new SwappingSampleVariantsProvider(this.getSampleVariantsProvider()));
//		this.setAlleles(this.getAlleles().getComplement());
//
//		if (this.minorAllele != null)
//		{
//			this.setMinorAllele(String.valueOf(Utils.getComplementNucleotide(this.getMinorAllele().charAt(0))));
//		}
//
//	}
//
// }
