package com.hisfrontend;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class UrlGenerator {
    public static URI userSignInURL(long id){
        return UriComponentsBuilder.fromHttpUrl("http://localhost:9090/his/users/signIn")
                .queryParam("userId", id)
                .build().encode().toUri();
    }

    public static URI getUserIdURL(String username, String password){
        return  UriComponentsBuilder.fromHttpUrl("http://localhost:9090/his/users/getId")
                .queryParam("username", username)
                .queryParam("password", password)
                .build().encode().toUri();
    }
}
