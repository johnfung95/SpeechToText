package com.example.texttospeech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class article_detail extends AppCompatActivity {
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private ImageView micButton;
    String[] titles, summaries, dates, contents;
    int ids[];
    Button last_page_btn, next_page_btn;
    TextView article_title, article_date, article_content;
    String title_str, date_str, content_str;
    int current_article_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        last_page_btn = findViewById(R.id.last_page_btn);
        next_page_btn = findViewById(R.id.next_page_btn);
        article_title = findViewById(R.id.article_title);
        article_date = findViewById(R.id.article_date);
        article_content = findViewById(R.id.article_content);

        titles = getResources().getStringArray(R.array.article_title);
        summaries = getResources().getStringArray(R.array.article_summary);
        dates = getResources().getStringArray(R.array.article_date);
        contents = getResources().getStringArray(R.array.article_content);
        ids = getResources().getIntArray(R.array.article_id);

        current_article_id = getIntent().getIntExtra("article_id", 1);
        if(current_article_id == 1) {
            last_page_btn.setVisibility(View.INVISIBLE);
            next_page_btn.setVisibility(View.VISIBLE);
        } else if (current_article_id == 5) {
            last_page_btn.setVisibility(View.VISIBLE);
            next_page_btn.setVisibility(View.INVISIBLE);
        }

        getData();
        setData();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }

        editText = findViewById(R.id.article_textbox);
        editText.setRawInputType(InputType.TYPE_NULL);
        micButton = findViewById(R.id.article_mic_btn);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

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
                micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (data.get(0).contains("返回")) {
                    Intent newIntent = new Intent(article_detail.this, MainActivity.class);
                    startActivity(newIntent);
                } else if (data.get(0).contains("上一頁") && current_article_id > 1) {
                    article_detail.this.startActivity(generateLastPageIntent());
                } else if (data.get(0).contains("下一頁") && current_article_id < 5) {
                    article_detail.this.startActivity(generateNextPageIntent());
                } else {
                    editText.setText(data.get(0));
                }
                ;
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
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    speechRecognizer.stopListening();
                    micButton.setImageResource(R.drawable.ic_mic_black_off);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

    }

    private void getData() {
        if(getIntent().hasExtra("article_title") && getIntent().hasExtra("article_date") && getIntent().hasExtra("article_content")) {
            title_str = getIntent().getStringExtra("article_title");
            date_str = getIntent().getStringExtra("article_date");
            content_str = getIntent().getStringExtra("article_content");
        } else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        article_title.setText(title_str);
        article_date.setText(date_str);
        article_content.setText(content_str);
    }

    public Intent generateLastPageIntent() {
        String oneTitle, oneDate, oneContent;
        int oneId;
        oneId = current_article_id - 1;
        oneTitle = titles[oneId - 1];
        oneDate = dates[oneId - 1];
        oneContent = contents[oneId - 1];
        Intent newIntent = new Intent(article_detail.this, article_detail.class);
        newIntent.putExtra("article_id", oneId);
        newIntent.putExtra("article_title", oneTitle);
        newIntent.putExtra("article_date", oneDate);
        newIntent.putExtra("article_content", oneContent);
        return newIntent;
    }

    public Intent generateNextPageIntent() {
        String oneTitle, oneDate, oneContent;
        int oneId;
        oneId = current_article_id + 1;
        oneTitle = titles[oneId - 1];
        oneDate = dates[oneId - 1];
        oneContent = contents[oneId - 1];
        Intent newIntent = new Intent(article_detail.this, article_detail.class);
        newIntent.putExtra("article_id", oneId);
        newIntent.putExtra("article_title", oneTitle);
        newIntent.putExtra("article_date", oneDate);
        newIntent.putExtra("article_content", oneContent);
        return newIntent;
    }
    public void onLastButtonClick(View view) {
        article_detail.this.startActivity(generateLastPageIntent());
    }

    public void onNextButtonClick(View view) {
        article_detail.this.startActivity(generateNextPageIntent());
    }
}