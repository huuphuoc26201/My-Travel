package com.example.mytravel;

import static com.example.mytravel.Home.manggiohang;
import static com.example.mytravel.Home.manggiooder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adapter.orderDetailsAdapter;
import com.example.model.orderDetailsData;
import com.example.model.orderTourData;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import vn.momo.momo_partner.AppMoMoLib;

public class orderDetails extends AppCompatActivity {
    ListView lvgiohang;
    orderDetailsAdapter giohangAdapter;
    private EditText edtphone,edtname,edtemail;
    RadioButton tttructiep,ttmomo;
    Button datTour,huybo;
    TextView total,etextview;
    String temp,tentour,date,temp1,matour;
    long giatour;
    int nglon,treem;
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
        setContentView(R.layout.activity_order_details);
        total=(TextView) findViewById(R.id.textView42);
        etextview=(TextView) findViewById(R.id.etour) ;
        lvgiohang=(ListView) findViewById(R.id.listView);
        tttructiep=(RadioButton) findViewById(R.id.tructiep);
        ttmomo=(RadioButton) findViewById(R.id.momo);
        datTour=(Button) findViewById(R.id.datTour);
        huybo=(Button) findViewById(R.id.huyBo);
        edtemail=(EditText) findViewById(R.id.email);
        edtname=(EditText) findViewById(R.id.name);
        edtphone=(EditText) findViewById(R.id.phone);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION

        giohangAdapter=new orderDetailsAdapter(orderDetails.this, manggiohang);
        lvgiohang.setAdapter(giohangAdapter);
        EvenUltil();

