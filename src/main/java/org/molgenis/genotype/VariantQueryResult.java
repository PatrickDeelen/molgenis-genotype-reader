package org.molgenis.genotype;

import java.io.Closeable;

import org.molgenis.genotype.variant.GeneticVariantOld;

/**
 * The result of a VariantQuery. Must be closed after use
 * 
 * @author erwin
 * 
 */
public interface VariantQueryResult extends Closeable, Iterable<GeneticVariantOld>
{
}
