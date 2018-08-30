package com.example.mgo983.myapplication;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by mgo983 on 8/24/18.
 */

public class QuestionAdapter extends ArrayAdapter {

    private LayoutInflater inflater;
    private Context context;
    private TextToSpeech myTTs;
    private ArrayList<String> mData = new ArrayList<String>();
    public static TextView currently_selected = null;




    public QuestionAdapter(Context context, int resource, TextToSpeech myTTS){
        super(context,resource);
        this.myTTs = myTTS;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    public void addItem(String wordInMeal){
        mData.add(wordInMeal);
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
        final TextView textView = (TextView) convertView.findViewById(R.id.question_text);
        textView.setText(texttoSpeak);

        textView.setSelected(false);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTTs.speak(texttoSpeak, TextToSpeech.QUEUE_FLUSH, null);

                //currently_selected = DetectImageActivity.last_parent_di;
                if (currently_selected != null && currently_selected!= textView){
                    currently_selected.setSelected(false);
                }
                if(textView.isSelected()){
                    textView.setSelected(false);
                }else{
                    textView.setSelected(true);
                }
                currently_selected = textView;

            }

        });







        return convertView;
    }


}
