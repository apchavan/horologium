package amey.clock.stopwatch;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import amey.clock.stopwatch.StopwatchLaps_Adapter;

public class StopwatchRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;


    public StopwatchRecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //super.onSelectedChanged(viewHolder, actionState);
        if(viewHolder!=null){
            final View stopwatchTextView_ConstraintLayout = ((StopwatchLaps_Adapter.MyViewHolder)viewHolder).stopwatchTextView_ConstraintLayout;
            getDefaultUIUtil().onSelected(stopwatchTextView_ConstraintLayout);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        final View stopwatchTextView_ConstraintLayout = ((StopwatchLaps_Adapter.MyViewHolder)viewHolder).stopwatchTextView_ConstraintLayout;
        getDefaultUIUtil().onDrawOver(c,recyclerView,stopwatchTextView_ConstraintLayout,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //super.clearView(recyclerView, viewHolder);
        final View stopwatchTextView_ConstraintLayout = ((StopwatchLaps_Adapter.MyViewHolder)viewHolder).stopwatchTextView_ConstraintLayout;
        getDefaultUIUtil().clearView(stopwatchTextView_ConstraintLayout);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        final View stopwatchTextView_ConstraintLayout = ((StopwatchLaps_Adapter.MyViewHolder) viewHolder).stopwatchTextView_ConstraintLayout;
        getDefaultUIUtil().onDraw(c,recyclerView,stopwatchTextView_ConstraintLayout,dX,dY,actionState,isCurrentlyActive);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