        etextview.setVisibility(View.INVISIBLE);
        huybo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(orderDetails.this,booking.class);
                startActivity(intent);
            }
        });

        ttmomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateemail() | !validateName() | !validateSDT()) {

                }else {

                }
            }
        });

        datTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateemail() | !validateName() | !validateSDT()) {

                } else {
                    if (tttructiep.isChecked() == false && ttmomo.isChecked() == false) {
                        Toast.makeText(orderDetails.this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_LONG).show();
                    } else if (tttructiep.isChecked()) {
                        hideSoftKeyborard();
                        tourRealTime();
                        history();
                        giohangDatabase.getInstance(orderDetails.this).userDao().deleteAllgiohang();
                        Home.manggiohang.clear();
                        Intent intent = new Intent(orderDetails.this, booking.class);
                        startActivity(intent);
                    }
                    else if (ttmomo.isChecked()) {
                        hideSoftKeyborard();
                        tourRealTime();
                        Toast.makeText(orderDetails.this, "Thanh toán bằng MOMO", Toast.LENGTH_LONG).show();

                        requestPayment();
                        history();
                        giohangDatabase.getInstance(orderDetails.this).userDao().deleteAllgiohang();
                        Home.manggiohang.clear();
                        Intent intent = new Intent(orderDetails.this, booking.class);
                       startActivity(intent);
                    }
                }
            }

        });
    }

    private void history() {
        String etotal=total.getText().toString();
        String name=edtname.getText().toString().trim();
        String email=edtemail.getText().toString().trim();
        String phone = edtphone.getText().toString().trim();
        hideSoftKeyborard();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateTime = sdf.format(calendar.getTime());
        String data = "";
        for(int i=0;i< manggiohang.size();i++){
            tentour= manggiohang.get(i).getTentour();
            giatour= manggiohang.get(i).getGiatour();
            nglon= manggiohang.get(i).getNglon();
            treem= manggiohang.get(i).getTreem();
            date= manggiohang.get(i).getDate();
            data += "- "+tentour+" ( "+giatour+" ₫ ) \n"+"     Người lớn: "+nglon+"     Trẻ em: "+treem+"\n     Ngày đi: "+date +"\n";
        }
        etextview.setText(data);
        temp1=etextview.getText().toString();
        orderDetailsData orderDetailsData=new orderDetailsData(etotal, name, email, phone, temp1, "Thanh toán Trực tiếp",dateTime);
        orderDetailsData orderDetailsData1=new orderDetailsData(etotal, name, email, phone, temp1, "Thanh toán MoMo",dateTime);

        if (manggiooder.size() > 0) {
            boolean exists = false;
            for (int i = 0; i < manggiooder.size(); i++) {
                if (Home.manggiooder.get(i).getName() == name && Home.manggiooder.get(i).getEmail() == email) {
                    if(tttructiep.isChecked()){
                        historyDatabase.getInstance(this).historyDao().inserthistory(orderDetailsData);
                        manggiooder.add(orderDetailsData);
                    }
                    if(ttmomo.isChecked()){
                        historyDatabase.getInstance(this).historyDao().inserthistory(orderDetailsData1);
                        manggiooder.add(orderDetailsData1);
                    }
                    exists = true;
                }
            }
            if (exists == false) {
                if(tttructiep.isChecked()){
                    historyDatabase.getInstance(this).historyDao().inserthistory(orderDetailsData);
                    manggiooder.add(orderDetailsData);
                }
                if(ttmomo.isChecked()){
                    historyDatabase.getInstance(this).historyDao().inserthistory(orderDetailsData1);
                    manggiooder.add(orderDetailsData1);
                }
            }
        } else {

            if(tttructiep.isChecked()){
                historyDatabase.getInstance(this).historyDao().inserthistory(orderDetailsData);
                manggiooder.add(orderDetailsData);}
            if(ttmomo.isChecked()){
                historyDatabase.getInstance(this).historyDao().inserthistory(orderDetailsData1);
                manggiooder.add(orderDetailsData1);}
        }
    }

    private void tourRealTime() {
        String name=edtname.getText().toString().trim();
        String email=edtemail.getText().toString().trim();
        String phone = edtphone.getText().toString().trim();
        hideSoftKeyborard();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateTime = sdf.format(calendar.getTime());
        temp=total.getText().toString();
        String tour="";
        if(tttructiep.isChecked()){

            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference myref=database.getReference("BookingTour");
            orderDetailsData orderDetails=new orderDetailsData(temp,name,email,phone,tour,"Thanh toán trực tiếp,",dateTime);
            String pathname=String.valueOf(orderDetails.getName());
            if (name.equals(pathname)) {
                myref.child(pathname).setValue(orderDetails, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    }
                });
                for(int i=0;i< manggiohang.size();i++){
                    tentour= manggiohang.get(i).getTentour();
                    giatour= manggiohang.get(i).getGiatour();
                    nglon= manggiohang.get(i).getNglon();
                    treem= manggiohang.get(i).getTreem();
                    date= manggiohang.get(i).getDate();
                    matour=manggiohang.get(i).getMaTour();
                    orderTourData ordertour=new orderTourData(tentour,giatour,nglon,treem,date,matour);
                    myref.child(pathname).child("tour").child(String.valueOf(ordertour.getMaTour())).setValue(ordertour, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        }
                    });
                }
            }
        }
        if(ttmomo.isChecked()){
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference myref=database.getReference("BookingTour");
            orderDetailsData orderDetails=new orderDetailsData(temp,name,email,phone,tour,"Thanh toán MOMO",dateTime);
            String pathname=String.valueOf(orderDetails.getName());

            if (name.equals(pathname)) {
                myref.child(pathname).setValue(orderDetails, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    }
                });
                for(int i=0;i< manggiohang.size();i++){
                    tentour= manggiohang.get(i).getTentour();
                    giatour= manggiohang.get(i).getGiatour();
                    nglon= manggiohang.get(i).getNglon();
                    treem= manggiohang.get(i).getTreem();
                    date= manggiohang.get(i).getDate();
                    matour=manggiohang.get(i).getMaTour();
                    orderTourData ordertour=new orderTourData(tentour,giatour,nglon,treem,date,matour);
                    myref.child(pathname).child("tour").child(String.valueOf(ordertour.getMaTour())).setValue(ordertour, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        }
                    });
                }
            }
        }


    }



    //Get token through MoMo app
    private void requestPayment() {
        String ename=edtname.getText().toString().trim();
        String eemail=edtemail.getText().toString().trim();
        String ephone = edtphone.getText().toString().trim();
        temp=total.getText().toString();
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
        eventValue.put("amount", temp); //Kiểu integer
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
    //Get token callback from MoMo app an submit to server side
   /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    Log.d("thanh cong",data.getStringExtra("message"));

                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }

                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
                        Log.d("thanh cong","khong thanh cong");
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    Log.d("thanh cong","khong thanh cong");
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    Log.d("thanh cong","khong thanh cong");
                } else {
                    //TOKEN FAIL
                    Log.d("thanh cong","khong thanh cong");
                }
            } else {
                Log.d("thanh cong","khong thanh cong");
            }
        } else {
            Log.d("thanh cong","khong thanh cong");
        }
    }
    */

    private void EvenUltil() {
        long tongtien=0;
        for(int i=0;i< manggiohang.size();i++){
            tongtien+= manggiohang.get(i).getGiatour();
        }
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        total.setText(decimalFormat.format(tongtien)+" Đ");
    }
    public Boolean validateemail() {
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
    public void hideSoftKeyborard(){
        try{
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1)  {
            if (data != null && data.getIntExtra("status", -1) == 0) {
        // Thanh toán thành công
                Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
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