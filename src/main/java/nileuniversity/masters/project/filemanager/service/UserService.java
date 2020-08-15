package nileuniversity.masters.project.filemanager.service;

import com.softobt.asgardian.control.config.JWTokenUtil;
import com.softobt.asgardian.control.models.AsgardianUser;
import com.softobt.asgardian.control.models.Authority;
import com.softobt.asgardian.control.models.Domain;
import com.softobt.asgardian.control.service.AsgardianUserValidationService;
import com.softobt.core.api.TokenDetail;
import com.softobt.core.exceptions.models.CredentialException;
import com.softobt.core.exceptions.models.RestServiceException;
import nileuniversity.masters.project.filemanager.apimodels.RegistrationRequest;
import nileuniversity.masters.project.filemanager.models.User;
import nileuniversity.masters.project.filemanager.repository.AuthorityRepository;
import nileuniversity.masters.project.filemanager.repository.DomainRepository;
import nileuniversity.masters.project.filemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author aobeitor
 * @since 8/12/20
 */
@Service
public class UserService extends AsgardianUserValidationService<User>{

    @Autowired
    AuthorityRepository authorityRepository;

    @Value("${app.domain.name:FILE-MGR}")
    private String defaultDomainName;

    @Override
    public TokenDetail getToken(AsgardianUser user) {
        return tokenUtil.getToken(user);
    }

    public TokenDetail getNewToken(String username, String password)throws CredentialException {
        return getToken(validateUserCredentials(username,password,defaultDomainName));
    }

    @Autowired
    @Override
    public void setTokenUtil(JWTokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Autowired
    public void setDomainRepository(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    @Autowired
    @Override
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegistrationRequest request)throws RestServiceException{
        User user  = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        Optional<Authority> authority = authorityRepository.findFirstByName("basic-user");
        if(authority.isPresent()) {
            user.getAuthorities().add(authority.get());
        }
        return this.saveNewUser(user,request.getPassword(),defaultDomainName);
    }
}
