package com.adventureseekers.adventurewebapi.helpers;

import static java.util.Objects.isNull;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Trims the strings in the JSON object
 */
public class TrimStringToNullDeserializer
	extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) 
			throws IOException, JsonProcessingException {
		String value = jsonParser.getValueAsString();

        if (isNull(value)) {
            return null;
        }

        value = value.trim();
        if (value.length() == 0) {
            value = null;
        }

        return value;
	}
	
}
