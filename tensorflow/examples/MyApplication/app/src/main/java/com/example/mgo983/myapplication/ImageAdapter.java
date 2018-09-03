package com.example.mgo983.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.speech.tts.TextToSpeech;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by mgo983 on 8/30/18.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private TextToSpeech myTTs;
    private ArrayList<String[]> mData = new ArrayList<String[]>();
    private ArrayList<Integer> state = new ArrayList<Integer>();




    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView;

        public View mParent;

        public ViewHolder(View convertView, View parent){
            super(convertView);

            mImageView = convertView.findViewById(R.id.icon_item);
            mTextView = convertView.findViewById(R.id.choice_text);
            mParent = parent;
        }

    }

    private static int STATE_NORMAL = 0;
    private static int STATE_SELECT = 1;
    private static int STATE_REJECT = 2;

    private int normal = R.drawable.layer_drawable;
    private int select = R.drawable.option_button;
    private int reject = R.drawable.option_button_on;




    public ImageAdapter(Context context, int resource, TextToSpeech myTTS){
        super();
        this.myTTs = myTTS;
        this.context = context;
        this.inflater = LayoutInflater.from(context);




    }


    private int[] STATES = { normal, select, reject };


    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View convertView = inflater.inflate(R.layout.list_view_icon_item, null);

        return new ImageAdapter.ViewHolder(convertView, parent);
    }

    @Override
    public void onBindViewHolder(final ImageAdapter.ViewHolder holder, final int position){

        int OPTION = 0;
        final int QUESTION = 1;
        final int DESCRIPTION = 2;

        final String currData [] = mData.get(position);

        final String fileName = currData[OPTION];


        Glide.with(context).load(Uri.parse("file:///android_asset/icons/" + fileName)).into(holder.mImageView);


        final TextView textViewQuestion = ((Activity) context).findViewById(R.id.selected_option);

        final String textToSpeak = fileName.replace(".png", "");
        holder.mTextView.setText(textToSpeak);


        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textViewQuestion.setText(currData[QUESTION]);

                myTTs.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null);

                int nextstate = next_state(state.get(position));
                Log.d("nextstate" , " " + nextstate + " ");
                holder.mImageView.setBackground(ContextCompat.getDrawable(context, STATES[nextstate]));

                state.set(position, nextstate);


            }

        });
        holder.mImageView.setBackground(ContextCompat.getDrawable(context, STATES[state.get(position)]));




    }

    private int next_state(int state_id){
        int new_state = state_id + 1;
        if (new_state >= STATES.length)
            return STATE_NORMAL;
        else
            return new_state;

    }


    public void addItem(String[] icon){
        mData.add(icon);
        state.add(STATE_NORMAL);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return mData.size();
    }

}
