package com.fieldcode.tesseract.jackson.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.Track;
import com.fieldcode.tesseract.track.Tracks;

public class TrackDeserializer extends JsonDeserializer<Track> {


  @Override
  public Track deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    var tree = jsonParser.readValueAsTree();

    var origin = jsonParser.getCodec().treeToValue(tree.get("origin"), Presence.class);
    var destination = jsonParser.getCodec().treeToValue(tree.get("destination"), Presence.class);
    var distance = jsonParser.getCodec().treeToValue(tree.get("distance"), Distance.class);

    return Tracks.track(origin, destination, distance);
  }
}
