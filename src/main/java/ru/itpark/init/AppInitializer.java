package ru.itpark.init;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import ru.itpark.config.JavaConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class AppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext)  {
        AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
        wac.register(JavaConfig.class);

        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", new DispatcherServlet(wac));
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}
