package com.social.ws.auth;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.social.ws.user.User;
import lombok.Data;

@Data
@Entity
@Table(name="tokens")
public class Token {

    @Id
    private String token;

    @ManyToOne
    private User user;

}