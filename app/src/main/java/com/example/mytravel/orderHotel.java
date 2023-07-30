package com.example.mytravel;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.model.bookHotelData;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import vn.momo.momo_partner.AppMoMoLib;

public class orderHotel extends AppCompatActivity {
    EditText edtName,edtPhone,edtEmail,edtPersons,edtChild,edtTotal,numberRooms,fromDate,toDate;
    ImageView imgFromdate,imgTodate;
    RadioButton tttructiep,ttmomo;
    TextView payTotal;
    int numberroom=0;
    private Calendar calendar;
    private bookHotelData bookHotelData;
    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "HoangNgoc";
    private String merchantCode = "MOMOC2IC20220510";
    private String merchantNameLabel = "My Travel";
    private String description = "Thanh toán dịch vụ đặt phòng khách sạn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhotel);

        edtName=findViewById(R.id.name);
        edtPhone=findViewById(R.id.phone);
        edtEmail=findViewById(R.id.email);
        edtPersons=findViewById(R.id.persons);
        edtChild=findViewById(R.id.child);
        edtTotal=findViewById(R.id.total);
        payTotal=findViewById(R.id.paytotal);
        numberRooms=findViewById(R.id.numberRooms);
        fromDate=findViewById(R.id.chonngay);
        toDate=findViewById(R.id.denngay);
        imgFromdate=findViewById(R.id.fromdate);
        imgTodate=findViewById(R.id.todate);
        tttructiep=findViewById(R.id.tructiep);
        ttmomo=findViewById(R.id.momo);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
        calendar = Calendar.getInstance();


