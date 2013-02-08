package org.molgenis.genotype.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * General Uilities
 * 
 * @author erwin
 * 
 */
public class Utils
{
	/**
	 * Add all objects from the iterator in a ArrayList
	 * 
	 * @param iterator
	 * @return
	 */
	public static <T> List<T> iteratorToList(Iterator<T> iterator)
	{
		List<T> result = new ArrayList<T>();
		while (iterator.hasNext())
		{
			result.add(iterator.next());
		}

		return result;
	}

}
