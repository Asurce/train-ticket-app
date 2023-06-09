package hu.mobilalk.trainticketapp.routes;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import hu.mobilalk.trainticketapp.R;
import hu.mobilalk.trainticketapp.tickets.TicketItem;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> {

    // ADAPTER
    Context context;
    ArrayList<RouteItem> items;

    // FIREBASE
    FirebaseAuth fireAuth;
    FirebaseFirestore firestore;
    CollectionReference ticketsCollection;

    int lastPosition = -1;

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
                .inflate(R.layout.route_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutesAdapter.ViewHolder holder, int position) {
        holder.bindTo(items.get(position));

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView originCityTextView;
        TextView destCityTextView;
        TextView departTimeTextView;
        TextView arriveTimeTextView;
        TextView travelTimeTextView;
        TextView distanceTextView;
        TextView comfortTextView;
        TextView discountTextView;
        Button purchaseButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            originCityTextView = itemView.findViewById(R.id.originCityTextView);
            destCityTextView = itemView.findViewById(R.id.destCityTextView);
            departTimeTextView = itemView.findViewById(R.id.departTimeTextView);
            arriveTimeTextView = itemView.findViewById(R.id.arriveTimeTextView);
            travelTimeTextView = itemView.findViewById(R.id.travelTimeTextView);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);
            comfortTextView = itemView.findViewById(R.id.comfortTextView);
            discountTextView = itemView.findViewById(R.id.discountTextView);
            purchaseButton = itemView.findViewById(R.id.purchaseButton);
        }

        public void bindTo(RouteItem currentItem) {
            Resources res = itemView.getResources();

            originCityTextView.setText(currentItem.getOriginCity().getName());
            destCityTextView.setText(currentItem.getDestCity().getName());
            departTimeTextView.setText(DateFormat.format("HH:mm", currentItem.getDepartTime()));
            arriveTimeTextView.setText(DateFormat.format("HH:mm", currentItem.getArriveTime()));
            travelTimeTextView.setText(String.format(res.getString(R.string.minutes), currentItem.getTravelTime()));
            distanceTextView.setText(String.format(res.getString(R.string.distance), currentItem.getDistance()));
            comfortTextView.setText(currentItem.getComfort().toString());
            discountTextView.setText(currentItem.getDiscount().toString());
            purchaseButton.setOnClickListener(view -> buy(currentItem));
            purchaseButton.setText(String.format(res.getString(R.string.price), currentItem.getPrice()));
        }

        public void buy(RouteItem item) {
            ((RoutesActivity) context).setTicketToBuy(new TicketItem(
                    item.getOriginCity().getName(),
                    item.getDestCity().getName(),
                    item.getDepartTime().getTime(),
                    item.getArriveTime().getTime(),
                    item.getTravelTime(),
                    item.getDiscount().toString(),
                    item.getComfort().toString(),
                    item.getDistance(),
                    item.getPrice(),
                    fireAuth.getUid()
            ));

            ((RoutesActivity) context).purchaseTicket();

        }
    }

}
