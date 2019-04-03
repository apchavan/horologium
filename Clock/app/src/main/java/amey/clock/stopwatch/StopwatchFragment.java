package amey.clock.stopwatch;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import amey.clock.R;

// https://www.android-examples.com/android-create-stopwatch-example-tutorial-in-android-studio/
// https://stackoverflow.com/questions/9280965/arrayadapter-requires-the-resource-id-to-be-a-textview-xml-problems/9282069
// RecyclerView -> https://www.androidhive.info/2016/01/android-working-with-recycler-view/
public class StopwatchFragment extends Fragment implements StopwatchRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    TextView stopwatchTextView;
    Button startBtn, pauseBtn, resetBtn, saveLapBtn, clearLapsBtn;
    RecyclerView recyclerViewLaps;
    List<StopwatchLaps> stopwatchLapsList = new ArrayList<>();
    StopwatchLaps_Adapter stopwatchLaps_adapter;

    Handler handler;
//    ListView listView;

    int seconds, minutes, milliseconds;
    long millisecondTime, startTime, timeBuff, updateTime = 0L;

    String[] listElements = new String[]{};
    List<String> listElementsArrayList;
    ArrayAdapter<String> arrayAdapter;
    Runnable runnable;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stopwatch_fragment, container, false);

        stopwatchTextView = view.findViewById(R.id.stopwatchTextView);
        startBtn = view.findViewById(R.id.startBtn);
        pauseBtn = view.findViewById(R.id.pauseBtn);
        resetBtn = view.findViewById(R.id.resetBtn);
        saveLapBtn = view.findViewById(R.id.saveLapBtn);
        clearLapsBtn = view.findViewById(R.id.clearLapsBtn);
        recyclerViewLaps = view.findViewById(R.id.recyclerViewLaps);

        stopwatchLaps_adapter = new StopwatchLaps_Adapter(stopwatchLapsList);

        /*RecyclerView.LayoutManager */
        LinearLayoutManager recyclerView_layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView_layoutManager.setStackFromEnd(true);
        recyclerView_layoutManager.setReverseLayout(true);
        recyclerViewLaps.setNestedScrollingEnabled(true);
        recyclerView_layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewLaps.setHasFixedSize(false);

        recyclerViewLaps.setLayoutManager(recyclerView_layoutManager);
        recyclerViewLaps.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLaps.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerViewLaps.setAdapter(stopwatchLaps_adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new StopwatchRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerViewLaps);
//        listView = view.findViewById(R.id.lapsListView);

        handler = new Handler();

        listElementsArrayList = new ArrayList<>(Arrays.asList(listElements));

        arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.stopwatch_fragment, R.id.stopwatchTextView, listElementsArrayList);

//        listView.setAdapter(arrayAdapter);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                resetBtn.setEnabled(false);
                startBtn.setEnabled(false);
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeBuff += millisecondTime;
                handler.removeCallbacks(runnable);
                resetBtn.setEnabled(true);
                startBtn.setEnabled(true);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                millisecondTime = startTime = timeBuff = updateTime = 0L;
                seconds = minutes = milliseconds = 0;
                stopwatchTextView.setText(R.string.stopwatch_TextView_Text);
                listElementsArrayList.clear();
                arrayAdapter.notifyDataSetChanged();
            }
        });

        saveLapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!stopwatchTextView.getText().toString().equalsIgnoreCase("0:00:000")) {

                    stopwatchLapsList.add(new StopwatchLaps(stopwatchTextView.getText().toString()));

                    recyclerViewLaps.smoothScrollToPosition(recyclerViewLaps.getAdapter().getItemCount() - 1);
                    //stopwatchLaps_adapter.notifyDataSetChanged();
                    //recyclerViewLaps.scrollToPosition(recyclerViewLaps.getAdapter().getItemCount() - 1);
                    stopwatchLaps_adapter.notifyItemInserted(stopwatchLapsList.size());


                }
            }
        });


        clearLapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stopwatchLapsList.size() != 0) {
                    stopwatchLapsList.clear();
                    stopwatchLaps_adapter.notifyDataSetChanged();
                }
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                millisecondTime = SystemClock.uptimeMillis() - startTime;
                updateTime = timeBuff + millisecondTime;
                seconds = (int) (updateTime / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                milliseconds = (int) (updateTime % 1000);

                stopwatchTextView.setText(minutes + ":" + String.format("%02d", seconds) + ":" + String.format("%03d", milliseconds));

                handler.postDelayed(this, 0);
            }
        };
        return (view);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof StopwatchLaps_Adapter.MyViewHolder) {
            String removedLapName = stopwatchLapsList.get(viewHolder.getAdapterPosition()).getLap();
            final StopwatchLaps stopwatchLap = stopwatchLapsList.get(viewHolder.getAdapterPosition());
            final int stopwatchLapIndex = viewHolder.getAdapterPosition();

            stopwatchLaps_adapter.removeItem(stopwatchLapIndex);
            Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.stopwatchFragment), removedLapName + " removed.", Snackbar.LENGTH_LONG);
            snackbar.setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopwatchLaps_adapter.restoreItem(stopwatchLap, stopwatchLapIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
