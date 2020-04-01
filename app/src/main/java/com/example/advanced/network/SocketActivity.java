package com.example.advanced.network;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advanced.R;
import com.example.advanced.utils.RxSchedulers;
import com.robot.seabreeze.log.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class SocketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        Network network = Network.getSingleInstance();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocketHook.enableSocketHook();
                Toast.makeText(SocketActivity.this, "开启成功", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.okhttp_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                network.updateProgram(1)
                        .compose(RxSchedulers.rxSchedulerHelperFlowable())
                        .subscribe(new Consumer<ResponseBody>() {
                            @Override
                            public void accept(ResponseBody responseBody) throws Exception {
                                Logger.i(responseBody.string());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Logger.e(throwable.getMessage());
                            }
                        });

            }

        });

        findViewById(R.id.newrequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flowable.just("http://www.baidu.com")
                        .map(new Function<String, String>() {
                            @Override
                            public String apply(String s) throws Exception {
                                return getURLResponse(s);
                            }
                        })
                        .compose(RxSchedulers.rxSchedulerHelperFlowable())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Logger.e(s);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Logger.e(throwable.getMessage());
                            }
                        });

            }
        });
    }

    private String getURLResponse(String urlString) {
        HttpURLConnection conn = null;
        InputStream is = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine;
            while ((inputLine = bufferReader.readLine()) != null) {
                stringBuffer.append(inputLine).append("\n");
            }

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return stringBuffer.toString();
    }
}
