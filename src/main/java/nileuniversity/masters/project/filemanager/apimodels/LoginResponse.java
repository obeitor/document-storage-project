package nileuniversity.masters.project.filemanager.apimodels;

import com.softobt.core.api.TokenDetail;
import nileuniversity.masters.project.filemanager.models.Organization;
import nileuniversity.masters.project.filemanager.models.User;

/**
 * @author aobeitor
 * @since 8/17/20
 */
public class LoginResponse extends TokenDetail {
    private String firstname;
    private String lastname;
    private String email;
    private String organization;

    public LoginResponse(TokenDetail tokenDetail, User user){
        this.setDomain(tokenDetail.getDomain());
        this.setToken(tokenDetail.getToken());
        this.setExpiry(tokenDetail.getExpiry());
        this.setOwner(tokenDetail.getOwner());
        this.setEmail(user.getEmail());
        this.setFirstname(user.getFirstname());
        this.setLastname(user.getLastname());
        this.setOrganization(user.getOrganization()==null ? "NONE" : user.getOrganization().getName());
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
