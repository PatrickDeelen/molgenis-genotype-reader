package org.molgenis.genotype.variant.id;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BlankGeneticVariantId extends GeneticVariantId
{

	@Override
	public Iterator<String> iterator()
	{
		return this.getVariantIds().iterator();
	}

	@Override
	public String getPrimairyId()
	{
		return null;
	}

	@Override
	public List<String> getVariantIds()
	{
		return Collections.emptyList();
	}

	@Override
	public List<String> getAlternativeIds()
	{
		return Collections.emptyList();
	}

	@Override
	public String getConcatenatedId()
	{
		return "";
	}

	@Override
	public String getConcatenatedId(String separator)
	{
		return "";
	}

	@Override
	public boolean isIdInVariantIds(String queryId)
	{
		return false;
	}

	@Override
	public boolean onlyPrimairyId()
	{
		return false;
	}

	@Override
	public boolean containsId()
	{
		return false;
	}

}
