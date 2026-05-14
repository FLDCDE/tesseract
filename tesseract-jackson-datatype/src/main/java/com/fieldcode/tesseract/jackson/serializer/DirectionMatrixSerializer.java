package com.fieldcode.tesseract.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.DirectionMatrix;

public class DirectionMatrixSerializer extends JsonSerializer<DirectionMatrix> {


  @Override
  public void serialize(DirectionMatrix matrix, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {

    generator.writeStartObject();
    serializerProvider.defaultSerializeField("distancesInMeter", matrix.getDistancesInMeter(), generator);
    serializerProvider.defaultSerializeField("durationsInSeconds", matrix.getDurationsInSeconds(), generator);
    serializerProvider.defaultSerializeField("locations", matrix.getLocations(), generator);
    generator.writeEndObject();
  }

}
