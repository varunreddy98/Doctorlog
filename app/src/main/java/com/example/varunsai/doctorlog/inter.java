package  com.example.varunsai.doctorlog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class inter extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 10000;
    public static SharedPreferences prefs;
    static int c=-1;
   static Clinic a[]=new Clinic[10];
    static int getC=0;
    static boolean check=false;
    FirebaseOptions firebaseOptions;
    DatabaseReference db;
    String previously;
    private ProgressDialog progressDialog;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_inter);
        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        previously = prefs1.getString("key", "null");
        progressDialog = new ProgressDialog(inter.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                  //  db=FirebaseDatabase.getInstance().getReference("doctor");
               // Toast.makeText(inter.this,previously ,Toast.LENGTH_LONG ).show();
                   read1(new Firebasecallback3() {
                        @Override
                        public void onCallback3(List<Clinic> list) {
                            //Toast.makeText(inter.this, "msg12",Toast.LENGTH_LONG).show();
                            for (Clinic e : list) {          // Toast.makeText(LoginActivity.this, "list",Toast.LENGTH_LONG).show();
                               // Toast.makeText(inter.this, "list", Toast.LENGTH_LONG).show();
                                a[getC++]=e;
                            }
                            Intent mainIntent = new Intent(inter.this, doc.class);
                            inter.this.startActivity(mainIntent);
                            progressDialog.dismiss();
                          inter.this.finish();
                        }

                    });

            }
        }, SPLASH_DISPLAY_LENGTH);

    }



    public void read1(final Firebasecallback3 firebasecallback){
        final List<Clinic> list=new ArrayList<Clinic>();
        list.clear();
        getC=0;
         Toast.makeText(inter.this, "msg3",Toast.LENGTH_LONG).show();
        FirebaseDatabase.getInstance().getReference("doctor").child(previously).child("clinics").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot==null){

                    Toast.makeText(inter.this, "msg10",Toast.LENGTH_LONG).show();

                }
                else {
                   // Toast.makeText(inter.this, "msg18",Toast.LENGTH_LONG).show();
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
                       // Toast.makeText(inter.this, "ms",Toast.LENGTH_LONG).show();
                        String pattern = "HH:mm";
                        String pattern1="hh:mma";
                        Date date=null;
                        Date date1=null;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern1);
                        try {
                             date= simpleDateFormat.parse(t1);
                            date1=simpleDateFormat.parse(t2 );
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Clinic clinic = new Clinic(key,simpleDateFormat1.format(date).toString(),simpleDateFormat1.format(date1).toString(),fee,name,clinicphn,strings,address,lat,lng);
                        list.add(clinic);

                    }

                }

               //  Toast.makeText(inter.this, "msg4",Toast.LENGTH_LONG).show();
                firebasecallback.onCallback3(list);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public interface Firebasecallback3{
        void onCallback3(List<Clinic> list);
    }
}

