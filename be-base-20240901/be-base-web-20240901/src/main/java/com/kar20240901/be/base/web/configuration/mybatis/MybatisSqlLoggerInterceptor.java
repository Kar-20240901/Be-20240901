package com.kar20240901.be.base.web.configuration.mybatis;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.kar20240901.be.base.web.configuration.base.BaseConfiguration;
import com.kar20240901.be.base.web.configuration.log.LogFilter;
import com.kar20240901.be.base.web.model.constant.log.LogTopicConstant;
import com.kar20240901.be.base.web.model.domain.sql.BaseSqlSlowDO;
import com.kar20240901.be.base.web.util.base.CallBack;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.sql.MySqlUtil;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

/**
 * mybatis-sql日志拦截器
 */
@Component
@Slf4j(topic = LogTopicConstant.MYBATIS)
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
            org.apache.ibatis.cache.CacheKey.class, BoundSql.class})})
public class MybatisSqlLoggerInterceptor implements Interceptor {

    @Resource
    BaseConfiguration baseConfiguration;

    @Resource
    MyThreadUtil myThreadUtil;

    @SneakyThrows
    @Override
    public Object intercept(Invocation invocation) {

        long startTime = System.currentTimeMillis();

        try {

            return invocation.proceed();

        } finally {

            long costTime = System.currentTimeMillis() - startTime;

            Long currentUserIdDefault = MyUserUtil.getCurrentUserIdDefault();

            MyThreadUtil.execute(() -> {

                // 解析打印 sql
                printSql(invocation, costTime, currentUserIdDefault);

            });

        }

    }

    /**
     * 解析SQL并打印（替换参数占位符，输出完整SQL + 执行耗时）
     */
    private void printSql(Invocation invocation, long costTime, Long currentUserIdDefault) {

        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];

        // sql语句类型：select、delete、insert、update
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        boolean logFlag;

        if (SqlCommandType.INSERT.equals(sqlCommandType)) {

            logFlag = LogFilter.baseProperties.getLogTopicSet().contains(LogTopicConstant.MYBATIS_INSERT);

        } else {

            logFlag = LogFilter.baseProperties.getLogTopicSet().contains(LogTopicConstant.MYBATIS);

        }

        if (logFlag) {

            logFlag = !LogFilter.baseProperties.getNotLogTopicSet().contains(mappedStatement.getId());

        }

        // 后续处理
        interceptSuf(costTime, logFlag, mappedStatement, sqlCommandType, currentUserIdDefault, invocation);

    }

    /**
     * 后续处理
     */
    private void interceptSuf(long costTime, boolean logFlag, MappedStatement mappedStatement,
        SqlCommandType sqlCommandType, Long currentUserIdDefault, Invocation invocation) {

        boolean slowFlag = costTime > 200; // 是否是：慢 sql

        CallBack<String> sqlIdCallBack = new CallBack<>();
        CallBack<String> sqlCallBack = new CallBack<>();

        if (logFlag || slowFlag) {

            // 当：要打印日志，或者要记录慢 sql的时候，才执行该方法
            handle(mappedStatement, sqlIdCallBack, sqlCallBack, invocation);

        }

        if (logFlag) {

            String pre = "";

            String formatStr = "\033[36m{}\033[0m";

            if (slowFlag) {

                pre = "慢";

                formatStr = "\033[35m{}\033[0m";

            }

            log.info("{}sql，耗时：{}ms，类型：【{}】{}\n" + formatStr, pre, costTime, sqlCommandType.toString(),
                sqlIdCallBack.getValue(), sqlCallBack.getValue());

        }

        if (slowFlag) { // 记录到数据库里

            BaseSqlSlowDO baseSqlSlowDO = new BaseSqlSlowDO();

            baseSqlSlowDO.setCreateId(currentUserIdDefault);
            baseSqlSlowDO.setCreateTime(new Date());

            baseSqlSlowDO.setName(sqlIdCallBack.getValue());
            baseSqlSlowDO.setType(sqlCommandType.toString());
            baseSqlSlowDO.setCostMs(costTime);
            baseSqlSlowDO.setSqlContent(sqlCallBack.getValue());

            MySqlUtil.add(baseSqlSlowDO);

        }

    }

    /**
     * 处理：sql语句
     */
    public void handle(MappedStatement mappedStatement, CallBack<String> sqlIdCallBack, CallBack<String> sqlCallBack,
        Invocation invocation) {

        Object parameter = null;

        Object[] args = invocation.getArgs();

        if (args.length > 1) {

            parameter = args[1];

        }

        // id为，执行的 mapper方法的全路径名，如：com.kar20240901.be.base.web.mapper.base.BaseUserMapper.insert
        String sqlId = mappedStatement.getId();

        BoundSql boundSql = null;

        // 处理BoundSql的两种获取方式
        if ("update".equals(invocation.getMethod().getName())) {

            boundSql = mappedStatement.getBoundSql(parameter);

        } else if ("query".equals(invocation.getMethod().getName())) {

            // 适配带CacheKey的query方法
            if (args.length >= 6) {

                boundSql = (BoundSql)args[5];

            } else {

                boundSql = mappedStatement.getBoundSql(parameter);

            }

        }

        if (boundSql == null) {

            // 设置：回调对象
            sqlIdCallBack.setValue(sqlId);

            sqlCallBack.setValue("无法解析SQL");

            return;

        }

        Configuration configuration = mappedStatement.getConfiguration();

        // 获取到最终的sql语句
        String sql = showSql(configuration, boundSql);

        // 设置：回调对象
        sqlIdCallBack.setValue(sqlId);

        sqlCallBack.setValue(sql);

    }

    /**
     * 替换SQL中的?占位符，填充实际参数值
     */
    private String showSql(Configuration configuration, BoundSql boundSql) {

        Object parameterObject = boundSql.getParameterObject();

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

        if (parameterMappings == null || parameterMappings.isEmpty()) {
            return sql;
        }

        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {

            sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

        } else {

            MetaObject metaObject = configuration.newMetaObject(parameterObject);

            for (ParameterMapping parameterMapping : parameterMappings) {

                String propertyName = parameterMapping.getProperty();

                if (metaObject.hasGetter(propertyName)) {

                    Object obj = metaObject.getValue(propertyName);

                    sql = sql.replaceFirst("\\?", getParameterValue(obj));

                } else if (boundSql.hasAdditionalParameter(propertyName)) {

                    Object obj = boundSql.getAdditionalParameter(propertyName);

                    sql = sql.replaceFirst("\\?", getParameterValue(obj));

                } else {

                    sql = sql.replaceFirst("\\?", "缺失");

                }

            }

        }

        return sql;

    }

    /**
     * 格式化参数值（处理字符串、日期等特殊类型）
     */
    private String getParameterValue(Object obj) {

        if (obj == null) {

            return "null";

        }

        String value;

        if (obj instanceof String) {

            value = "'" + obj + "'";

        } else if (obj instanceof Date) {

            value = "'" + DateUtil.formatDateTime((Date)obj) + "'";

        } else {

            Class<?> clazz = obj.getClass();

            if (obj.getClass().isEnum()) {

                value = obj.toString();

                for (Field field : clazz.getDeclaredFields()) {

                    if (field.isAnnotationPresent(EnumValue.class)) {

                        Object fieldValue = ReflectUtil.getFieldValue(obj, field);

                        value = fieldValue.toString();

                        break;

                    }

                }

            } else {

                value = obj.toString();

            }

        }

        return value;

    }

}
