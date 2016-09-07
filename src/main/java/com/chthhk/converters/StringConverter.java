package com.chthhk.converters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

public final class StringConverter implements Converter<String, String> {

	@Override
	public String convert(String source) {
		return StringUtils.trim(source);
	}

}