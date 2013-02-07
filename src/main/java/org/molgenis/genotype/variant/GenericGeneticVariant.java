package org.molgenis.genotype.variant;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GenericGeneticVariant extends AbstractGeneticVariant
{
	private final List<String> alleles;
	private final String refAllele;
	private String minorAllele = null;
	private float minorAlleleFreq = 0;

	public GenericGeneticVariant(List<String> ids, String sequenceName, int startPos, List<String> alleles,
			String refAllele, Map<String, List<String>> sampleVariants, Map<String, ?> annotationValues,
			Integer stopPos, List<String> altDescriptions, List<String> altTypes)
	{
		super(ids, sequenceName, startPos, sampleVariants, annotationValues, stopPos, altDescriptions, altTypes);
		this.alleles = alleles;
		this.refAllele = refAllele;
	}

	@Override
	public List<String> getAlleles()
	{
		return Collections.unmodifiableList(alleles);
	}

	@Override
	public String getRefAllele()
	{
		return refAllele;
	}

	@Override
	public String getMinorAllele() {
		if(minorAllele == null){
			deterimeMinorAllele();
		}
		return minorAllele;
	}
	
	@Override
	public float getMinorAlleleFrequency() {
		if(minorAllele == null){
			deterimeMinorAllele();
		}
		return minorAlleleFreq;
	}
	
	/**
	 * Determine the minor allele and its frequency and fill in these values
	 */
	private void deterimeMinorAllele(){
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	

}
