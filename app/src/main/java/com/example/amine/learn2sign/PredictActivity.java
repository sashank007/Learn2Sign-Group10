package com.example.amine.learn2sign;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.stetho.Stetho;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.ParseException;

import static com.example.amine.learn2sign.LoginActivity.INTENT_EMAIL;
import static com.example.amine.learn2sign.LoginActivity.INTENT_ID;
import static com.example.amine.learn2sign.LoginActivity.INTENT_SERVER_ADDRESS;
import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED;
import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED_VIDEO;
import static com.example.amine.learn2sign.LoginActivity.INTENT_URI;
import static com.example.amine.learn2sign.LoginActivity.INTENT_WORD;

public class PredictActivity extends AppCompatActivity {
//
//    @BindView(R.id.rg_practice_learn)
//    RadioGroup rg_practice_learn;
//
//    @BindView(R.id.rb_learn)
//    RadioButton rb_learn;
//
//    @BindView(R.id.rb_practice)
//    RadioButton rb_practice;
//
//    @BindView(R.id.rb_predict)
//    RadioButton rb_predict;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);
        //initialize variables
//        ButterKnife.bind(this);
//        rb_predict.setChecked(true);


    }
}
