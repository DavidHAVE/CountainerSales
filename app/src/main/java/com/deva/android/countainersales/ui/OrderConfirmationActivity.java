package com.deva.android.countainersales.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.deva.android.countainersales.R;
import com.deva.android.countainersales.helper.FirebaseHandler;
import com.deva.android.countainersales.helper.StaticValue;
import com.deva.android.countainersales.object.Cart;
import com.deva.android.countainersales.object.Order;
import com.deva.android.countainersales.object.OrderItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.R.attr.name;
import static android.R.attr.order;

public class OrderConfirmationActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mUploadLayout;
    RadioGroup mRadioDesignGroup;
    RadioButton mWithDesign, mWithoutDesign;
    TextView mImageTitleTextView, mEstimatePriceTextView, mDesignPriceTextView, mTotalPriceTextView;
    ImageView mDesignImageView;
    Button mDesignButton, mProceedButton;

    String name, phoneNumber, address;

    String keyHighCube;
    String keyFeet40;
    String keyFeet20;
    private String email;

    long highCubePrice = 300;
    long feet40Price = 200;
    long feet20Price = 100;

//    private long highCubePrice, feet40Price, feet20Price;
    private int countHighCube, countFeet40, countFeet20;
    private long estimate;

    private long defaultDesignPrice = 250000;
    private long customDesignPrice = 250000;
    private long totalPrice;

    private FirebaseUser user;
    private String userId;
    private FirebaseHandler firebaseHandler;
    private ChildEventListener mChildEventListenerCart;
    private ChildEventListener mChildEventListenerItem;


//
private static final String TAG = "ChangeImageActivity";
    private static final int RC_PHOTO_PICKER = 1;

//    private ImageButton mChangeImageButton;
    private Button mCancelButton;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mProfilePhotoStorageReference;

    private Bitmap bitmap;
    private String imageLastSegment;
    private Uri downloadUrl;
    private Uri selectedImageUri;

//    private String url;

    private int totalCount;
    private long totalDesignPrice;
    private String orderNumber;

    private ProgressDialog loadingDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            if (isPaidOff){
//                mAddItem0Button.setClickable(true);
//                mAddItem1Button.setClickable(false);
//                mNextOrderButton.setClickable(true);
//            }else {
//                mAddItem0Button.setClickable(false);
//                mNextOrderButton.setClickable(false);
//            }

            loadingDialog.dismiss();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
////        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
//        setSupportActionBar(toolbar);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mUploadLayout = (LinearLayout) findViewById(R.id.upload_layout);
        mRadioDesignGroup = (RadioGroup) findViewById(R.id.radioDesignGroup);
        mWithDesign = (RadioButton) findViewById(R.id.withDesign);
        mWithoutDesign = (RadioButton) findViewById(R.id.withoutDesign);
        mImageTitleTextView = (TextView) findViewById(R.id.image_title_text_view);
        mEstimatePriceTextView = (TextView) findViewById(R.id.estimate_price_text_view);
        mDesignPriceTextView = (TextView) findViewById(R.id.design_price_text_view);
        mTotalPriceTextView = (TextView) findViewById(R.id.total_price_text_view);
        mDesignImageView = (ImageView) findViewById(R.id.design_image_view);
        mDesignButton = (Button) findViewById(R.id.design_button);
        mProceedButton = (Button) findViewById(R.id.proceed_button);

//
//        mChangeImageButton = (ImageButton) findViewById(R.id.change_image_button);
        mCancelButton = (Button) findViewById(R.id.cancel_button);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            name = extra.getString(StaticValue.NAME);
            phoneNumber = extra.getString(StaticValue.PHONE_NUMBER);
            email = extra.getString(StaticValue.EMAIL);
            address = extra.getString(StaticValue.ADDRESS);
            keyHighCube = extra.getString(StaticValue.KEY_HIGH_CUBE);
            keyFeet40 = extra.getString(StaticValue.KEY_FEET_40);
            keyFeet20 = extra.getString(StaticValue.KEY_FEET_20);
            countHighCube = extra.getInt(StaticValue.HIGH_CUBE_COUNT);
            countFeet40 = extra.getInt(StaticValue.FEET_40_COUNT);
            countFeet20 = extra.getInt(StaticValue.FEET_20_COUNT);
            orderNumber = extra.getString(StaticValue.ORDER_ID);
//            highCubePrice = extra.getLong(StaticValue.HIGH_CUBE_PRICE);
//            feet40Price = extra.getLong(StaticValue.FEET_40_PRICE);
//            feet20Price = extra.getLong(StaticValue.FEET_20_PRICE);
            estimate = extra.getLong(StaticValue.ESTIMATE);

            mEstimatePriceTextView.setText(String.valueOf(estimate));

            totalCount = countHighCube + countFeet40 + countFeet20;
            totalDesignPrice = totalCount * 250000;
            Log.e("OrderCONFIRM", "Email:"+email+", OrderNumber:"+orderNumber);
            Log.e("OrderCONFIRM", totalCount+", "+totalDesignPrice+", "+countHighCube+", "+countFeet40);

        }

        if (countHighCube > 0){
            highCubePrice = highCubePrice * countHighCube;
        }
        if (countFeet40 > 0){
            feet40Price = feet40Price * countFeet40;
        }
        if (countFeet20 > 0){
            feet20Price = feet20Price * countFeet20;
        }

        Log.e("OrderCONFIRM", totalCount+", "+totalDesignPrice+", "+countHighCube+", "+countFeet40);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        firebaseHandler = new FirebaseHandler(this, userId);

        mFirebaseStorage = FirebaseStorage.getInstance();
        //      mProfileDatabaseReference.keepSynced(true);
        mProfilePhotoStorageReference = mFirebaseStorage.getReference().child("design_file");


        mDesignButton.setVisibility(View.VISIBLE);
        mDesignImageView.setVisibility(View.GONE);


