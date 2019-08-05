package com.example.varunsai.doctorlog;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import  java.util.*;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.client.result.BookmarkDoCoMoResultParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
public class start1 extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener{
    private MapView mMapView;
    private GoogleMap mMap;
    ProgressDialog progressDialog;
    private Button button;
    static String clinic_name="null";
    double lat,lng;
    SupportMapFragment mapFragment;
    String value[]={"10","15","20","30","45"};
    static TreeMap<Integer,Details> map1=new TreeMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        setContentView(R.layout.activity_start1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(start1.this,inter.class));
            }
        });
        lat=Double.parseDouble(doc.selectedclinic.lat);
        lng=Double.parseDouble(doc.selectedclinic.lng);
        //  navigationView.setCheckedItem(R.id.nav_);
        button = (Button) findViewById(R.id.start);
        button.setOnClickListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        ((TextView) findViewById(R.id.text_view2)).setText("Visiting hours:"+doc.selectedclinic.starttime+"  to  "+doc.selectedclinic.endtime );
        ((TextView) findViewById(R.id.text_view3)).setText("Consulation fee:"+doc.selectedclinic.fee );
        ((TextView) findViewById(R.id.text_view4)).setText(doc.selectedclinic.address);
        ((TextView) findViewById(R.id.text_view5)).setText(doc.selectedclinic.phone1);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      //  Button fab=(Button)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(start1.this,loc.class);
                clinic_name=doc.selectedclinic.cname;
                startActivity(intent1);
            }
        });
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title("location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),16));

    }

    @Override
    public void onClick(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure,You want to start appointments");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
                        if(networkInfo != null && networkInfo.isConnected()==true )
                        {
                           // Toast.makeText(start1.this, "Network Available", Toast.LENGTH_LONG).show();
                            starter();

                        }
                        else {
                            Toast.makeText(start1.this, "Network Not Available", Toast.LENGTH_LONG).show();
                        }

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

    private void starter() {
        // intent.putExtra("doc", mItem.name);
       progressDialog = new ProgressDialog(start1.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
      /*  final SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String previously=prefs1.getString("key","null" );
       // Toast.makeText(start1.this, previously,Toast.LENGTH_LONG ).show();
        //progressDialog.dismiss();
        String pattern2= "dd-MM-yyyy";
        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(pattern2);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("doctor").child(previously).child("clinics").child(doc.selectedclinickey).child("appointments").child(simpleDateFormat2.format(date1).toString());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for(DataSnapshot d:snapshot.getChildren())
                {   String mmt=(String)d.getKey();
                    if((!mmt.equals("doctoken")&&(!mmt.equals("status")&&(!mmt.equals("delay"))))) {
                        // Toast.makeText(book.this,"kkkk", Toast.LENGTH_LONG).show();
                        String pname=(String)d.child("plname").getValue();
                        String dob=(String)d.child("dob").getValue();
                        String booktime=(String )d.child("booktime").getValue();
                        String  cause=(String)d.child("cause").getValue();
                        String gender=(String )d.child("gender").getValue();
                        String tokenno=(String)d.child("tokenno").getValue();
                        String phn=(String )d.child("phoneno").getValue();
                        String lat=(String)d.child("lat").getValue();
                        String lng=(String )d.child("lng").getValue();
                        String estimatedtime=(String)d.child("estimatedtime").getValue();
                        list.add(new Details(pname,gender,tokenno,dob,cause,phn,lat,lng));
                    }
                }
                progressDialog.dismiss();
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });            */
        read(new Firebasecallback() {
            @Override
            public void onCallback(TreeMap<Integer,Details> map) {
               map1=map;
                Intent intent=new Intent(start1.this, book.class);
                 Toast.makeText(start1.this, "msg12",Toast.LENGTH_LONG).show();
                String pattern1 = "dd-MM-yyyy";
                Date date = new Date();
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern1);
                SharedPreferences prefs3 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String previously2 = prefs3.getString("key", "null");
                //Toast.makeText(start1.this,simpleDateFormat1.format(date),Toast.LENGTH_LONG ).show();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("doctor").child(previously2).child("clinics").child(doc.selectedclinickey).child("appointments").child(simpleDateFormat1.format(date).toString());
                //Toast.makeText(start1.this,selectedText ,Toast.LENGTH_LONG ).show();
                db.child("status").setValue("true");
                db.child("doctoken").setValue("1");
                if(map.isEmpty())
                {
                    Toast.makeText(start1.this,"No Appointments Yet" ,Toast.LENGTH_LONG ).show();
                }
                else {
                    Details details = map.get(1);
                   intent.putExtra("pname",details.pfname );
                    intent.putExtra("gender",details.gender );
                  //  Toast.makeText(start1.this,details.pfname ,Toast.LENGTH_LONG ).show();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
                    Date date1=new Date();
                    long l1=date1.getTime();
                    Date date2 = null;
                    try {
                         date2=simpleDateFormat.parse(details.dob);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar a=Calendar.getInstance();
                    a.setTime(date1);
                    Calendar b=Calendar.getInstance();
                    int years=b.get(Calendar.YEAR)-a.get(Calendar.YEAR);
                    intent.putExtra("age",String .valueOf(years));
                    intent.putExtra("tokenno", String .valueOf(1));
                    intent.putExtra("cause",details.cause );
                    intent.putExtra("phn",details.phoneno );
                    SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor edit = prefs1.edit();
                    edit.putString("clinickey",doc.selectedclinickey.toString());
                    edit.commit();
                    startService(new Intent(start1.this, MyService.class));
                   startActivity(intent);
                }
            }    
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_start, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_delay:
                String p1[]=doc.selectedclinic.starttime.split("\\s");
                String pattern3= "HH:mm";
                String pattern4="hh:mma";
                Date dates=null;
                SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(pattern3);
                SimpleDateFormat simpleDateFormat4 = new SimpleDateFormat(pattern4);
                try {
                    dates= simpleDateFormat4.parse(p1[0]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String  reqs= simpleDateFormat3.format(dates).toString();
                String myTimes[]=reqs.split(":");
                int h1=Integer.parseInt(myTimes[0]);
                int m1=Integer.parseInt(myTimes[1]);
                Calendar c1=Calendar.getInstance();
                c1.setTimeInMillis(System.currentTimeMillis());
                c1.set(Calendar.HOUR_OF_DAY,h1 );
                c1.set(Calendar.MINUTE, m1);
                Calendar c2=Calendar.getInstance();
                c2.setTimeInMillis(System.currentTimeMillis());
                long mm=c1.getTimeInMillis()-c2.getTimeInMillis();
                //int hours=(int)(mm/(1000*60*60)%24);
                int mins=(int)(mm/(1000*60));
                if(mins>10) {
                    AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(start1.this);
                    alertdialogbuilder.setTitle("Select time of delay in mins");
                    alertdialogbuilder.setItems(value, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String selectedText = Arrays.asList(value).get(which);
                            String pattern1 = "dd-MM-yyyy";
                            Date date = new Date();
                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern1);
                            SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            String previously = prefs1.getString("key", "null");
                            //Toast.makeText(start1.this,simpleDateFormat1.format(date),Toast.LENGTH_LONG ).show();
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("doctor").child(previously).child("clinics").child(doc.selectedclinickey).child("appointments").child(simpleDateFormat1.format(date).toString()).child("delay");
                            //Toast.makeText(start1.this,selectedText ,Toast.LENGTH_LONG ).show();
                            db.setValue(selectedText.toString());
                        }
                    });
                    AlertDialog dialog = alertdialogbuilder.create();
                    dialog.show();
                }
                else {
                    Toast.makeText(start1.this,"Too Late to Change" ,Toast.LENGTH_LONG ).show();
                }
                return true;
            case R.id.action_settings:
                // help action
                return true;
            case R.id.action_delete:
                // check for updates action
                delete_clinic();
                return true;
            case R.id.action_stop:
                stopService(new Intent(this,MyService.class));
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void delete_clinic() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure,You wanted to delete this Clinic");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        String previously=prefs1.getString("key","null" );
                        DatabaseReference db= FirebaseDatabase.getInstance().getReference("doctor").child(previously).child("clinics").child(doc.selectedclinickey);
                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                               // Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });
                        Intent intent=new Intent(start1.this,inter.class);
                        startActivity(intent);

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

    public void read(final Firebasecallback firebasecallback){
        final TreeMap<Integer ,Details> map=new TreeMap<>();
        map.clear();
        final SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String previously=prefs1.getString("key","null" );
        // Toast.makeText(start1.this, previously,Toast.LENGTH_LONG ).show();
        //progressDialog.dismiss();
        String pattern2= "dd-MM-yyyy";
        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(pattern2);
       // Toast.makeText(start1.this,simpleDateFormat2.format(date1 ) ,Toast.LENGTH_LONG ).show();
        FirebaseDatabase.getInstance().getReference("doctor").child(previously).child("clinics").child(doc.selectedclinickey).child("appointments").child(simpleDateFormat2.format(date1)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Toast.makeText(start1.this, "sdfff",Toast.LENGTH_LONG ).show();
                if (snapshot == null) {
                    //Toast.makeText(start1.this, "tipi",Toast.LENGTH_LONG ).show();
                    }
                else {
                   for (DataSnapshot d : snapshot.getChildren()) {
                        String mmt = (String)d.getKey();
                      // Toast.makeText(start1.this,mmt.toString(), Toast.LENGTH_LONG).show();
                        if ((!mmt.equals("doctoken") && (!mmt.equals("status") && (!mmt.equals("delay"))))) {
                            // Toast.makeText(book.this,"kkkk", Toast.LENGTH_LONG).show();
                           // String key= (String)d.getKey();
                            String pname = (String) d.child("pfname").getValue();
                            String dob = (String) d.child("dob").getValue();
                            String booktime = (String) d.child("booktime").getValue();
                            String cause = (String) d.child("cause").getValue();
                            String gender = (String) d.child("gender").getValue();
                           String tokenno = (String ) d.child("tokenno").getValue();
                           // String tokenno="1";
                            String phn = (String) d.child("phoneno").getValue();
                            String lat = (String) d.child("lat").getValue();
                            String lng = (String) d.child("lng").getValue();
                            String estimatedtime = (String) d.child("estimatedtime").getValue();
                            //Toast.makeText(start1.this,"sdt" ,Toast.LENGTH_LONG ).show();
                            map.put(Integer.parseInt(tokenno),new Details(mmt,pname, gender, tokenno, dob, cause, phn, lat, lng));
                        }
                    }
                  progressDialog.dismiss();
                   // Toast.makeText(start1.this,"sdt" ,Toast.LENGTH_LONG ).show();
                }
                firebasecallback.onCallback(map);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

               progressDialog.dismiss();

            }
        });
        // Toast.makeText(LoginActivity.this, "msg3",Toast.LENGTH_LONG).show();


    }
    public interface Firebasecallback {
        void onCallback(TreeMap<Integer,Details> map);
    }


}

