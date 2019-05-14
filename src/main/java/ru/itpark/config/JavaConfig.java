package ru.itpark.config;

import com.google.gson.Gson;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;

@EnableWebMvc
@Configuration
@ComponentScan("ru.itpark")
public class JavaConfig {

    @Bean
    public DataSource dataSource() {
        return new JndiDataSourceLookup().getDataSource("java:comp/env/jdbc/db");
    }

    @Bean
    public MessageSource messageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/resources/translate");
        messageSource.setDefaultEncoding("UTF-16");
        return messageSource;
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        var viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
