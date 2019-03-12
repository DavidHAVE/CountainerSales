package com.deva.android.countainersales.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.deva.android.countainersales.helper.FirebaseHandler;
import com.deva.android.countainersales.object.Cart;
import com.deva.android.countainersales.object.History;
import com.deva.android.countainersales.object.Item;
import com.deva.android.countainersales.object.Order;
import com.deva.android.countainersales.object.UserProfile;
import com.deva.android.countainersales.ui.OrderConfirmationActivity;
import com.deva.android.countainersales.R;
import com.deva.android.countainersales.helper.StaticValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.pdf.parser.Line;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.R.attr.languageTag;
import static android.R.attr.name;
import static android.R.attr.order;
import static android.R.attr.phoneNumber;
import static android.R.attr.tension;
import static android.media.CamcorderProfile.get;

public class OrderFragment extends Fragment implements View.OnClickListener {

    ScrollView mScrollView;
    CardView mNextOrderCardView;
    Button mHighCubeMinusButton, mHighCubeAddButton, mFeet40MinusButton, mFeet40AddButton, mFeet20MinusButton, mFeet20AddButton,
            mNextOrderButton;
    Button mAddItem0Button, mAddItem1Button, mAddItem2Button;
    TextView mHighCubeCountTextView, mFeet40CountTextView, mFeet20CountTextView, mTotalOrderPriceTextView;
    LinearLayout mPlusMinus0Layout, mPlusMinus1Layout, mPlusMinus2Layout;
    int countHighCube, countFeet40, countFeet20;
    long highCubePrice = 300;
    long feet40Price = 200;
    long feet20Price = 100;
    long totalHighCubePrice, totalFeet40Price, totalFeet20Price;
    long totalOrderPrice;
    String keyHighCube;
    String keyFeet40;
    String keyFeet20;
    String key;
    List<Integer> productItemID = null;
    List<Integer> productID = null;
    List<Integer> quantity = null;
    private String name, email, phoneNumber, address;
    private boolean isPaidOff = false;
    private FirebaseUser user;
    private String userId;
    private FirebaseHandler firebaseHandler;
    private ChildEventListener mChildEventListenerHistory;
    private ChildEventListener mChildEventListenerOrder;
    private ChildEventListener mChildEventListenerCart;
    private ChildEventListener mChildEventListenerItem;

