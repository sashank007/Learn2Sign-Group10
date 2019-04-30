package com.example.amine.learn2sign;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

class RetrieveFeedTask extends AsyncTask<Resource, Void, String> {

    private Exception exception;
    String answer;

    protected String doInBackground(Resource ... files) {
        Log.d("radhika","INSIDE PREDICT ALREADY------BACKGROUND----------------------");
        Resource file = files[0];
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        Log.d("radhika","radhika1 - adding file to request:"+file.getFilename() );

        body.set("user_csv", file);
        body.add("user_csv",file);
        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);
        Log.d("radhika","radhika2 - file added to requestentity" );
        RestTemplate restTemplate = new RestTemplate();
        restTemplate = setTimeout(restTemplate, 3000);
        Log.d("radhika","radhika3 -sending request" );
        ResponseEntity<String> response;
        response = restTemplate
                .postForEntity("http://18.217.146.55:5000/svm/runalgo", requestEntity, String.class);
        Log.d("radhika","radhika4 - got response" );
        HttpStatus statusCode = response.getStatusCode();
        if(statusCode!=HttpStatus.OK)
            return "Network Error or Could not predict";
        else{
            Log.d("radhika","status 400" );
        }

        Log.d("radhika","lalalala"+response.getBody());
        return response.getBody();

    }

    private RestTemplate setTimeout(RestTemplate restTemplate, int timeout) {
        Log.d("radhika","inside timeout");

        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
        SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate
                .getRequestFactory();
        rf.setReadTimeout(timeout);
        rf.setConnectTimeout(timeout);
        return restTemplate;
    }

}