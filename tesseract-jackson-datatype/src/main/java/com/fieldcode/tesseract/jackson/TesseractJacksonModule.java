package com.fieldcode.tesseract.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fieldcode.tesseract.Direction;
import com.fieldcode.tesseract.DirectionFeature;
import com.fieldcode.tesseract.DirectionMatrix;
import com.fieldcode.tesseract.Distance;
import com.fieldcode.tesseract.FiniteMoment;
import com.fieldcode.tesseract.InfiniteMoment;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.IntervalCollection;
import com.fieldcode.tesseract.IntervalContainer;
import com.fieldcode.tesseract.IntervalMap;
import com.fieldcode.tesseract.IntervalMap.IntervalEntry;
import com.fieldcode.tesseract.IntervalSet;
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.PresenceCollection;
import com.fieldcode.tesseract.PresenceSet;
import com.fieldcode.tesseract.Signal;
import com.fieldcode.tesseract.SignalMap;
import com.fieldcode.tesseract.Taggable.Tag;
import com.fieldcode.tesseract.Taggable.TagHolder;
import com.fieldcode.tesseract.TaggedIntervalCollection;
import com.fieldcode.tesseract.Track;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.jackson.deserializer.DirectionDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.DirectionMatrixDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.DistanceDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.IntervalCollectionDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.IntervalContainerDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.IntervalEntryDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.IntervalMapDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.IntervalSetDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.PresenceCollectionDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.PresenceSetDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.SignalDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.SignalMapDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.TagDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.TagHolderDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.TaggedIntervalCollectionDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.TrackDeserializer;
import com.fieldcode.tesseract.jackson.deserializer.TrackFeatureDeserializer;
import com.fieldcode.tesseract.jackson.serializer.DirectionMatrixSerializer;
import com.fieldcode.tesseract.jackson.serializer.IntervalCollectionSerializer;
import com.fieldcode.tesseract.jackson.serializer.IntervalContainerSerializer;
import com.fieldcode.tesseract.jackson.serializer.IntervalMapSerializer;
import com.fieldcode.tesseract.jackson.serializer.IntervalSetSerializer;
import com.fieldcode.tesseract.jackson.serializer.PresenceCollectionSerializer;
import com.fieldcode.tesseract.jackson.serializer.PresenceSetSerializer;
import com.fieldcode.tesseract.jackson.serializer.SignalMapSerializer;
import com.fieldcode.tesseract.jackson.serializer.SignalSerializer;
import com.fieldcode.tesseract.jackson.serializer.TagHolderSerializer;
import com.fieldcode.tesseract.jackson.serializer.TagSerializer;
import com.fieldcode.tesseract.jackson.serializer.TaggedIntervalCollectionSerializer;
import com.fieldcode.tesseract.jackson.serializer.TrackSerializer;
import com.fieldcode.tesseract.jackson.serializer.timeline.LayerSerializer;
import com.fieldcode.tesseract.jackson.serializer.timeline.TimelineDeserializer;
import com.fieldcode.tesseract.jackson.serializer.timeline.TimelineEventDeserializer;
import com.fieldcode.tesseract.jackson.serializer.timeline.TimelineEventSerializer;
import com.fieldcode.tesseract.jackson.serializer.timeline.TimelineSerializer;
import com.fieldcode.tesseract.jackson.utils.SimpleStringDeserializer;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.presence.Presences;
import com.fieldcode.tesseract.timeline.Layer;
import com.fieldcode.tesseract.timeline.Timeline;
import com.fieldcode.tesseract.timeline.TimelineEvent;

public class TesseractJacksonModule extends SimpleModule {

  public TesseractJacksonModule() {

    super("Tesseract module", new Version(1, 7, 2, null, "com.fieldcode", "tesseract-jackson-datatype"));

    addSerializer(Moment.class, new ToStringSerializer());
    addSerializer(Interval.class, new ToStringSerializer());
    addSerializer(Location.class, new ToStringSerializer());
    addSerializer(Presence.class, new ToStringSerializer());
    addSerializer(Distance.class, new ToStringSerializer());
    addSerializer(Track.class, new TrackSerializer());
    addSerializer(Signal.class, new SignalSerializer());
    addSerializer(SignalMap.class, new SignalMapSerializer());
    addSerializer(IntervalSet.class, new IntervalSetSerializer());
    addSerializer(PresenceSet.class, new PresenceSetSerializer());
    addSerializer(PresenceCollection.class, new PresenceCollectionSerializer());
    addSerializer(IntervalCollection.class, new IntervalCollectionSerializer());
    addSerializer(TaggedIntervalCollection.class, new TaggedIntervalCollectionSerializer());
    addSerializer(IntervalContainer.class, new IntervalContainerSerializer());
    addSerializer(DirectionMatrix.class, new DirectionMatrixSerializer());
    addSerializer(Tag.class, new TagSerializer());
    addSerializer(TagHolder.class, new TagHolderSerializer());
    addSerializer(IntervalMap.class, new IntervalMapSerializer());

    addSerializer(Layer.class, LayerSerializer.serializer());
    addSerializer(Timeline.class, TimelineSerializer.serializer());
    addSerializer(TimelineEvent.class, TimelineEventSerializer.serializer());

    addDeserializer(Moment.class, SimpleStringDeserializer.of(Moments::parse));
    addDeserializer(FiniteMoment.class, SimpleStringDeserializer.of(Moments::parseFinite));
    addDeserializer(InfiniteMoment.class, SimpleStringDeserializer.of(Moments::parseInfinite));
    addDeserializer(Interval.class, SimpleStringDeserializer.of(Intervals::parse));
    addDeserializer(Signal.class, SignalDeserializer.factory());
    addDeserializer(SignalMap.class, SignalMapDeserializer.factory());
    addDeserializer(Location.class, SimpleStringDeserializer.of(Locations::parse));
    addDeserializer(Presence.class, SimpleStringDeserializer.of(Presences::parse));
    addDeserializer(Direction.class, new DirectionDeserializer());
    addDeserializer(Distance.class, new DistanceDeserializer());
    addDeserializer(Track.class, new TrackDeserializer());
    addDeserializer(DirectionFeature.class, new TrackFeatureDeserializer());
    addDeserializer(IntervalSet.class, new IntervalSetDeserializer());
    addDeserializer(PresenceSet.class, new PresenceSetDeserializer());
    addDeserializer(PresenceCollection.class, new PresenceCollectionDeserializer());
    addDeserializer(IntervalCollection.class, new IntervalCollectionDeserializer());
    addDeserializer(TaggedIntervalCollection.class, new TaggedIntervalCollectionDeserializer());
    addDeserializer(IntervalContainer.class, new IntervalContainerDeserializer());
    addDeserializer(DirectionMatrix.class, new DirectionMatrixDeserializer());
    addDeserializer(Tag.class, new TagDeserializer());
    addDeserializer(TagHolder.class, new TagHolderDeserializer());
    addDeserializer(IntervalEntry.class, new IntervalEntryDeserializer<>());
    addDeserializer(IntervalMap.class, new IntervalMapDeserializer<>());

    addDeserializer(TimelineEvent.class, TimelineEventDeserializer.deserializer());
    addDeserializer(Timeline.class, TimelineDeserializer.deserializer());

  }

}
