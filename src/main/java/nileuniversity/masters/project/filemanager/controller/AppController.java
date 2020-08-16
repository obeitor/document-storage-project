package nileuniversity.masters.project.filemanager.controller;

import com.softobt.asgardian.control.apimodels.LoggedInUser;
import com.softobt.asgardian.control.config.JWTokenUtil;
import com.softobt.core.api.TokenDetail;
import com.softobt.core.exceptions.models.RestControllerException;
import com.softobt.core.exceptions.models.RestServiceException;
import nileuniversity.masters.project.filemanager.apimodels.LoginRequest;
import nileuniversity.masters.project.filemanager.apimodels.RegistrationRequest;
import nileuniversity.masters.project.filemanager.models.DocumentInfo;
import nileuniversity.masters.project.filemanager.models.User;
import nileuniversity.masters.project.filemanager.service.DocumentManagerService;
import nileuniversity.masters.project.filemanager.service.FileBlockStorageService;
import nileuniversity.masters.project.filemanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentManagerService documentManagerService;

    @Autowired
    private FileBlockStorageService blockStorageService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public TokenDetail login(@Validated @RequestBody LoginRequest request)throws RestControllerException{
        try{
           return  userService.getNewToken(request.getUsername(),request.getPassword());
        }
        catch (RestServiceException e){
            throw new RestControllerException(e);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public User register(@Validated @RequestBody RegistrationRequest request)throws RestControllerException{
        try{
            return userService.registerUser(request);
        }
        catch (RestServiceException e){
            throw new RestControllerException(e);
        }
    }

    @RequestMapping(value = "/documents",method = RequestMethod.GET)
    @ResponseBody
    public List<DocumentInfo> getDocumentsOfuser(@RequestAttribute(JWTokenUtil.USER_ATTRIBUTE_KEY)LoggedInUser loggedInUser)throws RestControllerException{
        try{
            return documentManagerService.getAllDocumentForUser(loggedInUser);
        }
        catch (RestServiceException e){
            throw new RestControllerException(e);
        }
    }

    @RequestMapping(value = "/save-document", method = RequestMethod.POST)
    @ResponseBody
    public DocumentInfo saveDocument(
            @RequestParam("docname") String name,
            @RequestParam("docdesc")String docDesc,
            @RequestParam("file") MultipartFile file,
            @RequestAttribute(JWTokenUtil.USER_ATTRIBUTE_KEY)LoggedInUser loggedInUser)throws RestControllerException{
        try{
            DocumentInfo documentInfo = new DocumentInfo();
            documentInfo.setDocumentName(name);
            documentInfo.setDocumentDesc(docDesc);
            return documentManagerService.saveNewDocument(loggedInUser,documentInfo,file);
        }
        catch (RestServiceException e){
            throw new RestControllerException(e);
        }
    }

    @RequestMapping(value = "/document/{hash}", method = RequestMethod.GET)
    @ResponseBody
    public DocumentInfo getDocument(@PathVariable(value = "hash")String hash)throws RestControllerException{
        try{
            return documentManagerService.getInfoForDocument(hash);
        }
        catch (RestServiceException e){
            throw new RestControllerException(e);
        }
    }

    @RequestMapping(value = "/download/{hash}", method = RequestMethod.GET)
    @ResponseBody
    public void downloadDocument(HttpServletResponse response,
                                 @PathVariable(value = "hash")String hash)throws RestControllerException{
        try{
            documentManagerService.downloadDocument(hash,response);
        }
        catch (RestServiceException e){
            throw new RestControllerException(e);
        }
    }

    @RequestMapping(value = "/get-chain", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getBlockChain()throws RestControllerException{
        try{
            return documentManagerService.getBlockChain();
        }
        catch (RestServiceException e){
            throw new RestControllerException(e);
        }
    }

    @RequestMapping(value = "/validate-chain", method = RequestMethod.GET)
    @ResponseBody
    public Boolean validateBlockChain()throws RestControllerException{
        try{
            return blockStorageService.validateChain();
        }
        catch (RestServiceException e){
            throw new RestControllerException(e);
        }
    }

}
