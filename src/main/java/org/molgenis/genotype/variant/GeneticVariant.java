package org.molgenis.genotype.variant;

import org.molgenis.genotype.Sequence;

public interface GeneticVariant
{
	String getVariantId();

	Long getStartPos();

	Sequence getSequence();
}
