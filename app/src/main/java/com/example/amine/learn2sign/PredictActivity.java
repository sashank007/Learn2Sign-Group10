package com.example.amine.learn2sign;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.stetho.Stetho;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import org.apache.commons.io.IOUtils;
import  org.apache.http.entity.mime.MultipartEntity;
import  org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;

import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;


public class PredictActivity extends AppCompatActivity {


    @BindView(R.id.rg_practice_learn)
    RadioGroup rg_practice_learn;

    @BindView(R.id.rb_learn)
    RadioButton rb_learn;

    @BindView(R.id.rb_practice)
    RadioButton rb_practice;

    @BindView(R.id.rb_predict)
    RadioButton rb_predict;

    @BindView(R.id.bt_select_data)
    Button bt_select_data;

    @BindView(R.id.bt_predict)
    Button bt_predict;

   @BindView(R.id.tv_answer)
    TextView tv_answer;

    String answer = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);


        //initialize variables
        ButterKnife.bind(this);
        rb_predict.setChecked(true);
        rb_practice.setEnabled(false);
        bt_predict.setClickable(false);
        bt_predict.setEnabled(false);


        rg_practice_learn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == rb_learn.getId()){
                    Toast.makeText(getApplicationContext(),"Learn",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent( PredictActivity.this , MainActivity.class);
                    startActivity(i);
                }
                else if(checkedId == rb_practice.getId()){
                    Toast.makeText(getApplicationContext(),"Practice",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent( PredictActivity.this , PracticeActivity.class);
                    startActivity(i);

                }
                else if(checkedId == rb_predict.getId()){
                    Toast.makeText(getApplicationContext(),"Predict",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @OnClick(R.id.bt_predict)
    public void displayResult(){
        if(bt_predict.isClickable()){
            Toast.makeText(getApplicationContext(),"predict button pressed!",Toast.LENGTH_SHORT).show();
            if(tv_answer!=null)
                tv_answer.setText(this.answer);
            else
                Toast.makeText(getApplicationContext(),"nothing came back!",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"no file selected!",Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.bt_select_data)
    public void uploadCSV(){

        Toast.makeText(getApplicationContext(),"Upload data",Toast.LENGTH_SHORT).show();

        Intent selectFile =  new Intent().setType("*/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(selectFile , "Upload file"), 3849);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Toast.makeText(getApplicationContext(),"Did something",Toast.LENGTH_SHORT).show();


        File signFile = null;
        if(requestCode == 3849 && resultCode == RESULT_OK){
            try {

                String str[] = data.getData().getPath().toString().split("/");
                String fileName = str[str.length - 1];
                //URI uri = new URI();
                signFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+"/"+fileName);
                Toast.makeText(getApplicationContext(),"this file selected",Toast.LENGTH_SHORT).show();

            }
            catch(Exception e){
                Log.e("URI error",e.getLocalizedMessage());
            }

            Resource resource = new FileSystemResource(signFile);
            if(requestCode!=RESULT_CANCELED){
                //String prediction = predictAlready(resource);
                String predict = "not set";
                try{
                     predict = new RetrieveFeedTask().execute(resource).get();

                }
                catch(Exception e){
                    Log.d("radhika async  ",e.getMessage());
                }

                //String prediction1  = prediction.doInBackground(resource);
                Toast.makeText(getApplicationContext(),"prediction - "+predict,Toast.LENGTH_SHORT).show();
                this.answer = predict;
            }


        }

        if(requestCode == 3849 && resultCode != RESULT_OK){
            Toast.makeText(getApplicationContext(),"File not selected",Toast.LENGTH_SHORT).show();
        }
        else{

            String name = "File selected = "+signFile.getName();
           // Toast.makeText(getApplicationContext(),"file done lalala " +name ,Toast.LENGTH_SHORT).show();
            bt_predict.setClickable(true);
            bt_predict.setEnabled(true);

        }
    }


    String predictAlready(Resource file){
        Log.d("radhika","INSIDE PREDICT ALREADY----------------------------");

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
            restTemplate = setTimeout(restTemplate, 10000);
            Log.d("radhika","radhika3 -sending request" );
            ResponseEntity<String> response;
            response = restTemplate.postForEntity("http://3.19.14.50:5000/svm/runalgo", requestEntity, String.class);
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

    String getPrediction(Resource File){
        //System.out.println("\n\n\n\n\n Inside get prediction \n\n\n\n");
        try {
            //System.out.println("\n\n\n\n\n Inside try get prediction \n\n\n\n");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body
                    = new LinkedMultiValueMap<>();
            System.out.print("working till here "  );

            body.add("user_csv", File);

            HttpEntity<MultiValueMap<String, Object>> requestEntity
                    = new HttpEntity<>(body, headers);
            System.out.print("working till here " +requestEntity.toString() );
            System.out.print("file :- " +File.getFilename() );
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response;
           // PostData postData = new PostData();
            response = restTemplate.postForEntity("http://3.19.14.50:5000/svm/runalgo", requestEntity, String.class);
            HttpStatus statusCode = response.getStatusCode();
            if(statusCode!=HttpStatus.OK)
                return "Network Error or Could not predict";


            return response.getBody();
        }

        catch (Exception e){
            System.out.print("get prediction threw error");
            return  e.getMessage();
        }
    }



}
