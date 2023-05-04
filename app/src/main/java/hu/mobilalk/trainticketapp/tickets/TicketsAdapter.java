package hu.mobilalk.trainticketapp.tickets;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

import hu.mobilalk.trainticketapp.R;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.ViewHolder> {

    // ADAPTER
    Context context;
    ArrayList<TicketItem> items;

    // FIREBASE
    FirebaseAuth fireAuth;
    FirebaseFirestore firestore;
    CollectionReference ticketsCollection;

    TicketsAdapter(Context context, ArrayList<TicketItem> items) {
        this.items = items;
        this.context = context;

        fireAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        ticketsCollection = firestore.collection("tickets");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.ticket_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TicketsAdapter.ViewHolder holder, int position) {
        holder.bindTo(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView timeTextView;
        TextView dateTextView;
        TextView originCityTextView;
        TextView destCityTextView;
        Calendar calendar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            originCityTextView = itemView.findViewById(R.id.originCityTextView);
            destCityTextView = itemView.findViewById(R.id.destCityTextView);
            calendar = Calendar.getInstance();
        }

        public void bindTo(TicketItem currentItem) {
            calendar.setTimeInMillis(currentItem.getDepartTime());
            timeTextView.setText(DateFormat.format("HH:mm", calendar.getTime()));
            dateTextView.setText(DateFormat.format("yyyy. MM. dd", calendar.getTime()));
            originCityTextView.setText(currentItem.getOriginCity());
            destCityTextView.setText(currentItem.getDestCity());

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), TicketDetailsActivity.class);
                intent.putExtra("ticketData", currentItem);
                view.getContext().startActivity(intent);
            });
        }
    }
}
