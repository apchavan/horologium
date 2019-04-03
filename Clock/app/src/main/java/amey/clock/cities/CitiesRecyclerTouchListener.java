package amey.clock.cities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

// https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
// https://www.androidhive.info/2016/01/android-working-with-recycler-view/

public class CitiesRecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private ClickListener clickListener;

    CitiesRecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        //return super.onSingleTapUp(e);
                        super.onSingleTapUp(e);

                        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if(childView !=null && clickListener != null)
                            clickListener.onClick(childView, recyclerView.getChildAdapterPosition(childView));
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);
                    }
                });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(),e.getY());

        /*
         * We are not using 'onClick' here since, it will call two times 'onClick'.
         * So to avoid this & we don't using 'onLongClick' here, we used 'onLongClick'.
         * In case of both the events, i.e. 'onClick' & 'onLongClick', we've to implement 'onLongPress()' above
         * just like 'onSingleTapUp()'... :)
          */
        if(childView != null && clickListener != null && gestureDetector.onTouchEvent(e)){
            clickListener.onLongClick(childView,rv.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface ClickListener{
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
}
