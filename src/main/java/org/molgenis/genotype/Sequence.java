package org.molgenis.genotype;

public class Sequence
{
	private String name;
	private Long length;

	public Sequence(String name, Long length)
	{
		this.name = name;
		this.length = length;
	}

	public String getName()
	{
		return name;
	}

	public Long getLength()
	{
		return length;
	}
}
