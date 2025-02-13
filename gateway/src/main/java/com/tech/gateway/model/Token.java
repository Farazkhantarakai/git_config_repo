package com.tech.gateway.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name="Tokens")
public class Token {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getUserLogStatus() {
        return userLogStatus;
    }

    public void setUserLogStatus(Boolean userLogStatus) {
        this.userLogStatus = userLogStatus;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "Logged_In")
    private Boolean userLogStatus;

    @Column(name = "ACCESS_TOKEN")
    private String accessToken;

@Column(name = "Refresh_Token")
    private String refreshToken;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(
            name = "user_id"
    )
    private MyUser myUser;

}
