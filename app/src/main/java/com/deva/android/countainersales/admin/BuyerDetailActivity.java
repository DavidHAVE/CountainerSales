package com.deva.android.countainersales.admin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.deva.android.countainersales.R;
import com.deva.android.countainersales.helper.CircleTransform;
import com.deva.android.countainersales.helper.FirebaseHandler;
import com.deva.android.countainersales.helper.PDFInvoiceGenerate;
import com.deva.android.countainersales.helper.StaticValue;
import com.deva.android.countainersales.helper.TouchImageView;
import com.deva.android.countainersales.helper.ZoomableImageView;
import com.deva.android.countainersales.object.History;
import com.deva.android.countainersales.object.Order;
import com.deva.android.countainersales.object.OrderItem;
import com.deva.android.countainersales.object.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static android.R.attr.name;
import static android.R.attr.order;
import static android.R.attr.phoneNumber;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.itextpdf.text.FontFactory.contains;
import static com.itextpdf.text.pdf.PdfName.S;
import static com.itextpdf.text.pdf.PdfName.W;
import static java.security.AccessController.getContext;

public class BuyerDetailActivity extends AppCompatActivity implements View.OnClickListener {  //, View.OnTouchListener {
    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;
    private static String LOG_TAG = "BuyerDetail";
    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    int mode = NONE;
    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    TextView mNameTextView, mEmailTextView, mPhoneNumberTextView, mItemOrderTextView,
            mDesignTextView, mTotalOrderPriceTextView;
    TextView mItemQuantityTextView, mDesignQuantityTextView, mItemPriceTextView, mDesignPriceTextView;
    ProgressBar mProgressBar;

    private TouchImageView mDesignTouchImageView;

    Button mCallButton, mSendInvoiceButton;
    Button mTransactionCompleteButton;
    String uid;
    String name, email, phoneNumber, address, uriDesign;
    PDFInvoiceGenerate pdfInvoiceGenerate;
    FirebaseStorage storage;
    private Button mDownloadDesignButton;
    private String orderId;
    private ChildEventListener mChildEventListenerOrderItem;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser user;
    private String userId;
    private FirebaseHandler firebaseHandler;
    private String itemOrder;
    private String itemQuantity;
    private String itemPrice;
    private String imageUrl;
    private boolean isDesign = false;
    private int designQuantity;
    private long designPrice;
    private long totalPrice;
    private long estimate;

    private String orderNumber;
    private String keyHighCube, keyFeet40, keyFeet20;

    private String design;

    private ProgressDialog loadingDialog;
    private ProgressDialog loadingDialog2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mItemOrderTextView.setText(itemOrder);
            mItemQuantityTextView.setText(itemQuantity);
            mItemPriceTextView.setText(itemPrice);
            if (isDesign) {
                mDesignTextView.setText("With Design");
                design = "With Design";
//                mDesignImageView.setVisibility(View.VISIBLE);
                showImageProfile();
            } else {
                mDesignTextView.setText("Without Design");
                mProgressBar.setVisibility(View.GONE);
                mDownloadDesignButton.setVisibility(View.GONE);
                design = "Without Design";
//                mDesignTouchImageView.setVisibility(View.GONE);
            }
            mDesignQuantityTextView.setText(String.valueOf(designQuantity));
            designPrice = designPrice * designQuantity;
            mDesignPriceTextView.setText(String.valueOf(designPrice));
            totalPrice = estimate + designPrice;
            mTotalOrderPriceTextView.setText(String.valueOf(totalPrice));

//            Log.e("BuyerDetail",  name);
            loadingDialog.dismiss();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_detail);

        mNameTextView = (TextView) findViewById(R.id.name_text_view);
        mPhoneNumberTextView = (TextView) findViewById(R.id.phone_number_text_view);
        mItemOrderTextView = (TextView) findViewById(R.id.item_order_text_view);
        mDesignTextView = (TextView) findViewById(R.id.design_text_view);
        mTotalOrderPriceTextView = (TextView) findViewById(R.id.total_order_price_text_view);
