package nileuniversity.masters.project.filemanager.service;

import com.softobt.asgardian.control.apimodels.LoggedInUser;
import com.softobt.asgardian.control.service.AuthorizationService;
import com.softobt.core.exceptions.models.CredentialException;
import nileuniversity.masters.project.filemanager.models.User;
import nileuniversity.masters.project.filemanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author aobeitor
 * @since 8/14/20
 */
@Service
public class AuthService implements AuthorizationService {

    @Autowired
    UserRepository userRepository;
    @Override
    public void preAuthorize(LoggedInUser user, String minimumAuthority) throws CredentialException {
        if(user==null)throw new CredentialException(CredentialException.CredentialExceptionType.UNKNOWN_USER);
    }

    public User getUser(LoggedInUser user)throws CredentialException{
        preAuthorize(user,"");
        Optional<User> optional = userRepository.findByUsernameAndDomain_Name(user.getUsername(),user.getDomain());
        if(optional.isPresent())return optional.get();
        throw new CredentialException(CredentialException.CredentialExceptionType.UNKNOWN_USER);
    }
}
