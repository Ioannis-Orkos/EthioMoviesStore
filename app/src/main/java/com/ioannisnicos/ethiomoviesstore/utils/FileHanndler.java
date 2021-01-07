package com.ioannisnicos.ethiomoviesstore.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileHanndler {

    public static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "EthioMovies");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image =  new File(mediaStorageDir.getPath() + File.separator +"IMG_"+ timeStamp + ".jpg");
        // Save a file: path for use with ACTION_VIEW intents
        //imageFilePath = "file:" + image.getAbsolutePath();
        //mImageFile =image;
        return image;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1.0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height,true);

        //return rotateBitmap(image,90.0f);
    }

    public static Bitmap rotateBitmap(Bitmap b,Float degrees) {
        Matrix matrix = new  Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
    }

}
