package com.chthhk.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.convert.converter.Converter;

public final class DateConverter implements Converter<String, Date> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(DateConverter.class);

	@Override
	public Date convert(String source) {
		try {
			if (StringUtils.isBlank(source)) {
				return null;
			} else {
				String value = source.trim();
				String[] dateFormat = new String[] { "yyyy-MM-dd HH:mm:ss",
						"yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd", "yyyy/MM/dd" };
				return DateUtils.parseDate(value, dateFormat);
			}
		} catch (ParseException e) {
			logger.warn("日期转换错误，错误日期为："+source);
			return null;
		}
	}

}