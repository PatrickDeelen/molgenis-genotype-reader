package org.molgenis.genotype.plink;

import java.io.IOException;

import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.variant.GeneticVariantOld;
import org.molgenis.genotype.variant.VariantLineMapper;
import org.molgenis.util.plink.datatypes.MapEntry;
import org.molgenis.util.plink.drivers.MapFileDriver;

public class PedMapVariantLineMapper implements VariantLineMapper
{
	private PedMapGenotypeData pedMapGenotypeData;

	public PedMapVariantLineMapper(PedMapGenotypeData pedMapGenotypeData)
	{
		this.pedMapGenotypeData = pedMapGenotypeData;
	}

	@Override
	public GeneticVariantOld mapLine(String line)
	{
		try
		{
			MapEntry mapEntry = MapFileDriver.parseEntry(line, MapFileDriver.DEFAULT_READ_FIELD_SEPARATORS);

			return pedMapGenotypeData.getVariantById(mapEntry.getSNP());
		}
		catch (IOException e)
		{
			throw new GenotypeDataException(e);
		}
	}

}
