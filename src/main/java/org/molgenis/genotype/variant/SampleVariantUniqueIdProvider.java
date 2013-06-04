package org.molgenis.genotype.variant;

public class SampleVariantUniqueIdProvider
{

	private static int counter = 0;

	public static synchronized int getNextUniqueId()
	{
		int nextId = counter;
		counter++;
		return nextId;
	}
}
