package com.example.amine.learn2sign;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
    @BindView(R.id.rg_practice_learn2)
    RadioGroup rg_practice_learn;

    @BindView(R.id.rb_learn2)
    RadioButton rb_learn;

    @BindView(R.id.rb_practice2)
    RadioButton rb_practice;

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

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    String returnedURI = "";
    String wordsArray[];
    String chosenWord="";
    private static int boundSize=24;
    int randomInt=0;
    MainActivity mainActivity = new MainActivity();
    long time_started_return = 0;
    long time_started=0;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        ButterKnife.bind(this);

        //generate random word
        Random rand = new Random();
        randomInt=  rand.nextInt(boundSize);
        rb_practice.setChecked(true);
        wordsArray = getResources().getStringArray(R.array.spinner_words);
        chosenWord = wordsArray[randomInt];
        randomWord.setText(chosenWord);
        time_started = System.currentTimeMillis();
        sharedPreferences =  this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString("Clicked", "Practice");
        prefEditor.commit();
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
            }
        });
    }
    @OnClick(R.id.bt_practiceRecord)
    public void record_video() {


        if( ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {

            // Permission is not granted
            // Should we show an explanation?

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        101);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        if ( ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {

            // Permission is not granted
            // Should we show an explanation?


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        } else {
            // Permission has already been granted
            File f = new File(Environment.getExternalStorageDirectory(), "Learn2Sign");

            if (!f.exists()) {
                f.mkdirs();
            }

            time_started = System.currentTimeMillis() - time_started;

            Intent t = new Intent(this,VideoActivity.class);
            t.putExtra(INTENT_WORD,chosenWord);
            t.putExtra(INTENT_TIME_WATCHED, time_started);

            startActivityForResult(t,9999);





 /*           File m = new File(Environment.getExternalStorageDirectory().getPath() + "/Learn2Sign");
            if(!m.exists()) {
                if(m.mkdir()) {
                    Toast.makeText(this,"Directory Created",Toast.LENGTH_SHORT).show();
                }
            }

            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(EXTRA_DURATION_LIMIT, 10);

            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }*/
        }
    }

    @OnClick(R.id.bt_practiceAccept)
    public void sendToServer() {
        Toast.makeText(this,"Send to Server",Toast.LENGTH_SHORT).show();
        Intent t = new Intent(this,UploadActivity.class);
        t.putExtra(INTENT_PRACTICE,1);
        startActivityForResult(t,2000);
    }

   @OnClick(R.id.bt_practiceRerecord)
    public void rerecord_video() {


        if( ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {

            // Permission is not granted
            // Should we show an explanation?

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        101);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        if ( ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {

            // Permission is not granted
            // Should we show an explanation?


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        } else {
            // Permission has already been granted
            File f = new File(Environment.getExternalStorageDirectory(), "Learn2Sign");

            if (!f.exists()) {
                f.mkdirs();
            }

            time_started = System.currentTimeMillis() - time_started;

            Intent t = new Intent(this,VideoActivity.class);
            t.putExtra(INTENT_WORD,chosenWord);
            t.putExtra(INTENT_TIME_WATCHED, time_started);
            startActivityForResult(t,9999);





 /*           File m = new File(Environment.getExternalStorageDirectory().getPath() + "/Learn2Sign");
            if(!m.exists()) {
                if(m.mkdir()) {
                    Toast.makeText(this,"Directory Created",Toast.LENGTH_SHORT).show();
                }
            }

            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(EXTRA_DURATION_LIMIT, 10);

            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }*/
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
        super.onBackPressed();
    }
//
//    @Override
//    protected void onResume() {
//
//        vv_video_learn.start();
//        time_started = System.currentTimeMillis();
//        super.onResume();
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        Log.e("OnActivityresult",requestCode+" "+resultCode);
        if(requestCode==2000 ) {
            //from video activity
//            vv_record.setVisibility(View.GONE);
//            rb_learn.setChecked(true);
//            bt_cancel.setVisibility(View.GONE);
//            bt_send.setVisibility(View.GONE);
           
//            sp_words.setEnabled(true);
//            rb_learn.setEnabled(true);
////            rb_practice.setEnabled(false);
//            sp_ip_address.setEnabled(true);
        System.out.println("successssss--");

        }
        if(requestCode==9999 && resultCode == 8888) {
            System.out.println("request code 9999 and result code 8888");
            if(intent.hasExtra(INTENT_URI) && intent.hasExtra(INTENT_TIME_WATCHED_VIDEO)) {
                time_started_return = intent.getLongExtra(INTENT_TIME_WATCHED_VIDEO,0);
                ratingBar.setVisibility(View.VISIBLE);

                bt_record.setVisibility(View.GONE);
                 bt_rerecord.setVisibility(View.VISIBLE);
                bt_accept.setVisibility(View.VISIBLE);
                System.out.println("inside has intent uri" + intent.getStringExtra(INTENT_URI));
                returnedURI = intent.getStringExtra(INTENT_URI);
                videoPractice.setVisibility(View.VISIBLE);
                videoPractice.setVideoURI(Uri.parse(returnedURI));
                videoPractice.start();
                //logging activities
                String toAdd  = randomWord.getText()+"_"+time_started_return;
                int ratingNum = ratingBar.getNumStars();
                HashSet<String> set1 = (HashSet<String>) sharedPreferences.getStringSet("PRACTICE",new HashSet<String>());
                HashSet<String> set2 = (HashSet<String>) sharedPreferences.getStringSet("PRACTICE_RATING",new HashSet<String>());
                set1.add(toAdd + "_"+Integer.toString(ratingNum));
                set2.add(Integer.toString(ratingNum));
                System.out.println("set2:" + set2);
                sharedPreferences.edit().putStringSet("PRACTICE",set1).apply();
                sharedPreferences.edit().putStringSet("PRACTICE_RATING" , set2).apply();

//                returnedURI = intent.getStringExtra(INTENT_URI);
//                time_started_return = intent.getLongExtra(INTENT_TIME_WATCHED_VIDEO,0);
//
//                vv_record.setVisibility(View.VISIBLE);
//                bt_record.setVisibility(View.GONE);
//                bt_send.setVisibility(View.VISIBLE);
//                bt_cancel.setVisibility(View.VISIBLE);
//                sp_words.setEnabled(false);
//                rb_learn.setEnabled(false);
////                rb_practice.setEnabled(false);
//                vv_record.setVideoURI(Uri.parse(returnedURI));
//                int try_number = sharedPreferences.getInt("record_"+sp_words.getSelectedItem().toString(),0);
//                try_number++;
//                String toAdd  = sp_words.getSelectedItem().toString()+"_"+try_number+"_"+time_started_return + "";
//                HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("RECORDED",new HashSet<String>());
//                set.add(toAdd);
//                sharedPreferences.edit().putStringSet("RECORDED",set).apply();
//                sharedPreferences.edit().putInt("record_"+sp_words.getSelectedItem().toString(), try_number).apply();
//
//                vv_video_learn.start();
            }

        }

        if(requestCode==9999 && resultCode==7777)
        {
            System.out.println("requestcode 9999 and resultCode 7777");
            if(intent!=null) {
                //create folder
                if(intent.hasExtra(INTENT_URI) && intent.hasExtra(INTENT_TIME_WATCHED_VIDEO)) {
//                    returnedURI = intent.getStringExtra(INTENT_URI);
//                    time_started_return = intent.getLongExtra(INTENT_TIME_WATCHED_VIDEO,0);
//                    File f = new File(returnedURI);
//                    f.delete();
//                    //  int try_number = sharedPreferences.getInt("record_"+sp_words.getSelectedItem().toString(),0);
//                    // try_number++;
//                    //String toAdd  = sp_words.getSelectedItem().toString()+"_"+try_number+"_"+time_started_return + "_cancelled";
//                    //HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("RECORDED",new HashSet<String>());
//                    // set.add(toAdd);
//                    //  sharedPreferences.edit().putStringSet("RECORDED",set).apply();
//                    //   sharedPreferences.edit().putInt("record_"+sp_words.getSelectedItem().toString(), try_number).apply();
//
//
//
//
//                    time_started = System.currentTimeMillis();
//                    vv_video_learn.start();
                }
            }

        }

        /*if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            final Uri videoUri = intent.getData();


            vv_record.setVisibility(View.VISIBLE);
            vv_record.setVideoURI(videoUri);
            vv_record.start();
            play_video(sp_words.getSelectedItem().toString());
            bt_record.setVisibility(View.GONE);
            int i=0;
            File n = new File(Environment.getExternalStorageDirectory().getPath() + "/Learn2Sign/"
                    + sharedPreferences.getString(INTENT_ID,"0000")+"_"+sp_words.getSelectedItem().toString()+"_0" + ".mp4");
            while(n.exists()) {
                i++;
                n = new File(Environment.getExternalStorageDirectory().getPath() + "/Learn2Sign/"
                        + sharedPreferences.getString(INTENT_ID,"0000")+"_"+sp_words.getSelectedItem().toString()+"_"+i + ".mp4");
            }
            SaveFile saveFile = new SaveFile();
            saveFile.execute(n.getPath(),videoUri.toString());

            bt_send.setVisibility(View.VISIBLE);
            bt_cancel.setVisibility(View.VISIBLE);

            sp_words.setEnabled(false);
            rb_learn.setEnabled(false);
            rb_practice.setEnabled(false);
        }*/
    }

}
