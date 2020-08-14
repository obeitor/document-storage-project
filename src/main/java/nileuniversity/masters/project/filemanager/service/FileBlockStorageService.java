package nileuniversity.masters.project.filemanager.service;

import com.google.gson.Gson;
import com.softobt.core.exceptions.models.RestServiceException;
import com.softobt.core.logger.services.LoggerService;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import nileuniversity.masters.project.filemanager.models.DocumentInfo;
import nileuniversity.masters.project.filemanager.repository.DocumentInfoRepository;
import nileuniversity.masters.project.filemanager.utils.HashingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

@Service
public class FileBlockStorageService {

    @Autowired
    IPFS ipfsStorage;


    @Autowired
    DocumentInfoRepository documentInfoRepository;


    public String storeOnIpfs(MultipartFile file)throws RestServiceException{
        try {
            NamedStreamable.InputStreamWrapper inputStreamWrapper = new NamedStreamable.InputStreamWrapper(file.getInputStream());
            MerkleNode rsp = ipfsStorage.add(inputStreamWrapper).get(0);
            return rsp.hash.toBase58();
        }
        catch (IOException e){
            LoggerService.severe(e);
            throw new RestServiceException("Failed to store on ipfs");
        }
    }

    public void fetchFromIpfs(String ipfsHash, OutputStream outputStream)throws RestServiceException{
        try{
            Multihash multihash = Multihash.fromBase58(ipfsHash);
            byte[] fileContent = ipfsStorage.cat(multihash);
            outputStream.write(fileContent);
        }
        catch (IOException e){
            LoggerService.severe(e);
            throw new RestServiceException("Failed to fetch from ipfs");
        }
    }

    public DocumentInfo generateDocumentHash(DocumentInfo documentInfo)throws RestServiceException{
        Gson gson = new Gson();
        Optional<DocumentInfo> lastDocument = documentInfoRepository.findFirstByOrderByIdDesc();
        documentInfo.setPrevioushash(lastDocument.isPresent() ? lastDocument.get().getDocumentHash() : null);
        String stringifiedJson = gson.toJson(documentInfo);
        documentInfo.setDocumentHash(HashingUtil.applySHA512(stringifiedJson));
        return documentInfo;
    }



}
