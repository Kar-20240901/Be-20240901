package com.kar20240901.be.base.web.configuration.mybatis;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.kar20240901.be.base.web.configuration.base.BaseConfiguration;
import com.kar20240901.be.base.web.configuration.log.LogFilter;
import com.kar20240901.be.base.web.model.constant.log.LogTopicConstant;
import com.kar20240901.be.base.web.model.domain.sql.BaseSqlSlowDO;
import com.kar20240901.be.base.web.util.base.CallBack;
import com.kar20240901.be.base.web.util.base.MyThreadUtil;
import com.kar20240901.be.base.web.util.base.MyUserUtil;
import com.kar20240901.be.base.web.util.sql.MySqlUtil;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

/**
 * mybatis-sql日志拦截器
 */
@Component
@Slf4j(topic = LogTopicConstant.MYBATIS)
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class MybatisSqlLoggerInterceptor implements Interceptor {

    @Resource
    BaseConfiguration baseConfiguration;

    @Resource
    MyThreadUtil myThreadUtil;

    @SneakyThrows
    @Override
    public Object intercept(Invocation invocation) {

        long timeNumber = System.currentTimeMillis();

        try {

            return invocation.proceed();

        } finally {

            Long currentUserIdDefault = MyUserUtil.getCurrentUserIdDefault();

            MyThreadUtil.execute(() -> {

                StatementHandler statementHandler = (StatementHandler)invocation.getTarget();

                MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                    SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());

                MappedStatement mappedStatement = (MappedStatement)metaObject.getValue("delegate.mappedStatement");

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

                interceptSuf(timeNumber, logFlag, mappedStatement, statementHandler, sqlCommandType,
                    currentUserIdDefault);

            });

        }

    }

    private void interceptSuf(long timeNumber, boolean logFlag, MappedStatement mappedStatement,
        StatementHandler statementHandler, SqlCommandType sqlCommandType, Long currentUserIdDefault) {

        timeNumber = (System.currentTimeMillis() - timeNumber);

        boolean slowFlag = timeNumber > 200; // 是否是：慢 sql

        CallBack<String> sqlIdCallBack = new CallBack<>();
        CallBack<String> sqlCallBack = new CallBack<>();

        if (logFlag || slowFlag) {

            // 当：要打印日志，或者要记录慢 sql的时候，才执行该方法
            handle(mappedStatement, statementHandler, sqlIdCallBack, sqlCallBack);

        }

        if (logFlag) {

            String pre = "";

            if (slowFlag) {

                pre = "慢";

            }

            log.info("{}sql，耗时：{}ms，类型：【{}】{}\n\033[0;31m{}\033[0m", pre, timeNumber, sqlCommandType.toString(),
                sqlIdCallBack.getValue(), sqlCallBack.getValue());

        }

        if (slowFlag) { // 记录到数据库里

            BaseSqlSlowDO baseSqlSlowDO = new BaseSqlSlowDO();

            baseSqlSlowDO.setCreateId(currentUserIdDefault);
            baseSqlSlowDO.setCreateTime(new Date());

            baseSqlSlowDO.setName(sqlIdCallBack.getValue());
            baseSqlSlowDO.setType(sqlCommandType.toString());
            baseSqlSlowDO.setCostMs(timeNumber);
            baseSqlSlowDO.setSqlContent(sqlCallBack.getValue());

            MySqlUtil.add(baseSqlSlowDO);

        }

    }

    /**
     * 处理：sql语句
     */
    public void handle(MappedStatement mappedStatement, StatementHandler statementHandler,
        CallBack<String> sqlIdCallBack, CallBack<String> sqlCallBack) {

        // id为，执行的 mapper方法的全路径名，如：com.kar20240901.be.base.web.mapper.base.BaseUserMapper.insert
        String sqlId = mappedStatement.getId();

        BoundSql boundSql = statementHandler.getBoundSql();

        // 获取节点的配置
        Configuration configuration = mappedStatement.getConfiguration();

        // 获取到最终的sql语句
        String sql = showSql(configuration, boundSql);

        // 设置：回调对象
        sqlIdCallBack.setValue(sqlId);

        sqlCallBack.setValue(sql);

    }

    /**
     * 进行 ? 的替换
     */
    public String showSql(Configuration configuration, BoundSql boundSql) {

        // 获取参数
        Object parameterObject = boundSql.getParameterObject();

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();

        // sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

        if (parameterObject == null) {
            return sql;
        }

        // 获取类型处理器注册器，类型处理器的功能是进行 java类型和数据库类型的转换
        // 如果根据 parameterObject.getClass(）可以找到对应的类型，则替换
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {

            sql = StrUtil.replaceFirst(sql, "?", getParameterValue(parameterObject));

        } else {

            if (CollUtil.isEmpty(parameterMappings)) {
                return sql;
            }

            // MetaObject主要是封装了 originalObject对象，
            // 提供了 get和 set的方法用于获取和设置 originalObject的属性值,
            // 主要支持对 JavaBean、Collection、Map三种类型对象的操作
            MetaObject metaObject = configuration.newMetaObject(parameterObject);

            for (ParameterMapping parameterMapping : parameterMappings) {

                String propertyName = parameterMapping.getProperty();

                if (metaObject.hasGetter(propertyName)) {

                    Object obj = metaObject.getValue(propertyName);

                    sql = StrUtil.replaceFirst(sql, "?", getParameterValue(obj));

                } else if (boundSql.hasAdditionalParameter(propertyName)) {

                    // 该分支是动态sql
                    Object obj = boundSql.getAdditionalParameter(propertyName);

                    sql = StrUtil.replaceFirst(sql, "?", getParameterValue(obj));

                } else {

                    // 打印出缺失，提醒该参数缺失并防止错位
                    sql = StrUtil.replaceFirst(sql, "?", "缺失");

                }

            }

        }

        return sql;

    }

    /**
     * 如果参数是 String，则添加单引号， 如果是日期，则转换为时间格式器并加单引号； 对参数是 null，和不是 null的情况作了处理
     */
    private String getParameterValue(Object obj) {

        String value;

        if (obj instanceof String) {

            value = "'" + obj + "'";

        } else if (obj instanceof Date) {

            value = "'" + DateUtil.formatDateTime((Date)obj) + "'";

        } else {

            if (obj != null) {

                value = obj.toString();

            } else {

                value = "";

            }

        }

        return value;

    }

}
