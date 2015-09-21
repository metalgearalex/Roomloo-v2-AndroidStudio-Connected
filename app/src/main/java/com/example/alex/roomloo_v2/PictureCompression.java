package com.example.alex.roomloo_v2;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by Alex on 9/20/2015.
 */
public class PictureCompression {

//this code is pretty much all from: http://developer.android.com/training/displaying-bitmaps/load-bitmap.html

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight; //outHeight is just a public .Options method that returns the height of the bitmap
        final int width = options.outWidth;

//inSampleSize is a method of BitmapFactory.Options that determines how big each sample should be for each pixel;
// a sample size of 2 has one horizontal pixel for every 2 horizontal pixels in the original file
// so a sample size of 2 has 1/4 as many pixels as the original
//note we give the reqWidth and reqHeight a real value when we call the decodeSampledBitmapFromResource method below
        int inSampleSize = 2;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

// Calculate the largest inSampleSize value that is a power of 2 and keeps both
// height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
                    }
        }

        return inSampleSize;
            }
    //note we give the reqWidth and reqHeight a real value when we call the method. for example a 100 x 100 pixel thumbnail
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        //Android's BitmapFactory class provides several decoding methods (decodeByteArray(), decodeFile(),decodeResource(), etc.) for creating a Bitmap from various sources.
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
        //decodeResource(Resources res, int id, BitmapFactory.Options opts)
        //res = resources object containing the image
        //id = resource id of the image (R.id.....)
        //opts	null-ok; Options that control downsampling and whether the image should be completely decoded, or just is size returned
            }



//the stuff below is from the book and is more geared for scaling down a Bitmap we get from an external file.
// NOT CURRENTLY USING THIS but will probably be useful later when I pull Images from the database






    //conservative scale method to check to see how big the screen is
    //and then scale the image down to that size
    //this way the ImageView you load in will always be smaller than this so very conservative estimate
    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path, size.x, size.y);
            }


    //scaling and displaying bitmap
    //BitmapFactory is used to get a Bitmap from a file
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        //Read in the dimensions of the new image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        //attempt: BitmapFactory.decodeResource(Context.getResources(), R.drawable.livingroom);//former code>>

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        //Figure out how much to scale down by
// inSampleSize determines how big each sample should be for each pixel;
// a sample size of 2 has one horizontal pixel for every 2 horizontal pixels in the original file
// so a sample size of 2 has 1/4 as many pixels as the original
        int inSampleSize = 3;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        //Read in and create final bitmap
        return BitmapFactory.decodeFile(path, options);
    }

}
