package app.mego.bluetoothsend;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

class Constants {

    // values have to be globally unique
    static final String INTENT_ACTION_DISCONNECT = BuildConfig.APPLICATION_ID + ".Disconnect";
    static final String NOTIFICATION_CHANNEL = BuildConfig.APPLICATION_ID + ".Channel";
    static final String INTENT_CLASS_MAIN_ACTIVITY = BuildConfig.APPLICATION_ID + ".MainActivity";

    // values have to be unique within each app
    static final int NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001;
    static ArrayList<Float> ph=new ArrayList<>();
    static ArrayList<Float> RT=new ArrayList<>();
    static ArrayList<Float> wt=new ArrayList<>();
    static ArrayList<Float> WL=new ArrayList<>();
    static String USERID=null;



    private Constants() {}
}