//        mDesignImageView = (ImageView) findViewById(R.id.design_image_view);
        mDownloadDesignButton = (Button) findViewById(R.id.download_design_button);
        mCallButton = (Button) findViewById(R.id.call_button);
        mSendInvoiceButton = (Button) findViewById(R.id.send_invoice_button);
        mItemQuantityTextView = (TextView) findViewById(R.id.item_quantity_text_view);
        mDesignQuantityTextView = (TextView) findViewById(R.id.design_quantity_text_view);
        mItemPriceTextView = (TextView) findViewById(R.id.item_price_text_view);
        mDesignPriceTextView = (TextView) findViewById(R.id.design_price_text_view);
        mTransactionCompleteButton = (Button) findViewById(R.id.transaction_complete_button);
        mEmailTextView = (TextView) findViewById(R.id.email_text_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mDesignTouchImageView = (TouchImageView) findViewById(R.id.design_touch_image_view);

//        ZoomableImageView touch = (ZoomableImageView) findViewById(R.id.design_image_view);
//        touch.setImageBitmap(bitmap);

        Log.e(LOG_TAG, uid + ", " + orderId);
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            uid = extra.getString(StaticValue.UID);
            orderNumber = extra.getString(StaticValue.ORDER_ID);

            mFirebaseAuth = FirebaseAuth.getInstance();
            storage = FirebaseStorage.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();

            firebaseHandler = new FirebaseHandler(this, uid);
            Log.e(LOG_TAG, uid + ", " + orderId);
            userOrderRead();

//            attachOrderItemReadListener();
//          attachOrderItemReadListener();
        }

        pdfInvoiceGenerate = new PDFInvoiceGenerate();

        Random rand = new Random();
        int n = rand.nextInt(50) + 1;

        Log.e("ORDER", "" + n);