    private String orderNumber;

//    private String isPaidOff = null;
//    private Boolean isPaidOffOrder = false;

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

//            if (productID.size() > 0) {
//                order();
//            }

//            if (productID.size() > 0) {
            if (countHighCube > 0 || countFeet40 > 0 || countFeet20 > 0) {

                totalHighCubePrice = countHighCube * highCubePrice;
                totalFeet40Price = countFeet40 * feet40Price;
                totalFeet20Price = countFeet20 * feet20Price;
                Log.e("ORDERFRAGMENNTT", "pID:"+productID.get(0));
//                order();
                totalOrderPrice = totalHighCubePrice + totalFeet40Price + totalFeet20Price;
                mTotalOrderPriceTextView.setText(String.valueOf(totalOrderPrice));
                mNextOrderCardView.setVisibility(View.VISIBLE);
                mScrollView.setPadding(0, 0, 0, 300);

            } else {
                mNextOrderCardView.setVisibility(View.GONE);
                mScrollView.setPadding(0, 0, 0, 0);
            }

//            }
            checkQuantity();
            loadingDialog.dismiss();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);

        mScrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
        mNextOrderCardView = (CardView) rootView.findViewById(R.id.next_order_card_view);
        mHighCubeMinusButton = (Button) rootView.findViewById(R.id.high_cube_minus_button);
        mHighCubeAddButton = (Button) rootView.findViewById(R.id.high_cube_add_button);
        mFeet40MinusButton = (Button) rootView.findViewById(R.id.feet_40_minus_button);
        mFeet40AddButton = (Button) rootView.findViewById(R.id.feet_40_add_button);
        mFeet20MinusButton = (Button) rootView.findViewById(R.id.feet_20_minus_button);
        mFeet20AddButton = (Button) rootView.findViewById(R.id.feet_20_add_button);
        mHighCubeCountTextView = (TextView) rootView.findViewById(R.id.high_cube_count_text_view);
        mFeet40CountTextView = (TextView) rootView.findViewById(R.id.feet_40_count_text_view);
        mFeet20CountTextView = (TextView) rootView.findViewById(R.id.feet_20_count_text_view);
        mTotalOrderPriceTextView = (TextView) rootView.findViewById(R.id.total_order_price_text_view);
        mNextOrderButton = (Button) rootView.findViewById(R.id.next_order_button);
        mAddItem0Button = (Button) rootView.findViewById(R.id.add_item0_button);
        mAddItem1Button = (Button) rootView.findViewById(R.id.add_item1_button);
        mAddItem2Button = (Button) rootView.findViewById(R.id.add_item2_button);
        mPlusMinus0Layout = (LinearLayout) rootView.findViewById(R.id.plus_minus0_layout);
        mPlusMinus1Layout = (LinearLayout) rootView.findViewById(R.id.plus_minus1_layout);
        mPlusMinus2Layout = (LinearLayout) rootView.findViewById(R.id.plus_minus2_layout);

        mNextOrderCardView.setVisibility(View.GONE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        firebaseHandler = new FirebaseHandler(getActivity(), userId);

        productItemID = new ArrayList<>();

        productID = new ArrayList<>();
        quantity = new ArrayList<>();

        mHighCubeMinusButton.setOnClickListener(this);
        mHighCubeAddButton.setOnClickListener(this);
        mFeet40MinusButton.setOnClickListener(this);
        mFeet40AddButton.setOnClickListener(this);
        mFeet20MinusButton.setOnClickListener(this);
        mFeet20AddButton.setOnClickListener(this);
        mNextOrderButton.setOnClickListener(this);
        mAddItem0Button.setOnClickListener(this);
        mAddItem1Button.setOnClickListener(this);
        mAddItem2Button.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.high_cube_minus_button) {
            if (countHighCube > 0) {
                countHighCube = countHighCube - 1;
                firebaseHandler.getRefCart().child(keyHighCube).child("quantity").setValue(countHighCube);
                mHighCubeCountTextView.setText(String.valueOf(countHighCube));
                order();
                if (countHighCube == 0) {
                    firebaseHandler.getRefCart().child(keyHighCube).removeValue();
                }
            }
        } else if (id == R.id.high_cube_add_button) {
            countHighCube = countHighCube + 1;
            firebaseHandler.getRefCart().child(keyHighCube).child("quantity").setValue(countHighCube);
            mHighCubeCountTextView.setText(String.valueOf(countHighCube));
            order();
        } else if (id == R.id.feet_40_minus_button) {
            if (countFeet40 > 0) {
                countFeet40 = countFeet40 - 1;
                firebaseHandler.getRefCart().child(keyFeet40).child("quantity").setValue(countFeet40);
                mFeet40CountTextView.setText(String.valueOf(countFeet40));
                order();
                if (countFeet40 == 0) {
                    firebaseHandler.getRefCart().child(keyFeet40).removeValue();
                }
            }
            if (countHighCube == 0) {
                mAddItem1Button.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.feet_40_add_button) {
            countFeet40 = countFeet40 + 1;
            firebaseHandler.getRefCart().child(keyFeet40).child("quantity").setValue(countFeet40);
            mFeet40CountTextView.setText(String.valueOf(countFeet40));
            order();
        } else if (id == R.id.feet_20_minus_button) {
            if (countFeet20 > 0) {
                countFeet20 = countFeet20 - 1;
                firebaseHandler.getRefCart().child(keyFeet20).child("quantity").setValue(countFeet20);
                mFeet20CountTextView.setText(String.valueOf(countFeet20));
                order();
                if (countFeet20 == 0) {
                    firebaseHandler.getRefCart().child(keyFeet20).removeValue();
                }
            }
        } else if (id == R.id.feet_20_add_button) {
            countFeet20 = countFeet20 + 1;
            firebaseHandler.getRefCart().child(keyFeet20).child("quantity").setValue(countFeet20);
            mFeet20CountTextView.setText(String.valueOf(countFeet20));
            order();
        } else if (id == R.id.next_order_button) {

            Log.e("ORDERFRAG", countHighCube + ", " + countFeet40);
            Intent i = new Intent(getContext(), OrderConfirmationActivity.class);
            i.putExtra(StaticValue.NAME, name);
            i.putExtra(StaticValue.EMAIL, email);
            i.putExtra(StaticValue.PHONE_NUMBER, phoneNumber);
            i.putExtra(StaticValue.ADDRESS, address);
            i.putExtra(StaticValue.KEY_HIGH_CUBE, keyHighCube);
            i.putExtra(StaticValue.KEY_FEET_40, keyFeet40);
            i.putExtra(StaticValue.KEY_FEET_20, keyFeet20);
            i.putExtra(StaticValue.HIGH_CUBE_COUNT, countHighCube);
            i.putExtra(StaticValue.FEET_40_COUNT, countFeet40);
            i.putExtra(StaticValue.FEET_20_COUNT, countFeet20);
            i.putExtra(StaticValue.ORDER_ID, orderNumber);

//            i.putExtra(StaticValue.HIGH_CUBE_PRICE, totalHighCubePrice);
//            i.putExtra(StaticValue.FEET_40_PRICE, totalFeet40Price);
//            i.putExtra(StaticValue.FEET_20_PRICE, totalFeet20Price);

            i.putExtra(StaticValue.ESTIMATE, totalOrderPrice);
            startActivity(i);
            getActivity().finish();
        } else if (id == R.id.add_item0_button) {
//            String key = firebaseHandler.getRefEatPlan().push().getKey();
//            EatPlan eatPlan = new EatPlan(userId, key, currentDate, currentTime, name, calorie,
//                    carbohydrate, protein, fat, quality);
//            firebaseHandler.getRefEatPlan().child(key).setValue(eatPlan);
            Log.e("OrderFRAG", "isPaidOff:" + isPaidOff + ", isPaidOffOrder:" + isPaidOff);
            if (isPaidOff) {
                countHighCube = 1;

                keyHighCube = firebaseHandler.getRefCart().push().getKey();
                Cart cart = new Cart(userId, orderNumber, 0, countHighCube, 0, keyHighCube);
                firebaseHandler.getRefCart().child(keyHighCube).setValue(cart);

                mAddItem0Button.setVisibility(View.GONE);
                mPlusMinus0Layout.setVisibility(View.VISIBLE);
                order();
            } else {
                Toast.makeText(getContext(), "Harus menyelesaikan pembayaran sebelumnya.", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.add_item1_button) {
            if (isPaidOff) {
                countFeet40 = 1;

                keyFeet40 = firebaseHandler.getRefCart().push().getKey();
                Cart cart = new Cart(userId, orderNumber, 1, countFeet40, 0, keyFeet40);
                firebaseHandler.getRefCart().child(keyFeet40).setValue(cart);

                mAddItem1Button.setVisibility(View.GONE);
                mPlusMinus1Layout.setVisibility(View.VISIBLE);
                order();
            } else {
                Toast.makeText(getContext(), "Harus menyelesaikan pembayaran sebelumnya.", Toast.LENGTH_SHORT).show();
            }
        }else if (id == R.id.add_item2_button) {
            if (isPaidOff) {
                countFeet20 = 1;

                keyFeet20 = firebaseHandler.getRefCart().push().getKey();
                Cart cart = new Cart(userId, orderNumber, 1, countFeet20, 0, keyFeet20);
                firebaseHandler.getRefCart().child(keyFeet20).setValue(cart);

                mAddItem2Button.setVisibility(View.GONE);
                mPlusMinus2Layout.setVisibility(View.VISIBLE);
                order();
            } else {
                Toast.makeText(getContext(), "Harus menyelesaikan pembayaran sebelumnya.", Toast.LENGTH_SHORT).show();
            }
        }
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
                    Log.e("ORDEFRAG", "userId:" + userId);

//                    readItem();
                    userProfileRead();
//                    userOrderRead();
//                    attachOrderReadListener();
//                    attachHistoryReadListener();
//                    attacthReadListenerItem();
                    attactReadListenerCart();
                    sleep(3000);
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
//        attactReadListenerCart();
        countHighCube = 0;
        countFeet40 = 0;
        countFeet20 = 0;
        startTread();
    }

    @Override
    public void onPause() {
        super.onPause();
//        detachOrderReadListener();
//        detachHistoryReadListener();
//        detachDatabaseReadListenerItem();
        detachDatabaseReadListenerCart();
    }

    private void readItem() {
        firebaseHandler.getRefItem().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Item item = dataSnapshot.getValue(Item.class);

//                productItemID.add(item.getProductID());
                if (item.getProductID() == 0) {
                    highCubePrice = item.getPrice();
                } else if (item.getProductID() == 1) {
                    feet40Price = item.getPrice();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void userProfileRead() {
        firebaseHandler.getRefProfileUser().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                String uid = userProfile.getUid();
                name = userProfile.getName();
                email = userProfile.getEmail();
                phoneNumber = userProfile.getPhoneNumber();
                address = userProfile.getAddress();
                isPaidOff = userProfile.isPaidOff();

                Log.e("ORDERFRAG", "isPaidOff:" + isPaidOff);

                String uidSubstr = uid.substring(20);
                Random rand = new Random();
                int n = rand.nextInt(500) + 1;

                Log.e("OrderFRAG", "random:" + n);


                orderNumber = uidSubstr + n;

                Log.e("OrderFRAG", "uidSubs:" + uidSubstr + ", orderNumber:" + orderNumber);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("AccountFragment", "getUser:onCancelled", databaseError.toException());
            }
        });
    }


    private void attacthReadListenerItem() {
        if (mChildEventListenerItem == null) {

            mChildEventListenerItem = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Item item = dataSnapshot.getValue(Item.class);

//                    productID.add(cart.getProductID());
//                    quantity.add(cart.getQuantity());

                    Log.e("ORDERFRAGMENTTTTT", "productID :"+item.getProductID());
                    if (item.getProductID() == 0) {
                        highCubePrice = item.getPrice();
                    } else if (item.getProductID() == 1) {
                        feet40Price = item.getPrice();
                    } else if (item.getProductID() == 2){
                        feet20Price = item.getPrice();
                    }
//
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            firebaseHandler.getRefItem().orderByChild("uid").equalTo(userId).
                    addChildEventListener(mChildEventListenerItem);
        }
    }

    private void detachDatabaseReadListenerItem() {
        if (mChildEventListenerItem != null) {
            firebaseHandler.getRefItem().removeEventListener(mChildEventListenerItem);
            mChildEventListenerItem = null;
        }
    }


    private void attactReadListenerCart() {
        if (mChildEventListenerCart == null) {

            mChildEventListenerCart = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Cart cart = dataSnapshot.getValue(Cart.class);

                    productID.add(cart.getProductID());
//                    quantity.add(cart.getQuantity());

                    if (cart.getProductID() == 0) {
                        keyHighCube = cart.getKey();
                        countHighCube = cart.getQuantity();
                        mHighCubeCountTextView.setText(String.valueOf(countHighCube));
                    } else if (cart.getProductID() == 1) {
                        keyFeet40 = cart.getKey();
                        countFeet40 = cart.getQuantity();
                        mFeet40CountTextView.setText(String.valueOf(countFeet40));
                    } else if (cart.getProductID() == 2){
                        keyFeet20 = cart.getKey();
                        countFeet20 = cart.getQuantity();
                        mFeet20CountTextView.setText(String.valueOf(countFeet20));
                    }

//                    for (int i = 0; i < productID.size(); i++) {
//                        for (int n = 0; i < quantity.size(); n++) {
//                            if (productID.get(i) == 0) {
//                                countHighCube = quantity.get(i);
//                                mHighCubeCountTextView.setText(String.valueOf(countHighCube));
//                            } else if (productID.get(i) == 1) {
//                                countFeet40 = quantity.get(i);
//                                mFeet40CountTextView.setText(String.valueOf(countFeet40));
//                            }
////                            Log.e("EatPlan", "calorieini = " + kaloriLatihan);
//                        }
//                    }
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            firebaseHandler.getRefCart().orderByChild("uid").equalTo(userId).
                    addChildEventListener(mChildEventListenerCart);
        }
    }

    private void detachDatabaseReadListenerCart() {
        if (mChildEventListenerCart != null) {
            firebaseHandler.getRefCart().removeEventListener(mChildEventListenerCart);
            mChildEventListenerCart = null;
        }
    }

    private void order() {

        if (countHighCube > 0 || countFeet40 > 0 || countFeet20 > 0) {
//            totalHighCubePrice = totalHighCubePrice - highCubePrice;
//            totalHighCubePrice = totalHighCubePrice + highCubePrice;
//            totalFeet40Price = totalFeet40Price - feet40Price;
//            totalFeet40Price = totalFeet40Price + feet40Price;
//            totalFeet20Price = totalFeet20Price - feet20Price;
//            totalFeet20Price = totalFeet20Price + feet20Price;

            totalHighCubePrice = countHighCube * highCubePrice;
            totalFeet40Price = countFeet40 * feet40Price;
            totalFeet20Price = countFeet20 * feet20Price;

            Log.e("Order Fragment", "highCubePrice :" + totalHighCubePrice + ", feet40Price : " + totalFeet40Price + ", " +
                    "feet20Price : " + totalFeet20Price);

            if (!TextUtils.isEmpty(keyHighCube)) {
                firebaseHandler.getRefCart().child(keyHighCube).child("totalPrice").setValue(totalHighCubePrice);
            }
            if (!TextUtils.isEmpty(keyFeet40)) {
                firebaseHandler.getRefCart().child(keyFeet40).child("totalPrice").setValue(totalFeet40Price);
            }
            if (!TextUtils.isEmpty(keyFeet20)) {
                firebaseHandler.getRefCart().child(keyFeet20).child("totalPrice").setValue(totalFeet20Price);
            }

            totalOrderPrice = totalHighCubePrice + totalFeet40Price + totalFeet20Price;
            mTotalOrderPriceTextView.setText(String.valueOf(totalOrderPrice));
            mNextOrderCardView.setVisibility(View.VISIBLE);
            mScrollView.setPadding(0, 0, 0, 300);

        } else {
            mNextOrderCardView.setVisibility(View.GONE);
            mScrollView.setPadding(0, 0, 0, 0);
        }

        checkQuantity();
    }

    private void checkQuantity() {
        if (countHighCube == 0) {
            mAddItem0Button.setVisibility(View.VISIBLE);
            mPlusMinus0Layout.setVisibility(View.GONE);
        } else {
            mAddItem0Button.setVisibility(View.GONE);
            mPlusMinus0Layout.setVisibility(View.VISIBLE);
        }
        if (countFeet40 == 0) {
            mAddItem1Button.setVisibility(View.VISIBLE);
            mPlusMinus1Layout.setVisibility(View.GONE);
        } else {
            mAddItem1Button.setVisibility(View.GONE);
            mPlusMinus1Layout.setVisibility(View.VISIBLE);
        }
        if (countFeet20 == 0) {
            mAddItem2Button.setVisibility(View.VISIBLE);
            mPlusMinus2Layout.setVisibility(View.GONE);
        } else {
            mAddItem2Button.setVisibility(View.GONE);
            mPlusMinus2Layout.setVisibility(View.VISIBLE);
        }
    }

    private void attachHistoryReadListener() {
        if (mChildEventListenerHistory == null) {
            mChildEventListenerHistory = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    History history = dataSnapshot.getValue(History.class);
                    //    String uid = newWeight.getUid();

//                    if (history.getPaidOff() == null) {
//                        isPaidOff = false;
//                    }else {
//                        isPaidOff = true;
//                    }

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
            firebaseHandler.getRefHistory().orderByChild("uid").equalTo(userId)
                    .addChildEventListener(mChildEventListenerHistory);
        }
    }

    private void detachHistoryReadListener() {
        if (mChildEventListenerHistory != null) {
            firebaseHandler.getRefHistory().removeEventListener(mChildEventListenerHistory);
            mChildEventListenerHistory = null;
//            mOrderAdapter.clear();
        }
    }

    private void userOrderRead() {
        firebaseHandler.getRefOrderUser().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.getValue(Order.class);

//                isPaidOffOrder = order.getPaidOff();
//                if (isPaidOffOrder == null) {
//                    isPaidOffOrder = true;
//                }else {
//                    isPaidOffOrder = false;
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("AccountFragment", "getUser:onCancelled", databaseError.toException());
            }
        });
    }
