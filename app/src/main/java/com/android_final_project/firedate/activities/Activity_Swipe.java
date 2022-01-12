package com.android_final_project.firedate.activities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android_final_project.firedate.R;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;


public class Activity_Swipe extends AppCompatActivity {

    private ArrayList<String> cardList;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    private SwipeFlingAdapterView flingContainer;
//    @BindView(R.id.frame) SwipeFlingAdapterView flingContainer;
    private Button swipe_BTN_left;
    private Button swipe_BTN_right;
    private TextView swipe_TXT_print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);
//        ButterKnife.bind(this);

        findViews();
        initViews();

    }

    private void findViews(){
        flingContainer = findViewById(R.id.swipe_FRM_cards);
        swipe_BTN_left = findViewById(R.id.swipe_BTN_left);
        swipe_BTN_right = findViewById(R.id.swipe_BTN_right);
        swipe_TXT_print = findViewById(R.id.swipe_TXT_print);
    }

    private void initViews(){
        swipe_BTN_left.setOnClickListener(v -> { flingContainer.getTopCardListener().selectLeft(); });
        swipe_BTN_right.setOnClickListener(v -> { flingContainer.getTopCardListener().selectRight(); });
        initCardList();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.card, R.id.card_text, cardList);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(
                new SwipeFlingAdapterView.onFlingListener() {
                    @Override
                    public void removeFirstObjectInAdapter() {
                        // this is the simplest way to delete an object from the Adapter (/AdapterView)
                        Log.d("LIST", "removed object!");
                        cardList.remove(0);
                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onLeftCardExit(Object dataObject) {
                        //Do something on the left!
                        //You also have access to the original object.
                        //If you want to use it just cast it (String) dataObject

//                        Toast.makeText(MainActivity.this, dataObject+" Left!", Toast.LENGTH_SHORT).show();
                        swipe_TXT_print.setText(dataObject+" Left!");
                    }

                    @Override
                    public void onRightCardExit(Object dataObject) {
//                        Toast.makeText(MainActivity.this, dataObject+" Right!", Toast.LENGTH_SHORT).show();
                        swipe_TXT_print.setText(dataObject+" Right!");
                    }

                    @Override
                    public void onAdapterAboutToEmpty(int itemsInAdapter) {
                        // Ask for more data here
                        cardList.add("XML "+i);
                        arrayAdapter.notifyDataSetChanged();
                        Log.d("LIST", "notified");
                        i++;
                    }

                    @Override
                    public void onScroll(float scrollProgressPercent) {
                        View selectedCard = flingContainer.getSelectedView();
                        selectedCard.findViewById(R.id.card_swiperight_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                        selectedCard.findViewById(R.id.card_swipeleft_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                    }
                }
        );

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
//                Toast.makeText(MainActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                swipe_TXT_print.setText(dataObject+" Clicked!");
            }
        });
    }

    private void initCardList(){
        cardList = new ArrayList<>();
        cardList.add("php");
        cardList.add("c");
        cardList.add("python");
        cardList.add("java");
        cardList.add("html");
        cardList.add("c++");
        cardList.add("css");
        cardList.add("javascript");
    }

}