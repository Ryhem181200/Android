package com.example.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class OrderListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> orders;

    public OrderListAdapter(Context context, ArrayList<String> orders) {
        super(context, R.layout.activity_list_item_order, orders); // Use the correct layout
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Inflate the item layout if it's not already available
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.activity_list_item_order, parent, false);
        }

        // Get the current order item
        String order = orders.get(position);

        // Populate the view with the order data
        TextView orderTextView = convertView.findViewById(R.id.orderTextView);
        orderTextView.setText(order);

        return convertView;
    }
}
