package com.pantuo.dao;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pantuo.dao.pojo.JpaCardBoxHelper.Stats;

public class JsonSerializerUtil extends JsonSerializer<Enum> {

	@Override
	public void serialize(Enum value, JsonGenerator generator, SerializerProvider provider) throws IOException,
			JsonProcessingException {

		// output the custom Json
		generator.writeStartObject();

		// the type
		generator.writeFieldName("name");
		generator.writeString(value.name());

		// the full name
		generator.writeFieldName("nameStr");
		if (value.getClass().isAssignableFrom(Stats.class)) {
			generator.writeString(Enum.valueOf(Stats.class, value.name()).getNameStr());
		}

		// end tag
		generator.writeEndObject();
	}
}