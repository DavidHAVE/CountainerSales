package com.deva.android.countainersales.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.deva.android.countainersales.R;
import com.deva.android.countainersales.adapter.OrderAdapter;
import com.deva.android.countainersales.helper.FirebaseHandler;
import com.deva.android.countainersales.object.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class BuyerFragment extends Fragment {

    ListView mBuyerListView;
    LinearLayout mEmptyView;

    private OrderAdapter mOrderAdapter;

    // Firebase instance variables
    private ChildEventListener mChildEventListenerOrder;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;

    private String userId;
    private FirebaseHandler firebaseHandler;

    private ProgressDialog loadingDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadingDialog.dismiss();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_buyer, container, false);

        mBuyerListView = (ListView) rootView.findViewById(R.id.buyer_list_view);
        mEmptyView = (LinearLayout) rootView.findViewById(R.id.emptyView);

        //Initialize Firebase Components
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        userId = user.getUid();
        firebaseHandler = new FirebaseHandler(getContext(), userId);

//        List<Sensor> sensors = new ArrayList<>();
//        mSensorAdapter = new SensorAdapter(this, R.layout.item_sensor, sensors);
        List<Order> orders = new ArrayList<>();
        mOrderAdapter = new OrderAdapter(getContext(), R.layout.item_order, orders);
        mBuyerListView.setEmptyView(mEmptyView);
        mBuyerListView.setAdapter(mOrderAdapter);
//        mSensorLogListView.setAdapter(mSensorAdapter);

        return rootView;
    }

    public void startTread() {
        loadingDialog = new ProgressDialog(getContext());
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage("Please Wait...");
        loadingDialog.show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                attachOrderReadListener();
                    sleep(2000);
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        startTread();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachOrderReadListener();
    }

    private void attachOrderReadListener() {
        if (mChildEventListenerOrder == null) {
            mChildEventListenerOrder = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Order order = dataSnapshot.getValue(Order.class);
                    //    String uid = newWeight.getUid();

                    mOrderAdapter.add(order);

//                    Log.e("ProgGoalActivity", "onAdapter2 , uid = " + uid);
//                    Log.e("ProgGoalActivity", "onAdapter , uid = " + uid + " userid =" + userId);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.e("ProgGoalActivity", "onChanged");
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    mOrderAdapter.clear();
                    Log.e("ProgGoalActivity", "onRemoved");
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.e("ProgGoalActivity", "onMoved");
                }

                public void onCancelled(DatabaseError databaseError) {
                    Log.e("ProgGoalActivity", "onCancel");
                }
            };
            firebaseHandler.getRefOrder().addChildEventListener(mChildEventListenerOrder);
        }
    }

    private void detachOrderReadListener() {
        if (mChildEventListenerOrder != null) {
            firebaseHandler.getRefOrder().removeEventListener(mChildEventListenerOrder);
            mChildEventListenerOrder = null;
            mOrderAdapter.clear();
        }
    }
}
