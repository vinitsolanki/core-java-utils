package com.util.bean.modules.converters;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.Converter;
import org.modelmapper.internal.Errors;
import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;

import com.util.bean.modules.Java8ModuleConfig;

public class ToTemporalConverter implements ConditionalConverter<Object, Temporal> {

    private Java8ModuleConfig config;

    private final LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();
    private final LocalDateConverter localDateConverter = new LocalDateConverter();
    private final OffsetDateTimeConverter offsetDateTimeConverter = new OffsetDateTimeConverter();
    private final InstantConverter instantConverter = new InstantConverter();
    private final ZonedDateTimeConverter zonedDateTimeConverter = new ZonedDateTimeConverter();

    @Override
    public MatchResult match(Class<?> sourceType, Class<?> destinationType) {
        return Temporal.class.isAssignableFrom(destinationType) ? MatchResult.FULL : MatchResult.NONE;
    }

    @Override
    public Temporal convert(MappingContext<Object, Temporal> mappingContext) {
        Class<?> destinationType = mappingContext.getDestinationType();
        if (LocalDateTime.class.equals(destinationType))
            return localDateTimeConverter.convert(mappingContext);
        else if (LocalDate.class.equals(destinationType))
            return localDateConverter.convert(mappingContext);
        else if (OffsetDateTime.class.equals(destinationType))
            return offsetDateTimeConverter.convert(mappingContext);
        else if (ZonedDateTime.class.equals(destinationType))
            return zonedDateTimeConverter.convert(mappingContext);
        else if (Instant.class.equals(destinationType))
            return instantConverter.convert(mappingContext);
        else
            throw new Errors().addMessage(
                    "Unsupported mapping types[%s->%s]",
                    mappingContext.getSourceType().getName(),
                    destinationType.getName()).toMappingException();
    }

    private class ZonedDateTimeConverter implements Converter<Object, Temporal> {
        @Override
        public Temporal convert(MappingContext<Object, Temporal> mappingContext) {
            return convertZonedDateTime(mappingContext);
        }
    }

    private ZonedDateTime convertZonedDateTime(MappingContext<?, ?> mappingContext) {
        Object source = mappingContext.getSource();
        Class<?> sourceType = source.getClass();
        if (sourceType.equals(String.class)) {
            ZonedDateTime zonedDateTime = null;
            // TODO: here we have support to get the LocalDateTime if the string is in date
            // pattern or date time pattern, So if possible we should support for the
            // time-zone pattern as well. e.g yyyy-MM-dd'T'HH:mm:ss.SSS
            try {
                zonedDateTime = ZonedDateTime.parse(
                        (String) source,
                        DateTimeFormatter.ofPattern(DateParser.determineDateFormat((String) source)));
            } catch (Exception ex) {
                // TODO: need some better way to avoid infinite execution
                zonedDateTime = convertLocalDateTime(mappingContext).atZone(config.getZoneId());
            } finally {
                return zonedDateTime;
            }
        }

        return convertInstant(mappingContext).atZone(config.getZoneId());
    }

    public ToTemporalConverter(Java8ModuleConfig config) {
        this.config = config;
    }

    private class LocalDateTimeConverter implements Converter<Object, Temporal> {
        @Override
        public Temporal convert(MappingContext<Object, Temporal> mappingContext) {
            return convertLocalDateTime(mappingContext);
        }
    }

    private class LocalDateConverter implements Converter<Object, Temporal> {
        @Override
        public Temporal convert(MappingContext<Object, Temporal> mappingContext) {
            return convertLocalDate(mappingContext);
        }
    }

    private class OffsetDateTimeConverter implements Converter<Object, Temporal> {
        @Override
        public Temporal convert(MappingContext<Object, Temporal> mappingContext) {
            return convertOffsetDateTime(mappingContext);
        }
    }

    private class InstantConverter implements Converter<Object, Temporal> {
        @Override
        public Temporal convert(MappingContext<Object, Temporal> mappingContext) {
            return convertInstant(mappingContext);
        }
    }

    private LocalDate convertLocalDate(MappingContext<?, ?> mappingContext) {
        Object source = mappingContext.getSource();
        Class<?> sourceType = source.getClass();
        if (sourceType.equals(String.class)) {
            LocalDate localDate = null;
            try {
                localDate = LocalDate.parse(
                        (String) source,
                        DateTimeFormatter.ofPattern(DateParser.determineDateFormat((String) source)));
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: need some better way to avoid infinite execution
//                localDate = convertOffsetDateTime(mappingContext).toLocalDate();
            } finally {
                return localDate;
            }
        }
        return convertInstant(mappingContext).atZone(config.getZoneId()).toLocalDate();
    }

