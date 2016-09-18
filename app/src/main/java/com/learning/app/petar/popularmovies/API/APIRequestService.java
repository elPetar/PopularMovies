package com.learning.app.petar.popularmovies.API;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.learning.app.petar.popularmovies.Fragment.PopMoviesFragment;
import com.learning.app.petar.popularmovies.MainActivity;
import com.learning.app.petar.popularmovies.WarningDialog;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Petar on 17.9.2016..
 */
public class APIRequestService extends Service {
    private static final String TAG = APIRequestService.class.getSimpleName();
    private IBinder mIBinder = new LocalBinder();
    public MainActivity main;
    @Override
    public void onCreate() {
        Log.d(TAG,"onCreateService");
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    public void run(final String baseUrl){
        toggleRefresh(true);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(baseUrl + TheMovieDataBaseAPI.API_KEY)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    WarningDialog dialog = new WarningDialog();
                    dialog.show(main.getSupportFragmentManager(), "WarningDialog");
                    toggleRefresh(false);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    TheMovieDataBaseAPI.JSONResponse = response.body().string();
                    main.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                main.parseJSON(baseUrl);
                                main.getFragmentToScreen();
                                toggleRefresh(false);
                            } catch (JSONException e) {
                            }
                        }
                    });
                }
            });
        }catch(Exception e){};
    }

    private void toggleRefresh(boolean refreshing) {
        if(PopMoviesFragment.swipeDown!=null) {
            PopMoviesFragment.swipeDown.setRefreshing(refreshing);
        }
    }

    public class LocalBinder extends Binder{
        public APIRequestService getService(){
            return APIRequestService.this;
        }
    }
}
