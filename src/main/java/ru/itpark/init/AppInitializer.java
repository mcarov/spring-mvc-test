package ru.itpark.init;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import ru.itpark.config.JavaConfig;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static javax.servlet.ServletRegistration.Dynamic;
import static ru.itpark.Constants.UPLOAD_PATH;

public class AppInitializer implements WebApplicationInitializer {

    public AppInitializer() throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_PATH));
    }

    @Override
    public void onStartup(ServletContext servletContext)  {
        AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
        wac.register(JavaConfig.class);

        var dispatcherServlet = new DispatcherServlet(wac);
        Dynamic registration = servletContext.addServlet("dispatcher", dispatcherServlet);
        long fileSize = 52428800;
        registration.setMultipartConfig(new MultipartConfigElement(UPLOAD_PATH, fileSize, fileSize, 0));
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}
