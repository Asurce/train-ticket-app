package hu.mobilalk.trainticketapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrainListAdapter extends RecyclerView.Adapter<TrainListAdapter.ViewHolder> {
    private ArrayList<SearchResultItem> resultItems;
    private Context context;


    TrainListAdapter(Context context, ArrayList<SearchResultItem> items) {
        this.resultItems = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrainListAdapter.ViewHolder holder, int position) {
        SearchResultItem currentItem = resultItems.get(position);

        holder.bindTo(currentItem);
    }

    @Override
    public int getItemCount() {
        return resultItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView city;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.cityTextView);
        }

        public void bindTo(SearchResultItem currentItem) {
            city.setText(currentItem.getCity());
        }
    }

}