//        mFirebaseAuth = FirebaseAuth.getInstance();
//        user = FirebaseAuth.getInstance().getCurrentUser();
////
//        userId = user.getUid();

        mDownloadDesignButton.setOnClickListener(this);
        mCallButton.setOnClickListener(this);
        mSendInvoiceButton.setOnClickListener(this);
        mTransactionCompleteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.download_design_button) {
            loadingDialog2 = new ProgressDialog(this);
            loadingDialog2.setCanceledOnTouchOutside(false);
            loadingDialog2.setCancelable(false);
            loadingDialog2.setMessage("Please Wait...");
            loadingDialog2.show();
            downloadDesign();
        } else if (id == R.id.send_invoice_button) {
            //getting the full path of the PDF report name
//            String mPath = Environment.getExternalStorageDirectory().toString()
//                    + StaticValue.PATH_PRODUCT_REPORT //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
//                    + reportName+".pdf"; //reportName could be any name

            createInvoice();

//            File path = Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES);
//            File file = new File(path, "cards_01.pdf");
            String mPath2 = Environment.getExternalStorageDirectory().toString()
                    + StaticValue.PATH_ORDER_INVOICE //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
                    + orderNumber + ".pdf";
//            File file = new File(mPath2, "David.pdf");
//            Intent intent = new Intent(Intent.ACTION_SEND ,Uri.parse("mailto:firebase.david@gmail.com")); // it's not ACTION_SEND
//            intent.setType("text/plain");
//            intent.putExtra(Intent.EXTRA_SUBJECT, "Card Set ");
//            intent.putExtra(Intent.EXTRA_TEXT, "");
//            intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
//            activity.startActivity(intent);

//            String email = "firebase.david@gmail.com";
            File pdfFile = new File(mPath2);

            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("application/pdf");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "INVOICE COUNTAINER ORDER ");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Berikut");
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdfFile));
//            emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(emailIntent);
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } else if (id == R.id.call_button) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        } else if (id == R.id.transaction_complete_button) {
            showSuccessConfirmationDialog();
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
                    mDesignTouchImageView.setVisibility(View.GONE);
//                    userProfileRead();
                    attachOrderItemReadListener();
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
        startTread();
//        userProfileRead();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachOrderItemReadListener();
    }


    private void userOrderRead() {
        firebaseHandler.getRefOrderUser().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.getValue(Order.class);

                name = order.getName();
                email = order.getEmail();
                phoneNumber = order.getPhoneNumber();
                address = order.getAddress();
                isDesign = order.isDesign();
                designPrice = order.getDesignPrice();
                estimate = order.getEstimate();
                uriDesign = order.getUrlDesign();

                if (order.isDesign()) {
                    imageUrl = order.getUrlDesign();
                }

                mNameTextView.setText(name);
                mEmailTextView.setText(email);
                mPhoneNumberTextView.setText(phoneNumber);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("AccountFragment", "getUser:onCancelled", databaseError.toException());
            }
        });
    }

    private void attachOrderItemReadListener() {
        if (mChildEventListenerOrderItem == null) {
            mChildEventListenerOrderItem = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    OrderItem orderItem = dataSnapshot.getValue(OrderItem.class);

                    if (orderItem.getName().equals("High Cube Countainer")) {
                        Log.e("BuyerDetail2", orderItem.getName() + ", " + orderItem.getQuantity());
                        keyHighCube = orderItem.getKey();
                        itemOrder = orderItem.getName();
                        itemQuantity = String.valueOf(orderItem.getQuantity());
                        itemPrice = String.valueOf(orderItem.getPrice());
                    }
                    if (orderItem.getName().equals("40 Feet Countainer")) {
                        Log.e("BuyerDetail2", orderItem.getName() + ", " + orderItem.getQuantity());
                        keyFeet40 = orderItem.getKey();
                        itemOrder = itemOrder + "\n" + orderItem.getName();
                        itemQuantity = itemQuantity + "\n" + String.valueOf(orderItem.getQuantity());
                        itemPrice = itemPrice + "\n" + String.valueOf(orderItem.getPrice());
                    }
                    if (orderItem.getName().equals("20 Feet Countainer")) {
                        Log.e("BuyerDetail2", orderItem.getName() + ", " + orderItem.getQuantity());
                        keyFeet20 = orderItem.getKey();
                        itemOrder = itemOrder + "\n" + orderItem.getName();
                        itemQuantity = itemQuantity + "\n" + String.valueOf(orderItem.getQuantity());
                        itemPrice = itemPrice + "\n" + String.valueOf(orderItem.getPrice());
                    }


                    Log.e("BuyerDetail3", itemOrder + ", " + itemQuantity + ", " + itemPrice);

                    designQuantity = designQuantity + orderItem.getQuantity();


//                    Order order = dataSnapshot.getValue(Order.class);
                    //    String uid = newWeight.getUid();

//                    mOrderAdapter.add(order);

//                    Log.e("ProgGoalActivity", "onAdapter2 , uid = " + uid);
//                    Log.e("ProgGoalActivity", "onAdapter , uid = " + uid + " userid =" + userId);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.e("ProgGoalActivity", "onChanged");
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    mOrderAdapter.clear();
                    Log.e("ProgGoalActivity", "onRemoved");
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    Log.e("ProgGoalActivity", "onMoved");
                }

                public void onCancelled(DatabaseError databaseError) {
                    Log.e("ProgGoalActivity", "onCancel");
                }
            };
            firebaseHandler.getRefOrderItem().orderByChild("orderID").equalTo(orderNumber)
                    .addChildEventListener(mChildEventListenerOrderItem);
        }
    }


    private void detachOrderItemReadListener() {
        if (mChildEventListenerOrderItem != null) {
            firebaseHandler.getRefOrderItem().removeEventListener(mChildEventListenerOrderItem);
            mChildEventListenerOrderItem = null;
//            mOrderAdapter.clear();
        }
    }

    private void showSuccessConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Transaksi sudah slesai ?");
        builder.setPositiveButton("Selesai", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
//                firebaseHandler.getRefOrderUser().child("isPaidOff").setValue(true);

                Log.e("BUYERDetail", keyHighCube + ", " + keyFeet40);
                if (!TextUtils.isEmpty(keyHighCube)) {
                    firebaseHandler.getRefOrderItem().child(keyHighCube).removeValue();
                }
                if (!TextUtils.isEmpty(keyFeet40)) {
                    firebaseHandler.getRefOrderItem().child(keyFeet40).removeValue();
                }
                if (!TextUtils.isEmpty(keyFeet20)) {
                    firebaseHandler.getRefOrderItem().child(keyFeet20).removeValue();
                }

                firebaseHandler.getRefProfileUser().child("paidOff").setValue(true);

                String key = firebaseHandler.getRefHistory().push().getKey();
                History history = new History(uid, orderNumber, itemOrder, itemQuantity, String.valueOf(totalPrice));
                firebaseHandler.getRefHistory().child(key).setValue(history);

                firebaseHandler.getRefOrderUser().removeValue();
                finish();
            }
        });
        builder.setNegativeButton("Belum", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void downloadDesign() {



        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://countainer-sales-1a2b3.appspot.com/");

//                    Log.e("TipActivity", "tipList1 :" + title);

        String LOC_SEPARATOR = "design_file";
        String offsetLocation;
        String primaryLocation = null;

        if (uriDesign.contains(LOC_SEPARATOR)) {
            String[] location = uriDesign.split(LOC_SEPARATOR);
            offsetLocation = location[0];
            primaryLocation = LOC_SEPARATOR + location[1];
            Log.e("TipActivity", "location[1] : " + location[1] + ", primaryLocation : " + primaryLocation);
        }
        Log.e("TipActivity", "primaryLocation2 : " + primaryLocation);
        Log.e("TipActivity", "imageUri : " + uriDesign);


        final URL[] imageUrl = new URL[1];
        try {
            imageUrl[0] = new URL(uriDesign);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new GetImageTask().execute(imageUrl[0]);

//     location[1] : /tips/-Kp7noSHMMv1yhDGhw9-/3.jpg, primaryLocation : admin/tips/-Kp7noSHMMv1yhDGhw9-/3.jpg
//     primaryLocation2 : admin/tips/-Kp7noSHMMv1yhDGhw9-/3.jpg
//     imageUri : gs://weightcontrol-f645f.appspot.com/admin/tips/-Kp7noSHMMv1yhDGhw9-/3.jpg

//        final URL[] imageUrl = new URL[1];
//        storageRef.child(primaryLocation).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                // Got the download URL for 'users/me/profile.png'
//                Log.e("TipActivity", "Uri : " + uri);
//
//                try {
//                    imageUrl[0] = new URL(uri.toString());
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//                new GetImageTask().execute(uriDesign[0]);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//                Log.e("TipActivity", "gagal");
//            }
//        });

    }

    public class GetImageTask extends AsyncTask<URL, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... params) {
            URL imgUrl = params[0];
            Log.e("TipActivity", " imgUrl : " + imgUrl);
            Bitmap bitmap = null;
            try {
                bitmap = Glide.with(getApplicationContext()).
                        load(imgUrl).
                        asBitmap().
                        into(800, 600). // Width and height
                        get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap == null) {
                Log.e("BuyerDetail", "Bitmap tidak ada");
            } else {
                Log.e("BuyerDetail", "Bitmap ada");
                storeImage(bitmap);
//                base = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
//                Log.e("TipActivity", "base64 : " + base);
//                baseList.add(base);
//                if (baseList.size() == tipLists.size()) {
//                    save();
//                }
            }
        }
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        //creating a directory in SD card
        File mydir = new File(Environment.getExternalStorageDirectory()
                + StaticValue.PATH_ORDER_INVOICE); //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
        //getting the full path of the PDF report name
        String mPath = Environment.getExternalStorageDirectory().toString()
                + StaticValue.PATH_ORDER_INVOICE //PATH_PRODUCT_REPORT="/SIAS/REPORT_PRODUCT/"
                + orderNumber + ".pdf"; //reportName could be any name

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + StaticValue.PATH_DESIGN_FILE);

        File mediaStorageDir2 = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
