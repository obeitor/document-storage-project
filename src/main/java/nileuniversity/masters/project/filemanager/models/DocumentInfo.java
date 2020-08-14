package nileuniversity.masters.project.filemanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softobt.jpa.helpers.converters.DateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_info")
public class DocumentInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentName;

    private String documentDesc;

    private String documentExt;

    private String documentHash;

    @JsonIgnore
    private String previoushash;

    @JsonIgnore
    private String ipfsHash;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uploaded_by",referencedColumnName = "id", nullable = false)
    private User uploadedBy;

    @Convert(converter = DateTimeConverter.class)
    private LocalDateTime uploadedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentHash() {
        return documentHash;
    }

    public void setDocumentHash(String documentHash) {
        this.documentHash = documentHash;
    }

    public String getPrevioushash() {
        return previoushash;
    }

    public void setPrevioushash(String previoushash) {
        this.previoushash = previoushash;
    }

    public String getIpfsHash() {
        return ipfsHash;
    }

    public void setIpfsHash(String ipfsHash) {
        this.ipfsHash = ipfsHash;
    }

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(LocalDateTime uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentDesc() {
        return documentDesc;
    }

    public void setDocumentDesc(String documentDesc) {
        this.documentDesc = documentDesc;
    }

    public String getDocumentExt() {
        return documentExt;
    }

    public void setDocumentExt(String documentExt) {
        this.documentExt = documentExt;
    }
}
