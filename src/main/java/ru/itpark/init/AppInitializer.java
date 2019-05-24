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

public class AppInitializer implements WebApplicationInitializer {
    private final String uploadPath;

    public AppInitializer() throws IOException {
        uploadPath = System.getenv("UPLOAD_PATH");
        Files.createDirectories(Paths.get(uploadPath));
    }

    @Override
    public void onStartup(ServletContext servletContext)  {
        AnnotationConfigWebApplicationContext wac = new AnnotationConfigWebApplicationContext();
        wac.register(JavaConfig.class);

        var dispatcherServlet = new DispatcherServlet(wac);
        Dynamic registration = servletContext.addServlet("dispatcher", dispatcherServlet);
        long fileSize = 52428800;
        registration.setMultipartConfig(new MultipartConfigElement(uploadPath, fileSize, fileSize, 0));
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}
