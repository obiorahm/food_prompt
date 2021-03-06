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
    private OrderInstructions orderInstructions;




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




    public ImageAdapter(Context context, int resource, TextToSpeech myTTS, OrderInstructions orderInstructions){
        super();
        this.myTTs = myTTS;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.orderInstructions = orderInstructions;

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

        //final String textToSpeak = fileName.replace(".png", "");
        //holder.mTextView.setText(textToSpeak);

        //set the order option value
        final String orderKey = fileName.replace(".png","");

        //orderKey is textToSpeak
        holder.mTextView.setText(orderKey);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //textViewQuestion.setText(currData[QUESTION]);

                int nextstate = next_state(state.get(position));
                Log.d("nextstate" , " " + nextstate + " ");

                myTTs.speak(orderKey, TextToSpeech.QUEUE_FLUSH, null);

                holder.mImageView.setBackground(ContextCompat.getDrawable(context, STATES[nextstate]));

                textViewQuestion.setText(getTextToSpeak(orderKey, nextstate));

                // collect state in orderInstruction to save in database
                orderInstructions.putOrder(orderKey, nextstate);

                state.set(position, nextstate);


            }

        });
        holder.mImageView.setBackground(ContextCompat.getDrawable(context, STATES[state.get(position)]));
        //holder.mImageView.setForeground(ContextCompat.getDrawable(context, STATES[state.get(position)]));




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
        //add state from database
        state.add(orderInstructions.getOrder(icon[0].replace(".png", "")));
        //state.add(STATE_NORMAL);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return mData.size();
    }


    public String getTextToSpeak(String switchString, int state){
        switch (switchString){
            case "big":
                String big[] = {
                        "",
                        "Can I order an appetizer size or half-size entree?",
                        "I don't want the appetizer size"
                };
                return big[state];
            case "bigger":
                String bigger[] = {
                        "",
                        "Can I have the normal size meal?",
                        "I don't want the normal size meal?"
                };
                return bigger[state];
            case "biggest":
                String biggest[] = {
                        "",
                        "Can I have a large size meal?",
                        "I don't want the large size meal."
                };
                return biggest[state];
            case "share":
                String share[] = {
                        "",
                        "Can I split my dish with someone?",
                        "I don't want to split this dish."
                };
                return share[state];
            case "vegetables":
                String vegetables[] = {
                        "",
                        "Can I have a larger portion of vegetables?",
                        "Can I have a smaller portion of vegetables?"
                };
                return vegetables[state];
            case "main_dish":
                String main_dish[]= {
                        "",
                        "Can I have a larger portion of the main dish and a smaller portion of vegetables?",
                        "Can I have a smaller portion of the main dish?"
                };
                return main_dish[state];
            case "substitution":
                String substitution[] = {
                        "",
                        "What can I substitute?",
                        ""
                };
                return substitution[state];
            case "mayonnaise":
                String mayonnaise[] = {
                        "",
                        "I'll have mayonnaise",
                        "Can you leave off the mayonnaise?"
                };
                return mayonnaise[state];
            case "dressing":
                String dressing[] = {
                        "",
                        "I'll have dressing",
                        "Can you leave off the dressing?"
                };
                return dressing[state];
            case "cheese_sauce":
                String cheese_sauce[] = {
                        "",
                        "I'll have cheese_sauce",
                        "Can you leave off the cheese sauce"
                };
                return cheese_sauce[state];
            case "mustard":
                String mustard [] = {
                        "",
                        "I'll have mustard",
                        "Can you leve off the mustard"
                };
                return mustard[state];
            case "sliced":
                String sliced [] = {
                        "",
                        "Can you make this dish with sliced chicken breast?",
                        "Don't slice the chicken breast"
                };
                return sliced[state];
            case "whole":
                String whole [] = {
                        "",
                        "Don't slice the chicken breast",
                        "Can you make this dish with sliced chicken breast?"
                };
                return whole[state];
            case "vegeterian":
                String vegetarian [] = {
                        "",
                        "Which dish do you recommend for vegetarians",
                        ""
                };
                return vegetarian[state];
            case "nutrition_facts":
                String nutrition_facts [] = {
                        "",
                        "Do you have nutrition information on any of your dishes?",
                        ""
                };
                return nutrition_facts[state];
            case "ice":
                String ice [] = {
                        "",
                        "ice please",
                        "no ice please"
                };
                return ice[state];

            case "medium":
                String medium [] = {
                        "",
                        "medium please",
                        ""
                };
                return medium[state];
            case "blue_rare":
                String blue_rare [] = {
                        "",
                        "blue rare",
                        ""
                };
                return blue_rare[state];
            case "rare":
                String rare [] = {
                        "",
                        "rare",
                        ""
                };
                return rare[state];
            case "medium_rare":
                String medium_rare [] = {
                        "",
                        "medium_rare",
                        ""
                };
                return medium_rare[state];
            case "medium_well":
                String medium_well [] = {
                        "",
                        "medium_well",
                        ""
                };
                return medium_well[state];
            case "well_done":
                String well_done [] = {
                        "",
                        "well_done",
                        ""
                };
                return well_done[state];
            default:
                return "";

        }
    }

}
