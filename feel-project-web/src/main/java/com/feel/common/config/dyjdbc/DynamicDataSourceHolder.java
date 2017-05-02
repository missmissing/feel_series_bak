package com.feel.common.config.dyjdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDataSourceHolder {

	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceHolder.class);

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setJdbcType(String jdbcType) {
		contextHolder.set(jdbcType);
	}

	public static void setMaster() {
		LOGGER.debug("set master ...");
		clearJdbcType();
	}

	public static void setSlave() {
		setJdbcType("slave");
	}

	public static String getJdbcType() {
		return (String) contextHolder.get();
	}

	public static void clearJdbcType() {
		contextHolder.remove();
	}
}
