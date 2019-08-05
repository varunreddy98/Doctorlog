package com.example.varunsai.doctorlog;
import android.content.Intent;
import android.support.v4.app.SupportActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class book extends AppCompatActivity {
    static TextView name,gender,age,phn,token,cause;
    static String name2,gender1,age1,phn2,token1,cause1;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        name=(TextView)findViewById(R.id.name1);
        gender=(TextView)findViewById(R.id.gender);
        age=(TextView)findViewById(R.id.age);
        phn=(TextView)findViewById(R.id.phn);
        token=(TextView)findViewById(R.id.token);
        cause=(TextView)findViewById(R.id.cause);
        imageView=findViewById(R.id.pic);
        Glide.with(getApplicationContext()).load(R.drawable.com_facebook_profile_picture_blank_portrait)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getApplicationContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
         Intent intent=getIntent();
        name2=intent.getStringExtra("pname");
        gender1=intent.getStringExtra("gender");
        age1=intent.getStringExtra("age");
        token1=intent.getStringExtra("tokenno");
        phn2=intent.getStringExtra("phn");
        cause1=intent.getStringExtra("cause");
        name.setText("Patient name: "+name2);
        gender.setText("Gender: "+gender1);
        age.setText("age: "+age1);
        phn.setText("Phone number: "+phn2);
        token.setText("Token no:"+token1);
        cause.setText("Cause: "+cause1);
    }
}
