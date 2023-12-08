package com.lowheap.poc.eventualconsistency.lib.common.serializers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * User: Ionut Barau (ionutbarau)
 * Project: eventual-consistency
 * Date: 07.12.2023.
 * Time: 12:25
 */
public class DebeziumLocalDateTimeDeserializer extends JSR310DateTimeDeserializerBase<LocalDateTime> {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DEFAULT_FORMATTER;
    public static final DebeziumLocalDateDeserializer INSTANCE;

    protected DebeziumLocalDateTimeDeserializer() {
        this(DEFAULT_FORMATTER);
    }

    public DebeziumLocalDateTimeDeserializer(DateTimeFormatter dtf) {
        super(LocalDateTime.class, dtf);
    }

    public DebeziumLocalDateTimeDeserializer(DebeziumLocalDateTimeDeserializer base, DateTimeFormatter dtf) {
        super(base, dtf);
    }

    protected DebeziumLocalDateTimeDeserializer(DebeziumLocalDateTimeDeserializer base, Boolean leniency) {
        super(base, leniency);
    }

    protected DebeziumLocalDateTimeDeserializer(DebeziumLocalDateTimeDeserializer base, JsonFormat.Shape shape) {
        super(base, shape);
    }

    protected DebeziumLocalDateTimeDeserializer withDateFormat(DateTimeFormatter dtf) {
        return new DebeziumLocalDateTimeDeserializer(this, dtf);
    }

    protected DebeziumLocalDateTimeDeserializer withLeniency(Boolean leniency) {
        return new DebeziumLocalDateTimeDeserializer(this, leniency);
    }

    protected DebeziumLocalDateTimeDeserializer withShape(JsonFormat.Shape shape) {
        return new DebeziumLocalDateTimeDeserializer(this, shape);
    }

    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasToken(JsonToken.VALUE_STRING)) {
            return this._fromString(parser, context, parser.getText());
        } else if (parser.isExpectedStartObjectToken()) {
            return this._fromString(parser, context, context.extractScalarFromObject(parser, this, this.handledType()));
        } else {
            if (parser.isExpectedStartArrayToken()) {
                JsonToken t = parser.nextToken();
                if (t == JsonToken.END_ARRAY) {
                    return null;
                }

                if (context.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS) && (t == JsonToken.VALUE_STRING || t == JsonToken.VALUE_EMBEDDED_OBJECT)) {
                    LocalDateTime parsed = this.deserialize(parser, context);
                    if (parser.nextToken() != JsonToken.END_ARRAY) {
                        this.handleMissingEndArrayForSingle(parser, context);
                    }

                    return parsed;
                }

                if (t == JsonToken.VALUE_NUMBER_INT) {
                    int year = parser.getIntValue();
                    int month = parser.nextIntValue(-1);
                    int day = parser.nextIntValue(-1);
                    int hour = parser.nextIntValue(-1);
                    int minute = parser.nextIntValue(-1);
                    if (parser.nextToken() != JsonToken.END_ARRAY) {
                        throw context.wrongTokenException(parser, this.handledType(), JsonToken.END_ARRAY, "Expected array to end");
                    }

                    return LocalDateTime.of(year, month, day, hour,minute);
                }

                context.reportInputMismatch(this.handledType(), "Unexpected token (%s) within Array, expected VALUE_NUMBER_INT", new Object[]{t});
            }

            if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
                return (LocalDateTime) parser.getEmbeddedObject();
            } else if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                return this._shape != JsonFormat.Shape.NUMBER_INT && !this.isLenient() ? (LocalDateTime) this._failForNotLenient(parser, context, JsonToken.VALUE_STRING) : LocalDateTime.ofInstant(Instant.ofEpochMilli(parser.getLongValue()), ZoneId.systemDefault());
            } else {
                return (LocalDateTime) this._handleUnexpectedToken(context, parser, "Expected array or string.", new Object[0]);
            }
        }
    }

    protected LocalDateTime _fromString(JsonParser p, DeserializationContext ctxt, String string0) throws IOException {
        String string = string0.trim();
        if (string.length() == 0) {
            return (LocalDateTime) this._fromEmptyString(p, ctxt, string);
        } else {
            try {
                DateTimeFormatter format = this._formatter;
                if (format == DEFAULT_FORMATTER && string.length() > 10 && string.charAt(10) == 'T') {
                    if (this.isLenient()) {
                        return string.endsWith("Z") ? LocalDateTime.parse(string.substring(0, string.length() - 1), DateTimeFormatter.ISO_LOCAL_DATE_TIME) : LocalDateTime.parse(string, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    } else {
                        JavaType t = this.getValueType(ctxt);
                        return (LocalDateTime) ctxt.handleWeirdStringValue(t.getRawClass(), string, "Should not contain time component when 'strict' mode set for property or type (enable 'lenient' handling to allow)", new Object[0]);
                    }
                } else {
                    return LocalDateTime.parse(string, format);
                }
            } catch (DateTimeException var7) {
                return (LocalDateTime) this._handleDateTimeException(ctxt, var7, string);
            }
        }
    }

    static {
        DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
        INSTANCE = new DebeziumLocalDateDeserializer();
    }
}
