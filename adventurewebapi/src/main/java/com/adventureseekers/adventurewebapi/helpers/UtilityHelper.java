package com.adventureseekers.adventurewebapi.helpers;

import java.util.Set;

public interface UtilityHelper {
	
	/**
	 * Converts a CSV string to a set of values.
	 * @param input The CSV string
	 * @return A set of values
	 */
	public Set<String> convertCSVToSet(String input);
}
