package hu.mobilalk.trainticketapp.tickets;

import android.content.Context;
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

        // TODO TICKET FIELDS
        TextView time;
        TextView city;
        TextView price;

        Calendar calendar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.timeTextView);
            city = itemView.findViewById(R.id.cityTextView);
            price = itemView.findViewById(R.id.priceTextView);
            calendar = Calendar.getInstance();
        }

        public void bindTo(TicketItem currentItem) {
            calendar.setTimeInMillis(currentItem.getDate());
            time.setText(DateFormat.format("HH:mm",calendar.getTime()));
            city.setText(currentItem.getOriginCity() + " ---> " + currentItem.getDestCity());
            price.setText(currentItem.getPrice() + "Ft");
        }
    }

}
