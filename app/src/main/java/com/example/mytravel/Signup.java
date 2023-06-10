package com.example.mytravel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.model.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Signup extends AppCompatActivity {
    private Button signupbtn;
    private EditText email1,password1,sdt1,repassword1,name1;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private TextView dangnhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth= FirebaseAuth.getInstance();
        email1=(EditText) findViewById(R.id.email);
        name1=(EditText) findViewById(R.id.name);
        password1=(EditText) findViewById(R.id.password);
        sdt1=(EditText) findViewById(R.id.sdt);
        repassword1=(EditText) findViewById(R.id.repassword);
        signupbtn=(Button) findViewById(R.id.signupbtn);
        dangnhap= (TextView) findViewById(R.id.textView36);
        progressDialog=new ProgressDialog(this);

        // show passwword
        ImageView showpassword = findViewById(R.id.show_password);
        showpassword.setImageResource(R.drawable.ic_hide_pwd);
        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password1.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showpassword.setImageResource(R.drawable.ic_hide_pwd);
                }else {
                    password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showpassword.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });
        // show repasswword
        ImageView showrepassword = findViewById(R.id.show_repassword);
        showrepassword.setImageResource(R.drawable.ic_hide_pwd);
        showrepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(repassword1.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    repassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showrepassword.setImageResource(R.drawable.ic_hide_pwd);
                }else {
                    repassword1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showrepassword.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });


        dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateemail() | !validatePassword()|!validateSDT()|!validaterepassword()|!validatename()){

                }else{
                    dangki();}
            }
        });
    }

    private boolean validateemail() {
        String val = email1.getText().toString();
        String emailPattern= "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            email1.setError("Email không được để trống");
            return false;
        }else if(!val.matches(emailPattern)){
            email1.setError("Địa chỉ email không hợp lệ");
            return false;
        }
        else {
            email1.setError(null);
            return true;
        }
    }
    private boolean validatename() {
        String val = name1.getText().toString();

        if (val.isEmpty()) {
            name1.setError("Name không được để trống");
            return false;}
        else {
            name1.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String val = password1.getText().toString();
        if (val.isEmpty()){
            password1.setError("Password không được để trống");
            return false;
        }
        else if(password1.length()<7){
            password1.setError(" Độ dài mật khẩu tối thiểu 7 ký tự ");
            return false;
        }
        else {
            password1.setError(null);
            return true;
        }
    }
    private boolean validaterepassword() {
        String password = password1.getText().toString();
        String val = repassword1.getText().toString();
        if (val.isEmpty()){
            repassword1.setError("Repassword không được để trống");
            return false;
        }else if(!password.equals(val)) {
            repassword1.setError("Nhập mật khẩu không khớp");
            return false;

        }
        else {
            repassword1.setError(null);
            return true;
        }
    }

    private boolean validateSDT() {
        String val = sdt1.getText().toString();
        String sdt = "[0-9]{10,11}";
        if (val.isEmpty()) {
            sdt1.setError("SĐT không được để trống");
            return false;
        } else if (!val.matches(sdt)) {
            sdt1.setError("Số điện thoại phải có 10 đến 11 chữ số");
            return false;
        } else {
            sdt1.setError(null);
            return true;
        }
    }

    private void dangki() {
        String name=name1.getText().toString().trim();
        String email=email1.getText().toString().trim();
        String password = password1.getText().toString().trim();
        String phone=sdt1.getText().toString().trim();
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myref=database.getReference("Tài Khoản");
        String randomString = "";
        Random rnd = new Random();
        char c = (char) (rnd.nextInt(26) + 'a'); // Tạo ngẫu nhiên một chữ cái từ a đến z
        int number = rnd.nextInt(100); // Tạo ngẫu nhiên một số từ 0 đến 99
        randomString = String.format("%02d%c", number, c); // Định dạng chuỗi với phần số có 2 chữ số và phần chữ cái
        String ename=name+randomString;

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    user User=new user(email,phone,name," ",ename);
                                    String ename = User.getKey(); // Lấy giá trị thuộc tính ename của đối tượng User
                                    myref.child(ename).setValue(User);

                                    Toast.makeText(Signup.this, "Tạo tài khoản thành công. Vui lòng xác minh email của bạn!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Signup.this,Login.class));
                                    progressDialog.cancel();
                                }else{
                                    Toast.makeText(Signup.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Signup.this, "Tài khoản đã tồn tại! Thử lại!", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });




    }
}