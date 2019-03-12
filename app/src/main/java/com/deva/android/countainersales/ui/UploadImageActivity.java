package com.deva.android.countainersales.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.deva.android.countainersales.R;
import com.deva.android.countainersales.helper.FirebaseHandler;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ChangeImageActivity";
    private static final int RC_PHOTO_PICKER = 1;

    private ImageButton mChangeImageButton;
    private Button mUploadButton, mCancelButton;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    private String userId;
    private FirebaseHandler firebaseHandler;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mProfilePhotoStorageReference;

    private Bitmap bitmap;
    private String imageLastSegment;
    private Uri downloadUrl;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        mChangeImageButton = (ImageButton) findViewById(R.id.change_image_button);
        mUploadButton = (Button) findViewById(R.id.upload_button);
        mCancelButton = (Button) findViewById(R.id.cancel_button);

        //Initialize Firebase Components
        mFirebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        firebaseHandler = new FirebaseHandler(this, userId);

        mFirebaseStorage = FirebaseStorage.getInstance();
        //      mProfileDatabaseReference.keepSynced(true);
        mProfilePhotoStorageReference = mFirebaseStorage.getReference().child("design_file");

        mChangeImageButton.setOnClickListener(this);
        mUploadButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.change_image_button) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(Intent.createChooser(i, "Complete action"), RC_PHOTO_PICKER);

        } else if (id == R.id.upload_button) {
            if (bitmap != null) {
                upload();
            } else {
                Toast.makeText(UploadImageActivity.this, "Foto belum dipilih", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.cancel_button) {
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mChangeImageButton.setImageBitmap(bitmap);
        }
    }

    private void upload() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        //Get reference to store file at profile_photo/<FILENAME>
        StorageReference photoRef = mProfilePhotoStorageReference.child(selectedImageUri.getLastPathSegment());
        imageLastSegment = selectedImageUri.getLastPathSegment();
        Log.e(TAG, "imgLastSegment = " + imageLastSegment);
        //Upload file to Firebase Storage
        photoRef.putFile(selectedImageUri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.e(TAG, "downloadUri = " + downloadUrl);

                        String url = downloadUrl.toString();
                        Log.e(TAG, "Url = " + url);
                        firebaseHandler.getRefProfileUser().child("photoUrl").setValue(url);
                        progressDialog.dismiss();
                        finish();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
