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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

        final OrderInstructions orderInstructions = new OrderInstructions();

        //get username
        Intent intent = getIntent();
        final String username = intent.getStringExtra(LoginActivity.USERNAME);



        AssetManager assetManager = getAssets();
        try{
            final String[] allAssets = assetManager.list("meals");

            String firstMeal = allAssets[assetcount];

            Glide.with(getApplicationContext())
                    .load(Uri.parse("file:///android_asset/meals/" + firstMeal))
                    .into(imageView);

            Log.d("chopped salad", firstMeal);

            UserExists(username, orderInstructions, firstMeal);


            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tempCount = assetcount + 1;
                    Log.d(LOG_TAG, "assetcount " + assetcount);
                    if (tempCount >= 0 && tempCount < allAssets.length){
                        assetcount = tempCount;
  //                      if (!allAssets[0].equals("")){
                            Glide.with(getApplicationContext())
                                    .load(Uri.parse("file:///android_asset/meals/" + allAssets[assetcount]))
                                    .into(imageView);
                            String meal = trimMealName(allAssets[assetcount - 1]);

                            String next_meal = trimMealName(allAssets[assetcount]);

                            firebaseSaveState(orderInstructions,username, meal);
                            getSavedFirebaseState(orderInstructions, username, next_meal);
                            //resetAdapter(orderInstructions);
//                        }
                    }
                    if (tempCount == allAssets.length){
                        String meal = trimMealName(allAssets[tempCount - 1]);

                        firebaseSaveState(orderInstructions,username, meal);
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
                            String meal = trimMealName(allAssets[assetcount]);

                            getSavedFirebaseState(orderInstructions, username, meal);
                            //resetAdapter(orderInstructions);
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



        //resetAdapter(orderInstructions);


    }


    private String trimMealName(String fileName){

        return fileName.replace(".jpg", "").replace(".jpeg", "")
                .replace(".png","");
    }


    private void UserExists(final String username, final OrderInstructions orderInstructions, final String firstMeal){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getKey().toString().equals(username)){
                        getSavedFirebaseState(orderInstructions, username, trimMealName(firstMeal));
                    }
                //resetAdapter(orderInstructions);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void resetAdapter(OrderInstructions orderInstructions){
        questionAdapter = new QuestionAdapter(this, R.layout.list_view_items,myTTS, orderInstructions);

        addAdapterItems(questionAdapter);

        ListView listView = (ListView) findViewById(R.id.questions);
        listView.setAdapter(questionAdapter);

    }

    private void firebaseSaveState(OrderInstructions orderInstructions, String username, String meal){

        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("user").child(username).child(meal).child("completeOrder");
        firebaseDatabase.setValue(orderInstructions.toMap());

    }

    private void getSavedFirebaseState(final OrderInstructions orderInstructions, String username, String meal){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user")
                .child(username).child(meal);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("datasnapshot", dataSnapshot.getValue().toString());
                //OrderInstructions orderInstructions1 = dataSnapshot.getChildren()getValue(OrderInstructions.class);
                orderInstructions.clone(dataSnapshot.getValue(OrderInstructions.class));
                resetAdapter(orderInstructions);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void saveMeal(OrderInstructions orderInstructions, String meal, String user_name){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user")
                .child(user_name).child(meal);
                databaseReference.setValue(orderInstructions);
    }
}
