package com.homecooked.karim.homecooked.CookerShowData;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.homecooked.karim.homecooked.R;
import com.homecooked.karim.homecooked.model.FoodDish;

public class DetailDishInfo extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    private DatabaseReference node;
    public static final String FIREBASE_DATABASE_PATH = "CookerInfo";

    private ImageView img;
    private TextView dish_name;
    private TextView dish_weight;
    private TextView dish_prepared;
    private TextView dish_price;
    private TextView dish_ingredients;
    private TextView dish_about;
    private TextView dish_serves;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dish_info);


        dish_name = (TextView) findViewById(R.id.dish_name_view);
        dish_weight = (TextView) findViewById(R.id.dish_weight_view);
        dish_prepared = (TextView) findViewById(R.id.dish_preparation_view);
        dish_price = (TextView) findViewById(R.id.dish_price_view);
        dish_ingredients=(TextView)findViewById(R.id.dish_ingredients_view);
        dish_about=(TextView)findViewById(R.id.dish_info_view);
        dish_serves=(TextView)findViewById(R.id.dish_serves_view);

        img=(ImageView)findViewById(R.id.dishimage);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FIREBASE_DATABASE_PATH);



         String foodnamekey  = getIntent().getStringExtra("foodnamekey");
        String userId = getIntent().getStringExtra("userID");


        node=mDatabaseRef.child(userId).child("cookerDishes");

        // make query to get specific child which has this attribute
        Query queryRef = node
                .orderByChild("foodName")
                .equalTo(foodnamekey);


        queryRef.addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {





              FoodDish data =dataSnapshot.getValue(FoodDish.class);
              dish_name.setText(data.getFoodName());
              dish_weight.setText(data.getWeight()+" gm");
              dish_prepared.setText(data.getTimeToPrepared()+" mins");
              dish_price.setText(data.getPrice());
              dish_ingredients.setText(data.getIngredients());
              dish_about.setText(data.getDish_about());
              dish_serves.setText(data.getServes()+" persons");

              Glide.with(getApplicationContext()).load(data.getFoodUrl().toString()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(img);





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

}
