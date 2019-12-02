package com.skat.basicauthproxy;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/")
public class controller {


    @Value("${app.user}")
    private String user;

    @Value("${app.password}")
    private String password;

    @Value("${app.url}")
    private String url;


    @RequestMapping(method = RequestMethod.POST, path = "")
    public ResponseEntity<String> post(@RequestBody String body){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createAuthHeaders(this.user, this.password);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity ent = new HttpEntity<>(body, headers);
        try {
            return restTemplate.exchange(this.url, HttpMethod.POST, ent, String.class);
        } catch (HttpStatusCodeException exception) {
            return new ResponseEntity<>(exception.getResponseBodyAsString(),exception.getStatusCode());
        }

    }

    private HttpHeaders createAuthHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }



}
