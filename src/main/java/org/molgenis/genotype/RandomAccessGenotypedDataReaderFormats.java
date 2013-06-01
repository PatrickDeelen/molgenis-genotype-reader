package org.molgenis.genotype;

import java.io.File;
import java.io.IOException;

import org.molgenis.genotype.multipart.IncompetibleMultiPartGenotypeDataException;
import org.molgenis.genotype.multipart.MultiPartGenotypeData;
import org.molgenis.genotype.plink.PedMapGenotypeData;
import org.molgenis.genotype.vcf.VcfGenotypeData;

public enum RandomAccessGenotypedDataReaderFormats
{

	PED_MAP("PED / MAP file", "plink PED MAP files gziped with tabix index."), VCF("VCF file",
			"gziped vcf with tabix index file"), VCF_FOLDER("VCF folder",
			"Matches all gziped vcf files + tabix index in a folder");

	private final String name;
	private final String description;

	RandomAccessGenotypedDataReaderFormats(String name, String description)
	{
		this.name = name;
		this.description = description;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public RandomAccessGenotypeData createGenotypeData(String path, int cacheSize) throws IOException,
			IncompetibleMultiPartGenotypeDataException
	{

		switch (this)
		{
			case PED_MAP:
				return new PedMapGenotypeData(new File(path + "ped"), new File(path + "map"));
			case VCF:
				return new VcfGenotypeData(new File(path), cacheSize);
			case VCF_FOLDER:
				return MultiPartGenotypeData.createFromVcfFolder(new File(path), cacheSize);
			default:
				throw new RuntimeException("This should not be reachable. Please contact the autors");
		}
	}
}
