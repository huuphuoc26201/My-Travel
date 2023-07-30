package com.example.mytravel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.model.historyTourData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import vn.momo.momo_partner.AppMoMoLib;

public class payMentTour extends AppCompatActivity {
    TextView nameTour,total,maTour,fromDate,nguoiLon,treEm;
    ImageView imageTour;
    historyTourData historyTourData;
    EditText edtphone,edtname,edtemail;
    RadioButton tttructiep,ttmomo;
    Button datTour,huybo;
    String id,enameTour,etotal,emaTour,efromDate,enguoiLon,etreEm,eimageTour, ename,ephone,eemail;

    private String amount = "10000";

    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "HoangNgoc";
    private String merchantCode = "MOMOC2IC20220510";
    private String merchantNameLabel = "My Travel";
    private String description = "Thanh toán dịch vụ đặt tuor du lịch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymenttour);

        nameTour=findViewById(R.id.nameTour);
        total=findViewById(R.id.textView42);
        maTour=findViewById(R.id.maTour);
        fromDate=findViewById(R.id.fromDate);
        nguoiLon=findViewById(R.id.nguoilon);
        treEm=findViewById(R.id.treem);
        imageTour=findViewById(R.id.imageTour);

        edtemail=findViewById(R.id.email);
        edtname= findViewById(R.id.name);
        edtphone=findViewById(R.id.phone);
        tttructiep=findViewById(R.id.tructiep);
        ttmomo=findViewById(R.id.momo);

        datTour=findViewById(R.id.datTour);
        huybo=findViewById(R.id.huyBo);

        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
        setDataTour();
        setUserData();
        UpDataFirebase();

