package com.example.varunsai.doctorlog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class loc extends AppCompatActivity implements View.OnClickListener {
    private TimePicker timePicker1;
    private TextView time1;
    private TextView time2;
    int am;
    static boolean sundaywork=false;
    static String id1;
    static String fname,t1,t2,fee,avg,phn;
    private Calendar calendar;
    static ArrayList<String> arrayList=new ArrayList<>();
    int sp,sp1=0;
    static String clinickey;
    Button b1,b2;
    private String format = "",previously;
    DatabaseReference db;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button button;
    AlertDialog.Builder alertdialogbuilder;
    String[] AlertDialogItems = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    List<String> ItemsIntoList;
    boolean[] Selectedtruefalse = new boolean[]{false, false, false, false, false,false,false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loc);
        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        previously=prefs1.getString("key","null" );
        db= FirebaseDatabase.getInstance().getReference("doctor").child(previously).child("clinics");
        Button next=(Button)findViewById(R.id.next2);
        next.setOnClickListener(this);
        time1=(TextView)findViewById(R.id.texttime1);
        time2=(TextView)findViewById(R.id.texttime2);
        b1=(Button)findViewById(R.id.timepic1);
        b2=(Button)findViewById(R.id.timepic2);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        button = (Button)findViewById(R.id.holiday);
        if(!start1.clinic_name.equals("null"))
              ((EditText)findViewById(R.id.fname)).setText(start1.clinic_name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp1++;
                alertdialogbuilder = new AlertDialog.Builder(loc.this);

                ItemsIntoList = Arrays.asList(AlertDialogItems);

                alertdialogbuilder.setMultiChoiceItems(AlertDialogItems, Selectedtruefalse, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked)
                        {
                            arrayList.add(AlertDialogItems[which]);
                        }
                        else if(arrayList.contains(AlertDialogItems[which]))
                        {
                               arrayList.remove(AlertDialogItems[which]);
                        }

                    }
                });

                alertdialogbuilder.setCancelable(false);

                alertdialogbuilder.setTitle("Select Working Days");

                alertdialogbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int a = 0;
                        while(a < Selectedtruefalse.length)
                        {
                            boolean value = Selectedtruefalse[a];
                            
                            a++;
                        }

                    }
                });

                alertdialogbuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = alertdialogbuilder.create();

                dialog.show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
          switch (id){

              case R.id.timepic1:
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            time1.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
            break;

              case R.id.timepic2:
                  // Get Current Time
                  final Calendar ch = Calendar.getInstance();
                  mHour = ch.get(Calendar.HOUR_OF_DAY);
                  mMinute = ch.get(Calendar.MINUTE);
                  //am=ch.get(Calendar.AM_PM);
                  // Launch Time Picker Dialog
                  TimePickerDialog timePickerDialog1 = new TimePickerDialog(this,
                          new TimePickerDialog.OnTimeSetListener() {

                              @Override
                              public void onTimeSet(TimePicker view, int hourOfDay,
                                                    int minute) {

                                  time2.setText(hourOfDay + ":" + minute);
                              }
                          }, mHour, mMinute, false);
                  timePickerDialog1.show();
                  break;

              case R.id.next2:
                  sp=0;
                  fname= String.valueOf(((EditText)findViewById(R.id.fname)).getText());
                  if(fname.equals(""))
                  {
                      EditText editText3= (EditText) findViewById(R.id.fname);
                      editText3.setError("This field is necessary");
                      sp++;
                  }
                  if(!start1.clinic_name.equals("null"))
                  {

                                  if(!fname.equals(start1.clinic_name))
                                  {
                                        sp++;
                                        Toast.makeText(loc.this,"clinic name should be same" ,Toast.LENGTH_LONG ).show();
                                  }

                  }
                  t1=  String.valueOf(((EditText)findViewById(R.id.texttime1)).getText());
                  if(t1.equals(""))
                  {
                      EditText editText4 = (EditText) findViewById(R.id.texttime1);
                      editText4.setError("This field is necessary");
                      sp++;
                  }
                  t2=String.valueOf(((EditText)findViewById(R.id.texttime2)).getText());
                  if(t2.equals(""))
                  {
                      EditText e = (EditText) findViewById(R.id.texttime2);
                      e.setError("This field is necessary");
                      sp++;
                  }
                  int ff=0;
                  if(sp1!=0) {
                      for (int i = 0; i < inter.getC; i++) {

                          SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                          Date d1 = null;
                          Date d2 = null;
                          Date d3 = null;
                          Date d4 = null;
                          try {
                              d1 = sdf.parse(inter.a[i].starttime);
                              d2 = sdf.parse(inter.a[i].endtime);
                              d3 = sdf.parse(t1);
                              d4 = sdf.parse(t2);
                          } catch (ParseException e) {
                              e.printStackTrace();
                          }
                          for (String str : inter.a[i].days) {

                              if (arrayList.contains(str) && !((d4.getTime() - d1.getTime()) < 0 || (d2.getTime() - d3.getTime() < 0))) {
                                  sp++;
                                  ff++;
                                  Toast.makeText(loc.this, "U already registered with this time", Toast.LENGTH_LONG).show();
                                  break;
                              }
                          }

                          if (ff != 0)
                              break;
                      }
                  }
                  fee=String.valueOf(((EditText)findViewById(R.id.fee)).getText());
                  if(fee.equals(""))
                  {
                      EditText editText1 = (EditText) findViewById(R.id.fee);
                      editText1.setError("This field is necessary");
                      sp++;
                  }
                  phn=String.valueOf(((EditText)findViewById(R.id.phn)).getText());
                  if(phn.length()!=10)
                  {
                      EditText editText1 = (EditText) findViewById(R.id.phn);
                      editText1.setError("This field is necessary");
                      sp++;
                  }

                  if((sp==0)&&(sp1!=0)) {
                      id1=db.push().getKey();
                      clinickey=id1;
                      db.child(id1).child("clinicname").setValue(fname);
                      db.child(id1).child("starttime").setValue(t1);
                      db.child(id1).child("endtime").setValue(t2);
                      db.child(id1).child("fee").setValue(fee);
                      if(!start1.clinic_name.equals("null"))
                      {   db.child(id1).child("latitude").setValue(doc.selectedclinic.lat);
                          db.child(id1).child("longitude").setValue(doc.selectedclinic.lng);
                          db.child(id1).child("address").setValue(doc.selectedclinic.address);
                      }
                     // db.child(id1).child("avgtime").setValue(avg);
                      db.child(id1).child("clinicphn").setValue(phn);
                      db.child(id1).child("workdays").setValue("--");
                      db.child(id1).child("appointments").setValue("--");
                      DatabaseReference mref= db= FirebaseDatabase.getInstance().getReference("doctor").child(previously).child("clinics").child(id1).child("workdays");
                      for(String e:arrayList)
                      {
                          mref.child(e).setValue("yes");
                      }
                      if(arrayList.contains("Sunday"))
                      {
                          sundaywork=true;
                      }
                      //   Toast.makeText(inform.this,"map" ,Toast.LENGTH_LONG ).show();
                      Intent intent = new Intent(getApplicationContext(), avg_wait.class);
                      startActivity(intent);
                  }
                  break;


        }
    }


}

