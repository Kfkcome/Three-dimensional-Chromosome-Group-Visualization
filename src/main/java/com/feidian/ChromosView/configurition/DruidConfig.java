package com.feidian.ChromosView.configurition;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    //后台监控功能
    //因为springboot内置了servlet容器，所以没有web.xml。替代方法 ServletRegistrationBean
    @Bean
    public ServletRegistrationBean StatViewServlet() {
        //固定写法
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>();
        bean.setServlet(new StatViewServlet());
        bean.addUrlMappings("/druid/*"); //"/druid/*"不是固定的你也可以写成"/tiger/*"

        //后台需要有人登陆
        HashMap<String, String> initParameters = new HashMap<>();
        //增加配置
        initParameters.put("loginUsername", "admin");//"loginUsername"是固定的
        initParameters.put("loginPassword", "123456"); //"loginPassword"是固定的
        //允许谁能访问
        initParameters.put("allow", ""); //("allow","localhost")只允许本机访问;("allow","")允许所有人访问;
        //禁止谁访问 initParameters.put("tiger","192.168.0.1");

        //设置初始化参数
        bean.setInitParameters(initParameters);
        return bean;
    }

    //Filter
    @Bean
    public FilterRegistrationBean b() {
        //固定写法
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());

        //可以过滤那些请求
        HashMap<String, String> initParameters = new HashMap<>();
        initParameters.put("exclusions", "*.js,*.css,/druid/*");

        //设置初始化参数
        bean.setInitParameters(initParameters);
        return bean;
    }
}