        huybo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(payMentTour.this,payTour.class);
                startActivity(intent);
            }
        });

        datTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateemail() | !validateName() | !validateSDT()) {

                } else {
                    if (tttructiep.isChecked() == false && ttmomo.isChecked() == false) {
                        Toast.makeText(payMentTour.this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_LONG).show();
                    } else if (ttmomo.isChecked()) {
                        hideSoftKeyborard();
                        AlertDialog.Builder ok = new AlertDialog.Builder(payMentTour.this);
                        ok.setTitle("Thanh toán");
                        ok.setMessage("Bạn đã chắc chắn thanh toán hóa đơn qua Momo?");
                        ok.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestPayment();

                            }
                        });
                        ok.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        ok.create().show();
                    }
                    else if (tttructiep.isChecked()) {
                        hideSoftKeyborard();
                        AlertDialog.Builder ok = new AlertDialog.Builder(payMentTour.this);
                        ok.setTitle("Thanh toán");
                        ok.setMessage("Bạn đã chắc chắn thanh toán hóa đơn trực tiếp?");
                        ok.setPositiveButton("CÓ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                UpDataFirebase();
                                deleteTour();
                                Toast.makeText(payMentTour.this, "Giao dịch thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(payMentTour.this,payTour.class);
                                startActivity(intent);
                            }
                        });
                        ok.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        ok.create().show();
                    }
                }
            }

        });

    }

    private void UpDataFirebase() {
        id=getIntent().getStringExtra("id");
        enameTour=nameTour.getText().toString();
        etotal=total.getText().toString();
        emaTour=getIntent().getStringExtra("maTour");
        efromDate=getIntent().getStringExtra("fromdate");
        enguoiLon=getIntent().getStringExtra("nguoilon");
        etreEm=getIntent().getStringExtra("treem");
        eimageTour=getIntent().getStringExtra("imageUrl");
        ename=edtname.getText().toString();
        ephone=edtphone.getText().toString();
        eemail=edtemail.getText().toString();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
        String dateTime = sdf.format(calendar.getTime());

        if (tttructiep.isChecked()) {
            historyTourData=new historyTourData(id,enameTour,etotal,eimageTour,efromDate,enguoiLon,etreEm,
                    emaTour,ename,ephone,eemail,"Thanh toán Trực tiếp",dateTime);
        }
        else if (ttmomo.isChecked()) {
            historyTourData=new historyTourData(id,enameTour,etotal,eimageTour,efromDate,
                    enguoiLon,etreEm,emaTour,ename,ephone,eemail,"Thanh toán Momo",dateTime);
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String eemail = user.getEmail();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tài Khoản");
        ref.orderByChild("email").equalTo(eemail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        com.example.model.user User = ds.getValue(com.example.model.user.class);
                        if (User != null) {
                            String key = User.getKey();
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("BookingTour").child(key);
                            myRef.child(id).setValue(historyTourData);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xảy ra lỗi trong quá trình đọc dữ liệu
            }
        });
    }
    private void setDataTour() {
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        Glide.with(payMentTour.this).load(getIntent().getStringExtra("imageUrl"))
                .into(imageTour);
        nameTour.setText(getIntent().getStringExtra("placeName"));
        total.setText(decimalFormat.format(Double.parseDouble(getIntent().getStringExtra("money")))+" ₫");
        maTour.setText("Mã Tour: "+getIntent().getStringExtra("maTour"));
        fromDate.setText("Ngày đi: "+getIntent().getStringExtra("fromdate"));
        nguoiLon.setText("Người lớn: "+getIntent().getStringExtra("nguoilon")+" vé");
        treEm.setText("Trẻ em: "+getIntent().getStringExtra("treem")+" vé");
    }
    private void setUserData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String eemail = user.getEmail();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tài Khoản");
        ref.orderByChild("email").equalTo(eemail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        com.example.model.user User = ds.getValue(com.example.model.user.class);
                        if (User != null) {
                            String name = User.getName();
                            String phone = User.getPhone();
                            String email = User.getEmail();
                            edtname.setText(name);
                            edtphone.setText(phone);
                            edtemail.setText(email);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xảy ra lỗi trong quá trình đọc dữ liệu
            }
        });

    }
    public boolean validateemail() {
        String val = edtemail.getText().toString();
        String emailPattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            edtemail.setError("Email không được để trống");
            return false;
        }else if(!val.matches(emailPattern)){
            edtemail.setError("Địa chỉ email không hợp lệ");
            return false;
        }
        else {
            edtemail.setError(null);
            return true;
        }
    }
    private boolean validateName() {
        String val = edtname.getText().toString();

        if (val.isEmpty()) {
            edtname.setError("Name không được để trống");
            return false;}
        else {
            edtname.setError(null);
            return true;
        }
    }
    private boolean validateSDT() {
        String val = edtphone.getText().toString();
        String sdt = "[0-9]{10,11}";
        if (val.isEmpty()) {
            edtphone.setError("SĐT không được để trống");
            return false;
        } else if (!val.matches(sdt)) {
            edtphone.setError("Số điện thoại phải có 10 đến 11 chữ số");
            return false;
        } else {
            edtphone.setError(null);
            return true;
        }
    }
    //Get token through MoMo app
    private void requestPayment() {
        String ename=edtname.getText().toString().trim();
        String eemail=edtemail.getText().toString().trim();
        String ephone = edtphone.getText().toString().trim();

        int amoutTotal= Integer.parseInt(getIntent().getStringExtra("money"));

        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        //if (edAmount.getText().toString() != null && edAmount.getText().toString().trim().length() != 0)
        // amount = edAmount.getText().toString().trim();
        Random rand = new Random();
        int randomNumber = rand.nextInt(90) + 10;
        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amoutTotal); //Kiểu integer
        eventValue.put("orderId", randomNumber); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", ename); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", merchantNameLabel);//gán nhãn
        eventValue.put("fee", fee); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("name", ename);
            objExtraData.put("email", eemail);
            objExtraData.put("phone", ephone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());
        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
    }

    public void hideSoftKeyborard(){
        try{
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private void deleteTour(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String eemail = user.getEmail();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tài Khoản");
        ref.orderByChild("email").equalTo(eemail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        com.example.model.user User = ds.getValue(com.example.model.user.class);
                        if (User != null) {
                            String key = User.getKey();
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("manyTour").child(key);
                            myRef.child(id).removeValue();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xảy ra lỗi trong quá trình đọc dữ liệu
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1)  {
            if (data != null && data.getIntExtra("status", -1) == 0) {
                // Thanh toán thành công
                Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                UpDataFirebase();
                deleteTour();
                Intent intent = new Intent(payMentTour.this, payTour.class);
                startActivity(intent);
            } else {
                // Thanh toán thất bại
                Toast.makeText(this, "Thanh toán thất bại!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Kết quả mã hoá thất bại hoặc hủy thanh toán
            Toast.makeText(this, "Mã hoá thất bại hoặc hủy thanh toán!", Toast.LENGTH_SHORT).show();
        }
    }
}