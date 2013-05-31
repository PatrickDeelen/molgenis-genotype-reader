package org.molgenis.genotype;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.molgenis.genotype.util.Utils;
import org.molgenis.genotype.variant.GeneticVariant;

public abstract class IndexedGenotypeData extends AbstractRandomAccessGenotypeData
{

	@Override
	public List<String> getSeqNames()
	{
		return getIndex().getSeqNames();
	}

	@Override
	public List<GeneticVariant> getVariantsByPos(String seqName, int startPos)
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

	@Override
	public Iterator<GeneticVariant> getSequenceGeneticVariants(String seqName)
	{
		return getIndex().createQuery().executeQuery(seqName).iterator();
	}

	protected abstract GenotypeDataIndex getIndex();
}
