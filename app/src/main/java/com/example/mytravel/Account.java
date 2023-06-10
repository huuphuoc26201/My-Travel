package com.example.mytravel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.model.user;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class Account extends AppCompatActivity {
    BottomNavigationView nav;
    private LinearLayout out,intro,evaluate,call,bia,feedback,changepassword,history;
    private TextView phone,tv_name,tv_email;
    private ImageView imgavt,edit;
    public EditText edtname;
    TextView editemail;
    String path;
    Uri uri;
    int SELECT_IMAGE_CODE=1;
    private static final int REQUEST_CALL = 1;
    private static final int MY_REQQUEST_CODE=10;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private static final String KEY_FIRST_INSTALL="KEY_FIRST_INSTALL";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        nav=findViewById(R.id.bottomNavigationView);
        out=(LinearLayout) findViewById(R.id.signout);
        history=(LinearLayout) findViewById(R.id.history);
        intro=(LinearLayout) findViewById(R.id.intro);
        call=(LinearLayout) findViewById(R.id.call);
        bia=(LinearLayout) findViewById(R.id.bia);
        phone=(TextView) findViewById(R.id.phone);
        evaluate=(LinearLayout) findViewById(R.id.evaluate);
        feedback=(LinearLayout) findViewById(R.id.feedback);
        changepassword=(LinearLayout) findViewById(R.id.changepassword);
        imgavt=(ImageView) findViewById(R.id.profile_image);
        edit=(ImageView) findViewById(R.id.edit);
        tv_name=(TextView) findViewById(R.id.tv_name);
        tv_email=(TextView) findViewById(R.id.tv_email);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Tài Khoản");
        showUser();
        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        nav.setSelectedItemId(R.id.profile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDialog();
            }

            private void EditDialog() {
                AlertDialog.Builder builderD = new AlertDialog.Builder(context);

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.edit, null);
                builderD.setView(view);
                builderD.setTitle("Chỉnh sửa thông tin");
                builderD.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builderD.setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editprofile();

                    }
                });//Get text from edit text in dialog into display textview
                edtname = view.findViewById(R.id.editname);
                editemail=view.findViewById(R.id.edtemail);
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user==null){
                    return;
                }
                String email=user.getEmail();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tài Khoản");
                ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                user User = ds.getValue(com.example.model.user.class);
                                if (User != null) {
                                    String name = User.getName();
                                    String email = User.getEmail();
                                    editemail.setText(email);
                                    edtname.setText(name);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Xảy ra lỗi trong quá trình đọc dữ liệu
                    }
                });

                builderD.create();
                builderD.show();
            }


        });
        imgavt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        //Trung tâm hổ trợ
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdt = "0943101434";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + sdt));
                if (ContextCompat.checkSelfPermission(Account.this, android.Manifest.permission.CALL_PHONE) != getPackageManager().PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Account.this,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    return;
                }
                startActivity(intent);
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdt = "0943101434";
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + sdt));
                if (ContextCompat.checkSelfPermission(Account.this, android.Manifest.permission.CALL_PHONE) != getPackageManager().PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Account.this,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    return;
                }
                startActivity(intent);
            }
        });

        //Đánh giá ứng dụng
        evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Account.this, Evaluate.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Account.this, History.class);
                startActivity(intent);
            }
        });

        //Giới thiệu My Travel
        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Account.this, Intro.class);
                startActivity(intent);
            }
        });
        //Góp ý
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Account.this, feedBack.class);
                startActivity(intent);
            }
        });


        //Đổi mật khẩu
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Account.this, changePassword.class);
                startActivity(intent);

            }
        });
        //Đăng xuất
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logOut();
            }

            private void logOut() {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                AlertDialog.Builder ok = new AlertDialog.Builder(Account.this);
                ok.setTitle("Đăng xuất");
                ok.setMessage("Bạn có chắc chắn muốn đăng xuất?");
                ok.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Account.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
                ok.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                ok.create().show();
            }
        });

        //menu
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Toast.makeText(Account.this,"Home",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.tour:
                        Toast.makeText(Account.this,"Tour",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),Tour.class));
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.booking:
                        Toast.makeText(Account.this,"Booking",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), booking.class));
                        overridePendingTransition(0, 0);
                        return true;


                    case R.id.news:
                        Toast.makeText(Account.this,"News",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),News.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        Toast.makeText(Account.this,"Profile",Toast.LENGTH_LONG).show();
                        return true;

                }
                return false;
            }
        });

    }
    private void editprofile() {
        String sfullName  = edtname.getText().toString();
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
                        user User = ds.getValue(com.example.model.user.class);
                        if (User != null) {
                            String key = User.getKey();
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Tài Khoản").child(key);
                            myRef.child("name").setValue(sfullName);
                            tv_name.setText(sfullName);
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
    private void openGallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null && data.getData()!=null){
            uri=data.getData();
            imgavt.setImageURI(uri);
            upLoadPicture();
        }
    }

    private void upLoadPicture() {
        final String randomkey = UUID.randomUUID().toString();
        StorageReference riverRef = storageReference.child("image/" + randomkey);
        final ProgressDialog pd = new ProgressDialog(Account.this);
        pd.setTitle("uploading Image...");
        pd.show();
        riverRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the uploaded image
                        riverRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Save the download URL to Realtime Database
                                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                if(user==null){
                                    return;
                                }

                                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                                if (user1 == null) {
                                    return;
                                }
                                String eemail = user1.getEmail();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tài Khoản");
                                ref.orderByChild("email").equalTo(eemail).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                user User = ds.getValue(com.example.model.user.class);
                                                if (User != null) {
                                                    String key = User.getKey();
                                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Tài Khoản").child(key);
                                                    myRef.child("imageAvt").setValue(uri.toString());
                                                    Glide.with(Account.this).load(uri.toString()).error(R.drawable.avt).into(imgavt);
                                                }
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Xảy ra lỗi trong quá trình đọc dữ liệu
                                    }
                                });


                                // Update the user profile with the new photo URI
                                UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(uri)
                                        .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    pd.dismiss();
                                                    Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double pro = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + (int) pro + "%");
                    }
                });

    }
    public void showUser(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            return;
        }
        String name=user.getDisplayName();
        String email=user.getEmail();
        Uri photoUrl=user.getPhotoUrl();
        tv_name.setText(name);
        tv_email.setText(email);
        Glide.with(Account.this).load(photoUrl).error(R.drawable.avt).into(imgavt);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tài Khoản");
        ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        user User = ds.getValue(com.example.model.user.class);
                        if (User != null) {
                            String name = User.getName();
                            String email = User.getEmail();
                            String Avt = User.getImageAvt();
                            tv_email.setText(email);
                            tv_name.setText(name);
                            Glide.with(Account.this).load(Avt).error(R.drawable.avt).into(imgavt);
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
    public void favorite(View view){
        Intent intent=new Intent(Account.this,Favorite.class);
        startActivity(intent);
    }
}