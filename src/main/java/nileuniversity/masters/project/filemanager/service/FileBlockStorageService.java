package nileuniversity.masters.project.filemanager.service;

import com.google.gson.Gson;
import com.softobt.core.exceptions.models.RestServiceException;
import com.softobt.core.logger.services.LoggerService;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import nileuniversity.masters.project.filemanager.apimodels.DocumentHashingData;
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
        if(ipfsStorage==null)throw new RestServiceException("IPFS was not enabled, no datastore to retrieve from!");
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
        documentInfo = documentInfoRepository.save(documentInfo);
        documentInfo.setDocumentHash(calculateHash(documentInfo));
        return documentInfo;
    }

    private String calculateHash(DocumentInfo documentInfo)throws RestServiceException{
        String stringifiedJson = new Gson().toJson(new DocumentHashingData(documentInfo));
        return HashingUtil.applySHA256(stringifiedJson);
    }

    /**
     * Chain validation
     * calculates hash of the each document and compares with its stored hash, must be equal
     * calculates hash of previous documents for every doc and compare with the previous stored for the doc
     * @return
     * @throws RestServiceException
     */
    public String[] validateChain()throws RestServiceException{
        DocumentInfo previous;
        DocumentInfo current;
        List<DocumentInfo> blockChain = documentInfoRepository.getAllDocumentInfo();
        String[] rsp = new String[blockChain.size()];
        Arrays.setAll(rsp,i->blockChain.get(i).getDocumentHash()+" -> IS "+(i==0?"":"NOT")+" VALID");
        for(int i =1; i<blockChain.size();i++){
            current = blockChain.get(i);
            previous = blockChain.get(i-1);
            String currentHash = current.getDocumentHash();
            if(!currentHash.equals(calculateHash(current))){
                return rsp;
                //throw new RestServiceException("Hash does not match for document - "+current.getDocumentName());
            }
            if(!current.getPrevioushash().equals(calculateHash(previous))){
                rsp[i-1] = previous.getDocumentHash()+" -> IS NOT VALID";
                return rsp;
                //throw new RestServiceException("Hash does not match for previous document");
            }
            rsp[i] = currentHash+" -> IS VALID";
        }
        return rsp;
    }

    public boolean validateChain(String documentHash)throws RestServiceException{
        DocumentInfo previous;
        DocumentInfo current;
        List<DocumentInfo> blockChain = documentInfoRepository.getAllDocumentInfo();
        for(int i =1; i<blockChain.size();i++){
            current = blockChain.get(i);
            previous = blockChain.get(i-1);
            String currentHash = current.getDocumentHash();
            if(!currentHash.equals(calculateHash(current))){
                throw new RestServiceException("Chain is not valid up to document requested");
            }
            if(!current.getPrevioushash().equals(calculateHash(previous))){
                throw new RestServiceException("Chain is not valid up to document requested");
            }
            /**
             * check if is 2nd block then check if document hash searched for is that of the previous,
             * i.e. first block and return true
             */
            if(i==1 && current.getPrevioushash().equals(documentHash))
                return true;
            if(currentHash.equals(documentHash))return true;
        }
        throw new RestServiceException("Hash was not found on the chain");
    }



}
