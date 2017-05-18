package com.homecooked.karim.homecooked.CookerShowData;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import com.homecooked.karim.homecooked.R;
import com.homecooked.karim.homecooked.model.FoodDish;

public class ShowGridCookerDishes extends AppCompatActivity {



    private DatabaseReference mDatabaseRef;
    private DatabaseReference node;
    private ListView lv;
    private FirebaseListAdapter mAdapter;
    String USERID;

    String COOKERID;
    public static final String FIREBASE_DATABASE_PATH = "CookerInfo";

    private List<FoodDish> foodList = new ArrayList<FoodDish>();

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dishes_grid);




        lv = (ListView) findViewById(R.id.dishes_list);




        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FIREBASE_DATABASE_PATH);

            // Client Aspect
        COOKERID = getIntent().getStringExtra("COOKER ID");

        if(COOKERID!=null){



                node = mDatabaseRef.child(COOKERID).child("cookerDishes");


                mAdapter = new FirebaseListAdapter<FoodDish>(this, FoodDish.class, R.layout.grid_dish_item, node) {


                    @Override
                    protected void populateView(View view, final FoodDish chatMessage, final int position) {

                        ((TextView) view.findViewById(R.id.dish_name_item)).setText(chatMessage.getFoodName());


                        ((TextView) view.findViewById(R.id.dish_price_item)).setText(chatMessage.getPrice());
                        img = (ImageView) view.findViewById(R.id.dish_image);
                        Glide.with(getApplicationContext()).load(chatMessage.getFoodUrl().toString()).into(img);

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Intent intent = new Intent(ShowGridCookerDishes.this, DetailDishInfo.class);
                                intent.putExtra("foodnamekey", chatMessage.getFoodName());
                                intent.putExtra("userID", COOKERID);

                                startActivity(intent);
                            }
                        });
                    }
                };
            lv.setAdapter(mAdapter);




        }



// Cooker aspect

        USERID = getIntent().getStringExtra("USER ID");


        if (USERID != null) {

            node = mDatabaseRef.child(USERID).child("cookerDishes");


            mAdapter = new FirebaseListAdapter<FoodDish>(this, FoodDish.class, R.layout.grid_dish_item, node) {


                @Override
                protected void populateView(View view, final FoodDish chatMessage, final int position) {

                    ((TextView) view.findViewById(R.id.dish_name_item)).setText(chatMessage.getFoodName());


                    ((TextView) view.findViewById(R.id.dish_price_item)).setText(chatMessage.getPrice());
                    img = (ImageView) view.findViewById(R.id.dish_image);
                    Glide.with(getApplicationContext()).load(chatMessage.getFoodUrl().toString()).into(img);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent intent = new Intent(ShowGridCookerDishes.this, DetailDishInfo.class);
                            intent.putExtra("foodnamekey", chatMessage.getFoodName());
                            intent.putExtra("userID", USERID);

                            startActivity(intent);
                        }
                    });
                }
            };
            lv.setAdapter(mAdapter);


        }





    }



}
