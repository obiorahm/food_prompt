package com.example.mgo983.myapplication;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by mgo983 on 8/24/18.
 */

public class QuestionAdapter extends ArrayAdapter {

    private LayoutInflater inflater;
    private Context context;
    private TextToSpeech myTTs;
    private ArrayList<String> mData = new ArrayList<String>();
    private ArrayList<ImageAdapter> mImageAdapters = new ArrayList<>();
    public static TextView currently_selected = null;
    private OrderInstructions orderInstructions;

    static final HashMap<String, String[]> mDataPair = new HashMap<>();
    static {
        String[] big = {"big.png", "bigger.png", "biggest.png"};
        mDataPair.put("Can I order an appetizer size or half-size entrée?", big);

        String [] share = {"share.png"};
        mDataPair.put("Can I split a dish with someone at my table?", share);

        String [] vegetableOrMeat = {"vegetables.png", "main_dish.png"};
        mDataPair.put("Could you give me a larger portion of vegetables and a smaller portion of the main dish? ", vegetableOrMeat);

        String [] substitute = {"substitution.png"};
        mDataPair.put("What can I substitute?", substitute);

        String [] slime = {"mayonnaise.png", "dressing.png", "cheese_sauce.png", "mustard.png"};
        mDataPair.put("Could you leave off the (sour cream, cheese sauce, dressing, mayonnaise, etc.)?", slime);

        String [] sliced = {"sliced.png", "whole.png"};
        mDataPair.put("Can you make this dish with sliced chicken breast?", sliced);

        String [] vegetarian = {"vegetarian.png"};
        mDataPair.put("Which dishes do you recommend for vegetarians?", vegetarian);

        String [] nutrition = {"nutrition_facts.png"};
        mDataPair.put("Do you have nutrition information on any of your dishes?", nutrition);

        String[] water = {"ice.png", "water.png", "warm.png"};
        mDataPair.put("No ice please?", water);

        String[] meat = { "blue_rare.png", "rare.png" , "medium_rare.png", "medium.png","medium_well.png", "well_done.png"};
        mDataPair.put("Can I have my meat well done?", meat);
    }




    public QuestionAdapter(Context context, int resource, TextToSpeech myTTS, OrderInstructions orderInstructions){
        super(context,resource);
        this.myTTs = myTTS;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.orderInstructions = orderInstructions;

    }

    public void addItem(String wordInMeal){

        mData.add(wordInMeal);

        /* Initialize image adapter here so that states are saved when the views are loaded
         * Don't initialize in get view else new image adapters are created
         * */

        ImageAdapter imageAdapter = new ImageAdapter(context, R.layout.list_view_icon_item, myTTs,orderInstructions);

        String [] options = mDataPair.get(wordInMeal);

        for(String option : options){
            String expanded_pair[] = {option, wordInMeal};
            imageAdapter.addItem(expanded_pair);
        }
        mImageAdapters.add(imageAdapter);




    }


    @Override
    public int getCount(){
        return mData.size();
    }

    @Override
    public String getItem(int position){
        return (String ) mData.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_view_items, null);
        }
        final String texttoSpeak =  mData.get(position);

        ImageAdapter imageAdapter = mImageAdapters.get(position);

        RecyclerView recyclerView =  convertView.findViewById(R.id.option_icons);

        LinearLayoutManager layoutManager= new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(imageAdapter);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textViewQuestion = ((Activity) context).findViewById(R.id.selected_option);
                textViewQuestion.setText(texttoSpeak);
            }
        });

        return convertView;
    }




}
