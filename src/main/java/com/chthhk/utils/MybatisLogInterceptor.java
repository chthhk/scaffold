package com.chthhk.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

@Intercepts({ @org.apache.ibatis.plugin.Signature(type = org.apache.ibatis.executor.Executor.class, method = "update", args = { MappedStatement.class, Object.class }), @org.apache.ibatis.plugin.Signature(type = org.apache.ibatis.executor.Executor.class, method = "query", args = { MappedStatement.class, Object.class, org.apache.ibatis.session.RowBounds.class, org.apache.ibatis.session.ResultHandler.class }) })
public class MybatisLogInterceptor implements Interceptor {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(MybatisLogInterceptor.class);
	
	
	public Map<String, Logger> loggerMap = new HashMap();

	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

		Object parameter = null;
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}
		String sqlId = mappedStatement.getId();
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();
		Object returnValue = null;
		long start = System.currentTimeMillis();
		returnValue = invocation.proceed();
		long end = System.currentTimeMillis();
		long time = end - start;
		logSql(sqlId, configuration, boundSql, time);
		return returnValue;
	}

	public Logger getLogger(String id) {
		if (StringUtils.isBlank(id)) {
			id = getClass().getName();
		}

		Logger daoLogger = null;
		if (this.loggerMap.get(id) != null) {
			daoLogger = (Logger) this.loggerMap.get(id);
		} else {
			daoLogger = LoggerFactory.getLogger(id);
			this.loggerMap.put(id, daoLogger);
		}

		if (StringUtils.isBlank(id)) {
			logger.error("can not find the sqlId!");
		}

		return daoLogger;
	}

	public void logSql(String sqlId, Configuration configuration, BoundSql boundSql,long time) {
	    if(!logger.isInfoEnabled()){
            return;
        }
		Logger logger = getLogger(sqlId);
		String sql = showSql(configuration, boundSql);
		String parameters = getParameters(configuration, boundSql);

		logger.info("==> SQL: " + sql);
		logger.info("==> Parameters: " + parameters);
		logger.info("<== Time cost: " + time + "ms");

	}

	private static String getParameterValue(Object obj) {
		String value = null;
		if ((obj instanceof String)) {
			value = "'" + obj.toString() + "'";
		} else if ((obj instanceof Date)) {
			DateFormat formatter = DateFormat.getDateTimeInstance(2, 2, Locale.CHINA);

			value = "'" + formatter.format(new Date()) + "'";
		} else if (obj != null) {
			value = obj.toString();
		} else {
			value = "";
		}

		return value;
	}

	public static String getParameters(Configuration configuration, BoundSql boundSql) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

		String parameters = null;
		if ((parameterMappings.size() > 0) && (parameterObject != null)) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				parameters = getParameterValue(parameterObject);
			} else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);

				StringBuffer parametersBuffer = new StringBuffer("");
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						parametersBuffer.append(propertyName);
						parametersBuffer.append("= ");
						parametersBuffer.append(getParameterValue(obj));
						parametersBuffer.append(",");
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						Object obj = boundSql.getAdditionalParameter(propertyName);

						parametersBuffer.append(propertyName);
						parametersBuffer.append("= ");
						parametersBuffer.append(getParameterValue(obj));
						parametersBuffer.append(",");
					}
				}
				parameters = parametersBuffer.deleteCharAt(parametersBuffer.length() - 1).toString();
			}
		}
		return parameters;
	}

	public static String showSql(Configuration configuration, BoundSql boundSql) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
		MetaObject metaObject;
		if ((parameterMappings.size() > 0) && (parameterObject != null)) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
			} else {
				metaObject = configuration.newMetaObject(parameterObject);

				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						Object obj = boundSql.getAdditionalParameter(propertyName);

						sql = sql.replaceFirst("\\?", getParameterValue(obj));
					}
				}
			}
		}
		return sql;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub

	}
}