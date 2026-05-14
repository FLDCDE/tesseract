package com.fieldcode.tesseract.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fieldcode.tesseract.Track;

public class TrackSerializer extends JsonSerializer<Track> {

  @Override
  public void serialize(Track track, JsonGenerator jGen, SerializerProvider serializerProvider) throws IOException {
    jGen.writeStartObject();
    serializerProvider.defaultSerializeField("origin", track.getOrigin(), jGen);
    serializerProvider.defaultSerializeField("destination", track.getDestination(), jGen);
    serializerProvider.defaultSerializeField("distance", track.getDistance(), jGen);
    jGen.writeEndObject();
  }
}