//        mChangeImageButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        mWithDesign.setOnClickListener(this);
        mWithoutDesign.setOnClickListener(this);
        mDesignButton.setOnClickListener(this);
        mProceedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.design_button) {
//            startActivity(new Intent(OrderConfirmationActivity.this, UploadImageActivity.class));
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(Intent.createChooser(i, "Complete action"), RC_PHOTO_PICKER);
        } else if(id == R.id.cancel_button){
            mDesignButton.setVisibility(View.VISIBLE);
            mDesignImageView.setVisibility(View.GONE);
        } else if (id == R.id.withDesign) {
            mDesignPriceTextView.setText(String.valueOf(totalDesignPrice));
            totalPrice = totalDesignPrice + estimate;
            mTotalPriceTextView.setText(String.valueOf(totalPrice));
            mUploadLayout.setVisibility(View.VISIBLE);
        }else if(id == R.id.withoutDesign){
            mDesignPriceTextView.setText(String.valueOf(totalDesignPrice));
            totalPrice = totalDesignPrice + estimate;
            mTotalPriceTextView.setText(String.valueOf(totalPrice));
            mUploadLayout.setVisibility(View.GONE);
        }else if(id == R.id.proceed_button){
//            loadingDialog = new ProgressDialog(this);
//            loadingDialog.setCanceledOnTouchOutside(false);
//            loadingDialog.setCancelable(false);
//            loadingDialog.setMessage("Please Wait...");
//            loadingDialog.show();
            proceed();
        } else if (id == R.id.upload_button) {
//            if (bitmap != null) {
//                upload();
//            } else {
//                Toast.makeText(OrderConfirmationActivity.this, "Foto belum dipilih", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    public void startTread() {
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage("Please Wait...");
        loadingDialog.show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    proceed();

                    sleep(2000);
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDesignPriceTextView.setText(String.valueOf(totalDesignPrice));
        totalPrice = totalDesignPrice + estimate;
        mTotalPriceTextView.setText(String.valueOf(totalPrice));
        mUploadLayout.setVisibility(View.VISIBLE);
    }

    private void proceed(){
//        UserData userData = new UserData(user.getUid(), name, gender, age, weight, height, activity);
//        if (!TextUtils.isEmpty(name) && age != 0 && weight != 0 & height != 0) {
//            firebaseHandler.getRefDataUser().setValue(userData);

//        String item =

//        Random rand = new Random();
//        int  n = rand.nextInt(500) + 1;
//50 is the maximum and the 1 is our minimum





        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = sdf.format(new Date());

        Log.e("OrderConfirmation", "4");

        int id = mRadioDesignGroup.getCheckedRadioButtonId();
        switch (id) {
            case R.id.withDesign:
                Log.e("OrderConfirmation", "1");

//                String key = firebaseHandler.getRefEatPlan().push().getKey();
//                EatPlan eatPlan = new EatPlan(userId, key, currentDate, currentTime, name, calorie,
//                        carbohydrate, protein, fat, quality);
//                firebaseHandler.getRefEatPlan().child(key).setValue(eatPlan);


                if (bitmap != null) {
                    Log.e("OrderConfirmation", "2");
                    if (!TextUtils.isEmpty(keyHighCube)){
                        String key = firebaseHandler.getRefOrderItem().push().getKey();
                        OrderItem orderItem = new OrderItem(orderNumber, "High Cube Countainer", countHighCube, highCubePrice, key);
                        firebaseHandler.getRefOrderItem().child(key).setValue(orderItem);
                    }

                    if (!TextUtils.isEmpty(keyFeet40)){
                        String key = firebaseHandler.getRefOrderItem().push().getKey();
                        OrderItem orderItem = new OrderItem(orderNumber, "40 Feet Countainer", countFeet40, feet40Price, key);
                        firebaseHandler.getRefOrderItem().child(key).setValue(orderItem);
                    }

                    if (!TextUtils.isEmpty(keyFeet20)){
                        String key = firebaseHandler.getRefOrderItem().push().getKey();
                        OrderItem orderItem = new OrderItem(orderNumber, "20 Feet Countainer", countFeet20, feet20Price, key);
                        firebaseHandler.getRefOrderItem().child(key).setValue(orderItem);
                    }

                    firebaseHandler.getRefProfileUser().child("paidOff").setValue(false);

                    Order order = new Order(userId, orderNumber, name, email, phoneNumber, address, currentDate, estimate,
                            customDesignPrice, true, "");
                    firebaseHandler.getRefOrderUser().setValue(order);

                    if (!TextUtils.isEmpty(keyHighCube)){
                        firebaseHandler.getRefCart().child(keyHighCube).removeValue();
                    }
                    if (!TextUtils.isEmpty(keyFeet40)) {
                        firebaseHandler.getRefCart().child(keyFeet40).removeValue();
                    }
                    if (!TextUtils.isEmpty(keyFeet20)) {
                        firebaseHandler.getRefCart().child(keyFeet20).removeValue();
                    }

                    upload();
                } else {
                    Toast.makeText(OrderConfirmationActivity.this, "Foto belum dipilih", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.withoutDesign:
                Log.e("OrderConfirmation", "3"+", keyHiighCube:"+keyHighCube+", keyFeet40:"+keyFeet40);
                if (!TextUtils.isEmpty(keyHighCube)){
                    String key = firebaseHandler.getRefOrderItem().push().getKey();
                    OrderItem orderItem = new OrderItem(orderNumber, "High Cube Countainer", countHighCube, highCubePrice, key);
                    firebaseHandler.getRefOrderItem().child(key).setValue(orderItem);
                }

                if (!TextUtils.isEmpty(keyFeet40)){
                    String key = firebaseHandler.getRefOrderItem().push().getKey();
                    OrderItem orderItem = new OrderItem(orderNumber, "40 Feet Countainer", countFeet40, feet40Price, key);
                    firebaseHandler.getRefOrderItem().child(key).setValue(orderItem);
                }

                if (!TextUtils.isEmpty(keyFeet20)){
                    String key = firebaseHandler.getRefOrderItem().push().getKey();
                    OrderItem orderItem = new OrderItem(orderNumber, "20 Feet Countainer", countFeet20, feet20Price, key);
                    firebaseHandler.getRefOrderItem().child(key).setValue(orderItem);
                }

                firebaseHandler.getRefProfileUser().child("paidOff").setValue(false);

                Order order2 = new Order(userId, orderNumber, name, email, phoneNumber, address, currentDate, estimate,
                        defaultDesignPrice, false, "");
                firebaseHandler.getRefOrderUser().setValue(order2);
                if (!TextUtils.isEmpty(keyHighCube)){
                    firebaseHandler.getRefCart().child(keyHighCube).removeValue();
                }
                if (!TextUtils.isEmpty(keyFeet40)) {
                    firebaseHandler.getRefCart().child(keyFeet40).removeValue();
                }
                if (!TextUtils.isEmpty(keyFeet20)) {
                    firebaseHandler.getRefCart().child(keyFeet20).removeValue();
                }

                startActivity(new Intent(OrderConfirmationActivity.this, MainActivity.class));
                finish();
                break;
        }


    }

    //
//    private void attactReadListenerCart() {
//        if (mChildEventListenerCart == null) {
//
//            mChildEventListenerCart = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                    Cart cart = dataSnapshot.getValue(Cart.class);
//
////                    productID.add(cart.getProductID());
////                    quantity.add(cart.getQuantity());
//
//                    if (cart.getProductID() == 0) {
////                        keyHighCube = cart.getKey();
//                        countHighCube = cart.getQuantity();
//
////                        mHighCubeCountTextView.setText(String.valueOf(countHighCube));
//                    } else if (cart.getProductID() == 1) {
////                        keyFeet40 = cart.getKey();
//                        countFeet40 = cart.getQuantity();
////                        mFeet40CountTextView.setText(String.valueOf(countFeet40));
//                    }
//
////                    for (int i = 0; i < productID.size(); i++) {
////                        for (int n = 0; i < quantity.size(); n++) {
////                            if (productID.get(i) == 0) {
////                                countHighCube = quantity.get(i);
////                                mHighCubeCountTextView.setText(String.valueOf(countHighCube));
////                            } else if (productID.get(i) == 1) {
////                                countFeet40 = quantity.get(i);
////                                mFeet40CountTextView.setText(String.valueOf(countFeet40));
////                            }
//////                            Log.e("EatPlan", "calorieini = " + kaloriLatihan);
////                        }
////                    }
//                }
//
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                }
//
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                }
//
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                }
//
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            };
//            firebaseHandler.getRefCart().orderByChild("uid").equalTo(userId).
//                    addChildEventListener(mChildEventListenerCart);
//        }
//    }
//
//    private void detachDatabaseReadListenerCart() {
//        if (mChildEventListenerCart != null) {
//            firebaseHandler.getRefCart().removeEventListener(mChildEventListenerCart);
//            mChildEventListenerCart = null;
//        }
//    }

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

            mDesignButton.setVisibility(View.GONE);
            mDesignImageView.setVisibility(View.VISIBLE);
            mDesignImageView.setImageBitmap(bitmap);
//            mChangeImageButton.setImageBitmap(bitmap);
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
//                        firebaseHandler.getRefProfileUser().child("photoUrl").setValue(url);
                        Log.e("OrderConfirmation", "3"+", keyHiighCube:"+keyHighCube+", keyFeet40:"+keyFeet40);
                        firebaseHandler.getRefOrderUser().child("urlDesign").setValue(url);
//                        if (!TextUtils.isEmpty(keyHighCube)){
//                            firebaseHandler.getRefCart().child(keyHighCube).removeValue();
//                    }
//                        if (!TextUtils.isEmpty(keyFeet40)) {
//                            firebaseHandler.getRefCart().child(keyFeet40).removeValue();
//                        }
//                        if (!TextUtils.isEmpty(keyFeet20)) {
//                            firebaseHandler.getRefCart().child(keyFeet20).removeValue();
//                        }


//                        firebaseHandler.getRefCartUser().removeValue();
                        progressDialog.dismiss();
                        startActivity(new Intent(OrderConfirmationActivity.this, MainActivity.class));
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
