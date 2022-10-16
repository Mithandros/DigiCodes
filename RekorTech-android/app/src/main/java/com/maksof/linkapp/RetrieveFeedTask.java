package com.maksof.linkapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class RetrieveFeedTask extends AsyncTask<String, Void, Bitmap> {
    Bitmap bitmap = null;
    InputStream is;
    String myUrl;
    String url = commonClass.host;
    String imgUrl =url+"cdn/images/";


    RetrieveFeedTask(String url){
        this.myUrl = url;
    }

    protected Bitmap doInBackground(String... urls) {
        try {
            URL url = new URL(imgUrl+this.myUrl);
            is= url.openConnection().getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if(bitmap!=null)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            bitmap.recycle();
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            return decoded;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
