package com.lowheap.poc.eventualconsistency.lib.common.serializers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * A copy of the LocalDateDeserializer that fixes deserializing unix timestamps that come from debezium.
 * This statement at line 96 fixes the issue: LocalDate.ofInstant(Instant.ofEpochMilli(parser.getLongValue()), ZoneId.systemDefault())
 * For more details compare with LocalDateDeserializer.
 */
public class DebeziumLocalDateDeserializer extends JSR310DateTimeDeserializerBase<LocalDate> {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DEFAULT_FORMATTER;
    public static final DebeziumLocalDateDeserializer INSTANCE;

    protected DebeziumLocalDateDeserializer() {
        this(DEFAULT_FORMATTER);
    }

    public DebeziumLocalDateDeserializer(DateTimeFormatter dtf) {
        super(LocalDate.class, dtf);
    }

    public DebeziumLocalDateDeserializer(DebeziumLocalDateDeserializer base, DateTimeFormatter dtf) {
        super(base, dtf);
    }

    protected DebeziumLocalDateDeserializer(DebeziumLocalDateDeserializer base, Boolean leniency) {
        super(base, leniency);
    }

    protected DebeziumLocalDateDeserializer(DebeziumLocalDateDeserializer base, JsonFormat.Shape shape) {
        super(base, shape);
    }

    protected DebeziumLocalDateDeserializer withDateFormat(DateTimeFormatter dtf) {
        return new DebeziumLocalDateDeserializer(this, dtf);
    }

    protected DebeziumLocalDateDeserializer withLeniency(Boolean leniency) {
        return new DebeziumLocalDateDeserializer(this, leniency);
    }

    protected DebeziumLocalDateDeserializer withShape(JsonFormat.Shape shape) {
        return new DebeziumLocalDateDeserializer(this, shape);
    }

    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
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
                    LocalDate parsed = this.deserialize(parser, context);
                    if (parser.nextToken() != JsonToken.END_ARRAY) {
                        this.handleMissingEndArrayForSingle(parser, context);
                    }

                    return parsed;
                }

                if (t == JsonToken.VALUE_NUMBER_INT) {
                    int year = parser.getIntValue();
                    int month = parser.nextIntValue(-1);
                    int day = parser.nextIntValue(-1);
                    if (parser.nextToken() != JsonToken.END_ARRAY) {
                        throw context.wrongTokenException(parser, this.handledType(), JsonToken.END_ARRAY, "Expected array to end");
                    }

                    return LocalDate.of(year, month, day);
                }

                context.reportInputMismatch(this.handledType(), "Unexpected token (%s) within Array, expected VALUE_NUMBER_INT", new Object[]{t});
            }

            if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
                return (LocalDate) parser.getEmbeddedObject();
            } else if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                return this._shape != JsonFormat.Shape.NUMBER_INT && !this.isLenient() ? (LocalDate) this._failForNotLenient(parser, context, JsonToken.VALUE_STRING) : LocalDate.ofInstant(Instant.ofEpochMilli(parser.getLongValue()), ZoneId.systemDefault());
            } else {
                return (LocalDate) this._handleUnexpectedToken(context, parser, "Expected array or string.", new Object[0]);
            }
        }
    }

    protected LocalDate _fromString(JsonParser p, DeserializationContext ctxt, String string0) throws IOException {
        String string = string0.trim();
        if (string.length() == 0) {
            return (LocalDate) this._fromEmptyString(p, ctxt, string);
        } else {
            try {
                DateTimeFormatter format = this._formatter;
                if (format == DEFAULT_FORMATTER && string.length() > 10 && string.charAt(10) == 'T') {
                    if (this.isLenient()) {
                        return string.endsWith("Z") ? LocalDate.parse(string.substring(0, string.length() - 1), DateTimeFormatter.ISO_LOCAL_DATE_TIME) : LocalDate.parse(string, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    } else {
                        JavaType t = this.getValueType(ctxt);
                        return (LocalDate) ctxt.handleWeirdStringValue(t.getRawClass(), string, "Should not contain time component when 'strict' mode set for property or type (enable 'lenient' handling to allow)", new Object[0]);
                    }
                } else {
                    return LocalDate.parse(string, format);
                }
            } catch (DateTimeException var7) {
                return (LocalDate) this._handleDateTimeException(ctxt, var7, string);
            }
        }
    }

    static {
        DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
        INSTANCE = new DebeziumLocalDateDeserializer();
    }
}
