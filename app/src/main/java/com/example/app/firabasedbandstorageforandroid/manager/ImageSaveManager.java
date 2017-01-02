package com.example.app.firabasedbandstorageforandroid.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by Tolik on 25.12.2016.
 */

public final class ImageSaveManager {
    private static final String IMAGE_FOLDER = "/FireBase/Images";
    private static final String EXTENSION = ".png";

    public static void writeImage(Bitmap bitmap, String imageName) {
        File imageFile = getFileByImageName(imageName);
        Log.e("FILE", imageFile.getAbsolutePath());

        FileOutputStream out = null;

        try {
            out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, out);
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Bitmap readImage(String imageName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 2;
        File imageFile = getFileByImageName(imageName);

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap readByFile(File imageFile){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 2;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static File getFileByImageName(String fileName) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + IMAGE_FOLDER;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return new File(dir, fileName + EXTENSION);
    }

    public static void renameFile(String oldName,String newName){
        File oldFile = getFileByImageName(oldName);
        File newFile = getFileByImageName(newName);
        if(oldFile.exists()){
            oldFile.renameTo(newFile);
        }
    }

    public static void deleteImageByName(String imageName){
        File imageFile = getFileByImageName(imageName);
        imageFile.delete();
    }

    public static void saveImageFromURI(Context context, Uri imgUri, String imageName){
        File imageFile = getFileByImageName(imageName);
        Log.e("FILE", imageFile.getAbsolutePath());

        final int chunkSize = 1024;
        byte[] imageData = new byte[chunkSize];

        InputStream in = null;
        OutputStream out = null;

        try {
            in = context.getContentResolver().openInputStream(imgUri);
            out = new FileOutputStream(imageFile);

            int bytesRead;
            while ((bytesRead = in.read(imageData)) > 0) {
                out.write(Arrays.copyOfRange(imageData, 0, Math.max(0, bytesRead)));
            }

        } catch (Exception ex) {
            Log.e("Something went wrong.", ex.toString());
        } finally {
            if(in!=null && out!=null){
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
