package com.jasonjohn.eattimer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jasonjohn.eattimer.db.MealRecordEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MealStatsAdapter extends RecyclerView.Adapter<MealStatsAdapter.ViewHolder> {

    private ArrayList<MealRecordEntity> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView date, time, count;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            date = view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);
            count = view.findViewById(R.id.count);
        }

        public TextView getDate() {
            return date;
        }

        public TextView getTime() {
            return time;
        }

        public TextView getCount() {
            return count;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public MealStatsAdapter(ArrayList<MealRecordEntity> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.meal_row_adapter, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        MealRecordEntity entity = localDataSet.get(position);

        SimpleDateFormat dateSdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat timeSdf = new SimpleDateFormat("hh:mm a");

        timeSdf.format(new Date(entity.time));

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getDate().setText(dateSdf.format(new Date(entity.time)));
        viewHolder.getTime().setText(timeSdf.format(new Date(entity.time)));
        viewHolder.getCount().setText("" + entity.counter);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
