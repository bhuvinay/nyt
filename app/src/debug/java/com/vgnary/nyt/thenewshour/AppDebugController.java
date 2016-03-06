package com.vgnary.nyt.thenewshour;

import com.android.volley.toolbox.Volley;
import com.vgnary.nyt.thenewshour.application.AppController;
import com.vgnary.nyt.thenewshour.network.NetworkController;
import com.vgnary.nyt.thenewshour.network.OkHttpStack;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Field;

public class AppDebugController extends AppController {

    @Override
    public void onCreate() {
        super.onCreate();

        // Create an InitializerBuilder
        Stetho.Initializer initializer = Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)) // Enable Chrome DevTools
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))  // Enable command line interface
                .build();  // Use the InitializerBuilder to generate an Initializer

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);

        NetworkController nwController = NetworkController.getInstance(this);
        try {
            Field mRequestQueue = nwController.getClass().getDeclaredField("mRequestQueue");
            mRequestQueue.setAccessible(true);

            OkHttpClient client = new OkHttpClient();
            client.networkInterceptors().add(new StethoInterceptor());

            mRequestQueue.set(nwController,Volley.newRequestQueue(this, new OkHttpStack(client)));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
