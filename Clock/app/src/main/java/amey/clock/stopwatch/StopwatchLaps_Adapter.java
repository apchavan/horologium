package amey.clock.stopwatch;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import amey.clock.R;

public class StopwatchLaps_Adapter extends RecyclerView.Adapter<StopwatchLaps_Adapter.MyViewHolder> {
    private List<StopwatchLaps> stopwatchLapsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView stopwatchTextView_ForMyViewHolder;
        public ConstraintLayout stopwatchTextView_ConstraintLayout;

        public MyViewHolder(View view) {
            super(view);
            stopwatchTextView_ForMyViewHolder = view.findViewById(R.id.stopwatchTextView_ForMyViewHolder);
            stopwatchTextView_ConstraintLayout = view.findViewById(R.id.stopwatchTextView_ConstraintLayout);
        }
    }

    public StopwatchLaps_Adapter(List<StopwatchLaps> stopwatchLapsList) {
        this.stopwatchLapsList = stopwatchLapsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stopwatch_lap_design, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StopwatchLaps stopwatchLaps = stopwatchLapsList.get(position);
        holder.stopwatchTextView_ForMyViewHolder.setText(stopwatchLaps.getLap());
    }

    @Override
    public int getItemCount() {
        return stopwatchLapsList.size();
    }

    public void removeItem(int position) {
        stopwatchLapsList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(StopwatchLaps stopwatchLaps, int position) {
        stopwatchLapsList.add(position, stopwatchLaps);
        notifyItemInserted(position);
    }
}
