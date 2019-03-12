package com.deva.android.countainersales.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.deva.android.countainersales.adapter.HistoryAdapter;
import com.deva.android.countainersales.object.History;
import com.deva.android.countainersales.object.Order;
import com.deva.android.countainersales.ui.EditAccountActivity;
import com.deva.android.countainersales.R;
import com.deva.android.countainersales.autentikasi.LoginActivity;
import com.deva.android.countainersales.helper.FirebaseHandler;
import com.deva.android.countainersales.object.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment implements View.OnClickListener {

    TextView mEditAccountTextView, mNameTextView, mEmailTextView, mPhoneNumberTextView, mAddressTextView;
    Button mSignOutButton;
    ListView mHistoryListView;

    String name, email, phoneNumber, address;

    private ChildEventListener mChildEventListenerHistory;
    private FirebaseUser user;
    private String userId;
    private FirebaseHandler firebaseHandler;

    private HistoryAdapter mHistoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        mEditAccountTextView = (TextView) rootView.findViewById(R.id.edit_account_text_view);
        mNameTextView = (TextView) rootView.findViewById(R.id.name_text_view);
        mEmailTextView = (TextView) rootView.findViewById(R.id.email_text_view);
        mPhoneNumberTextView = (TextView) rootView.findViewById(R.id.phone_number_text_view);
        mAddressTextView = (TextView) rootView.findViewById(R.id.address_text_view);
        mSignOutButton = (Button) rootView.findViewById(R.id.sign_out_button);
        mHistoryListView = (ListView) rootView.findViewById(R.id.history_list_view);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        firebaseHandler = new FirebaseHandler(getActivity(), userId);

        List<History> histories = new ArrayList<>();
        mHistoryAdapter = new HistoryAdapter(getContext(), R.layout.item_history, histories);
        mHistoryListView.setAdapter(mHistoryAdapter);

        mEditAccountTextView.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.edit_account_text_view){
            startActivity(new Intent(getContext(), EditAccountActivity.class));
        }else if(id == R.id.sign_out_button){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userProfileRead();
        attachHistoryReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachHistoryReadListener();
    }

    private void userProfileRead(){
        firebaseHandler.getRefProfileUser().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                name = userProfile.getName();
                email = userProfile.getEmail();
                phoneNumber = userProfile.getPhoneNumber();
                address = userProfile.getAddress();

                mNameTextView.setText(name);
                mEmailTextView.setText(email);
                mPhoneNumberTextView.setText(phoneNumber);
                mAddressTextView.setText(address);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("AccountFragment", "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    private void attachHistoryReadListener() {
        if (mChildEventListenerHistory == null) {
            mChildEventListenerHistory = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    History history = dataSnapshot.getValue(History.class);
                    //    String uid = newWeight.getUid();

                    mHistoryAdapter.add(history);

//                    Log.e("ProgGoalActivity", "onAdapter2 , uid = " + uid);
//                    Log.e("ProgGoalActivity", "onAdapter , uid = " + uid + " userid =" + userId);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.e("ProgGoalActivity", "onChanged");
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    mHistoryAdapter.clear();
                    Log.e("ProgGoalActivity", "onRemoved");
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.e("ProgGoalActivity", "onMoved");
                }

                public void onCancelled(DatabaseError databaseError) {
                    Log.e("ProgGoalActivity", "onCancel");
                }
            };
            firebaseHandler.getRefHistory().orderByChild("uid").equalTo(userId)
                    .addChildEventListener(mChildEventListenerHistory);
        }
    }

    private void detachHistoryReadListener() {
        if (mChildEventListenerHistory != null) {
            firebaseHandler.getRefHistory().removeEventListener(mChildEventListenerHistory);
            mChildEventListenerHistory = null;
            mHistoryAdapter.clear();
        }
    }
}
