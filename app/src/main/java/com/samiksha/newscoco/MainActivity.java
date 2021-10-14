package com.samiksha.newscoco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.samiksha.newscoco.Model.Articles;
import com.samiksha.newscoco.Model.Headlines;
import com.github.clans.fab.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btnSearch;
    EditText etQuery;
    SwipeRefreshLayout swipeRefreshLayout;
    final String API_KEY = "c75774df01ed413b83109a7597f68845";
    Adapter adapter;
    List<Articles> articles = new ArrayList<>();
    public FloatingActionButton floatingActionButton1,floatingActionButton2,floatingActionButton3;
    // For Google ads


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Google ads


        floatingActionButton1 = findViewById(R.id.floatingActionitem1);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareintent=new Intent();
                shareintent.setAction(Intent.ACTION_SEND);
                shareintent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.samiksha.newscoco");
                shareintent.setType("text/plain");
                startActivity(Intent.createChooser(shareintent,"Share via"));

            }
        });
        // For rating option
        floatingActionButton2 = findViewById(R.id.floatingActionitem2);
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://play.google.com/store//apps/details?id="+getApplicationContext().getPackageName());
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                try {
                    startActivity(i);
                }catch (Exception e){
                    Toast.makeText( MainActivity.this,"Unable to open"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        floatingActionButton3 = findViewById(R.id.floatingActionitem3);
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity.this,FloatingButtonImgSlider.class);
                startActivity(intent3);
            }
        });




        recyclerView = findViewById(R.id.recyclerView);
        btnSearch = findViewById(R.id.btnSearch);
        etQuery = findViewById(R.id.etQuery);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final String country  = getCountry();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson("", country,API_KEY);
            }
        });
        retrieveJson("",country,API_KEY);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!etQuery.getText().toString().equals("")){

                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson(etQuery.getText().toString(), country,API_KEY);
                        }
                    });
                    retrieveJson("",country,API_KEY);

                }else {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson("", country,API_KEY);
                        }
                    });
                    retrieveJson("",country,API_KEY);
                }

            }
        });
        // For firebase push Notification the codes are here :-
        // For firebase push Notification the codes are here :-
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg= "Successful";
                        if (!task.isSuccessful()){
                            msg = "Failed";
                        }
                        Toast.makeText(MainActivity.this, msg,Toast.LENGTH_LONG).show();
                    }
                });





    }
    public void retrieveJson(String query, String country,String apiKey){
        Call<Headlines> call;
        swipeRefreshLayout.setRefreshing(true);
        if (!etQuery.getText().toString().equals("")){
            call = ApiClient.getInstance().getApi().getSpecificData(query,apiKey);

        }else{
            call = ApiClient.getInstance().getApi().getHeadlines( country,apiKey);

        }

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if (response.isSuccessful() && response.body().getArticles() != null){
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(MainActivity.this,articles);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
    public String getCountry(){
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }
}
