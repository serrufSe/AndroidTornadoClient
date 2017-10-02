package com.example.adm.myapplication;


import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.util.Properties;

public class AssetsPropertyReader {

    private Context context;
    private Properties properties;

    public AssetsPropertyReader(Context context) {
        this.context = context;
        properties = new Properties();
    }

    public Properties getProperties(String fileName) {
        try {
            AssetManager assetManager = context.getAssets();
            properties.load(assetManager.open(fileName));
        } catch (IOException e) {
            Log.e("AssetsPropertiesReader", e.toString());
        }

        return properties;
    }
}
