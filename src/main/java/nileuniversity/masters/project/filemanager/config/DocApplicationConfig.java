package nileuniversity.masters.project.filemanager.config;

import com.softobt.asgardian.control.models.Authority;
import com.softobt.asgardian.control.models.Domain;
import com.softobt.core.logger.services.LoggerService;
import io.ipfs.api.IPFS;
import nileuniversity.masters.project.filemanager.repository.AuthorityRepository;
import nileuniversity.masters.project.filemanager.repository.DomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class DocApplicationConfig {

    @Value("${ipfs.config.url:localhost}")
    private String ipfsUrl;

    @Value("${ipfs.config.port:5001}")
    private String ipfsPort;

    @Value("${app.domain.name:FILE-MGR}")
    private String defaultDomainName;

    @Value("${app.ipfs.enabled:true}")
    private boolean enableIpfs;

    @Bean
    public IPFS getIpfsConnection(){
        int ipfsPort = 5001;
        try{
            ipfsPort = Integer.parseInt(this.ipfsPort);
        }
        catch (Exception e){}
        return  new IPFS(ipfsUrl,ipfsPort);
    }


    @Autowired
    DomainRepository domainRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Bean
    void createDefaultAuthority(){
        if(!authorityRepository.findFirstByName("basic-user").isPresent()){
            authorityRepository.save(new Authority("basic-user","Basic user authority",0,false));
        }
    }

    @Bean
    void createDefaultDomain(){
        if(!domainRepository.findFirstByName(defaultDomainName).isPresent()){
            domainRepository.save(new Domain(defaultDomainName));
            LoggerService.info(this.getClass(),"Created default service domain ~ "+defaultDomainName);
        }
    }


}
