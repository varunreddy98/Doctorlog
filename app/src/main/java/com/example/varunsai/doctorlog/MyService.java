package com.example.varunsai.doctorlog;
import android.Manifest.permission;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.example.varunsai.doctorlog.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sac.speech.GoogleVoiceTypingDisabledException;
import com.sac.speech.Speech;
import com.sac.speech.SpeechDelegate;
import com.sac.speech.SpeechRecognitionNotAvailable;
import com.sac.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
public class MyService extends Service implements SpeechDelegate, Speech.stopDueToDelay {
    DatabaseReference db;
    static  int c=1;
    ArrayList<String> arrayList = new ArrayList<>();
    private TextToSpeech tts;
    int id;
    String speak;
    public static SpeechDelegate delegate;
    @Override
    public void onCreate()
    {          Toast.makeText(this,"service created " ,Toast.LENGTH_LONG ).show();
        SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("dd-MM-yyyy");
        Date date=new Date();
        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String previously=prefs1.getString("key","null" );
        String clinickey=prefs1.getString("clinickey","null" );
        db = FirebaseDatabase.getInstance().getReference("doctor").child(previously).child("clinics").child(clinickey).child("appointments").child(simpleDateFormat1.format(date).toString());
        Toast.makeText(this,"created" ,Toast.LENGTH_LONG ).show();
        // Speech.getInstance().setPreferOffline(true);
        //tts = new TextToSpeech(MyService.this, MyService.this);
        //Speech.getInstance()
        arrayList.add("next");
        arrayList.add("remove");
        arrayList.add("stop");
    }
    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {

        //TODO do something useful
        Toast.makeText(this,"service " ,Toast.LENGTH_LONG ).show();

        try {

            if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {

                ((AudioManager) Objects.requireNonNull(

                        getSystemService(Context.AUDIO_SERVICE))).setStreamMute(AudioManager.STREAM_SYSTEM, true);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }



        Speech.init(this);

        delegate = this;

        Speech.getInstance().setListener(this);



        if (Speech.getInstance().isListening()) {

            Speech.getInstance().stopListening();

            muteBeepSoundOfRecorder();

        } else {

            System.setProperty("rx.unsafe-disable", "True");

            // RxPermissions.getInstance(this).request(permission.RECORD_AUDIO).subscribe(granted -> {
            boolean ch=true;
            if (ch) { // Always true pre-M

                try {

                    Speech.getInstance().stopTextToSpeech();

                    Speech.getInstance().startListening(null, this);

                } catch (SpeechRecognitionNotAvailable exc) {

                    //showSpeechNotSupportedDialog();



                } catch (GoogleVoiceTypingDisabledException exc) {

                    //showEnableGoogleVoiceTyping();

                }

            } else {

                Toast.makeText(this, "disabled", Toast.LENGTH_LONG).show();

            }

            //  });

            muteBeepSoundOfRecorder();

        }

        return Service.START_STICKY;

    }



    @Override

    public IBinder onBind(Intent intent) {

        //TODO for communication return IBinder implementation

        return null;

    }



    @Override

    public void onStartOfSpeech() {

    }



    @Override

    public void onSpeechRmsChanged(float value) {



    }



    @Override

    public void onSpeechPartialResults(List<String> results) {

        for (String partial : results) {
            //  Toast.makeText(MyService.this,partial ,Toast.LENGTH_LONG ).show();
            Log.d("Result", partial+"");

        }

    }



    @Override

    public void onSpeechResult(String result) {

        Log.d("Result", result+"");

        if (!TextUtils.isEmpty(result)) {

            if(arrayList.contains(result)) {
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                Details details = start1.map1.get(c);
                if(result.equals("next"))
                {    AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    if (amanager != null) {
                        amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
                    }
                        String kk=details.key;
                        db.child(kk).child("status").setValue("completed");
                        db.child("doctoken").setValue(String.valueOf(c+1));
                       // db.child("doctoken").child(kk).setValue("b");
                        start1.map1.remove(c);
                        if(!start1.map1.isEmpty()) {
                            c=c+1;
                            Details details1=start1.map1.get(c);
                            Intent intent = new Intent(MyService.this, book.class);
                            intent.putExtra("pname", details1.pfname);
                            //book.name.setText(details1.pfname);
                          intent.putExtra("gender", details1.gender);
                          //  book.gender.setText(details1.gender);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            //Date date1 = new Date();
                            //long l1 = date1.getTime();
                            Date date2 = null;
                            try {
                                date2 = simpleDateFormat.parse(details1.dob);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar a = Calendar.getInstance();
                            a.setTime(date2);
                            Calendar b = Calendar.getInstance();
                            int years = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
                           intent.putExtra("age", years);
                            //book.age.setText(years);
                            intent.putExtra("tokenno", String.valueOf(c));
                            //  book.token.setText(String .valueOf(c));
                            intent.putExtra("cause", details1.cause);
                            //book.cause.setText(details1.cause);
                            intent.putExtra("phn", details1.phoneno);
                           // book.phn.setText(details1.phoneno);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                          //  db.child("status").child(kk).setValue("c");
                            db.child("status").setValue("stop");
                            Toast.makeText(MyService.this,"Appointments completed" ,Toast.LENGTH_LONG ).show();
                            Intent intent1=new Intent(MyService.this,inter.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                //amanager.setStreamMute(AudioManager.STREAM_RING, true);

                                //amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);

                            startActivity(intent1);
                            stopSelf();
                        }
                }
                else if(result.equals("remove"))
                {
                    String kk=details.key;
                    AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                    if (amanager != null) {
                        amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
                    }
                        db.child(kk).child("status").setValue("not completed");
                    start1.map1.remove(c);
                    db.child("doctoken").setValue(String.valueOf(c+1));
                   // db.child("doctoken").child(kk).setValue("d");
                    if(!start1.map1.isEmpty()) {
                        c=c+1;
                        Details details1=start1.map1.get(c);
                        Intent intent = new Intent(MyService.this, book.class);
                        intent.putExtra("pname", details1.pfname);
                        intent.putExtra("gender", details1.gender);
                       // book.name.setText(details1.pfname);
                        //book.gender.setText(details1.gender);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        //Date date1 = new Date();
                        //long l1 = date1.getTime();
                        Date date2 = null;
                        try {
                            date2 = simpleDateFormat.parse(details1.dob);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar a = Calendar.getInstance();
                        a.setTime(date2);
                        Calendar b = Calendar.getInstance();
                        int years = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
                        intent.putExtra("age", years);
                        intent.putExtra("tokenno", String.valueOf(c));
                        intent.putExtra("cause", details1.cause);
                        intent.putExtra("phn", details1.phoneno);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else
                    {  // this.stopSelf();
                       // db.child("status").child(kk).setValue("e");
                       db.child("status").setValue("stop");
                        Toast.makeText(MyService.this,"Appointments completed" ,Toast.LENGTH_LONG ).show();
                        Intent intent1=new Intent(MyService.this,inter.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent1);
                        stopSelf();
                    }
                }
                else if(result.equals("stop"))
                {  // db.child("status").child("aaa").setValue("f");
                    db.child("status").setValue("stop");
                    Toast.makeText(MyService.this,"Appointments stopped" ,Toast.LENGTH_LONG ).show();
                    Intent intent1=new Intent(MyService.this,inter.class);
                    AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                    if (amanager != null) {
                        amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
                        //amanager.setStreamMute(AudioManager.STREAM_RING, true);

                        //amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);

                    }
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    stopSelf();
                }
                AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                if (amanager != null) {
                    amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
                    //amanager.setStreamMute(AudioManager.STREAM_RING, true);

                    //amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);

                }
                //new Tell("ok",MyService.this);
                //MainActivity.tt.setText(result);
                try {
                    Thread.sleep(1000*2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }



    @Override

    public void onSpecifiedCommandPronounced(String event) {

        try {

            if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {

                ((AudioManager) Objects.requireNonNull(

                        getSystemService(Context.AUDIO_SERVICE))).setStreamMute(AudioManager.STREAM_SYSTEM, true);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        if (Speech.getInstance().isListening()) {

            muteBeepSoundOfRecorder();

            Speech.getInstance().stopListening();

        } else {

            //  RxPermissions.getInstance(this).request(permission.RECORD_AUDIO).subscribe(granted -> {
            boolean granted=true;
            if (granted) { // Always true pre-M

                try {

                    Speech.getInstance().stopTextToSpeech();

                    Speech.getInstance().startListening(null, this);

                } catch (SpeechRecognitionNotAvailable exc) {

                    //showSpeechNotSupportedDialog();



                } catch (GoogleVoiceTypingDisabledException exc) {

                    //showEnableGoogleVoiceTyping();

                }

            } else {

                Toast.makeText(this, "disabled", Toast.LENGTH_LONG).show();

            }

            //});

            muteBeepSoundOfRecorder();

        }

    }



    /**

     * Function to remove the beep sound of voice recognizer.

     */

    private void muteBeepSoundOfRecorder() {

        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (amanager != null) {

            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);

            // amanager.setStreamMute(AudioManager.STREAM_ALARM, true);

            amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);

            //amanager.setStreamMute(AudioManager.STREAM_RING, true);

            //amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);

        }

    }



    @Override

    public void onTaskRemoved(Intent rootIntent) {

        //Restarting the service if it is removed.

        PendingIntent service =

                PendingIntent.getService(getApplicationContext(), new Random().nextInt(),

                        new Intent(getApplicationContext(), MyService.class), PendingIntent.FLAG_ONE_SHOT);



        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        assert alarmManager != null;

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, service);

        super.onTaskRemoved(rootIntent);

    }

}