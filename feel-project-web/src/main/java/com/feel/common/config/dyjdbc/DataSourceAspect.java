package com.feel.common.config.dyjdbc;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceAspect.class);

	public void before(JoinPoint point) {
		String method = point.getSignature().getName();
		LOGGER.debug("method is : {}", method);

		if (method.startsWith("find") || method.startsWith("query") || method.startsWith("get")
				|| method.startsWith("extract") || method.startsWith("retrieve") || method.startsWith("count")) {
			DynamicDataSourceHolder.setSlave();
			return;
		}

		DynamicDataSourceHolder.setMaster();
	}
}
