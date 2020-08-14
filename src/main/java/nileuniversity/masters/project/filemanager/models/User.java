package nileuniversity.masters.project.filemanager.models;

import com.softobt.asgardian.control.models.AsgardianUser;

import javax.persistence.*;

@Entity
@Table(name = "portal_user")
public class User extends AsgardianUser {
    private String firstname;
    private String lastname;
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "org_id",referencedColumnName = "id", nullable = true)
    private Organization organization;

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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
