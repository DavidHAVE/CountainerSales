package com.deva.android.countainersales.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.deva.android.countainersales.R;
import com.deva.android.countainersales.object.History;

import java.util.List;

/**
 * Created by David on 14/10/2017.
 */

public class HistoryAdapter extends ArrayAdapter<History> {

    public HistoryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<History> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_history, parent, false);
        }


        TextView mItemOrderTextView = (TextView) convertView.findViewById(R.id.item_order_text_view);
        TextView mDesignTextView = (TextView) convertView.findViewById(R.id.design_text_view);
//        mTotalOrderPriceTextView = (TextView) findViewById(R.id.total_order_price_text_view);
        TextView mItemQuantityTextView = (TextView) convertView.findViewById(R.id.item_quantity_text_view);
        TextView mDesignQuantityTextView = (TextView) convertView.findViewById(R.id.design_quantity_text_view);
        TextView mItemPriceTextView = (TextView) convertView.findViewById(R.id.item_price_text_view);
        TextView mDesignPriceTextView = (TextView) convertView.findViewById(R.id.design_price_text_view);

        final History history = getItem(position);

        mItemOrderTextView.setText(history.getItem());
//        mDesignTextView.setText("");
        mItemQuantityTextView.setText(history.getQuantity());
        mDesignQuantityTextView.setText(history.getQuantity());
        mItemPriceTextView.setText(history.getTotalPrice());

        return convertView;
    }
}