//        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = orderNumber +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        loadingDialog2.dismiss();
        return mediaFile;
    }


    private void createInvoice() {

        Log.e("BUYERDetail", "totaPrice:" + totalPrice);

        pdfInvoiceGenerate.createPDF(this, "invoice", name, phoneNumber, address, orderNumber, itemOrder,
                itemQuantity, itemPrice, design, String.valueOf(designQuantity), String.valueOf(designPrice),
                String.valueOf(totalPrice));

    }


    private void showImageProfile() {
//        Glide.with(this).load(imageUrl)
//                .crossFade()
////                .thumbnail(0.5f)
////                .bitmapTransform(new CircleTransform(this))
//
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .override(800, 600)
//                .into(mDesignImageView);

        Glide.with(this)
                .load(imageUrl)
                .asBitmap()
                //.placeholder(R.drawable.default_placeholder)
                .override(400, 200) // Can be 2000, 2000
        .into(new BitmapImageViewTarget(mDesignTouchImageView) {
            @Override
            public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                super.onResourceReady(drawable, anim);
                mProgressBar.setVisibility(View.GONE);
                mDesignTouchImageView.setImageBitmap(drawable);
                mDesignTouchImageView.setVisibility(View.VISIBLE);
            }
        });
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event)
//    {
//        ImageView mDesignImageView = (ImageView) v;
//        mDesignImageView.setScaleType(ImageView.ScaleType.MATRIX);
//        float scale;
//
//        dumpEvent(event);
//        // Handle touch events here...
//
//        switch (event.getAction() & MotionEvent.ACTION_MASK)
//        {
//            case MotionEvent.ACTION_DOWN:   // first finger down only
//                savedMatrix.set(matrix);
//                start.set(event.getX(), event.getY());
//                Log.d(TAG, "mode=DRAG"); // write to LogCat
//                mode = DRAG;
//                break;
//
//            case MotionEvent.ACTION_UP: // first finger lifted
//
//            case MotionEvent.ACTION_POINTER_UP: // second finger lifted
//
//                mode = NONE;
//                Log.d(TAG, "mode=NONE");
//                break;
//
//            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down
//
//                oldDist = spacing(event);
//                Log.d(TAG, "oldDist=" + oldDist);
//                if (oldDist > 5f) {
//                    savedMatrix.set(matrix);
//                    midPoint(mid, event);
//                    mode = ZOOM;
//                    Log.d(TAG, "mode=ZOOM");
//                }
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//
//                if (mode == DRAG)
//                {
//                    matrix.set(savedMatrix);
//                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
//                }
//                else if (mode == ZOOM)
//                {
//                    // pinch zooming
//                    float newDist = spacing(event);
//                    Log.d(TAG, "newDist=" + newDist);
//                    if (newDist > 5f)
//                    {
//                        matrix.set(savedMatrix);
//                        scale = newDist / oldDist; // setting the scaling of the
//                        // matrix...if scale > 1 means
//                        // zoom in...if scale < 1 means
//                        // zoom out
//                        matrix.postScale(scale, scale, mid.x, mid.y);
//                    }
//                }
//                break;
//        }
//
//        mDesignImageView.setImageMatrix(matrix); // display the transformation on screen
//
//        return true; // indicate event was handled
//    }
//
//    /*
//     * --------------------------------------------------------------------------
//     * Method: spacing Parameters: MotionEvent Returns: float Description:
//     * checks the spacing between the two fingers on touch
//     * ----------------------------------------------------
//     */
//
//    private float spacing(MotionEvent event)
//    {
//        float x = event.getX(0) - event.getX(1);
//        float y = event.getY(0) - event.getY(1);
//        return (float) Math.sqrt(x * x + y * y);
//    }
//
//    /*
//     * --------------------------------------------------------------------------
//     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
//     * Description: calculates the midpoint between the two fingers
//     * ------------------------------------------------------------
//     */
//
//    private void midPoint(PointF point, MotionEvent event)
//    {
//        float x = event.getX(0) + event.getX(1);
//        float y = event.getY(0) + event.getY(1);
//        point.set(x / 2, y / 2);
//    }
//
//    /** Show an event in the LogCat view, for debugging */
//    private void dumpEvent(MotionEvent event)
//    {
//        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
//        StringBuilder sb = new StringBuilder();
//        int action = event.getAction();
//        int actionCode = action & MotionEvent.ACTION_MASK;
//        sb.append("event ACTION_").append(names[actionCode]);
//
//        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP)
//        {
//            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
//            sb.append(")");
//        }
//
//        sb.append("[");
//        for (int i = 0; i < event.getPointerCount(); i++)
//        {
//            sb.append("#").append(i);
//            sb.append("(pid ").append(event.getPointerId(i));
//            sb.append(")=").append((int) event.getX(i));
//            sb.append(",").append((int) event.getY(i));
//            if (i + 1 < event.getPointerCount())
//                sb.append(";");
//        }
//
//        sb.append("]");
//        Log.d("Touch Events ---------", sb.toString());
//    }
}
