package org.molgenis.genotype.plink;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.molgenis.genotype.GenotypeDataException;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SnpGeneticVariant;
import org.molgenis.genotype.variant.VariantLineMapper;
import org.molgenis.util.plink.datatypes.Biallele;
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
	public GeneticVariant mapLine(String line)
	{
		try
		{
			MapEntry mapEntry = MapFileDriver.parseEntry(line, pedMapGenotypeData.getSeparator() + "");

			List<String> ids = Collections.singletonList(mapEntry.getSNP());
			String sequenceName = mapEntry.getChromosome();
			int startPos = (int) mapEntry.getBpPos();
			Integer stopPos = null;
			List<String> altDescriptions = Collections.emptyList();
			List<String> altTypes = Collections.emptyList();
			Map<String, ?> annotationValues = Collections.emptyMap();
			char[] snpAlleles = new char[]
			{};
			char refAllele = '\0';

			List<Biallele> sampleGenotypes = pedMapGenotypeData.getSampleGenotypes(mapEntry.getSNP());
			List<List<Character>> sampleSnpVariants = new ArrayList<List<Character>>(sampleGenotypes.size());

			for (Biallele biallele : sampleGenotypes)
			{
				sampleSnpVariants.add(Arrays.asList(biallele.getAllele1(), biallele.getAllele2()));
			}

			return new SnpGeneticVariant(ids, sequenceName, startPos, snpAlleles, refAllele, sampleSnpVariants,
					annotationValues, stopPos, altDescriptions, altTypes);
		}
		catch (IOException e)
		{
			throw new GenotypeDataException(e);
		}

	}
}
