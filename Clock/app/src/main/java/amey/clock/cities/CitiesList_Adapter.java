package amey.clock.cities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import amey.clock.R;

public class CitiesList_Adapter extends RecyclerView.Adapter<CitiesList_Adapter.MyViewHolder> {

    private Context context;
    private List<Cities> citiesList;

    public CitiesList_Adapter(Context context, List<Cities> citiesList) {
        this.context = context;
        this.citiesList = citiesList;
    }

    @NonNull
    @Override
    public CitiesList_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cities_list_design,parent,false);
        return new MyViewHolder(view);
        */
        return new MyViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.cities_list_design, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesList_Adapter.MyViewHolder holder, int position) {
        final Cities city = citiesList.get(position);
        holder.cityName_TextView.setText(city.getCityName());
        holder.countryName_TextView.setText(city.getCountryName());
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    public void removeCity(int position) {
        citiesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreCity(Cities cities, int position) {
        citiesList.add(position, cities);
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cityName_TextView, countryName_TextView;
        public ConstraintLayout designCitiesForeground_ConstraintLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            cityName_TextView = itemView.findViewById(R.id.cityName_TextView);
            countryName_TextView = itemView.findViewById(R.id.countryName_TextView);
            designCitiesForeground_ConstraintLayout = itemView.findViewById(R.id.designCitiesForeground_ConstraintLayout);
        }   // 'MyViewHolder(View itemView)' closed.
    }   // class 'MyViewHolder' closed.
}
