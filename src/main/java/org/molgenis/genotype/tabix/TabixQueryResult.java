package org.molgenis.genotype.tabix;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.molgenis.genotype.VariantQueryResult;
import org.molgenis.genotype.variant.GeneticVariantOld;

public class TabixQueryResult implements VariantQueryResult
{
	private final InputStream inputStream;
	private final Iterator<GeneticVariantOld> iterator;

	public TabixQueryResult(InputStream inputStream, Iterator<GeneticVariantOld> iterator)
	{
		super();
		this.inputStream = inputStream;
		this.iterator = iterator;
	}

	@Override
	public void close() throws IOException
	{
		inputStream.close();
	}

	@Override
	public Iterator<GeneticVariantOld> iterator()
	{
		return iterator;
	}

}
