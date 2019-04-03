package amey.clock.alarm.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import amey.clock.alarm.data.DatabaseHelper;
import amey.clock.alarm.model.Alarm;

public final class LoadAlarmsService extends IntentService {

    public static final String ALARMS_EXTRA = "alarms_extra";
    private static final String TAG = LoadAlarmsService.class.getSimpleName();
    public static final String ACTION_COMPLETE = TAG + ".ACTION_COMPLETE";

    @SuppressWarnings("unused")
    public LoadAlarmsService() {
        this(TAG);
    }

    public LoadAlarmsService(String name) {
        super(name);
    }

    public static void launchLoadAlarmsService(Context context) {
        final Intent launchLoadAlarmsServiceIntent = new Intent(context, LoadAlarmsService.class);
        context.startService(launchLoadAlarmsServiceIntent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final ArrayList<Alarm> alarms = DatabaseHelper.getInstance(this).getAlarms();

        final Intent i = new Intent(ACTION_COMPLETE);
        i.putParcelableArrayListExtra(ALARMS_EXTRA, alarms);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);

    }

}
