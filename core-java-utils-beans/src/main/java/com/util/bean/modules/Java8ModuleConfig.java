package com.util.bean.modules;

import java.time.ZoneId;

public class Java8ModuleConfig {
	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String DEFAULT_DATE_TIME_OFFSET_PATTERN = "yyyy-MM-dd HH:mm:ssX";
	private static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

	private String datePattern = DEFAULT_DATE_PATTERN;
	private String dateTimePattern = DEFAULT_DATE_TIME_PATTERN;
	private String dateTimeOffsetPattern = DEFAULT_DATE_TIME_OFFSET_PATTERN;
	private String timePattern = DEFAULT_TIME_PATTERN;
	private ZoneId zoneId = ZoneId.systemDefault().normalized();

	public Java8ModuleConfig() {
	}

	public Java8ModuleConfig(Java8ModuleBuilder builder) {
		this.datePattern = builder.datePattern;
		this.dateTimePattern = builder.dateTimePattern;
		this.dateTimeOffsetPattern = builder.dateTimeOffsetPattern;
		this.timePattern = builder.timePattern;
		this.zoneId = builder.zoneId;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public String getDateTimePattern() {
		return dateTimePattern;
	}

	public void setDateTimePattern(String dateTimePattern) {
		this.dateTimePattern = dateTimePattern;
	}

	public String getDateTimeOffsetPattern() {
		return dateTimeOffsetPattern;
	}

	public void setDateTimeOffsetPattern(String dateTimeOffsetPattern) {
		this.dateTimeOffsetPattern = dateTimeOffsetPattern;
	}

	public String getTimePattern() {
		return timePattern;
	}

	public void setTimePattern(String timePattern) {
		this.timePattern = timePattern;
	}

	public ZoneId getZoneId() {
		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public static Java8ModuleBuilder builder() {
		return new Java8ModuleBuilder();
	}

	public static class Java8ModuleBuilder {
		private String datePattern = DEFAULT_DATE_PATTERN;
		private String dateTimePattern = DEFAULT_DATE_TIME_PATTERN;
		private String dateTimeOffsetPattern = DEFAULT_DATE_TIME_OFFSET_PATTERN;
		private String timePattern = DEFAULT_TIME_PATTERN;
		private ZoneId zoneId = ZoneId.systemDefault();

		public Java8ModuleBuilder() {

		}

		public Java8ModuleBuilder(String datePattern, String dateTimePattern, String dateTimeOffsetPattern,
				String timePattern, ZoneId zoneId) {
			super();
			this.datePattern = datePattern;
			this.dateTimePattern = dateTimePattern;
			this.dateTimeOffsetPattern = dateTimeOffsetPattern;
			this.timePattern = timePattern;
			this.zoneId = zoneId;
		}

		public Java8ModuleBuilder datePattern(String datePattern) {
			this.datePattern = datePattern;
			return this;
		}

		public Java8ModuleBuilder dateTimePattern(String dateTimePattern) {
			this.dateTimePattern = dateTimePattern;
			return this;
		}

		public Java8ModuleBuilder dateTimeOffsetPattern(String dateTimeOffsetPattern) {
			this.dateTimeOffsetPattern = dateTimeOffsetPattern;
			return this;
		}

		public Java8ModuleBuilder timePattern(String timePattern) {
			this.timePattern = timePattern;
			return this;
		}

		public Java8ModuleBuilder zoneId(ZoneId zoneId) {
			this.zoneId = zoneId;
			return this;
		}

		public Java8ModuleConfig build() {
			return new Java8ModuleConfig(this);
		}

	}

}
