package com.example.varunsai.doctorlog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class avg_wait extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    Spinner s1,s2;
    String previously;
    int sp = 0;
    String time1[]={"3","5","7","10","12","15","20"};
    String maxpat[]={"10","20","30","40","50","60"};
    boolean holidays=false;
    String times="",counts= "";
    RadioButton r1,r2;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avg_wait);
        s1=(Spinner)findViewById(R.id.specs2);
        s2=(Spinner)findViewById(R.id.specs3);
        r1=(RadioButton)findViewById(R.id.radio1);
        r2=(RadioButton)findViewById(R.id.radio2);
        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        previously=prefs1.getString("key","null" );
        db= FirebaseDatabase.getInstance().getReference("doctor").child(previously).child("clinics");
        ArrayAdapter tt= new ArrayAdapter(this,android.R.layout.simple_spinner_item,time1);
        tt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(tt);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,maxpat);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(aa);
        s1.setOnItemSelectedListener(this);
        s2.setOnItemSelectedListener(this);
        Button b1=(Button)findViewById(R.id.nextavg);
        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        sp=0;
        if(times.equals(""))
        {   Toast.makeText(this, "select ", Toast.LENGTH_LONG).show();
            sp++;
        }
        if(counts.equals(""))
        {   Toast.makeText(this, "select specialisation", Toast.LENGTH_LONG).show();
            sp++;
        }
        if(sp==0) {
            db.child(loc.id1).child("maxpatient").setValue(counts.toString());
            db.child(loc.id1).child("avgtime").setValue(times.toString());
            db.child(loc.id1).child("publicholidays").setValue(String .valueOf(holidays));
            if(!start1.clinic_name.equals("null"))
            {         start1.clinic_name="null";
                      Intent intent1=new Intent(this,inter.class);
                      startActivity(intent1);
            }
            else {
                Intent intent = new Intent(this, loc2.class);
                startActivity(intent);
            }
        }
        
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch(adapterView.getId()){
            case R.id.specs2:
                 times= time1[i];
                 break;

            case R.id.specs3:
                counts=maxpat[i];
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio1:
                if (checked)
                    r1.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                holidays=true;
               // Log.d("name", "hhh");
                break;
            case R.id.radio2:
                if (checked)
                    r1.setBackgroundColor(getResources().getColor(R.color.btn_logut_bg));
                holidays=false;
              //  Log.d("ff", "ggggg");
                break;


        }
    }

}
