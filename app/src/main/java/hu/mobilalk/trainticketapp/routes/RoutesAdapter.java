package hu.mobilalk.trainticketapp.routes;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import hu.mobilalk.trainticketapp.PurchaseActivity;
import hu.mobilalk.trainticketapp.R;
import hu.mobilalk.trainticketapp.tickets.TicketItem;
import hu.mobilalk.trainticketapp.tickets.TicketsActivity;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> {

    // ADAPTER
    Context context;
    ArrayList<RouteItem> items;

    // FIREBASE
    FirebaseAuth fireAuth;
    FirebaseFirestore firestore;
    CollectionReference ticketsCollection;

    RoutesAdapter(Context context, ArrayList<RouteItem> items) {
        this.items = items;
        this.context = context;

        fireAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        ticketsCollection = firestore.collection("tickets");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(
                        R.layout.route_item,
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutesAdapter.ViewHolder holder, int position) {
        holder.bindTo(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        // TODO ROUTE FIELDS
        TextView departTimeTextView;
        TextView originCityTextView;
        TextView arriveTimeTextView;
        TextView destCityTextView;
        Button buyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            departTimeTextView = itemView.findViewById(R.id.departTimeTextView);
            originCityTextView = itemView.findViewById(R.id.originCityTextView);
            arriveTimeTextView = itemView.findViewById(R.id.arriveTimeTextView);
            destCityTextView = itemView.findViewById(R.id.destCityTextView);
            buyButton = itemView.findViewById(R.id.buyButton);
        }

        public void bindTo(RouteItem currentItem) {
            departTimeTextView.setText(DateFormat.format("HH:mm", currentItem.getDepartTime()));
            originCityTextView.setText(currentItem.getOriginCity());
            arriveTimeTextView.setText(DateFormat.format("HH:mm", currentItem.getArriveTime()));
            destCityTextView.setText(currentItem.getDestCity());
            buyButton.setOnClickListener(view -> buy(view, currentItem));
            buyButton.setText(currentItem.getPrice() + "Ft");
        }

        public void buy(View view, RouteItem item) {
//            ticketsCollection.add(new TicketItem(
//                    item.getOriginCity(),
//                    item.getDestCity(),
//                    2500,
//                    item.getDate().getTime(),
//                    fireAuth.getCurrentUser().getUid()));
            view.getContext().startActivity(new Intent(view.getContext(), PurchaseActivity.class));
        }
    }

}
