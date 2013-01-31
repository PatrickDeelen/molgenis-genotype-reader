package org.molgenis.genotype.tabix;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.molgenis.genotype.GenotypeDataIndex;
import org.molgenis.genotype.Sequence;
import org.molgenis.genotype.VariantQuery;
import org.molgenis.genotype.variant.GeneticVariant;
import org.molgenis.genotype.variant.SnpGeneticVariant;
import org.molgenis.genotype.variant.SnpVariantHandler;
import org.molgenis.genotype.variant.VariantHandler;

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

	public String getName()
	{
		return name;
	}

	public Integer getLength()
	{
		return length;
	}

	public boolean isChromosome()
	{
		return CHROMOSOMES.contains(name.toLowerCase());
	}

	public void variants(VariantHandler handler)
	{
		VariantQuery q = index.createQuery();
		try
		{
			boolean proceed = true;
			Iterator<GeneticVariant> it = q.executeQuery(name);
			while (it.hasNext() && proceed)
			{
				GeneticVariant variant = it.next();
				proceed = handler.handle(variant);
			}
		}
		finally
		{
			IOUtils.closeQuietly(q);
		}

	}

	public void snpVariants(final SnpVariantHandler handler)
	{
		variants(new VariantHandler()
		{
			public boolean handle(GeneticVariant variant)
			{
				if (variant instanceof SnpGeneticVariant)
				{
					return handler.handle((SnpGeneticVariant) variant);
				}

				return true;
			}
		});
	}

}
