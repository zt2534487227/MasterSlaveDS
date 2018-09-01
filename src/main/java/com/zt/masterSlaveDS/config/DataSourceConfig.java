package com.zt.masterSlaveDS.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zt.masterSlaveDS.aspect.DynamicDataSourceAdvice;
import com.zt.masterSlaveDS.datasource.DynamicDataSource;
import com.zt.masterSlaveDS.mapperscanner.MapperScan;
import com.zt.masterSlaveDS.property.DataSourceProps;
import org.aopalliance.aop.Advice;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.*;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(DataSourceProps.class)
@EnableTransactionManagement
@MapperScan(basePackages="${zt.datasource.base-packages}")
public class DataSourceConfig implements EnvironmentAware {

    private DataSourceProps dataSourceProps;


    @Override
    public void setEnvironment(Environment environment) {
        if (this.dataSourceProps ==null){
            BindResult<DataSourceProps> bind = Binder.get(environment).bind("zt.datasource", DataSourceProps.class);
            dataSourceProps= bind.get();
        }
    }

    // 数据源
    private DataSource masterDS;
    private Map<String, DataSource> SlavesDS = new HashMap<String, DataSource>();

    private DataSource bulidDataSource(String url,String username,String password){
        DruidDataSource dataSource=new DruidDataSource();
        dataSource.setDriverClassName(dataSourceProps.getDriverClassName());
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(dataSourceProps.getInitialSize());
        dataSource.setMaxActive(dataSourceProps.getMaxActive());
        dataSource.setMinIdle(dataSourceProps.getMinIdle());
        dataSource.setDefaultAutoCommit(dataSourceProps.isDefaultAutoCommit());
        dataSource.setTestOnBorrow(dataSourceProps.isTestOnBorrow());
        dataSource.setValidationQuery(dataSourceProps.getValidationQuery());
        return  dataSource;
    }

    private void initDataSource(){
        masterDS=bulidDataSource(dataSourceProps.getUrl(),dataSourceProps.getUsername(),dataSourceProps.getPassword());
        Map<String, DataSourceProps.MyDataSource> slaves = dataSourceProps.getSlaves();
        if (slaves != null && slaves.size() > 0){
            for (Map.Entry<String, DataSourceProps.MyDataSource> slave : slaves.entrySet()) {
                String key = slave.getKey();
                DataSourceProps.MyDataSource ds = slave.getValue();
                DataSource dataSource = bulidDataSource(ds.getUrl(), ds.getUsername(), ds.getPassword());
                SlavesDS.put(key,dataSource);
            }
        }
    }

    @Bean
    public AbstractRoutingDataSource dataSourceProxy(){
        DynamicDataSource proxy=new DynamicDataSource();
        if (masterDS==null){
            initDataSource();
        }
        Map<Object, Object> targetDataSource = new HashMap<Object, Object>();
        targetDataSource.put("master",masterDS);
        targetDataSource.putAll(SlavesDS);
        proxy.setTargetDataSources(targetDataSource);
        proxy.setDefaultTargetDataSource(masterDS);
        proxy.setDataSourceProps(dataSourceProps);
        return proxy;
    }

    /**
     * SqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory SqlSessionFactory()throws Exception{
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSourceProxy());
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(dataSourceProps.getMapperLocations()));
        return factoryBean.getObject();
    }


    /**
     * 事务管理
     * @return
     */
    @Bean
    public DataSourceTransactionManager TransactionManager(){
        DataSourceTransactionManager transactionManager=new DataSourceTransactionManager(dataSourceProxy());
        return transactionManager;
    }

    /**
     * 声明式事务
     * @return
     */
    @Bean
    public TransactionInterceptor txAdvice(){
        NameMatchTransactionAttributeSource source=new NameMatchTransactionAttributeSource();
        //只读事务
        RuleBasedTransactionAttribute readOnlyTx=new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED );
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        Map<String, TransactionAttribute> txMap = new HashMap<String, TransactionAttribute>();
        txMap.put("add*", requiredTx);
        txMap.put("create*", requiredTx);
        txMap.put("insert*", requiredTx);
        txMap.put("save*", requiredTx);
        txMap.put("update*", requiredTx);
        txMap.put("modify*", requiredTx);
        txMap.put("edit*", requiredTx);
        txMap.put("batch*", requiredTx);
        txMap.put("remove*", requiredTx);
        txMap.put("delete*", requiredTx);
        txMap.put("get*", readOnlyTx);
        txMap.put("find*", readOnlyTx);
        txMap.put("search*", readOnlyTx);
        txMap.put("select*", readOnlyTx);
        txMap.put("query*", readOnlyTx);
        txMap.put("list*", readOnlyTx);
        txMap.put("count*", readOnlyTx);
        source.setNameMap( txMap );
        return new TransactionInterceptor(TransactionManager(),source);
    }


    /**
     * 事务aop配置
     * @return
     */
    @Bean
    public Advisor txAdvisor(){
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(dataSourceProps.getTxAopExpression());
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }

    @Bean
    public Advice dsAdvice(){
        DynamicDataSourceAdvice dataSourceAdvice=new DynamicDataSourceAdvice();
        dataSourceAdvice.setDynamicDataSource((DynamicDataSource) dataSourceProxy());
        return dataSourceAdvice;
    }

    @Bean
    public Advisor dsAdvisor(){
        String expression = dataSourceProps.getTxAopExpression();
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        return new DefaultPointcutAdvisor(pointcut, dsAdvice());
    }

}
