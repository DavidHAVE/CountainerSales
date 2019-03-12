package com.deva.android.countainersales.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.deva.android.countainersales.R;
import com.deva.android.countainersales.helper.FirebaseHandler;
import com.deva.android.countainersales.object.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class EditAccountActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mNameEditText, mAddressEditText, mPhoneNumberEditText;
    ImageButton mChangeButton, mResetButton;

    String name, address, phoneNumber;

    private FirebaseUser user;
    private String userId;
    private FirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        mNameEditText = (EditText) findViewById(R.id.name_edit_text);
        mAddressEditText = (EditText) findViewById(R.id.address_edit_text);
        mPhoneNumberEditText = (EditText) findViewById(R.id.phone_number_edit_text);
        mChangeButton = (ImageButton) findViewById(R.id.change_button);
        mResetButton = (ImageButton) findViewById(R.id.reset_button);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        firebaseHandler = new FirebaseHandler(this, userId);

        mChangeButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.change_button){
            userProfileChange();
        }else if(id == R.id.reset_button){
            resetInput();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userProfileRead();
    }

    private void userProfileRead(){
        firebaseHandler.getRefProfileUser().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                name = userProfile.getName();
                phoneNumber = userProfile.getPhoneNumber();
                address = userProfile.getAddress();

                mNameEditText.setText(name);
                mAddressEditText.setText(address);
                mPhoneNumberEditText.setText(phoneNumber);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("AccountFragment", "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    private void userProfileChange() {
                name = mNameEditText.getText().toString();
                address = mAddressEditText.getText().toString();
                phoneNumber = mPhoneNumberEditText.getText().toString();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(phoneNumber)) {
                    String userId = user.getUid();
                    Log.i("IntroProfileActivity", "userId =" + userId);

                    firebaseHandler.getRefProfileUser().child("name").setValue(name);
                    firebaseHandler.getRefProfileUser().child("phoneNumber").setValue(phoneNumber);
                    firebaseHandler.getRefProfileUser().child("address").setValue(address);

                    startActivity(new Intent(EditAccountActivity.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(this, "Harap Isi semua data.", Toast.LENGTH_SHORT).show();
                }
    }


    private void resetInput() {
        mNameEditText.setText("");
        mAddressEditText.setText("");
        mPhoneNumberEditText.setText("");
    }
}
