package com.homecooked.karim.homecooked;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.homecooked.karim.homecooked.CookerInsertsData.InsertCoockerInfo;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import com.homecooked.karim.homecooked.CookerInsertsData.InsertCookerDishInfo;
import com.homecooked.karim.homecooked.CookerShowData.ShowGridCookerDishes;
import com.homecooked.karim.homecooked.loginSignUpAttachments.LoginActivity;
import com.homecooked.karim.homecooked.model.Cooker;


public class ShowSingleCookerInfo extends AppCompatActivity {


    //Drawer Variables
    private AccountHeader Header = null;
    private Drawer myDrawer = null;
    private IProfile profile;

    private DatabaseReference mDatabaseRef;

    private ImageView cookerphoto ;
    private TextView cookername ;
    private TextView cookerphone ;
    private TextView cookeravailabilty ;
    private TextView cookerabout ;
    private TextView cookeraddress ;

    String InsertedIDtoDB;

    private ProgressDialog progressDialog;
    private EditText EditTextNewINFO;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    Toolbar toolbar;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String cooker_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.homecooked.karim.homecooked.R.layout.activity_show_single_cooker_info);

        // bring back ID which it's inserted in DB - hint it's has the same value for Authentication ID
        InsertedIDtoDB = getIntent().getStringExtra("InsertedIDtoDB");



           progressDialog = new ProgressDialog(ShowSingleCookerInfo.this);

        cookerphoto = (ImageView) findViewById(com.homecooked.karim.homecooked.R.id.cooker_profile_image);
        cookername = (TextView) findViewById(com.homecooked.karim.homecooked.R.id.cooker_text_name);
        cookerphone = (TextView) findViewById(com.homecooked.karim.homecooked.R.id.cooker_text_number);
        cookeravailabilty = (TextView) findViewById(com.homecooked.karim.homecooked.R.id.cooker_text_availabilty);
        cookerabout = (TextView) findViewById(com.homecooked.karim.homecooked.R.id.cooker_text_about);
        cookeraddress = (TextView) findViewById(com.homecooked.karim.homecooked.R.id.cooker_text_address);

        // get database refrence
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(InsertCoockerInfo.FIREBASE_DATABASE_PATH);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();








    //get current user


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in.


                    toolbar = (Toolbar) findViewById(com.homecooked.karim.homecooked.R.id.toolbar);

                    setSupportActionBar(toolbar);


                    // Create a few sample profile
                    profile = new ProfileDrawerItem().withName("").withEmail("").withIcon(getResources().getDrawable(com.homecooked.karim.homecooked.R.drawable.chef));


                    // Create the AccountHeader
                    buildHeader(false);

                    //Create the drawer

                    createDrawer();

                    String x = "";

                    x = user.getDisplayName();

                    //get cooker info if there is a user

                    getDatabaseInfoForCookerUser();


                    if (x != null) {

                    }

                } else if (user == null) { // customer Aspect



                    toolbar = (Toolbar) findViewById(com.homecooked.karim.homecooked.R.id.toolbar);
                    toolbar.setTitle(getString(com.homecooked.karim.homecooked.R.string.app_name));

                    getCookerInfoForCustomer();
                    createCookerClientDrawer();

                }
            }};



        progressBar = (ProgressBar) findViewById(com.homecooked.karim.homecooked.R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }


    }


    public void createDrawer(){

    myDrawer = new DrawerBuilder()
            .withActivity(ShowSingleCookerInfo.this)
            .withToolbar(toolbar)
            .withAccountHeader(Header) //set the AccountHeader we created earlier for the header
            .addDrawerItems(
                    new PrimaryDrawerItem().withName("Inset yours info").withIcon(FontAwesome.Icon.faw_user_circle).withIdentifier(1),
                    new PrimaryDrawerItem().withName("Insert new dish").withIcon(FontAwesome.Icon.faw_cutlery).withIdentifier(2),
                    new PrimaryDrawerItem().withName("Show yours dishes").withIcon(FontAwesome.Icon.faw_birthday_cake).withIdentifier(3)

            )

            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                    if (drawerItem != null) {
                        Intent intent = null;
                        if (drawerItem.getIdentifier() == 1) {
                              intent = new Intent(ShowSingleCookerInfo.this, InsertCoockerInfo.class);

                            intent.putExtra("USER-ID", user.getUid());

                            startActivity(intent);

                        } else if (drawerItem.getIdentifier() == 2) {


                            intent = new Intent(ShowSingleCookerInfo.this, InsertCookerDishInfo.class);
                            intent.putExtra("USER ID", user.getUid());
                            startActivity(intent);


                        } else if (drawerItem.getIdentifier() == 3) {


                            intent = new Intent(ShowSingleCookerInfo.this, ShowGridCookerDishes.class);
                            intent.putExtra("USER ID", user.getUid());
                            startActivity(intent);
                        }
                    }

                    return false;
                }
            })

            .build();


}



 public void createCookerClientDrawer(){



     myDrawer = new DrawerBuilder()
             .withActivity(ShowSingleCookerInfo.this)
         .withToolbar(toolbar)
             .addDrawerItems(

                     new PrimaryDrawerItem().withName("Show Cooker dishes").withIcon(FontAwesome.Icon.faw_birthday_cake).withIdentifier(1)

             )

             .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                 @Override
                 public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                     if (drawerItem != null) {
                         Intent intent = null;
                         if (drawerItem.getIdentifier() == 1) {


                             intent = new Intent(ShowSingleCookerInfo.this, ShowGridCookerDishes.class);
                              intent.putExtra("COOKER ID", cooker_id);


                             startActivity(intent);
                         }
                     }

                     return false;
                 }
             })

             .build();



 }


    /**
     * small helper method to reuse the logic to build the AccountHeader
     * this will be used to replace the header of the drawer with a compact/normal header
     *
     * @param compact

     */

    private void buildHeader(boolean compact) {
        // Create the AccountHeader
        Header = new AccountHeaderBuilder()
                .withActivity(ShowSingleCookerInfo.this)
                .withHeaderBackground(com.homecooked.karim.homecooked.R.drawable.background)
                .addProfiles(
                        profile
                )

                .build();
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);

