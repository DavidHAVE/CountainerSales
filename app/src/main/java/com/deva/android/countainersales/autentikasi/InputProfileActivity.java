package com.deva.android.countainersales.autentikasi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deva.android.countainersales.ui.MainActivity;
import com.deva.android.countainersales.R;
import com.deva.android.countainersales.helper.FirebaseHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.itextpdf.xmp.XMPMetaFactory.reset;

public class InputProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mNameEditText, mPhoneNumberEdiText, mAddressEditText;
    Button mOkButton, mResetButton;

    private String name, phoneNumber, address;
    private float weight, height;

    private FirebaseUser user;
    private String userId;

    private FirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_profile);

        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mPhoneNumberEdiText = (EditText) findViewById(R.id.phoneNumberEditText);
        mAddressEditText = (EditText) findViewById(R.id.addressEditText);
        mOkButton = (Button) findViewById(R.id.ok_button);
        mResetButton = (Button) findViewById(R.id.reset_button);


        //Initialize Firebase Components
//        mFirebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        userId = user.getUid();
        firebaseHandler = new FirebaseHandler(this, userId);


        mOkButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ok_button){
            saveProfile();
        }else if(id == R.id.reset_button){
            resetInput();
        }
    }

    private void saveProfile() {
        name = mNameEditText.getText().toString();
        address = mAddressEditText.getText().toString();
        phoneNumber = mPhoneNumberEdiText.getText().toString();

        if(!TextUtils.isEmpty(name)  && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(phoneNumber)) {
            String userId = user.getUid();
            Log.i("IntroProfileActivity", "userId =" + userId);

            firebaseHandler.getRefProfileUser().child("name").setValue(name);
            firebaseHandler.getRefProfileUser().child("phoneNumber").setValue(phoneNumber);
            firebaseHandler.getRefProfileUser().child("address").setValue(address);

            startActivity(new Intent(InputProfileActivity.this, MainActivity.class));
            finish();
        }else{
            Toast.makeText(this, "Harap Isi semua data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetInput() {
        mNameEditText.setText("");
        mAddressEditText.setText("");
        mPhoneNumberEdiText.setText("");
    }
}
