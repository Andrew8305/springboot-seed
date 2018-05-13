package com.wind.oauth.integration;

import lombok.Data;

import java.util.Map;

@Data
public class IntegrationAuthentication {

    private String authType;
    private String username;
    private Map<String,String[]> authParameters;

    public String getAuthParameter(String parameters){
        String[] values = this.authParameters.get(parameters);
        if(values != null && values.length > 0){
            return values[0];
        }
        return null;
    }
}