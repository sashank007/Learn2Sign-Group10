package com.example.amine.learn2sign;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.HashSet;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.amine.learn2sign.LoginActivity.INTENT_PRACTICE;
import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED;
import static com.example.amine.learn2sign.LoginActivity.INTENT_TIME_WATCHED_VIDEO;
import static com.example.amine.learn2sign.LoginActivity.INTENT_URI;
import static com.example.amine.learn2sign.LoginActivity.INTENT_WORD;

public class PracticeActivity extends AppCompatActivity {
    @BindView(R.id.rg_practice_learn)
    RadioGroup rg_practice_learn;

    @BindView(R.id.rb_learn)
    RadioButton rb_learn;

    @BindView(R.id.rb_practice)
    RadioButton rb_practice;

    @BindView(R.id.rb_predict)
    RadioButton rb_predict;

    @BindView(R.id.bt_practiceRecord)
    Button bt_record;

    @BindView(R.id.bt_practiceAccept)
    Button bt_accept;

    @BindView(R.id.bt_practiceRerecord)
    Button bt_rerecord;

    @BindView(R.id.tv_randomword)
    TextView randomWord;

    @BindView(R.id.vv_video_practice)
    VideoView videoPractice;
    @BindView(R.id.vv_video_original)
    VideoView vv_video_original;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    String old_text = "";
    String path;
    String returnedURI = "";
    String wordsArray[];
    String chosenWord = "";
    String rating = "";
    private static int boundSize = 24;
    long time_started_return = 0;
    long time_started = 0;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        //initialize variables
        ButterKnife.bind(this);
        rb_practice.setChecked(true);
        wordsArray = getResources().getStringArray(R.array.spinner_words);
        time_started = System.currentTimeMillis();

