package nileuniversity.masters.project.filemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.softobt","nileuniversity.masters.project.filemanager"},
        exclude = { SecurityAutoConfiguration.class })
public class AppllicationService {
    public static void main(String... args){
        SpringApplication.run(AppllicationService.class,args);
    }
}
