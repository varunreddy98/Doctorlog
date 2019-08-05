package com.example.varunsai.doctorlog;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.common.reedsolomon.GenericGF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class doc extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,android.support.v4.app.LoaderManager.LoaderCallbacks{
    private static int RESULT_LOAD_IMG = 1;
    final String TAG=this.getClass().getName();
    private Bitmap bitmap;
    static Uri url;
    static Clinic selectedclinic=null;
    static String selectedclinickey=null;
    CallbackManager callbackManager;
    int b[]=new int[10];
    static Clinic getSelectedclinic[]=new Clinic[5];
    static int count=0;
    public  DrawerLayout drawer;
     View  navHeader;
    private Uri filePath;
    private ImageView imgNavHeaderBg,imgProfile;
    NavigationView navigationView;
    static String previously;
    private TextView txtName, txtWebsite;
    private static final String urlNavHeaderBg = "https://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://www.bing.c";
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        previously=prefs1.getString("key","null" );
        callbackManager = CallbackManager.Factory.create();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
       // Bitmap bitmap=intent.getParcelableExtra("BitmapImage");
      //  BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
                String app_url = "bachivarunreddy@gmail.com";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
         navHeader = navigationView.getHeaderView(0);
       txtName = (TextView) navHeader.findViewById(R.id.name1);
        //txtWebsite = (TextView) navHeader.findViewById(R.id.website);
       // imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        imgProfile.setOnClickListener(this);
       // imgProfile.setBackground(inform2.ob);
        txtName.setText(MainActivity.usname);
       txtName.setTextColor(Color.WHITE);
        //loadNavHeader();
        //txtWebsite.setText("bachivarunreddy@gmail.com");
       // txtWebsite.setTextColor(Color.WHITE);
      StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        Uri uri = null;
        storageReference.child(previously).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // TODO: handle uri
                url=uri;
                Glide.with(getApplicationContext()).load(uri)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(getApplicationContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                if (!MainActivity.userg.url.equals("null")) {
                    url = Uri.parse(MainActivity.userg.url);
                    Glide.with(getApplicationContext())
                            .load(url)
                            .crossFade()
                            .bitmapTransform(new CircleTransform(getApplicationContext()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgProfile);
                } else {
                    imgProfile.setBackgroundResource(R.drawable.com_facebook_profile_picture_blank_portrait);
                }
            }
        });
        //  navView.invalidate();
      /* read(new Firebasecallback1() {
                 @Override
                 public void onCallback(List<Clinic> list) {
                     Toast.makeText(doc.this, "msg12", Toast.LENGTH_LONG).show();
                     for (Clinic e : list) {
                         Toast.makeText(doc.this, "list", Toast.LENGTH_LONG).show();
                         doc.getSelectedclinic[doc.count] = e;
                         doc.count++;
                     }
                 }
             });                    */
        navigationView.setNavigationItemSelectedListener(this);
        addMenuItemInNavMenuDrawer();
    }


    private void addMenuItemInNavMenuDrawer() {
        Toast.makeText(this,"1" , Toast.LENGTH_LONG).show();
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        Menu submenu = menu.addSubMenu("Serving Clinics");
        Toast.makeText(this,"3" , Toast.LENGTH_LONG).show();
        for(int i=0;i<inter.getC;i++)
       {
           b[i]= i;
           submenu.add(0,i,0,inter.a[i].cname+" "+inter.a[i].starttime);
         //  Toast.makeText(this,b[i], Toast.LENGTH_LONG).show();

       }
        Toast.makeText(this,"7" , Toast.LENGTH_LONG).show();
        navigationView.invalidate();
    }
    private void loadNavHeader() {
        // name, website
        txtName.setText(MainActivity.usname);
        txtName.setTextColor(Color.WHITE);
      //  txtWebsite.setText("bachivarunreddy@gmail.com");
       // txtWebsite.setTextColor(Color.WHITE);
        // loading header background image
   /*    Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);   */

        // Loading profile image
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageReference.child(previously);
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // TODO: handle uri
              doc.url=uri;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                doc.url=null;
            }
        });

        Glide.with(this).load(doc.url)
               .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }
    boolean twice;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d(TAG, "click");
            if(twice==true)
            {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(intent.CATEGORY_HOME);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                System.exit(0);


            }
            twice=true;
            Log.d(TAG, "twice"+twice);
            Toast.makeText(this,"press back again to exit" ,Toast.LENGTH_LONG ).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    twice=false;
                    Log.d(TAG, "twice"+twice);
                }
            }, 3000);
        }
    }
    
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.profile)
        {
            Intent intent=new Intent(this, persondoc.class);
            startActivity(intent);
        }

         if (id == R.id.nav_slideshow) {
             Intent intent=new Intent(this,loc.class) ;
             startActivity(intent);

        } else if (id == R.id.logout){
                      open(navHeader);
                     }
                     /*else if(id==R.id.nav_manage)
         {
             Intent intent=new Intent(this,SettingsActivity.class);
             startActivity(intent);
         }  */
        else
         {   int i ;
             for(i=0;i<inter.getC;i++)
             {
                 if(id==b[i])
                 {   selectedclinic=inter.a[i];
                     selectedclinickey=inter.a[i].ckey;
                     Intent intent=new Intent(this,start1.class);
                     startActivity(intent);
                     break;
                 }
             }
         }
        

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMG);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadFile();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference pathReference = storageReference.child(previously);
            pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // TODO: handle uri
                    doc.url=uri;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    doc.url=null;
                }
            });

            Glide.with(this).load(doc.url)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);
           /* try {


            } catch (IOException e) {
                e.printStackTrace();
            }                     */
        }
    }
    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object o) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    public void open(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure,You wanted to LOGOUT");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //FirebaseAuth auth = FirebaseAuth.getInstance();
                       // auth.signOut();
                        if (MainActivity.sign_status == false) {
                            LoginManager.getInstance().logOut();
                            Intent intent = new Intent(doc.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            MainActivity.mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(doc.this, new OnCompleteListener<Void>() {
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // ...
                                        }
                                    });
                            MainActivity.mGoogleSignInClient.revokeAccess()
                                    .addOnCompleteListener(doc.this, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // ...
                                        }
                                    });
                            Intent intent = new Intent(doc.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(doc.this, "Logged Out", Toast.LENGTH_LONG).show();
                        }

                        splash.c=0;
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void read(final Firebasecallback1 firebasecallback){
        final List<Clinic> list=new ArrayList<Clinic>();
        list.clear();
        Toast.makeText(doc.this, "msg3",Toast.LENGTH_LONG).show();
        FirebaseDatabase.getInstance().getReference("doctor").child(previously).child("clinics").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot==null){

                   // Toast.makeText(, "msg10",Toast.LENGTH_LONG).show();

                }
                else {
                    //Toast.makeText(doc.this, "msg18",Toast.LENGTH_LONG).show();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key=snapshot.getKey();
                        ArrayList<String > strings = new ArrayList<>();
                        String t1= (String) snapshot.child("starttime").getValue();
                        String t2 = (String) snapshot.child("endtime").getValue();
                        String name = (String) snapshot.child("clinicname").getValue();
                        String fee=(String )snapshot.child("fee").getValue();
                        String avgtime=(String )snapshot.child("avgtime").getValue();
                        String clinicphn=(String )snapshot.child("clinicphn").getValue();
                        String address=    (String )snapshot.child("address").getValue();
                        String lat=(String )snapshot.child("latitude").getValue();
                        String lng=(String )snapshot.child("longitude").getValue();
                        for( DataSnapshot snapshot1:snapshot.child("workdays").getChildren())
                        {
                            strings.add((String) snapshot1.getValue());
                        }
                       // Toast.makeText(doc.this, "ms",Toast.LENGTH_LONG).show();
                        Clinic clinic = new Clinic(key,t1,t2,fee,name,clinicphn,strings,address,lat,lng);
                        list.add(clinic);

                    }

                }

                // Toast.makeText(LoginActivity.this, "msg4",Toast.LENGTH_LONG).show();
                firebasecallback.onCallback(list);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public interface Firebasecallback1{
        void onCallback(List<Clinic> list);
    }


    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = mStorageRef.child(previously);
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                          //  picup =true;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }
}

