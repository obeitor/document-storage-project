package nileuniversity.masters.project.filemanager.repository;

import com.softobt.asgardian.control.repositories.AsgardianUserRepository;
import nileuniversity.masters.project.filemanager.models.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AsgardianUserRepository<User> {
}
