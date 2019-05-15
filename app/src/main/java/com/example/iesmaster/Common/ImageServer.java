package com.example.iesmaster.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageServer {

    private static String AppName = "IESMaster";

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return true;
            }
            else
            {
                return false;
            }

        }
        catch (Exception ex)
        {
            return false;
        }
    }


    public static boolean SaveFileToExternal(byte[] b, String FileName, Context mContext)
    {
        try
        {
            if(isExternalStorageWritable()) {
                File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File myDir = new File(root + "/"+ AppName);
                myDir.mkdirs();
                //File directory = mContext.getDir(directoryName, Context.MODE_PRIVATE);
                //File directory = getAlbumStorageDir("LetsMeet");
                File file = new File(myDir, FileName);
                // file.createNewFile();

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(b,0,b.length);
                fos.flush();
                fos.close();

                MediaScannerConnection.scanFile(mContext,
                        new String[]{file.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {

                            }
                        });
                return true;
            }
            else
            {return false;}
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static String getStringFromBitmap(Bitmap bitmapPicture) {
        try {
            final int COMPRESSION_QUALITY = 100;

            String encodedImage;
            ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
            bitmapPicture.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, byteArrayBitmapStream);

            byte[] ImageByte = byteArrayBitmapStream.toByteArray();


            encodedImage = Base64.encodeToString(ImageByte, Base64.DEFAULT);
            return encodedImage;
        }
        catch (Exception ex)
        {
            return "";
        }
    }
}