        setDataUser();
        // Thiết lập sự kiện click cho button
        fromDate.setOnClickListener(v -> {
            hideSoftKeyborard();
            showFromDatePickerDialog();
        });
        imgFromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyborard();
                showFromDatePickerDialog();
            }
        });

        toDate.setOnClickListener(v -> {
            hideSoftKeyborard();
            showToDatePickerDialog();
        });
        imgTodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyborard();
                showToDatePickerDialog();
            }
        });

        //payment
        ttmomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateemail() | !validateName() | !validateSDT()|!validatenumber()|!validatenumberRooms()|!validateFromDate()|!validateToDate()) {
                }
                else {
                    totalamount();
                    hideSoftKeyborard();
                    AlertDialog.Builder ok = new AlertDialog.Builder(orderHotel.this);
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
            }
        });

        tttructiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateemail() | !validateName() | !validateSDT() | !validatenumber() |!validatenumberRooms()|!validateFromDate()|!validateToDate()) {
                }else if((Integer.parseInt(edtPersons.getText().toString())+Integer.parseInt(edtChild.getText().toString()))>4*Integer.parseInt(numberRooms.getText().toString())){
                    Toast.makeText(orderHotel.this, "1 phòng chỉ tối đa 4 người", Toast.LENGTH_SHORT).show();
                }
                else {
                    hideSoftKeyborard();
                    AlertDialog.Builder ok = new AlertDialog.Builder(orderHotel.this);
                    ok.setTitle("Thanh toán");
                    ok.setMessage("Bạn đã chắc chắn thanh toán hóa đơn trực tiếp?");
                    ok.setPositiveButton("CÓ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getDataPayHotel();
                            Toast.makeText(orderHotel.this, "Giao dịch thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(orderHotel.this,Hotel.class);
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
        });


    }

    private void setDataUser() {
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
                            edtName.setText(name);
                            edtPhone.setText(phone);
                            edtEmail.setText(email);
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


    //Get token through MoMo app
    private void requestPayment() {
        String ename=edtName.getText().toString().trim();
        String eemail=edtEmail.getText().toString().trim();
        String ephone = edtPhone.getText().toString().trim();
        String total = payTotal.getText().toString().trim();
        int amount1= Integer.parseInt(total);
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Random rand = new Random();
        int randomNumber = rand.nextInt(90) + 10;
        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount1); //Kiểu integer
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
    public String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(10);
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    private void getDataPayHotel() {
        String fullName=edtName.getText().toString();
        String Phone=edtPhone.getText().toString();
        String email=edtEmail.getText().toString();
        String persons=edtPersons.getText().toString();
        String child=edtChild.getText().toString();
        hideSoftKeyborard();
        String from=fromDate.getText().toString();
        String to=toDate.getText().toString();
        String numberrooms=numberRooms.getText().toString();
        String total=edtTotal.getText().toString();
        String nameHotel = getIntent().getStringExtra("name");
        String id=generateRandomString();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateTime = sdf.format(calendar.getTime());

        if (tttructiep.isChecked()) {
            bookHotelData=new bookHotelData(fullName,Phone,email,persons,child,numberrooms,from,to,total,"Thanh toán Trực tiếp",dateTime,nameHotel,id);
        }
        else if (ttmomo.isChecked()) {
            bookHotelData=new bookHotelData(fullName,Phone,email,persons,child,numberrooms,from,to,total,"Thanh toán Momo",dateTime,nameHotel,id);
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
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("BookingHotel").child(key);
                            myRef.child(id).setValue(bookHotelData);
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

    private boolean validateToDate() {
        String val = toDate.getText().toString();

        if (val.isEmpty()) {
            toDate.setError("To Date không được để trống");
            return false;}
        else {
            toDate.setError(null);
            return true;
        }
    }

    private boolean validateFromDate() {
        String val = fromDate.getText().toString();

        if (val.isEmpty()) {
            fromDate.setError("From Date không được để trống");
            return false;}
        else {
            fromDate.setError(null);
            return true;
        }
    }
    private boolean validatenumberRooms() {
        String val = numberRooms.getText().toString();
        if (val.isEmpty()) {
            numberRooms.setError("Không được để trống tin");
            return false;
        }else if(val.equals("0")){
            numberRooms.setError("Vui lòng nhập số phòng");
            return false;
        }
        else {
            numberRooms.setError(null);
            return true;
        }
    }

    private boolean validatenumber() {
        String val = edtPersons.getText().toString();
        String val1 = edtChild.getText().toString();

        if (val.isEmpty() && val1.isEmpty()) {
            edtPersons.setError("Không được để trống thông tin");
            edtChild.setError("Không được để trống thông tin");
            return false;}
        else {
            edtPersons.setError(null);
            edtChild.setError(null);
            return true;
        }
    }


    private boolean validateSDT() {
        String val = edtPhone.getText().toString();
        String sdt = "[0-9]{10,11}";
        if (val.isEmpty()) {
            edtPhone.setError("SĐT không được để trống");
            return false;
        } else if (!val.matches(sdt)) {
            edtPhone.setError("Số điện thoại phải có 10 đến 11 chữ số");
            return false;
        } else {
            edtPhone.setError(null);
            return true;
        }
    }

    private boolean validateName() {
        String val = edtName.getText().toString();

        if (val.isEmpty()) {
            edtName.setError("Name không được để trống");
            return false;}
        else {
            edtName.setError(null);
            return true;
        }
    }

    private boolean validateemail() {
        String val = edtEmail.getText().toString();
        String emailPattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            edtEmail.setError("Email không được để trống");
            return false;
        }else if(!val.matches(emailPattern)){
            edtEmail.setError("Địa chỉ email không hợp lệ");
            return false;
        }
        else {
            edtEmail.setError(null);
            return true;
        }
    }

    private void showFromDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(orderHotel.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        fromDate.setText(selectedDate);
                        calendar.set(year, month, dayOfMonth); // Cập nhật lại calendar khi ngày tháng được chọn
                    }
                }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Đặt giá trị tối thiểu là thời gian hiện tại - 1 giây
        datePickerDialog.show();
    }

    private void showToDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(orderHotel.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        toDate.setText(selectedDate);
                        calendar.set(year, month, dayOfMonth); // Cập nhật lại calendar khi ngày tháng được chọn
                        totalamount();
                    }
                }, year, month, day);

        // Ẩn các ngày trước ngày fromDate
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date minDate = sdf.parse(fromDate.getText().toString());
            datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            // Xử lý nếu có lỗi xảy ra khi chuyển đổi chuỗi thành đối tượng Date
        }

        datePickerDialog.show();
    }

    public void add(View view){
        hideSoftKeyborard();
        if (!validatenumber()) {
        }
        else {
            numberroom++;
            numberRooms.setText(Integer.toString(numberroom));
            totalamount();
        }
    }
    public void sub(View view) {
        hideSoftKeyborard();
        if (!validatenumber()) {
        }
        else {
            if (numberroom > 0) {
                numberroom--;
                numberRooms.setText(Integer.toString(numberroom));
                totalamount();
            }
        }
    }

    private void totalamount() {

        String persons=edtPersons.getText().toString();
        String child=edtChild.getText().toString();
        String from=fromDate.getText().toString();
        String to=toDate.getText().toString();

        Integer total  =0;
        String money = getIntent().getStringExtra("money");

        if (persons.isEmpty()){
            persons="0";
        } else if (child.isEmpty()){
            child="0";
        }
        if(from.isEmpty()){
            from="1/1/2023";
        }
        if(to.isEmpty()){
            to="1/1/2023";
        }
        // Định dạng ngày/tháng/năm
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            // Chuyển đổi string thành ngày
            Date fromdate = dateFormat.parse(from);
            Date todate = dateFormat.parse(to);

            // Tạo đối tượng Calendar và thiết lập ngày từ và ngày đến
            Calendar fromCalendar = Calendar.getInstance();
            fromCalendar.setTime(fromdate);

            Calendar toCalendar = Calendar.getInstance();
            toCalendar.setTime(todate);

            // Tính số ngày giữa 2 ngày
            long diffInMillis = toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis();
            int diffInDays = (int) (diffInMillis / (24 * 60 * 60 * 1000));

            if((Integer.parseInt(persons)+Integer.parseInt(child))>4*Integer.parseInt(numberRooms.getText().toString())){
                Toast.makeText(this, "1 phòng chỉ tối đa 4 người", Toast.LENGTH_SHORT).show();
                numberRooms.setTextColor(getResources().getColor(R.color.red));
                numberRooms.setBackgroundResource(R.drawable.round_bordereror);
            }
            else{
                DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
                total  = Integer.parseInt(money)*Integer.parseInt(numberRooms.getText().toString())*(diffInDays+1);
                edtTotal.setText(decimalFormat.format(total)+" Đ");
                numberRooms.setTextColor(getResources().getColor(R.color.black));
                numberRooms.setBackgroundResource(R.drawable.round_border);
                payTotal.setText(String.valueOf(total));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }




    }
    public void pev(View view){
        Intent intent=new Intent(orderHotel.this,bookHotel.class);
        intent.putExtra("imageUrl",getIntent().getStringExtra("imageUrl"));
        intent.putExtra("imageUrl1",getIntent().getStringExtra("imageUrl1"));
        intent.putExtra("imageUrl2",getIntent().getStringExtra("imageUrl2"));
        intent.putExtra("imageUrl3",getIntent().getStringExtra("imageUrl3"));
        intent.putExtra("about",getIntent().getStringExtra("about"));
        intent.putExtra("diachi",getIntent().getStringExtra("diachi"));
        intent.putExtra("name",getIntent().getStringExtra("name"));
        intent.putExtra("danhgia",getIntent().getStringExtra("danhgia"));
        intent.putExtra("gia",getIntent().getStringExtra("gia"));
        startActivity(intent);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1)  {
            if (data != null && data.getIntExtra("status", -1) == 0) {
                // Thanh toán thành công
                Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                getDataPayHotel();
                Intent intent = new Intent(orderHotel.this, Hotel.class);
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
    public void hideSoftKeyborard(){
        try{
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

}