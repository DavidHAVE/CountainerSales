package com.deva.android.countainersales.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.deva.android.countainersales.R;
import com.deva.android.countainersales.helper.StaticValue;
import com.deva.android.countainersales.object.Order;
import com.deva.android.countainersales.admin.BuyerDetailActivity;

import java.util.List;

/**
 * Created by David on 08/10/2017.
 */

public class OrderAdapter extends ArrayAdapter<Order> {

    public OrderAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Order> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_order, parent, false);
        }

        TextView mNameTextView = (TextView) convertView.findViewById(R.id.name_text_view);
        TextView mPhoneNumberTextView = (TextView) convertView.findViewById(R.id.phone_number_text_view);
        TextView mOrderNumberTextView = (TextView) convertView.findViewById(R.id.order_number_text_view);

        final Order order = getItem(position);

        mNameTextView.setText(order.getName());
        mPhoneNumberTextView.setText(order.getPhoneNumber());
        mOrderNumberTextView.setText(order.getOrderID());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailsActivity(order.getUid(), order.getOrderID());
            }
        });

        return convertView;
    }

    public void openDetailsActivity(String uid, String orderId){
        Intent i = new Intent(getContext(), BuyerDetailActivity.class);
        i.putExtra(StaticValue.UID, uid);
        i.putExtra(StaticValue.ORDER_ID, orderId);
        getContext().startActivity(i);
    }
}
