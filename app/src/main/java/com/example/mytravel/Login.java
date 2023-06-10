package com.example.mytravel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.model.user;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class Login extends AppCompatActivity {
    private Button btndn;
    private LinearLayout googleBtn;
    private ImageView fbBtn;
    private EditText email1, password1;
    private TextView quen_mk;
    private LinearLayout dangki;
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private static final String TAG="FacebookLogin";
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mAuth = FirebaseAuth.getInstance();
        btndn = (Button) findViewById(R.id.btndn);
        dangki = (LinearLayout) findViewById(R.id.dangki);
        quen_mk = (TextView) findViewById(R.id.quenmk);
        googleBtn = (LinearLayout) findViewById(R.id.google_img);
        email1 = (EditText) findViewById(R.id.email);
        password1 = (EditText) findViewById(R.id.password);
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




        btndn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateemail() | !validatePassword()){

                } else {
                    checkUser();
                }
            }
        });

        quen_mk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Forgotpassword.class);
                startActivity(intent);
            }
        });

        dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        //Đăng nhập bằng gooogle
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc= GoogleSignIn.getClient(this,gso);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=gsc.getSignInIntent();
                startActivityForResult(intent,1234);
            }
        });


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1234){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account=task.getResult(ApiException.class);
                AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    String userEmail = account.getEmail();
                                    FirebaseAuth.getInstance().fetchSignInMethodsForEmail(userEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                            if (task.isSuccessful()) {
                                                SignInMethodQueryResult result = task.getResult();
                                                List<String> signInMethods = result.getSignInMethods();
                                                if (signInMethods != null && signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                                    // Email has already been used to create an account
                                                    Toast.makeText(getApplicationContext(),"Email đã được sử dụng để đăng kí tài khoản trên hệ thống!",Toast.LENGTH_SHORT).show();
                                                } else {
                                                    // Email has not been used before, login successful
                                                    FirebaseAuth auth = FirebaseAuth.getInstance();
                                                    FirebaseUser user1 = auth.getCurrentUser();
                                                    String uid = user1.getUid();

                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tài Khoản").child(uid);

                                                    // Lấy thông tin cần thiết từ đối tượng FirebaseUser
                                                    String email = user1.getEmail();
                                                    String name = user1.getDisplayName();
                                                    Uri photoUrl = user1.getPhotoUrl();

                                                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                                    if(user==null){
                                                        return;
                                                    }
                                                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Tài Khoản");
                                                    ref1.orderByChild("key").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists()) {
                                                            }else{
                                                                // Tạo một đối tượng User với các thuộc tính cần lưu trữ
                                                                com.example.model.user currentUser = new user(email," ", name, photoUrl.toString(),uid);
                                                                // Đưa đối tượng User lên Realtime Database
                                                                ref.setValue(currentUser);
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            // Xảy ra lỗi trong quá trình đọc dữ liệu
                                                        }
                                                    });
                                                    Toast.makeText(getApplicationContext(),"Đăng nhập bằng tài khoản Google thành công",Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(getApplicationContext(),Home.class);
                                                    startActivity(intent);
                                                }
                                            } else {
                                                // An error occurred while checking if email has been used
                                                Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(Login.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }catch (ApiException e){
                e.printStackTrace();
            }
        }
    }


    //Đăng ký, đăng nhập tài khoản
    public Boolean validateemail() {
        String val = email1.getText().toString().trim();
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

    public Boolean validatePassword(){
        String val = password1.getText().toString().trim();
        if (val.isEmpty()){
            password1.setError("Password không được để trống");
            return false;
        } else {
            password1.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String email = email1.getText().toString().trim();
        String password = password1.getText().toString().trim();
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    if(mAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(Login.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Login.this,Home.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Login.this, "Please Verify your email!", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        email1.setError("Người dùng không tồn tại. Vui lòng đăng ký người dùng mới.");
                        email1.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        password1.setError("Mật khẩu không đúng. vui lòng kiểm tra và nhập lại.");
                        password1.requestFocus();}
                    catch (Exception e) {
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // User is signed in
                                Intent i = new Intent(Login.this, Home.class);
                                startActivity(i);
                                finish();
                                Toast.makeText(Login.this, "Đăng nhập bằng tài khoản Facebook thành công", Toast.LENGTH_SHORT).show();
                            } else {

                                // No user is signed in
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // [END auth_with_facebook]

}