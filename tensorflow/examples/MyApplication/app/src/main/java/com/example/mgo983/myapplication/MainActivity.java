package com.example.mgo983.myapplication;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    QuestionAdapter questionAdapter;
    TextToSpeech myTTS;
    private int MY_DATA_CHECK_CODE = 0;
    private int assetcount = 0;
    String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imageButton = (ImageButton) findViewById(R.id.next_image);

        ImageButton prevImageBtn = (ImageButton) findViewById(R.id.previous_image);

        final ImageView imageView = (ImageView) findViewById(R.id.image) ;

        myTTS = new TextToSpeech(this, this);


        AssetManager assetManager = getAssets();
        try{
            final String[] allAssets = assetManager.list("meals");

            Glide.with(getApplicationContext())
                    .load(Uri.parse("file:///android_asset/meals/" + allAssets[assetcount]))
                    .into(imageView);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tempCount = assetcount + 1;
                    Log.d(LOG_TAG, "assetcount " + assetcount);
                    if (tempCount >= 0 && tempCount < allAssets.length){
                        assetcount = tempCount;
                        if (!allAssets[0].equals("")){
                            Glide.with(getApplicationContext())
                                    .load(Uri.parse("file:///android_asset/meals/" + allAssets[assetcount]))
                                    .into(imageView);
                        }
                    }
                }
            });

            prevImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tempCount = assetcount - 1;
                    Log.d(LOG_TAG, "assetcount " + assetcount);
                    if (tempCount >= 0 && tempCount < allAssets.length){
                        assetcount = tempCount;
                        if (!allAssets[0].equals("")){
                            Glide.with(getApplicationContext())
                                    .load(Uri.parse("file:///android_asset/meals/" + allAssets[assetcount]))
                                    .into(imageView);
                        }

                    }
                }
            });



        }catch (IOException e){
            Log.e(LOG_TAG, e + "");
        }
        //start text to speech
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        final TextView textViewQuestion = findViewById(R.id.selected_option);
        textViewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTTS.speak(textViewQuestion.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        questionAdapter = new QuestionAdapter(this, R.layout.list_view_items,myTTS );

        addAdapterItems(questionAdapter);

        ListView listView = (ListView) findViewById(R.id.questions);
        listView.setAdapter(questionAdapter);

    }

    private void addAdapterItems(QuestionAdapter questionAdapter){
        questionAdapter.addItem("Can I order an appetizer size or half-size entrée?");
        questionAdapter.addItem("Can I split a dish with someone at my table?");
        questionAdapter.addItem("Could you give me a larger portion of vegetables and a smaller portion of the main dish? ");
        questionAdapter.addItem("What can I substitute?");
        questionAdapter.addItem("Could you leave off the (sour cream, cheese sauce, dressing, mayonnaise, etc.)?");
        questionAdapter.addItem("Can you make this dish with sliced chicken breast?");
        questionAdapter.addItem("Which dishes do you recommend for vegetarians?");
        questionAdapter.addItem("Do you have nutrition information on any of your dishes?");
        questionAdapter.addItem("No ice please?");
        questionAdapter.addItem("Can I have my meat well done?");


    }

    //checks whether the user has the TTS data installed. If it is not, the user will be prompted to install it.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTS = new TextToSpeech(this, this);
            } else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
    @Override
    public void onInit(int initStatus){
        if (initStatus == TextToSpeech.SUCCESS) {
            myTTS.setLanguage(Locale.US);
            myTTS.setSpeechRate(0.8f);
        }

    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (myTTS != null) {
            myTTS.stop();
            myTTS.shutdown();
        }
        super.onDestroy();
    }
}
