package com.hisfrontend;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class UrlGenerator {
    public final static String REGISTER_PATIENT = "http://localhost:9090/his/patients/register";
    public final static String GET_PATIENTS = "http://localhost:9090/his/patients/getPatients";
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

    public static URI getUser(long id){
        return  UriComponentsBuilder.fromHttpUrl("http://localhost:9090/his/users/getUser")
                .queryParam("id", id)
                .build().encode().toUri();
    }
}
