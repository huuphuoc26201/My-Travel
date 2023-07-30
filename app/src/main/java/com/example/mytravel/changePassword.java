package com.example.mytravel;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class changePassword extends AppCompatActivity {
    private Button btnqlai,change_mk;
    private EditText password;

    private boolean isGoogleSignIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        btnqlai = (Button) findViewById(R.id.d_qlai);
        password = (EditText) findViewById(R.id.dmk);
        change_mk = (Button) findViewById(R.id.change_mk);


        // show passwword
        ImageView showpassword = (ImageView) findViewById(R.id.show_password);
        showpassword.setImageResource(R.drawable.ic_hide_pwd);
        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showpassword.setImageResource(R.drawable.ic_hide_pwd);
                }else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showpassword.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });
        btnqlai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(changePassword.this, Account.class);
                startActivity(intent);
            }
        });


        change_mk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatePassword()){
                } else {
                    // Tiến hành thay đổi mật khẩu thông thường nếu người dùng không đăng nhập bằng tài khoản Google
                    Changepassword();
                }


            }

           

            private void Changepassword() {

                final ProgressDialog progressDialog = new ProgressDialog(changePassword.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String newpassword = password.getText().toString().trim();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null) return;
                        // Check if the user is using a Google account
                        if (user.getProviderData().stream().anyMatch(userInfo -> userInfo.getProviderId().equals(GoogleAuthProvider.PROVIDER_ID))) {
                            Toast.makeText(changePassword.this, "Bạn không thể thay đổi mật khẩu khi đăng nhập bằng tài khoản Google.", Toast.LENGTH_SHORT).show();
                        }else {
                            user.updatePassword(newpassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                AlertDialog.Builder ok = new AlertDialog.Builder(changePassword.this);
                                                ok.setTitle("Đổi mật khẩu");
                                                ok.setMessage("Bạn muốn tiếp tục duy trì trạng thái đăng nhập hay không?");
                                                ok.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        progressDialog.dismiss();
                                                        Intent intent = new Intent(changePassword.this, Account.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                                ok.setNegativeButton("Log Out", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        progressDialog.dismiss();
                                                        FirebaseAuth.getInstance().signOut();
                                                        Intent intent = new Intent(changePassword.this, Login.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                                ok.create().show();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(changePassword.this, "Bạn không thể thay đổi mật khẩu khi đăng nhập bằng tài khoản Google.", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                        }
                    }
                }, 1000);
            }
        });


    }

    private boolean validatePassword() {
        String val = password.getText().toString();
        if (val.isEmpty()){
            password.setError("Password không được để trống");
            return false;
        }else if(password.length()<7){
            password.setError(" Độ dài mật khẩu tối thiểu 8 ký tự ");
            return false;
        }
        else {
            password.setError(null);
            return true;
        }
    }

}