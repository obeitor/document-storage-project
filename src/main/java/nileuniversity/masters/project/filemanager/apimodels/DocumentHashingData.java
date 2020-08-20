package nileuniversity.masters.project.filemanager.apimodels;

import nileuniversity.masters.project.filemanager.models.DocumentInfo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author aobeitor
 * @since 8/17/20
 */
public class DocumentHashingData {
    private String uploadedDate;
    private String uploadedBy;
    private String ipfshash;
    private String previousHash;
    private String documentExt;
    private String documentDesc;
    private String documentName;
    private String documentId;

    public DocumentHashingData(DocumentInfo info){
        this.uploadedBy = info.getUploadedBy().getUsername();
        this.uploadedDate = info.getUploadedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.ipfshash = info.getIpfsHash();
        this.previousHash = info.getPrevioushash();
        this.documentExt = info.getDocumentExt();
        this.documentDesc = info.getDocumentDesc();
        this.documentName = info.getDocumentName();
        this.documentId = info.getId()+"";
    }

    public String getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(String uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getIpfshash() {
        return ipfshash;
    }

    public void setIpfshash(String ipfshash) {
        this.ipfshash = ipfshash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getDocumentExt() {
        return documentExt;
    }

    public void setDocumentExt(String documentExt) {
        this.documentExt = documentExt;
    }

    public String getDocumentDesc() {
        return documentDesc;
    }

    public void setDocumentDesc(String documentDesc) {
        this.documentDesc = documentDesc;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
