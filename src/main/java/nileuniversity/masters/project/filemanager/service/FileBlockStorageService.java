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
import java.util.*;

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
        Optional<DocumentInfo> lastDocument = documentInfoRepository.findFirstByOrderByIdDesc();
        documentInfo.setPrevioushash(lastDocument.isPresent() ? lastDocument.get().getDocumentHash() : null);
        documentInfo.setDocumentHash(calculateHash(documentInfo));
        return documentInfo;
    }

    private String calculateHash(DocumentInfo documentInfo)throws RestServiceException{
        documentInfo.setDocumentHash(null);
        String stringifiedJson = new Gson().toJson(documentInfo);
        return HashingUtil.applySHA256(stringifiedJson);
    }

    /**
     * Chain validation
     * calculates hash of the each document and compares with its stored hash, must be equal
     * calculates hash of previous documents for every doc and compare with the previous stored for the doc
     * @return
     * @throws RestServiceException
     */
    public Boolean validateChain()throws RestServiceException{
        DocumentInfo previous;
        DocumentInfo current;
        List<DocumentInfo> blockChain = documentInfoRepository.getAllDocumentInfo();
        for(int i =1; i<blockChain.size();i++){
            current = blockChain.get(i);
            previous = blockChain.get(i-1);
            String currentHash = current.getDocumentHash();
            if(!currentHash.equals(calculateHash(current))){
                throw new RestServiceException("Hash does not match for document!");
            }
            if(!current.getPrevioushash().equals(calculateHash(previous))){
                throw new RestServiceException("Hash does not match for previous document");
            }
        }
        return true;
    }



}
