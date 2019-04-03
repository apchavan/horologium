package amey.clock.cities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import amey.clock.R;
import amey.clock.WeatherFragment;

public class CitiesFragment extends Fragment implements CitiesRecyclerItemTouchHelper.CitiesRecyclerItemTouchHelperListener {

    private final static String STORED_CITY_FILE = "storedCity";
    RecyclerView cities_RecyclerView;
    CitiesDatabaseHelper citiesDatabaseHelper;
    LayoutInflater layoutInflater;
    ViewGroup viewGroup;
    Bundle bundle;
    private List<Cities> citiesList;
    private CitiesList_Adapter citiesList_adapter;
    private TextView noCitiesMessage_TextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutInflater = inflater;
        viewGroup = container;
        bundle = savedInstanceState;

        View view = inflater.inflate(R.layout.cities_fragment, container, false);

        citiesList = new ArrayList<>();
        citiesList_adapter = new CitiesList_Adapter(Objects.requireNonNull(getContext()).getApplicationContext(), citiesList);

        cities_RecyclerView = view.findViewById(R.id.cities_RecyclerView);
        noCitiesMessage_TextView = view.findViewById(R.id.noCitiesMessage_TextView);

        citiesDatabaseHelper = new CitiesDatabaseHelper(getContext());
        if (citiesDatabaseHelper.getCitiesCount() == 0)
            noCitiesMessage_TextView.setVisibility(View.VISIBLE);
        else
            noCitiesMessage_TextView.setVisibility(View.GONE);

        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(Objects.requireNonNull(getContext()).getApplicationContext());

        cities_RecyclerView.setLayoutManager(recyclerViewLayoutManager);
        cities_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        cities_RecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        cities_RecyclerView.setAdapter(citiesList_adapter);

        /*
         * The 'addOnItemTouchListener' is used for 'click' and 'long-click' events handling.
         * Class 'CitiesRecyclerTouchListener' is used to listen for 'click' and 'long-click' events,
         * but in this case only 'click' events are used.
         */
        cities_RecyclerView.addOnItemTouchListener(
                new CitiesRecyclerTouchListener(getContext(), cities_RecyclerView,
                        new CitiesRecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Cities cities = citiesList.get(position);

                                // Save the city using PreferenceManager to set it to show weather status.
                                PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString(STORED_CITY_FILE, cities.getCityName()).apply();

                                // Redirect to
                                Objects.requireNonNull(
                                        getActivity())
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new WeatherFragment())
                                        .commit();
                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                // Nothing for now... :)
                            }
                        }));



        /*
         * The 'ItemTouchHelper' anonymous object below is to capture SWIPE events only
         * & the handler class is 'CitiesRecyclerItemTouchHelper' that handles those SWIPE events.
         */

        /*
        ItemTouchHelper.SimpleCallback itemTouchHelperSimpleCallback =
                new CitiesRecyclerItemTouchHelper
                        (0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT, this);

        new ItemTouchHelper(itemTouchHelperSimpleCallback)
                .attachToRecyclerView(cities_RecyclerView);
        */
        // OR
        new ItemTouchHelper(
                new CitiesRecyclerItemTouchHelper
                        (0, ItemTouchHelper.LEFT, this))
                .attachToRecyclerView(cities_RecyclerView);

        citiesList.addAll(citiesDatabaseHelper.getAllCities());
        citiesList_adapter.notifyDataSetChanged();
        return view;

        // return inflater.inflate(R.layout.cities_fragment, container, false);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CitiesList_Adapter.MyViewHolder) {
            //String cityName = citiesList.get(viewHolder.getAdapterPosition()).getCityName();
            final Cities deletedCity = citiesList.get(viewHolder.getAdapterPosition());
            final int deleteCityIndex = viewHolder.getAdapterPosition();

            final AlertDialog.Builder builder =
                    new AlertDialog.Builder(Objects.requireNonNull(getContext()), R.style.DeleteAlarmDialogTheme);
            builder.setTitle(R.string.confirmDeleteCity_str);
            builder.setMessage(R.string.contentDeleteCity_str);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    /*
                      Check if the city is recently new added.
                      If it is, then clear the 'PreferenceManager' to avoid re-adding of deleted city in the database.
                    */
                    if (citiesList.get(deleteCityIndex).getCityName()
                            .equalsIgnoreCase(PreferenceManager.getDefaultSharedPreferences(getContext()).getString(STORED_CITY_FILE, null)))
                        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().clear().apply();

                    // Delete city from database.
                    deleteCity(deleteCityIndex);
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    citiesList_adapter.restoreCity(deletedCity, deleteCityIndex);
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CitiesFragment()).commit();
                }
            });
            builder.setCancelable(false);   // To avoid closing on back button or outside click of alert-dialog.
            builder.show();
        }   // 'if (viewHolder instanceof CitiesList_Adapter.MyViewHolder)' closed.
    }   // 'onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position)' closed.

    private void deleteCity(int position) {
        citiesDatabaseHelper.deleteCity(citiesList.get(position));
        citiesList.remove(position);
        citiesList_adapter.notifyItemRemoved(position);
        checkForNoCities();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CitiesFragment()).commit();
    }

    private void checkForNoCities() {
        if (citiesDatabaseHelper.getCitiesCount() == 0)
            noCitiesMessage_TextView.setVisibility(View.VISIBLE);
        else
            noCitiesMessage_TextView.setVisibility(View.GONE);
    }
}   // class 'CitiesFragment' closed.
