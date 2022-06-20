package com.example.texttospeech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private ImageView micButton;
    private Button crashBtn, exceptionBtn;
    private FirebaseCrashlytics crashlytics;

    String[] titles, summaries, dates, contents;
    int ids[];
    RecyclerView articles_section;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        articles_section = findViewById(R.id.articles_section);
        titles = getResources().getStringArray(R.array.article_title);
        summaries = getResources().getStringArray(R.array.article_summary);
        dates = getResources().getStringArray(R.array.article_date);
        contents = getResources().getStringArray(R.array.article_content);
        ids = getResources().getIntArray(R.array.article_id);
        crashBtn = findViewById(R.id.crashBtn);
        exceptionBtn = findViewById(R.id.exceptionBtn);
        crashlytics = FirebaseCrashlytics.getInstance();

        crashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crashlytics.log("This is a forced crash error. Please ignore");
                throw new RuntimeException("Test fatal app Crash"); // Force a crash
            }
        });

        exceptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    throw new Exception("Testing non-fatal error");
                } catch (Exception e) {
                    Log.i("Test Error", "errrrrrrr");
                    crashlytics.log("This is a non-fatal error and should not obstruct app running");
                    crashlytics.recordException(e);
                }
            }
        });

        //        addContentView(crashBtn, new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
        adapter newAdapter = new adapter(this, titles, summaries, dates, contents, ids);
        articles_section.setAdapter(newAdapter);
        articles_section.setLayoutManager(new LinearLayoutManager(this));

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        editText = findViewById(R.id.menu_textbox);
        editText.setRawInputType(InputType.TYPE_NULL);
        micButton = findViewById(R.id.menu_mic_btn);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh");
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "zh");
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "zh");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                editText.setText("");
                editText.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                crashlytics.log("The use of mic");
                micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String oneTitle, oneDate, oneContent;
                int oneId;
                if(data.get(0).contains("打開一") || data.get(0).contains("打開1") || data.get(0).contains("打開壹")) {
                    oneId = ids[0];
                    oneTitle = titles[0];
                    oneDate = dates[0];
                    oneContent = contents[0];

                    Intent newIntent = new Intent(MainActivity.this, article_detail.class);
                    newIntent.putExtra("article_id", oneId);
                    newIntent.putExtra("article_title", oneTitle);
                    newIntent.putExtra("article_date", oneDate);
                    newIntent.putExtra("article_content", oneContent);

                    MainActivity.this.startActivity(newIntent);
                }  else if(data.get(0).contains("打開二") || data.get(0).contains("打開2") || data.get(0).contains("打開貳")) {
                    oneId = ids[1];
                    oneTitle = titles[1];
                    oneDate = dates[1];
                    oneContent = contents[1];

                    Intent newIntent = new Intent(MainActivity.this, article_detail.class);
                    newIntent.putExtra("article_id", oneId);
                    newIntent.putExtra("article_title", oneTitle);
                    newIntent.putExtra("article_date", oneDate);
                    newIntent.putExtra("article_content", oneContent);

                    MainActivity.this.startActivity(newIntent);
                } else if(data.get(0).contains("打開三") || data.get(0).contains("打開3") || data.get(0).contains("打開參") || data.get(0).contains("打開衫")) {
                    oneId = ids[2];
                    oneTitle = titles[2];
                    oneDate = dates[2];
                    oneContent = contents[2];

                    Intent newIntent = new Intent(MainActivity.this, article_detail.class);
                    newIntent.putExtra("article_id", oneId);
                    newIntent.putExtra("article_title", oneTitle);
                    newIntent.putExtra("article_date", oneDate);
                    newIntent.putExtra("article_content", oneContent);

                    MainActivity.this.startActivity(newIntent);
                } else if(data.get(0).contains("打開四") || data.get(0).contains("打開4")  || data.get(0).contains("打開肆")) {
                    oneId = ids[3];
                    oneTitle = titles[3];
                    oneDate = dates[3];
                    oneContent = contents[3];

                    Intent newIntent = new Intent(MainActivity.this, article_detail.class);
                    newIntent.putExtra("article_id", oneId);
                    newIntent.putExtra("article_title", oneTitle);
                    newIntent.putExtra("article_date", oneDate);
                    newIntent.putExtra("article_content", oneContent);

                    MainActivity.this.startActivity(newIntent);
                } else if(data.get(0).contains("打開五") || data.get(0).contains("打開5") || data.get(0).contains("打開伍")) {
                    oneId = ids[4];
                    oneTitle = titles[4];
                    oneDate = dates[4];
                    oneContent = contents[4];

                    Intent newIntent = new Intent(MainActivity.this, article_detail.class);
                    newIntent.putExtra("article_id", oneId);
                    newIntent.putExtra("article_title", oneTitle);
                    newIntent.putExtra("article_date", oneDate);
                    newIntent.putExtra("article_content", oneContent);

                    MainActivity.this.startActivity(newIntent);
                } else {
                    editText.setText(data.get(0));
                };
                Log.v("Received commands", data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                    micButton.setImageResource(R.drawable.ic_mic_black_off);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    micButton.setImageResource(R.drawable.ic_mic_black_24dp);
                    Log.v("Start listening", "onTouch");
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }

    public void onButtonClick(View view) {
        Intent intent = new Intent(this, article_detail.class);
        startActivity(intent);
    }

}
