package com.adventureseekers.adventurewebapi.helpers;

import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class UtilityHelperImpl implements UtilityHelper {

	@Override
	public Set<String> convertCSVToSet(String input) {
		String[] values = input.split(",");
		return Set.of(values);
	}

}
