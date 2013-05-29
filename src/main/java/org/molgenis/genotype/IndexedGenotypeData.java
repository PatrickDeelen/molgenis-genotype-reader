package org.molgenis.genotype;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.molgenis.genotype.util.Utils;
import org.molgenis.genotype.variant.GeneticVariantOld;

public abstract class IndexedGenotypeData extends AbstractGenotypeData
{

	@Override
	public List<String> getSeqNames()
	{
		return getIndex().getSeqNames();
	}

	@Override
	public List<GeneticVariantOld> getVariantsByPos(String seqName, int startPos)
	{
		VariantQueryResult result = getIndex().createQuery().executeQuery(seqName, startPos);
		try
		{
			return Utils.iteratorToList(result.iterator());
		}
		finally
		{
			IOUtils.closeQuietly(result);
		}
	}

	protected abstract GenotypeDataIndex getIndex();
}
