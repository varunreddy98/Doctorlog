package  com.example.varunsai.doctorlog;
import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

public class splash extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 10000;
    public static SharedPreferences prefs;
    static int c=-1;
    static boolean check=false;
    FirebaseOptions firebaseOptions;
    DatabaseReference db;
    String previously;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
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
                prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean previouslyStarted = prefs.getBoolean("isFirstRun", false);
                if(!(previouslyStarted)||c==0) {
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean("isFirstRun", Boolean.TRUE);
                    edit.commit();
                      Intent mainIntent = new Intent(splash.this,MainActivity.class);
                    splash.this.startActivity(mainIntent);
                    splash.this.finish();
                }

                else {
                    db=FirebaseDatabase.getInstance().getReference("doctor");
                    SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    previously = prefs1.getString("key", "null");
                    //Toast.makeText(getBaseContext(),previously ,Toast.LENGTH_LONG ).show();
                   read(new MainActivity.Firebasecallback() {
                        @Override
                        public void onCallback(List<user> list) {
                            // Toast.makeText(getActivity(), "msg12",Toast.LENGTH_LONG).show();
                            for (user e:list)
                            {          // Toast.makeText(LoginActivity.this, "list",Toast.LENGTH_LONG).show();
                                if(previously.equals(e.uid))
                                {      //  Toast.makeText(splash.this, "msg5",Toast.LENGTH_LONG).show();
                                    MainActivity.userg=new user(e.uid,e.username,e.mail,e.url,e.fname,e.lname,e.spec,e.phn,e.exp,e.degree,e.address);
                                    MainActivity.name=e.username;
                                    MainActivity.usname=e.mail;
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                    SharedPreferences.Editor edit = preferences.edit();
                                    edit.putString("key",e.uid);
                                    edit.commit();
                                    break;

                                }
                            }

                            check=true;
                            //  Toast.makeText(splash.this, "mmmm", Toast.LENGTH_SHORT).show();
                            //LoginActivity.usname=null;
                            Intent mainIntent = new Intent(splash.this, inter.class);
                            splash.this.startActivity(mainIntent);
                            splash.this.finish();

                      }

                    });


                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }


    public void read(final MainActivity.Firebasecallback firebasecallback){
        final List<user> list=new ArrayList<user>();
        list.clear();
        // Toast.makeText(LoginActivity.this, "msg3",Toast.LENGTH_LONG).show();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot==null){

                    //Toast.makeText(LoginActivity.this, "msg10",Toast.LENGTH_LONG).show();

                }
                else {
                    //  Toast.makeText(LoginActivity.this, "msg18",Toast.LENGTH_LONG).show();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Toast.makeText(splash.this, "msg",Toast.LENGTH_LONG).show();
                        String key=snapshot.getKey();
                        String mail = (String) snapshot.child("mail").getValue();
                        String url = (String) snapshot.child("url").getValue();
                        String name=(String )snapshot.child("username").getValue();
                        String fname = (String) snapshot.child("fname").getValue();
                        String lname = (String) snapshot.child("lname").getValue();
                        String spec = (String) snapshot.child("specialisation").getValue();
                        String phn = (String) snapshot.child("phn").getValue();
                        String exp = (String) snapshot.child("experience").getValue();
                        String degree = (String) snapshot.child("degree").getValue();
                        String address= (String) snapshot.child("address").getValue();
                        user user1 = new user(key,name, mail, url,fname,lname,spec,phn,exp,degree,address);
                        list.add(user1);
                        //  Toast.makeText(LoginActivity.this, "msg20",Toast.LENGTH_LONG).show();

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

}