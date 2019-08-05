package com.example.varunsai.doctorlog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class persondoc extends AppCompatActivity {
         ImageView img;
         TextView t1,t2,t3,t4,t5,t6,t7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persondoc);
        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String previously=prefs1.getString("key","null" );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        img=(ImageView)findViewById(R.id.pic);
        t1=(TextView)findViewById(R.id.name1);
        t2=(TextView)findViewById(R.id.email1);
        t3=(TextView)findViewById(R.id.spec1);
        t4=(TextView)findViewById(R.id.degree1);
        t5=(TextView)findViewById(R.id.exp1);
        t6=(TextView)findViewById(R.id.address1);
        t7=(TextView)findViewById(R.id.phn1);
       /* Glide.with(getApplicationContext()).load(doc.url)
                .into(img); */
        Glide.with(getApplicationContext()).load(doc.url)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getApplicationContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);
          t1.setText("Name: "+MainActivity.userg.username);
        t2.setText("Email: "+MainActivity.userg.mail);
        t3.setText("Specialisation: "+MainActivity.userg.spec);
        t4.setText("Degree: "+MainActivity.userg.degree);
        t5.setText("Experience:  "+MainActivity.userg.exp);
        t6.setText("Address: "+MainActivity.userg.address);
        t7.setText("Phonenumber: "+MainActivity.userg.phn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(persondoc.this,inter.class));
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
