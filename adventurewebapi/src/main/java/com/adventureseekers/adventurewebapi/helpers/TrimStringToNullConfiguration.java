package com.adventureseekers.adventurewebapi.helpers;

import static java.util.Objects.isNull;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.adventureseekers.adventurewebapi.entity.UserDetailEntity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class TrimStringToNullConfiguration
		extends JsonDeserializer<Object> {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public Object deserialize(JsonParser p, DeserializationContext ctxt) 
			throws IOException, JsonProcessingException {
		Object obj = p.readValueAs(Object.class);
		this.trimToNull(obj);
		System.out.println("da");
		return obj;
	}
	

    private Object trimToNull(Object inputClient) throws JsonProcessingException {
        return getObjectMapper().readValue(
        		getObjectMapper().writeValueAsString(inputClient), Object.class);
    }

    private ObjectMapper getObjectMapper() {
        if (isNull(objectMapper)) {
            objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(String.class, new TrimStringToNullDeserializer());
            objectMapper.registerModule(module);
        }
        return objectMapper;
    }

}
