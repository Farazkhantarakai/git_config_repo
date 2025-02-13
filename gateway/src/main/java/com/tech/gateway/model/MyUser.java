package com.tech.gateway.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(name = "EMAIL",unique=true , nullable=false)
     @NotBlank(message = "UserEmail is mandatory")
    private String email;

    public MyUser() {
    }

    public MyUser(Long id, String email, String userName,  String password, String roles, boolean isVerified, List<Token> token ) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
        this.isVerified = isVerified;
        this.token = token;
    }



    public MyUser id(Long id) {
        setId(id);
        return this;
    }

    public MyUser email(String email) {
        setEmail(email);
        return this;
    }

    public MyUser userName(String userName) {
        setUserName(userName);
        return this;
    }

    public MyUser password(String password) {
        setPassword(password);
        return this;
    }

    public MyUser roles(String roles) {
        setRoles(roles);
        return this;
    }

    public MyUser isVerified(boolean isVerified) {
        setIsVerified(isVerified);
        return this;
    }

    public MyUser token(List<Token> token) {
        setToken(token);
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MyUser)) {
            return false;
        }
        MyUser myUser = (MyUser) o;
        return Objects.equals(id, myUser.id) && Objects.equals(email, myUser.email) && Objects.equals(userName, myUser.userName)  && Objects.equals(password, myUser.password) && Objects.equals(roles, myUser.roles) && isVerified == myUser.isVerified && Objects.equals(token, myUser.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, userName,  password, roles, isVerified, token);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", email='" + getEmail() + "'" +
            ", userName='" + getUserName() + "'" +
            ", password='" + getPassword() + "'" +
            ", roles='" + getRoles() + "'" +
            ", isVerified='" + isIsVerified() + "'" +
            ", token='" + getToken() + "'" +
            "}";
    }

     @NotBlank
     @Column(name = "USER_NAME")
     private String userName;



    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "USER_PASSWORD")
    private String password;

    @Column(name = "ROLE_ASSIGNED", nullable=false)
    @NotBlank(message = "Please assign a role as well.")
    //there will be two roles user or landlord
    private String roles;


private boolean isVerified;

    public boolean isIsVerified() {
        return this.isVerified;
    }

    public boolean getIsVerified() {
        return this.isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }



//    @Enumerated(EnumType.STRING)
//    private Sources sources;
     @JsonManagedReference
    @OneToMany(mappedBy = "myUser", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Token> token;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "UserEmail is mandatory") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "UserEmail is mandatory") String email) {
        this.email = email;
    }

    public @NotBlank String getUserName() {
        return userName;
    }

    public void setUserName(@NotBlank String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public @NotBlank(message = "Please assign a role as well.") String getRoles() {
        return roles;
    }

    public void setRoles(@NotBlank(message = "Please assign a role as well.") String roles) {
        this.roles = roles;
    }



//    public Sources getSources() {
//        return sources;
//    }
//
//    public void setSources(Sources sources) {
//        this.sources = sources;
//    }

    public List<Token> getToken() {
        return token;
    }

    public void setToken(List<Token> token) {
        this.token = token;
    }


}
