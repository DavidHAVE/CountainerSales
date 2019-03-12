package com.deva.android.countainersales.fragment;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deva.android.countainersales.R;
import com.deva.android.countainersales.helper.FirebaseHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroFragment extends Fragment {

    TextView mIntroTextView;
    ImageView mIntroImageView;

    private FirebaseUser user;
    private String userId;

    private FirebaseHandler firebaseHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_intro, container, false);

        WebView view = new WebView(getContext());
        view.setVerticalScrollBarEnabled(false);

        ((LinearLayout)rootView.findViewById(R.id.rootView)).addView(view);

        view.loadData(getString(R.string.hello), "text/html; charset=utf-8", "utf-8");


//        user = FirebaseAuth.getInstance().getCurrentUser();
//
//        userId = user.getUid();
//        firebaseHandler = new FirebaseHandler(getActivity(), userId);

//        mIntroImageView = (ImageView) rootView.findViewById(R.id.intro_image_view);
//        mIntroTextView = (TextView) rootView.findViewById(R.id.intro_text_view);

        return rootView;
    }

    private void introRead(){

    }
}
