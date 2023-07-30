package com.example.mytravel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.adapter.SendMail;
import com.example.model.feedBackData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class feedBack extends AppCompatActivity {
    EditText nd1,nd2,nd3;
    String originalText = "";
    boolean undoState = false;
    private ImageView back;
    private RatingBar ratingbar;
    private TextView txtCount;
    private Button btndanhgia;
    float rateValue;
    String temp;
    private EditText name,email,phone,address,tentour,matour;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        nd1=(EditText) findViewById(R.id.nd1);
        nd2=(EditText) findViewById(R.id.nd2);
        nd3=(EditText) findViewById(R.id.nd3);
        back=(ImageView) findViewById(R.id.back);
        txtCount=(TextView) findViewById(R.id.textcount);
        ratingbar=(RatingBar) findViewById(R.id.ratingbar);
        btndanhgia=(Button)  findViewById(R.id.btndanhgia); 
        
        name=(EditText) findViewById(R.id.name);
        email=(EditText) findViewById(R.id.email);
        phone=(EditText) findViewById(R.id.phone);
        address=(EditText) findViewById(R.id.diachi);
        tentour=(EditText) findViewById(R.id.tour);
        matour=(EditText) findViewById(R.id.matour);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(feedBack.this, Account.class);
                startActivity(intent);
            }
        });
        btndanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guiphanhoi();
                sendmail();
            }
        });

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

        nd1.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL){
                    if(undoState == false){
                        originalText = nd1.getText().toString().trim();
                        undoState = true;
                    }
                }
                return false;
            }
        });
        nd2.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL){
                    if(undoState == false){
                        originalText = nd2.getText().toString().trim();
                        undoState = true;
                    }
                }
                return false;
            }
        });
        nd3.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL){
                    if(undoState == false){
                        originalText = nd3.getText().toString().trim();
                        undoState = true;
                    }
                }
                return false;
            }
        });
    }

    private void sendmail() {
        String ename=name.getText().toString().trim();
        String eemail=email.getText().toString().trim();
        String subject="[MY TRAVEL - THƯ CẢM ƠN]";
        String Message = "Kính gửi: "+ename+"\n\nLời đầu tiên, My Travel xin gửi lời cám ơn chân thành và sâu sắc nhất đến Quý khách hàng đã tin tưởng và sử dụng dịch vụ của chúng tôi. Sự tin tưởng của quý vị đã góp phần lớn quyết định sự phát triển và thành công của My Travel trong thời gian qua.\n\n"
                +"Với phương châm “Vì sự hài lòng cao nhất của khách hàng”, trong những năm qua, My Travel chúng tôi luôn lắng nge và không ngừng phấn đấu, nâng cao chất lượng dịch vụ để mang lại nhiều lợi ích hơn và có thể đáp ứng mọi yêu cầu của khách hàng.\n\n"
                +"Chúng tôi luôn biết rằng, sự ủng hộ và sự tin yêu của Quý khách hàng là tài sản vô giá với My Traevl chúng tôi, để đạt được điều này chúng tôi luôn nỗ lực không ngừng, hướng tới mục tiêu phát triển bền vững, chú trọng xây dựng các chính sách chăm sóc khách hàng,mang lại những giá trị thiết thực để luôn làm hài lòng khách hàng ở mức cao nhất nhằm đáp lại tình cảm và sự tin yêu của Quý khách.\n\n"
                +"Với mong muốn phục vụ khách hàng ngày càng tốt hơn, MyTravel cảm ơn đã đóng góp ý kiến hết sức quý báu của Quý khách hàng đối với chất lượng dịch vụ trong chương trình du lịch này. Chúng tôi chân thành cảm ơn Quý khách về sự tin tưởng đã dành cho MyTravel. Kích chúc quý khách một chuyến du lịch thật vui vẻ và bổ ích. rất mong được phục vụ Quý khách trong những chuyến du lịch tiếp theo.\n\n"
                +"Một lần nữa, My Travel chúng tôi xin được gửi lời cảm ơn chân thành đến Quý khách hàng đã không ngừng quan tâm và luôn đồng hành cùng My Travel trong suốt thời gian qua. Chúng tôi cũng hy vọng trong thời gian tới sẽ tiếp tục nhận được sự quan tâm và tín nhiệm của Quý khách.\n\n"+
                "Xin chúc quý khách hàng sức khỏe, may mắn và thành công\n\n\n"+"Kính thư !!!";

        SendMail sm = new SendMail(this, eemail, subject, Message);

        //Executing sendmail to send email
        sm.execute();
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
    private void guiphanhoi() {
        String ename=name.getText().toString().trim();
        String eemail=email.getText().toString().trim();
        String ephone=phone.getText().toString().trim();
        String ediachi=address.getText().toString();
        String end1=nd1.getText().toString();
        String end2=nd2.getText().toString();
        String end3=nd3.getText().toString();
        String etentour=tentour.getText().toString();
        String ematour=matour.getText().toString();
        temp=txtCount.getText().toString();

        String key=generateRandomString();
        hideSoftKeyborard();

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myref=database.getReference("Phản hồi");

        feedBackData feedBack=new feedBackData(ename,eemail,ephone,ediachi,etentour,ematour,end1,end2,temp,end3);

        myref.child(key).setValue(feedBack);
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