package org.molgenis.genotype.tabix;

import java.util.Arrays;
import java.util.List;

import org.molgenis.genotype.GenotypeDataIndex;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.VariantQueryResult;

public class TabixSequence implements Sequence
{
	private static final List<String> CHROMOSOMES = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
			"11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "x", "y");
	private final String name;
	private final Integer length;
	private final GenotypeDataIndex index;

	public TabixSequence(String name, Integer length, GenotypeDataIndex index)
	{
		if (name == null) throw new IllegalArgumentException("Name is null");
		if (index == null) throw new IllegalArgumentException("Index is null");

		this.name = name;
		this.length = length;
		this.index = index;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Integer getLength()
	{
		return length;
	}

	@Override
	public boolean isChromosome()
	{
		return CHROMOSOMES.contains(name.toLowerCase());
	}

	@Override
	public VariantQueryResult getVariants()
	{
		return index.createQuery().executeQuery(name);
	}

}
