package amey.clock.cities;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

//https://www.androidhive.info/2017/09/android-recyclerview-swipe-delete-undo-using-itemtouchhelper/?utm_source=recyclerview&utm_medium=site&utm_campaign=refer_article

/**
 * NOTE :->
 *      This class is only for SWIPE events that may occur in recycler-view of 'Cities' fragment
 *      SWIPE events are only for 'DELETE' the items from list as well as database in this case.
 */

public class CitiesRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private CitiesRecyclerItemTouchHelperListener listener;

    /*
      Creates a Callback for the given drag and swipe allowance. These values serve as
      defaults
      and if you want to customize behavior per ViewHolder, you can override
      {@link #getSwipeDirs(RecyclerView, ViewHolder)}
      and / or {@link #getDragDirs(RecyclerView, ViewHolder)}.

      @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
                       composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
                       #END},
                       {@link #UP} and {@link #DOWN}.
      @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
                       composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
                       #END},
                       {@link #UP} and {@link #DOWN}.
    */

    CitiesRecyclerItemTouchHelper(int dragDirs, int swipeDirs, CitiesRecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
        // return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // super.onSelectedChanged(viewHolder, actionState);
        if (viewHolder != null) {
            /*
            final View foregroundView = ((CitiesList_Adapter.MyViewHolder)viewHolder).designCitiesForeground_ConstraintLayout;
            getDefaultUIUtil().onSelected(foregroundView);
            */
            getDefaultUIUtil()
                    .onSelected(
                            ((CitiesList_Adapter.MyViewHolder) viewHolder).designCitiesForeground_ConstraintLayout
                    );
        }   // 'if(viewHolder != null)' closed.
    }   // 'onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState)' closed.

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder
            , float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        getDefaultUIUtil().onDrawOver(c, recyclerView,
                ((CitiesList_Adapter.MyViewHolder) viewHolder).designCitiesForeground_ConstraintLayout,
                dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // super.clearView(recyclerView, viewHolder);
        getDefaultUIUtil().clearView(((CitiesList_Adapter.MyViewHolder) viewHolder).designCitiesForeground_ConstraintLayout);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder
            , float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        getDefaultUIUtil().onDraw(c, recyclerView,
                ((CitiesList_Adapter.MyViewHolder) viewHolder).designCitiesForeground_ConstraintLayout,
                dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder,direction, viewHolder.getAdapterPosition());
    }

    public interface CitiesRecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
