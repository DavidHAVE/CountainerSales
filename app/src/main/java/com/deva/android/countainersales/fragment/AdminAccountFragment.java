package com.deva.android.countainersales.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.deva.android.countainersales.R;
import com.deva.android.countainersales.autentikasi.LoginActivity;
import com.deva.android.countainersales.helper.FirebaseHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;

public class AdminAccountFragment extends Fragment implements View.OnClickListener {

    TextView mEmailTextView;
    Button mSignOutButton;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;

    private String userId;
    private FirebaseHandler firebaseHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_account, container, false);

        mEmailTextView = (TextView) rootView.findViewById(R.id.email_text_view);
        mSignOutButton = (Button) rootView.findViewById(R.id.sign_out_button);

        //Initialize Firebase Components
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        userId = user.getUid();
        firebaseHandler = new FirebaseHandler(getContext(), userId);

        mEmailTextView.setText(user.getEmail());

        mSignOutButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.sign_out_button){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        }
    }
}
