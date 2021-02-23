package com.util.bean.modules.converters;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

import org.modelmapper.Converter;
import org.modelmapper.internal.Errors;
import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;

import com.util.bean.modules.Java8ModuleConfig;

public class TemporalToTemporalConverter implements ConditionalConverter<Temporal, Temporal> {
	private Java8ModuleConfig config;

	public TemporalToTemporalConverter(Java8ModuleConfig config) {
		this.config = config;
	}

	private final LocalDateConverter localDateConverter = new LocalDateConverter();
	private final LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();
	private final ZonedDateTimeConverter zonedDateTimeConverter = new ZonedDateTimeConverter();
	private final OffsetDateTimeConverter offsetDateTimeConverter = new OffsetDateTimeConverter();

	@Override
	public MatchResult match(Class<?> sourceType, Class<?> destinationType) {
		return Temporal.class.isAssignableFrom(sourceType) && Temporal.class.isAssignableFrom(destinationType)
				? MatchResult.FULL
				: MatchResult.NONE;
	}

	@Override
	public Temporal convert(MappingContext<Temporal, Temporal> mappingContext) {
		Temporal source = mappingContext.getSource();
		Class<?> sourceType = mappingContext.getSourceType();
		Class<?> destType = mappingContext.getDestinationType();
		if (source == null)
			return null;
		else if (sourceType.equals(destType)) {
			// TODO: handle the offset from the Java8ModuleConfig zone.
			return mappingContext.getSource();
		} else if (LocalDate.class.equals(destType)) {
			return localDateConverter.convert(mappingContext);
		} else if (LocalDateTime.class.equals(destType)) {
			return localDateTimeConverter.convert(mappingContext);
		} else if (ZonedDateTime.class.equals(destType)) {
			return zonedDateTimeConverter.convert(mappingContext);
		} else if (OffsetDateTime.class.equals(destType)) {
			return offsetDateTimeConverter.convert(mappingContext);
		} else
			throw new Errors().addMessage("Unsupported mapping types[%s->%s]", mappingContext.getSourceType().getName(),
					mappingContext.getDestinationType()).toMappingException();
	}

	private class LocalDateConverter implements Converter<Temporal, Temporal> {
		@Override
		public Temporal convert(MappingContext<Temporal, Temporal> mappingContext) {
			return convertLocalDate(mappingContext);
		}
	}

	private LocalDate convertLocalDate(MappingContext<?, ?> mappingContext) {
		Object source = mappingContext.getSource();
		Class<?> sourceType = source.getClass();
		if (sourceType.equals(String.class))
			return LocalDate.parse((String) source, DateTimeFormatter.ofPattern(config.getDatePattern()));
		return convertInstant(mappingContext).atZone(config.getZoneId()).toLocalDate();
	}

	private class LocalDateTimeConverter implements Converter<Temporal, Temporal> {
		@Override
		public Temporal convert(MappingContext<Temporal, Temporal> mappingContext) {
			return convertLocalDateTime(mappingContext);
		}
	}

	private LocalDateTime convertLocalDateTime(MappingContext<?, ?> mappingContext) {
		Object source = mappingContext.getSource();
		Class<?> sourceType = source.getClass();
		if (sourceType.equals(String.class))
			return LocalDateTime.parse((String) source, DateTimeFormatter.ofPattern(config.getDateTimePattern()));
		return convertInstant(mappingContext).atZone(config.getZoneId()).toLocalDateTime();
	}

	private class ZonedDateTimeConverter implements Converter<Temporal, Temporal> {
		@Override
		public Temporal convert(MappingContext<Temporal, Temporal> mappingContext) {
			return convertZonedDateTime(mappingContext);
		}
	}

	private ZonedDateTime convertZonedDateTime(MappingContext<?, ?> mappingContext) {
		Object source = mappingContext.getSource();
		Class<?> sourceType = source.getClass();
		if (sourceType.equals(String.class))
			return ZonedDateTime.parse((String) source, DateTimeFormatter.ofPattern(config.getDateTimePattern()));
		return ZonedDateTime.ofInstant(convertInstant(mappingContext), config.getZoneId());
	}

	private class OffsetDateTimeConverter implements Converter<Temporal, Temporal> {
		@Override
		public Temporal convert(MappingContext<Temporal, Temporal> mappingContext) {
			return convertOffsetDateTime(mappingContext);
		}
	}

	private OffsetDateTime convertOffsetDateTime(MappingContext<?, ?> mappingContext) {
		Object source = mappingContext.getSource();
		Class<?> sourceType = source.getClass();
		if (sourceType.equals(String.class))
			return OffsetDateTime.parse((String) source,
					DateTimeFormatter.ofPattern(config.getDateTimeOffsetPattern()));
		return OffsetDateTime.ofInstant(convertInstant(mappingContext), config.getZoneId());
	}

	private Instant convertInstant(MappingContext<?, ?> mappingContext) {
		Object source = mappingContext.getSource();
		Class<?> sourceType = source.getClass();
		if (sourceType.equals(mappingContext.getDestinationType())) {
			return (Instant) source;
		} else if (sourceType.equals(String.class))
			return LocalDateTime.parse((String) source, DateTimeFormatter.ofPattern(config.getDateTimePattern()))
					.atZone(ZoneId.systemDefault()).toInstant();
		else if (Date.class.isAssignableFrom(sourceType))
			return Instant.ofEpochMilli(((Date) source).getTime());
		else if (Calendar.class.isAssignableFrom(sourceType))
			return Instant.ofEpochMilli(((Calendar) source).getTime().getTime());
		else if (Number.class.isAssignableFrom(sourceType))
			return Instant.ofEpochMilli(((Number) source).longValue());
		else if (LocalDateTime.class.isAssignableFrom(sourceType))
			return ((LocalDateTime) source).atZone(ZoneId.systemDefault()).toInstant();
		else if (LocalDate.class.isAssignableFrom(sourceType))
			return ((LocalDate) source).atStartOfDay(ZoneId.systemDefault()).toInstant();
		else if (ZonedDateTime.class.isAssignableFrom(sourceType))
			return ((ZonedDateTime) source).toInstant();
		else if (OffsetDateTime.class.isAssignableFrom(sourceType))
			return ((OffsetDateTime) source).toInstant();
		else
			throw new Errors().addMessage("Unsupported mapping types[%s->%s]", sourceType.getName(),
					mappingContext.getDestinationType().getName()).toMappingException();
	}

}
