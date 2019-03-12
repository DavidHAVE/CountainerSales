package com.deva.android.countainersales.helper;

import android.content.Context;

import com.google.android.gms.common.util.DynamiteApi;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by David on 06/10/2017.
 */

public class FirebaseHandler {

    private Context context;
    private String userKey;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserProfileDatabaseReference;
    private DatabaseReference mItemDatabaseReference;
    private DatabaseReference mCartDatabaseReference;
    private DatabaseReference mOrderDatabaseReference;
    private DatabaseReference mOrderItemDatabaseReference;
    private DatabaseReference mHistoryDatabaseReference;

    private static boolean isPersistenceEnabled = false;

    public FirebaseHandler(Context context, String userKey){
        this.context = context;
        this.userKey = userKey;
        if (!isPersistenceEnabled){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            isPersistenceEnabled = true;
        }

        mUserProfileDatabaseReference = FirebaseDatabase.getInstance().getReference().child("userprofile");
        mCartDatabaseReference = FirebaseDatabase.getInstance().getReference().child("item");
        mItemDatabaseReference = FirebaseDatabase.getInstance().getReference().child("cart");
        mOrderDatabaseReference = FirebaseDatabase.getInstance().getReference().child("order");
        mOrderItemDatabaseReference = FirebaseDatabase.getInstance().getReference().child("orderitem");
        mHistoryDatabaseReference = FirebaseDatabase.getInstance().getReference().child("history");
    }

    public DatabaseReference getRefProfile(){
        mUserProfileDatabaseReference = FirebaseDatabase.getInstance().getReference().child("userprofile");
        return mUserProfileDatabaseReference;
    }

    public DatabaseReference getRefProfileUser(){
        mUserProfileDatabaseReference = FirebaseDatabase.getInstance().getReference().child("userprofile");
        return mUserProfileDatabaseReference.child(userKey);
    }

    public DatabaseReference getRefItem(){
        mItemDatabaseReference = FirebaseDatabase.getInstance().getReference().child("item");
        return mItemDatabaseReference;
    }

    public DatabaseReference getRefCart(){
        mCartDatabaseReference = FirebaseDatabase.getInstance().getReference().child("cart");
        return mCartDatabaseReference;
    }

    public DatabaseReference getRefCartUser(){
        mCartDatabaseReference = FirebaseDatabase.getInstance().getReference().child("cart");
        return mCartDatabaseReference.child(userKey);
    }

    public DatabaseReference getRefOrder(){
        mOrderDatabaseReference = FirebaseDatabase.getInstance().getReference().child("order");
        return mOrderDatabaseReference;
    }

    public DatabaseReference getRefOrderUser(){
        mOrderDatabaseReference = FirebaseDatabase.getInstance().getReference().child("order");
        return mOrderDatabaseReference.child(userKey);
    }

    public DatabaseReference getRefOrderItem(){
        mOrderItemDatabaseReference = FirebaseDatabase.getInstance().getReference().child("orderitem");
        return mOrderItemDatabaseReference;
    }

    public DatabaseReference getRefOrderItemUser(){
        mOrderItemDatabaseReference = FirebaseDatabase.getInstance().getReference().child("orderitem");
        return mOrderItemDatabaseReference.child(userKey);
    }

    public DatabaseReference getRefHistory(){
        mHistoryDatabaseReference = FirebaseDatabase.getInstance().getReference().child("history");
        return mHistoryDatabaseReference;
    }

    public DatabaseReference getRefHistoryUser(){
        mHistoryDatabaseReference = FirebaseDatabase.getInstance().getReference().child("history");
        return mHistoryDatabaseReference.child(userKey);
    }
}
