package amey.clock.alarm.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;

import amey.clock.R;
import amey.clock.alarm.adapter.AlarmsAdapter;
import amey.clock.alarm.model.Alarm;
import amey.clock.alarm.service.LoadAlarmsReceiver;
import amey.clock.alarm.service.LoadAlarmsService;
import amey.clock.alarm.util.AlarmUtils;
import amey.clock.alarm.view.DividerItemDecoration;
import amey.clock.alarm.view.EmptyRecyclerView;


// https://www.codingconnect.net/android-application-creates-alarm-clock/

public class AlarmFragment extends Fragment implements LoadAlarmsReceiver.OnAlarmsLoadedListener {

    private LoadAlarmsReceiver mReceiver;
    private AlarmsAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new LoadAlarmsReceiver(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_alarm, container, false);

        final EmptyRecyclerView emptyRecyclerView = v.findViewById(R.id.recycler);
        mAdapter = new AlarmsAdapter();
        emptyRecyclerView.setEmptyView(v.findViewById(R.id.empty_view));
        emptyRecyclerView.setAdapter(mAdapter);
        emptyRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        emptyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyRecyclerView.setItemAnimator(new DefaultItemAnimator());

        final FloatingActionButton addAlarm_FAB = v.findViewById(R.id.addAlarm_FAB);
        addAlarm_FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmUtils.checkAlarmPermissions(getActivity());
                final Intent intent =
                        AddEditAlarmActivity.buildAddEditAlarmActivityIntent(
                                getContext(), AddEditAlarmActivity.ADD_ALARM
                        );
                intent.putExtra("addAlarm_FAB_clicked",true);
                startActivity(intent);
            }
        });

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter(LoadAlarmsService.ACTION_COMPLETE);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(mReceiver, filter);
        LoadAlarmsService.launchLoadAlarmsService(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(mReceiver);
    }

    @Override
    public void onAlarmsLoaded(ArrayList<Alarm> alarms) {
        mAdapter.setAlarms(alarms);
    }

}
