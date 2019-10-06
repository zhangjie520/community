package com.example.community.dto;

import lombok.Data;

//import lombok.Data;
//
@Data
public class AccessTokenDTO {
//    client_id	string	Required. The client ID you received from GitHub for your GitHub App.
//    client_secret	string	Required. The client secret you received from GitHub for your GitHub App.
//    code	string	Required. The code you received as a response to Step 1.
//    redirect_uri	string	The URL in your application where users are sent after authorization.
//    state	string	The unguessable random string you provided in Step 1.
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;

}
