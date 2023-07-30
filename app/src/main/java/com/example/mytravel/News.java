package com.example.mytravel;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adapter.CustomAdapter;
import com.example.adapter.XMLDOMParser;
import com.example.model.Docbaodata;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class News extends AppCompatActivity {
    BottomNavigationView nav;
    ListView listView;
    CustomAdapter customAdapter;
    ArrayList<Docbaodata> mangdocbao;
    TextView countCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        nav=findViewById(R.id.bottomNavigationView);
        listView=(ListView)findViewById(R.id.listview);
        nav.setSelectedItemId(R.id.news);
        mangdocbao =new ArrayList<Docbaodata>();
        countCart=findViewById(R.id.count);
        numberCart();


        //menu
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Toast.makeText(News.this,"Home",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.tour:
                        Toast.makeText(News.this,"Tour",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),Tour.class));
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.booking:
                        Toast.makeText(News.this,"Booking",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), payTour.class));
                        overridePendingTransition(0, 0);
                        return true;


                    case R.id.news:
                        Toast.makeText(News.this,"News",Toast.LENGTH_LONG).show();

                        return true;

                    case R.id.profile:
                        Toast.makeText(News.this,"Profile",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),Account.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
        //Date
        setTitle("Date");

        Calendar calendar=Calendar.getInstance();
        String currentDate= DateFormat.getDateInstance(android.icu.text.DateFormat.FULL).format(calendar.getTime());
        TextView textViewDate=findViewById(R.id.date);
        textViewDate.setText(currentDate);

        //Tin tức
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Readdata().execute("https://vnexpress.net/rss/du-lich.rss");
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(News.this,Web_view.class);
                intent.putExtra("link",mangdocbao.get(position).link);
                startActivity(intent);
            }
        });
    }
    class Readdata extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            XMLDOMParser parser=new XMLDOMParser();
            Document document=parser.getDocument(s);
            NodeList nodeList=document.getElementsByTagName("item");
            NodeList nodeListdescription=document.getElementsByTagName("description");
            String hinhanh="";
            String title="";
            String pubDate="";
            String link="" ;
            for(int i=0;i<nodeList.getLength();i++){
                String cdata=nodeListdescription.item(i+1).getTextContent();
                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher=p.matcher(cdata);
                if(matcher.find()){
                    hinhanh=matcher.group(1);
                }
                Element element=(Element) nodeList.item(i);
                title=parser.getValue(element,"title");
                pubDate=parser.getValue(element,"pubDate");
                link=parser.getValue(element,"link");
                mangdocbao.add(new Docbaodata(title,link,hinhanh,pubDate));
            }
            customAdapter=new CustomAdapter(News.this, android.R.layout.simple_expandable_list_item_1,mangdocbao);
            listView.setAdapter(customAdapter);

            super.onPostExecute(s);

        }
    }
    private static String docNoiDung_Tu_URL(String theUrl){
        StringBuilder content=new StringBuilder();
        try
        {
            URL url=new URL(theUrl);
            URLConnection urlConnection= url.openConnection();
            BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line= bufferedReader.readLine())!=null)
            {
                content.append(line+"\n");
            }
            bufferedReader.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return content.toString();
    }
    private void numberCart() {
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
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        countCart.setVisibility(View.VISIBLE);
                                        int count = (int) dataSnapshot.getChildrenCount();
                                        countCart.setText(String.valueOf(count));
                                    }else{
                                        countCart.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Xử lý khi có lỗi xảy ra
                                }
                            });
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

}