//    private void attachOrderReadListener() {
//        if (mChildEventListenerOrder == null) {
//            mChildEventListenerOrder = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    Order order = dataSnapshot.getValue(Order.class);
//                    //    String uid = newWeight.getUid();
//
//                    isPaidOffOrder = order.isPaidOff();
//
////                    Log.e("ProgGoalActivity", "onAdapter2 , uid = " + uid);
////                    Log.e("ProgGoalActivity", "onAdapter , uid = " + uid + " userid =" + userId);
//                }
//
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                    Log.e("ProgGoalActivity", "onChanged");
//                }
//
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
////                    mOrderAdapter.clear();
//                    Log.e("ProgGoalActivity", "onRemoved");
//                }
//
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                    Log.e("ProgGoalActivity", "onMoved");
//                }
//
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.e("ProgGoalActivity", "onCancel");
//                }
//            };
//            firebaseHandler.getRefOrder().orderByChild("orderID").equalTo(orderNumber)
//                    .addChildEventListener(mChildEventListenerOrder);
//        }
//    }
//
//    private void detachOrderReadListener() {
//        if (mChildEventListenerOrder != null) {
//            firebaseHandler.getRefOrder().removeEventListener(mChildEventListenerOrder);
//            mChildEventListenerOrder = null;
////            mOrderAdapter.clear();
//        }
//    }
}
