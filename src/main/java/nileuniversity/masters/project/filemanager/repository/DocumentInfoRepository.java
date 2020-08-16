package nileuniversity.masters.project.filemanager.repository;

import nileuniversity.masters.project.filemanager.models.DocumentInfo;
import nileuniversity.masters.project.filemanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface DocumentInfoRepository extends JpaRepository<DocumentInfo,Long> {
    /**
     * Find Last Item on
     * @return
     */
    Optional<DocumentInfo> findFirstByOrderByIdDesc();

    @Query("SELECT d.documentHash FROM DocumentInfo d ORDER BY d.id ASC")
    List<String> getAllHashesOrdered();

    Optional<DocumentInfo> findByDocumentHash(String documentHash);

    List<DocumentInfo> findAllByUploadedBy(User user);

    @Query("SELECT d FROM DocumentInfo d ORDER BY d.id ASC")
    List<DocumentInfo> getAllDocumentInfo();
}
