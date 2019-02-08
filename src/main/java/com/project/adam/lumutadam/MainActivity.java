package com.project.adam.lumutadam;


import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
public class MainActivity extends AppCompatActivity {

    private Button buttonSync, buttonAsync;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        buttonSync = findViewById(R.id.buttonSync);
        buttonAsync = findViewById(R.id.buttonAsync);
        buttonSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestSync();
            }
        });

        buttonAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAsync();
            }
        });
    }
    private void requestSync() {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.readTimeout(5, TimeUnit.SECONDS);
        builder.writeTimeout(5, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        Request request = new Request.Builder()
                //.url("https://publicobject.com/helloworld.txt")
                .url("https://jsonplaceholder.typicode.com/todos/1")
                .build();
        try {
            Response response = client.newCall(request).execute();
            //response.body().string() tidak boleh dipanggil 2x
            String responseString = response.body().string();
            Toast.makeText(this, responseString, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Toast.makeText(this, "OkHTTP requestSync", Toast.LENGTH_SHORT).show();
    }
    private void requestAsync() {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.readTimeout(5, TimeUnit.SECONDS);
        builder.writeTimeout(5, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        Request request = new Request.Builder()
                //.url("https://publicobject.com/helloworld.txt")
                .url("https://jsonplaceholder.typicode.com/todos/1")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("debuging", "Request Failed."+e.getMessage());
                responseAsync("Request Failed."+e.getMessage());
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String responseString = response.body().string();
                        Log.d("debuging", responseString);
                        responseAsync(responseString);
                    } else {
                        Log.d("debuging", "Error "+ response);
                        responseAsync("Error "+ response);
                    }
                } catch (IOException e) {
                    Log.d("debuging", "Exception caught : ", e);
                    responseAsync("Error "+ e.getMessage());
                }
            }
        });
        Toast.makeText(this, "OkHTTP requestAsync", Toast.LENGTH_SHORT).show();
    }
    private void responseAsync(final String responseStr) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), responseStr, Toast.LENGTH_SHORT).show();
            }

        });

    }
    RequestBody requestBody = new FormBody.Builder()
            .add("param1", "value1")
            .add("param2", "value2")
            .build();
    Request request = new Request.Builder()
            //.url("https://publicobject.com/helloworld.txt")
            .url("https://jsonplaceholder.typicode.com/todos/1")
            .post(requestBody)
            .addHeader("Connection", "Keep-Alive")
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build();

}
