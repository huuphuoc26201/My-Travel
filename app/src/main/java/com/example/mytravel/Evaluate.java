package com.example.mytravel;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.adapter.SendMail;
import com.example.model.danhGiaData;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Evaluate extends AppCompatActivity {
    private ImageView back;
    private EditText noidung,edtname,edtemail;
    private Button btndanhgia;
    private RatingBar ratingbar;
    private TextView txtCount;
    String originalText = "";
    boolean undoState = false;
    float rateValue;String temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        back=(ImageView) findViewById(R.id.back);
        edtemail=(EditText) findViewById(R.id.email);
        edtname=(EditText) findViewById(R.id.name);
        txtCount=(TextView) findViewById(R.id.textcount);
        noidung=(EditText) findViewById(R.id.noidung);
        btndanhgia=(Button) findViewById(R.id.btn_danhgia);
        ratingbar=(RatingBar) findViewById(R.id.ratingbar);

        LayerDrawable stars = (LayerDrawable) ratingbar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(this, R.color.yelow), PorterDuff.Mode.SRC_ATOP);


        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue=ratingBar.getRating();

                if(rateValue<=1&&rateValue>0){
                    txtCount.setText("Bad "+rateValue+"/5");
                }
                else if(rateValue<=2&&rateValue>1){
                    txtCount.setText("Okey "+rateValue+"/5");
                }
                else if(rateValue<=3&&rateValue>2){
                    txtCount.setText("Good "+rateValue+"/5");
                }
                else if(rateValue<=4&&rateValue>3){
                    txtCount.setText("Very Good "+rateValue+"/5");
                }
                else if(rateValue<=5&&rateValue>4){
                    txtCount.setText("Best "+rateValue+"/5");
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Evaluate.this, Account.class);
                startActivity(intent);
            }
        });
        noidung.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL){
                    if(undoState == false){
                        originalText = noidung.getText().toString().trim();
                        undoState = true;
                    }
                }
                return false;
            }
        });

        btndanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateemail() | !validatenoidung()|!validatename()){

                }else{
                    danhgia();
                    sendMail();
                }
            }
        });

    }

    private void sendMail() {
        String name=edtname.getText().toString().trim();
        String email=edtemail.getText().toString().trim();
        String subject="[MY TRAVEL - THƯ CẢM ƠN]";
        String Message = "Kính gửi: "+name+"\n\nLời đầu tiên, My Travel xin gửi lời cám ơn chân thành và sâu sắc nhất đến Quý khách hàng đã tin tưởng và sử dụng dịch vụ của chúng tôi. Sự tin tưởng của quý vị đã góp phần lớn quyết định sự phát triển và thành công của My Travel trong thời gian qua.\n\n"
                +"Với phương châm “Vì sự hài lòng cao nhất của khách hàng”, trong những năm qua, My Travel chúng tôi luôn lắng nge và không ngừng phấn đấu, nâng cao chất lượng dịch vụ để mang lại nhiều lợi ích hơn và có thể đáp ứng mọi yêu cầu của khách hàng.\n\n"
                +"Chúng tôi luôn biết rằng, sự ủng hộ và sự tin yêu của Quý khách hàng là tài sản vô giá với My Traevl chúng tôi, để đạt được điều này chúng tôi luôn nỗ lực không ngừng, hướng tới mục tiêu phát triển bền vững, chú trọng xây dựng các chính sách chăm sóc khách hàng,mang lại những giá trị thiết thực để luôn làm hài lòng khách hàng ở mức cao nhất nhằm đáp lại tình cảm và sự tin yêu của Quý khách.\n\n"
                +"Một lần nữa, My Travel chúng tôi xin được gửi lời cảm ơn chân thành đến Quý khách hàng đã không ngừng quan tâm và luôn đồng hành cùng My Travel trong suốt thời gian qua. Chúng tôi cũng hy vọng trong thời gian tới sẽ tiếp tục nhận được sự quan tâm và tín nhiệm của Quý khách.\n\n"+
                "Xin chúc quý khách hàng sức khỏe, may mắn và thành công\n\n\n"+"Kính thư !!!";

        SendMail sm = new SendMail(this, email, subject, Message);

        //Executing sendmail to send email
        sm.execute();

    }

    private boolean validatenoidung() {
        String val = noidung.getText().toString();

        if (val.isEmpty()) {
            noidung.setError("Nội dung không được để trống");
            return false;}
        else {
            noidung.setError(null);
            return true;
        }

    }

    private boolean validatename() {
        String val = edtname.getText().toString();

        if (val.isEmpty()) {
            edtname.setError("Name không được để trống");
            return false;}
        else {
            edtname.setError(null);
            return true;
        }
    }

    private boolean validateemail() {
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
    private void danhgia() {
        String name=edtname.getText().toString().trim();
        String email=edtemail.getText().toString().trim();
        String noi_dung = noidung.getText().toString().trim();
        temp=txtCount.getText().toString();


        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myref=database.getReference("Đánh giá");

        danhGiaData danhgia=new danhGiaData(name,email,noi_dung,temp);
        String pathname=String.valueOf(danhgia.getName());

        if (name.equals(pathname)){
            myref.child(pathname).setValue(danhgia, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Intent intent=new Intent(Evaluate.this, Account.class);
                    startActivity(intent);

                }
            });
        }
    }

}