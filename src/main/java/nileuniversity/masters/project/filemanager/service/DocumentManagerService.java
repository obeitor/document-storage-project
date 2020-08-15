package nileuniversity.masters.project.filemanager.service;

import com.softobt.asgardian.control.apimodels.LoggedInUser;
import com.softobt.core.exceptions.models.RestServiceException;
import nileuniversity.masters.project.filemanager.models.DocumentInfo;
import nileuniversity.masters.project.filemanager.models.User;
import nileuniversity.masters.project.filemanager.repository.DocumentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service
public class DocumentManagerService {
    @Autowired
    DocumentInfoRepository documentInfoRepository;

    @Autowired
    FileBlockStorageService storageService;

    @Autowired
    AuthService authService;

    public List<DocumentInfo> getAllDocumentForUser(LoggedInUser loggedInUser)throws RestServiceException{
        User user = authService.getUser(loggedInUser);
        return documentInfoRepository.findAllByUploadedBy(user);
    }

    public DocumentInfo saveNewDocument(LoggedInUser loggedInUser,DocumentInfo documentInfo, MultipartFile file)throws RestServiceException{
        User user = authService.getUser(loggedInUser);
        documentInfo.setUploadedBy(user);
        String filename = file.getOriginalFilename();
        int dot = filename.lastIndexOf(".")+1;
        documentInfo.setDocumentExt(filename.substring(dot));
        documentInfo.setIpfsHash(storageService.storeOnIpfs(file));
        documentInfo = storageService.generateDocumentHash(documentInfo);
        return documentInfoRepository.save(documentInfo);
    }

    public DocumentInfo getInfoForDocument(String documentHash)throws RestServiceException{
        Optional<DocumentInfo> optional = documentInfoRepository.findByDocumentHash(documentHash);
        if(optional.isPresent())
            return optional.get();
        throw new RestServiceException("Sorry Document was not found");
    }

    public void downloadDocument(String documentHash, HttpServletResponse response)throws RestServiceException{
        try {
            DocumentInfo info = getInfoForDocument(documentHash);
            storageService.fetchFromIpfs(info.getIpfsHash(), response.getOutputStream());
            response.setContentType("application/octet-stream");
            if("pdf".equalsIgnoreCase(info.getDocumentExt())){
                response.setContentType("application/pdf");
            }
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", info.getDocumentName()+"."+info.getDocumentExt());
            response.setHeader(headerKey, headerValue);
            response.flushBuffer();
        }
        catch (IOException e){
            throw new RestServiceException("Unable to read document");
        }
    }

    public List<String> getBlockChain()throws RestServiceException{
        return documentInfoRepository.getAllHashesOrdered();
    }
}