        //updating shared preferences
        sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString("Clicked", "Practice");
        prefEditor.commit();
        //enable radio clicking
        radioClick();
        //generate random word
        getRandomWord();
    }

    @OnClick(R.id.bt_practiceRecord)
    public void record_video() {

      recordVideo();
    }

    @OnClick(R.id.bt_practiceAccept)
    public void sendToServer() {
        rating = String.valueOf(ratingBar.getRating());
        createSharedPrefs();
        Toast.makeText(this, "Send to Server", Toast.LENGTH_SHORT).show();
        Intent t = new Intent(this, UploadActivity.class);
        t.putExtra(INTENT_PRACTICE, 1);
        startActivityForResult(t, 2000);
    }

    @OnClick(R.id.bt_practiceRerecord)
    public void rerecord_video() {

        recordVideo();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
        super.onBackPressed();
    }

    public void playOriginalVideo(String text) {
        old_text = text;
        if (text.equals("Alaska")) {

            path = "android.resource://" + getPackageName() + "/" + R.raw.alaska;
        } else if (text.equals("Arizona")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.arizona;
        } else if (text.equals("California")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.california;
        } else if (text.equals("Colorado"))  {
            path = "android.resource://" + getPackageName() + "/" + R.raw.colorado;
        } else if (text.equals("Florida")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.florida;
        } else if (text.equals("Georgia")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.georgia;
        } else if (text.equals("Hawaii")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.hawaii;
        } else if (text.equals("Illinois")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.illinois;
        } else if (text.equals("Indiana")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.indiana;
        } else if (text.equals("Kansas")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.kansas;
        } else if (text.equals("Louisiana")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.louisiana;
        } else if (text.equals("Massachusetts")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.massachusetts;
        } else if (text.equals("Michigan")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.michigan;
        } else if (text.equals("Minnesota")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.minnesota;
        } else if (text.equals("Nevada")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.nevada;
        } else if (text.equals("NewJersey")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.new_jersey;
        } else if (text.equals("NewMexico")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.new_mexico;
        } else if (text.equals("NewYork")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.new_york;
        } else if (text.equals("Ohio")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.ohio;
        } else if (text.equals("Pennsylvania")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.pennsylvania;
        } else if (text.equals("SouthCarolina")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.south_carolina;
        } else if (text.equals("Texas")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.texas;
        } else if (text.equals("Utah")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.utah;
        } else if (text.equals("Washington")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.washington;
        } else if (text.equals("Wisconsin")) {
            path = "android.resource://" + getPackageName() + "/" + R.raw.wisconsin;
        }
        if (!path.isEmpty()) {
            Uri uri = Uri.parse(path);
            vv_video_original.setVisibility(View.VISIBLE);
            vv_video_original.setVideoURI(uri);
            vv_video_original.start();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        Log.e("OnActivityresult", requestCode + " " + resultCode);
        if (requestCode == 2000) {
            System.out.println("success");

        }
        if (requestCode == 9999 && resultCode == 8888) {
            System.out.println("request code 9999 and result code 8888");
            if (intent.hasExtra(INTENT_URI) && intent.hasExtra(INTENT_TIME_WATCHED_VIDEO)) {
                time_started_return = intent.getLongExtra(INTENT_TIME_WATCHED_VIDEO, 0);
                ratingBar.setVisibility(View.VISIBLE);
                bt_record.setVisibility(View.GONE);
                bt_rerecord.setVisibility(View.VISIBLE);
                bt_accept.setVisibility(View.VISIBLE);
                returnedURI = intent.getStringExtra(INTENT_URI);
                videoPractice.setVisibility(View.VISIBLE);
                videoPractice.setVideoURI(Uri.parse(returnedURI));
                videoPractice.start();

                //logging activities

                playOriginalVideo(randomWord.getText().toString());
                loopVideos();

            }

        }
        if (requestCode == 9999 && resultCode == 7777) {
            if (intent != null) {
                //create folder
                if (intent.hasExtra(INTENT_URI) && intent.hasExtra(INTENT_TIME_WATCHED_VIDEO)) {
                    returnedURI = intent.getStringExtra(INTENT_URI);
                    time_started_return = intent.getLongExtra(INTENT_TIME_WATCHED_VIDEO, 0);
                    File f = new File(returnedURI);
                    f.delete();
                    time_started = System.currentTimeMillis();
                    //vv_video_learn.start();
                }
            }

        }

    }

    private void getRandomWord() {
        Random rand = new Random();
        int random = rand.nextInt(boundSize);
        chosenWord = wordsArray[random];
        randomWord.setText(chosenWord);
    }

    private void loopVideos() {

        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer != null) {
                    mediaPlayer.start();

                }

            }
        };
        vv_video_original.setOnCompletionListener(onCompletionListener);
        vv_video_original.setOnCompletionListener(onCompletionListener);
        videoPractice.setOnCompletionListener(onCompletionListener);
        videoPractice.setOnCompletionListener(onCompletionListener);
    }

    private void radioClick()
    {
        rg_practice_learn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rb_learn.getId()) {
                    Toast.makeText(getApplicationContext(), "Learn", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(PracticeActivity.this, MainActivity.class);
                    startActivity(i);
                } else if (checkedId == rb_practice.getId()) {
                    Toast.makeText(getApplicationContext(), "Practice", Toast.LENGTH_SHORT).show();

                }
                else if(checkedId == rb_predict.getId()){
                   // Toast.makeText(getApplicationContext(),"Practice",Toast.LENGTH_SHORT.show());
                    Toast.makeText(getApplicationContext(), "Predict", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(PracticeActivity.this, PredictActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void recordVideo()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        101);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);
            }

        } else {
            // Permission has already been granted
            File f = new File(Environment.getExternalStorageDirectory(), "Learn2Sign");

            if (!f.exists()) {
                f.mkdirs();
            }
            time_started = System.currentTimeMillis() - time_started;
            Intent t = new Intent(this, VideoActivity.class);
            t.putExtra(INTENT_WORD, chosenWord);
            t.putExtra(INTENT_TIME_WATCHED, time_started);
            startActivityForResult(t, 9999);


        }
    }

    private void createSharedPrefs()
    {
        String toAdd = randomWord.getText().toString() + "_" + time_started_return;

        System.out.println("rating : -------------------" + rating);
        HashSet<String> set1 = (HashSet<String>) sharedPreferences.getStringSet("PRACTICE", new HashSet<String>());
        HashSet<String> set2 = (HashSet<String>) sharedPreferences.getStringSet("PRACTICE_RATING", new HashSet<String>());
        set1.add(toAdd + "_" + (rating));
        set2.add(rating);
        System.out.println("set2:" + set2);
        sharedPreferences.edit().putStringSet("PRACTICE", set1).apply();
        sharedPreferences.edit().putStringSet("PRACTICE_RATING", set2).apply();
    }
}
