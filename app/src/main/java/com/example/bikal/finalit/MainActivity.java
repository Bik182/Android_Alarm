package com.example.bikal.finalit;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextClock;
import android.widget.TimePicker;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.server.*;
import com.pubnub.api.models.*;
public class MainActivity extends AppCompatActivity {

    TimePicker alarmTime;
    TextClock currentTime;
    private Pubnub mPubNub;
    public static final String PUBLISH_KEY = "pub-c-4fcc306b-dd9f-44b2-b751-eba5968df953";
    public static final String SUBSCRIBE_KEY = "sub-c-01677cd2-ef8c-11e8-86f0-9a6b1c0db2e9";
    public static final String CHANNEL = "bikal";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPubNub();


        alarmTime = findViewById(R.id.timePicker);
        currentTime = findViewById(R.id.textClock);
        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(currentTime.getText().toString().equals(alarmTime())){
                    r.play();
                    subscribe();

                }else{
                    r.stop();
                    subscribe();


                }
            }
        },0,  500);
    }





    public String alarmTime(){
        Integer alarmHours = alarmTime.getCurrentHour();
        Integer alarmMinutes = alarmTime.getCurrentMinute();

        String stringAlarmMinutes;
        if(alarmMinutes<10){
            stringAlarmMinutes = "0";
            stringAlarmMinutes = stringAlarmMinutes.concat(alarmMinutes.toString());
        }else {
            stringAlarmMinutes = alarmMinutes.toString();
        }
        String alarmtimeString;

            if(alarmHours>12){
                alarmHours = alarmHours-12;
                alarmtimeString = alarmHours.toString().concat(":").concat(stringAlarmMinutes.concat(" PM"));
            }else{
                alarmtimeString = alarmHours.toString().concat(":").concat(stringAlarmMinutes.toString().concat(" AM"));
            }


        return alarmtimeString;

    }




    //PubNub
    public void initPubNub(){
        this.mPubNub = new Pubnub(
                PUBLISH_KEY,
                SUBSCRIBE_KEY
        );
        this.mPubNub.setUUID("bikal");
        subscribe();
    }

    Callback callback = new Callback() {
        public void successCallback(String channel, Object response) {
            Log.d("PUBNUB",response.toString());
        }
    };

    public void subscribe(){
        try {
            this.mPubNub.subscribe(CHANNEL, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    if(currentTime.getText().toString().equals(alarmTime())){
                        mPubNub.publish("bikal", "ON",callback);
                        Log.d("PUBNUB","SUBSCRIBE : " + "bikal" + " : "
                                + "zzzzz" + " : " + "zzzz");
                    }else{
                        mPubNub.publish("bikal", "OFF",callback);
                        Log.d("PUBNUB","SUBSCRIBE : " + "bikal" + " : "
                                + "zzzwewezz" + " : " + "zwewezzz");
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    }




