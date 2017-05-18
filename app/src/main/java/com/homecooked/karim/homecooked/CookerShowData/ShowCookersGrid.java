package com.homecooked.karim.homecooked.CookerShowData;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homecooked.karim.homecooked.CookerGridAdapter;
import com.homecooked.karim.homecooked.ShowSingleCookerInfo;


import java.util.ArrayList;
import java.util.List;

import com.homecooked.karim.homecooked.R;
import com.homecooked.karim.homecooked.model.Cooker;

public class ShowCookersGrid extends AppCompatActivity {

    private List<Cooker> CookerList = new ArrayList<Cooker>();

    CookerGridAdapter adapter;

    SearchView searchView;
    private DatabaseReference mDatabaseRef;
    private GridView gv;


    public static final String FIREBASE_DATABASE_PATH = "CookerInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cookers_grid);



        gv = (GridView) findViewById(R.id.cookers_grid);
        adapter = new CookerGridAdapter(this, CookerList);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FIREBASE_DATABASE_PATH);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {


                    Cooker   cooker = new Cooker();

                    String cooker_id = child.child("id").getValue(String.class);
                    cooker.setId(cooker_id);

                    String cooker_image_url = child.child("url").getValue(String.class);
                    cooker.setUrl(cooker_image_url);


                    String cooker_name = child.child("name").getValue(String.class);
                    cooker.setName("Chef." + cooker_name);

                    String cooker_address = child.child("address").getValue(String.class);
                    cooker.setAddress(cooker_address);



                    adapter.add(cooker);


                }

                gv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(ShowCookersGrid.this, ShowSingleCookerInfo.class);
                intent.putExtra("cooker_id", CookerList.get(position).getId());
                startActivity(intent);

            }
        });


    }












    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchviewmenu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
         searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //*** setOnQueryTextFocusChangeListener ***





        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {



            }
        });




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {



            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                adapter.filter(searchQuery.toString().trim());
                adapter.notifyDataSetChanged();
                gv.setAdapter(adapter);

                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed

                // failed try




                return true;  // Return true to collapse action view

            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded


                return true;  // Return true to expand action view


            }
        });
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }


        return super.onOptionsItemSelected(item);
    }













    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        gv.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
        gv.setAdapter(adapter);
    }
}