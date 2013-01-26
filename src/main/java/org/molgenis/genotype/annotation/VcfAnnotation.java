package org.molgenis.genotype.annotation;

import org.apache.commons.lang3.math.NumberUtils;
import org.molgenis.io.vcf.VcfInfo;
import org.molgenis.io.vcf.VcfInfo.InfoType;

public class VcfAnnotation extends Annotation
{
	public static final int NUMBER_UNKNOWN = -1;

	private int number;
	private boolean perAltAllele;
	private boolean perGenotype;

	public static VcfAnnotation fromVcfInfo(VcfInfo info)
	{
		Annotation.Type type = VcfAnnotation.toAnnotationType(info.getType());

		int number = -1;
		boolean perAltAllele = false;
		boolean perGenotype = false;

		// Number can be A -> field has one value per alternate allele
		// Or G -> the field has one value for each possible genotype
		// Or . -> is unknown, or is unbounded
		// Or an Integer
		if (info.getNumber() != null)
		{
			if (info.getNumber().equalsIgnoreCase("A"))
			{
				perAltAllele = true;
			}
			else if (info.getNumber().equalsIgnoreCase("G"))
			{
				perGenotype = true;
			}
			else if (NumberUtils.isDigits(info.getNumber()))
			{
				number = Integer.parseInt(info.getNumber());
			}
		}

		return new VcfAnnotation(info.getId(), info.getDescription(), type, number, perAltAllele, perGenotype);
	}

	private static Annotation.Type toAnnotationType(VcfInfo.InfoType infoType)
	{
		if (infoType == InfoType.CHARACTER) return Type.CHAR;
		if (infoType == InfoType.STRING) return Type.STRING;
		if (infoType == InfoType.FLAG) return Type.BOOLEAN;
		if (infoType == InfoType.FLOAT) return Type.FLOAT;
		if (infoType == InfoType.INTEGER) return Type.INTEGER;
		return Type.UNKOWN;
	}

	public VcfAnnotation(String id, String description, Annotation.Type type, int number, boolean perAltAllele,
			boolean perGenotype)
	{
		super(id, id, description, type);
		this.number = number;
		this.perAltAllele = perAltAllele;
		this.perGenotype = perGenotype;
	}

	public int getNumber()
	{
		return number;
	}

	public boolean isPerAltAllele()
	{
		return perAltAllele;
	}

	public boolean isPerGenotype()
	{
		return perGenotype;
	}
}