    private LocalDateTime convertLocalDateTime(MappingContext<?, ?> mappingContext) {
        Object source = mappingContext.getSource();
        Class<?> sourceType = source.getClass();
        if (sourceType.equals(String.class)) {
            LocalDateTime localDateTime = null;
            // TODO: here we have support to get the LocalDateTime if the string is in date
            // pattern or date time pattern, So if possible we should support for the
            // time-zone pattern as well. e.g yyyy-MM-dd'T'HH:mm:ss.SSS
            try {
                localDateTime = LocalDateTime.parse(
                        (String) source,
                        DateTimeFormatter.ofPattern(DateParser.determineDateFormat((String) source)));
            } catch (Exception ex) {
                localDateTime =
                        convertLocalDate(mappingContext).atStartOfDay(config.getZoneId()).toLocalDateTime();
            } finally {
                return localDateTime;
            }

        }
        return convertInstant(mappingContext).atZone(config.getZoneId()).toLocalDateTime();
    }

    private OffsetDateTime convertOffsetDateTime(MappingContext<?, ?> mappingContext) {
        Object source = mappingContext.getSource();
        Class<?> sourceType = source.getClass();
        if (sourceType.equals(String.class)) {
            OffsetDateTime offssetDateTime = null;
            try {
                offssetDateTime = OffsetDateTime.parse(
                        (String) source,
                        DateTimeFormatter.ofPattern(DateParser.determineDateFormat((String) source)));
            } catch (Exception e) {
                offssetDateTime = convertZonedDateTime(mappingContext).toOffsetDateTime();
            } finally {
                return offssetDateTime;
            }
        }

        return convertInstant(mappingContext).atZone(config.getZoneId()).toOffsetDateTime();
    }

    private Instant convertInstant(MappingContext<?, ?> mappingContext) {
        Object source = mappingContext.getSource();
        Class<?> sourceType = source.getClass();
        if (sourceType.equals(String.class))
            return LocalDateTime
                    .parse((String) source, DateTimeFormatter.ofPattern(config.getDateTimePattern()))
                    .atZone(config.getZoneId()).toInstant();
        else if (Date.class.isAssignableFrom(sourceType))
            return Instant.ofEpochMilli(((Date) source).getTime());
        else if (Calendar.class.isAssignableFrom(sourceType))
            return Instant.ofEpochMilli(((Calendar) source).getTime().getTime());
        else if (Number.class.isAssignableFrom(sourceType))
            return Instant.ofEpochMilli(((Number) source).longValue());
        else
            throw new Errors().addMessage(
                    "Unsupported mapping types[%s->%s]",
                    sourceType.getName(),
                    mappingContext.getDestinationType().getName()).toMappingException();
    }

    public static class DateParser {
        private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {
            {
                put("^\\d{8}$", "yyyyMMdd");
                put("^\\d{12}$", "yyyyMMddHHmm");
                put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
                put("^\\d{14}$", "yyyyMMddHHmmss");
                put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
                put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
                put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
                put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
                put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
                put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
                put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
                put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
                put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
                put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
                put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
                put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
                put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
                put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
                put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
                put(
                        "^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}\\.\\d{3}$",
                        "yyyy-MM-dd HH:mm:ss.SSS");
                put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
                put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
                put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
                put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
                put(
                        "^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}\\.\\d{3}$",
                        "yyyy-MM-dd'T'HH:mm:ss.SSS");
                put(
                        "^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}\\.\\d{2}[-+]\\d{2}:\\d{2}$",
                        "yyyy-MM-dd'T'HH:mm:ss.SSS");
                put(
                        "^\\d{4}-\\d{1,2}-\\d{1,2}T\\d{1,2}:\\d{2}:\\d{2}[-+]\\d{2}:\\d{2}$",
                        "yyyy-MM-dd'T'HH:mm:ss.ZZZ");
            }
        };

        /**
         * To Determine the pattern by the string date value
         * 
         * @param dateString
         * @return The matching SimpleDateFormat pattern, or null if format is unknown.
         */
        public static String determineDateFormat(String dateString) {
            for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
                if (dateString.matches(regexp) || dateString.toLowerCase().matches(regexp)) {
                    return DATE_FORMAT_REGEXPS.get(regexp);
                }
            }
            return null;
        }
    }
}