/*
        //getDatabaseInfoForCooker();

        buildHeader(false);
        createDrawer();
*/
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.homecooked.karim.homecooked.R.menu.my_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case com.homecooked.karim.homecooked.R.id.change_email:
                createInputDialog( "Change the old E-mail");

                break;

            case com.homecooked.karim.homecooked.R.id.change_password:
                createInputDialog( "Change the old Password");
                break;

            case com.homecooked.karim.homecooked.R.id.sign_out:
                signOut();
                break;
        }
        return true;
    }


    public void  createInputDialog(final String title ){

        EditTextNewINFO = new EditText(this);



        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(EditTextNewINFO)

                .setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                          if(title.equals("Change the old E-mail"))
                          {
                              progressBar.setVisibility(View.VISIBLE);
                              if (user != null && !EditTextNewINFO.getText().toString().trim().equals("")) {
                                  user.updateEmail(EditTextNewINFO.getText().toString().trim())
                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {
                                                  if (task.isSuccessful()) {
                                                      Toast.makeText(ShowSingleCookerInfo.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                                      signOut();
                                                      progressBar.setVisibility(View.GONE);
                                                  } else {
                                                      Toast.makeText(ShowSingleCookerInfo.this, "Failed to update email!", Toast.LENGTH_LONG).show();

                                                      progressBar.setVisibility(View.GONE);
                                                  }
                                              }
                                          });
                              } else if (EditTextNewINFO.getText().toString().trim().equals("")) {
                                 Toast.makeText(ShowSingleCookerInfo.this,"Enter your E-mail",Toast.LENGTH_LONG).show();
                                  progressBar.setVisibility(View.GONE);
                              }
                          }





                          else if (title.equals("Change the old Password")){




                            progressBar.setVisibility(View.VISIBLE);
                            if (user != null && !EditTextNewINFO.getText().toString().trim().equals("")) {
                                if (EditTextNewINFO.getText().toString().trim().length() < 6) {
                                    EditTextNewINFO.setError("Password too short, enter minimum 6 characters");
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    user.updatePassword(EditTextNewINFO.getText().toString().trim())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(ShowSingleCookerInfo.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                                        signOut();
                                                        progressBar.setVisibility(View.GONE);
                                                    } else {
                                                        Toast.makeText(ShowSingleCookerInfo.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                }
                            } else if (EditTextNewINFO.getText().toString().trim().equals("")) {
                                Toast.makeText(ShowSingleCookerInfo.this,"Enter Password",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }


                        }








                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    //sign out method
    public void signOut() {


        FirebaseAuth.getInstance().signOut();

        LoginManager.getInstance().logOut();
      Intent intent = new Intent(ShowSingleCookerInfo.this,LoginActivity.class);
        startActivity(intent);
    }



    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (myDrawer != null && myDrawer.isDrawerOpen()) {
            myDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = myDrawer.saveInstanceState(outState);

        super.onSaveInstanceState(outState);
    }



    public  void  getDatabaseInfoForCookerUser() {




        // hint : we made the ID of DB equal to Authentiaction user ID to Link between DB +  Authentiaction ID
        // So if you want the info of specific user, you will use his Authentication ID which we linked it to DB


        // if the ID of DB is null - make the DB ID equal to current authentication user ID
        //hint : if you open the app without go to InsertActivity - InsertIDtoDb will be always = null, only in one condition InsertIDtoDb will take value if u used the insert activity
        // so we give InsertedIDtoDB value of user Authentication ID , to we can retrieve the data without go to Insert activity
        if (InsertedIDtoDB == null) {

            InsertedIDtoDB = user.getUid();

        }


        // we can call all the DB by this code  " mDatabaseRef.addValueEventListener() "
        // we retrieve specific child(DB ID) to get it's INFO " mDatabaseRef.child(InsertedIDtoDB).addValueEventListener"


        mDatabaseRef.child(InsertedIDtoDB).addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //VIP we must check if there is already ID in the DB , WHY ! to sure there is data in the DB
                // so if you sign up ( new User ) with no Info , and you retrieve Info from DB which is not exist, the APP will crash
                // so you must make sure there is Data to retrieve for  users who added their Info before or it will crash
                //so we check if (ID) inserted to DB to retrieve info (old USER)
                // if (ID) not inserted to DB Don't retrieve Info ( new User )
                if (dataSnapshot.exists()) {

                    progressDialog.setMessage("Loading data ....");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    // get the all values of specific child in with Datasnapshot.child() , WHY use this way and not get all the data !
                    // because if we get all the data like this "   dataSnapshot.getValue() " # the Cooker class won't able understand the Dishfood which added
                    // so we retrieve only the childs which we want and not retrieve the dishfoods which Cooker class can't deal with it

                     String n = dataSnapshot.child("name").getValue(String.class);
                    String p = dataSnapshot.child("phone").getValue(String.class);
                    String av = dataSnapshot.child("available").getValue(String.class);
                    String ab = dataSnapshot.child("about").getValue(String.class);
                    String url = dataSnapshot.child("url").getValue(String.class);
                    String adr = dataSnapshot.child("address").getValue(String.class);


                    Glide.with(ShowSingleCookerInfo.this).load(url).into(cookerphoto);


                    if (n == null) {
                        cookername.setText("Null");
                    } else cookername.setText("Chef."+ n);
                    toolbar.setTitle("Chef."+ n);


                    if (p == null) {
                        cookerphone.setText("Null");
                    } else cookerphone.setText(p);


                    if (av == null) {
                        cookeravailabilty.setText("Null");
                    } else cookeravailabilty.setText(av);


                    if (ab == null) {
                        cookerabout.setText("Null");
                    } else cookerabout.setText(ab);



                    if (adr == null) {
                        cookeraddress.setText("Null");
                    } else cookeraddress.setText(adr);


                    // Create a few sample profile
                    profile = new ProfileDrawerItem().withName(n).withEmail(p).withIcon(url.toString());

                    // Create the AccountHeader
                    buildHeader(false);
                    createDrawer();

                     progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }


     public  void getCookerInfoForCustomer() {

         cooker_id= getIntent().getStringExtra("cooker_id");

     Query queryRef = mDatabaseRef
             .orderByChild("id")
             .equalTo(cooker_id);


     queryRef.addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(DataSnapshot dataSnapshot, String s) {



             Cooker data =dataSnapshot.getValue(Cooker.class);

             cookername.setText("Chef."+data.getName());
             toolbar.setTitle("Chef."+data.getName());

             cookerphone.setText(data.getPhone());

             cookeravailabilty.setText(data.getAvailable());

             cookerabout.setText(data.getAbout());

             cookeraddress.setText(data.getAddress());

             Glide.with(ShowSingleCookerInfo.this).load(data.getUrl()).into(cookerphoto);


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
