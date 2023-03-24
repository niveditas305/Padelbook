package snow.app.padelbook.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import snow.app.padelbook.R;

public class Utils {
     public static ProgressDialog progressDialog;

    public static Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public static Utils getInstance(Context context) {
        return new Utils(context);
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showToast(Context context,String toast_string) {
        Toast toast = Toast.makeText(context, toast_string, Toast.LENGTH_SHORT);
       //  View view = toast.getView();
      //   view.setBackgroundColor(ContextCompat.getColor(context,R.color.purple_500));
     //   view.getBackground().setColorFilter(ContextCompat.getColor(context,R.color.purple_500), PorterDuff.Mode.SRC_IN);
      //  TextView text = view.findViewById(android.R.id.message);
      //  text.setTextColor(ContextCompat.getColor(context,R.color.white));
        toast.show();
    }


    public static boolean isNetworkConnected() {
        return new CheckConnectivity(context).isNetworkAvailable();
    }
    public static void showInternetToast() {
        Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
    }

    public static boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }
    public static void  loadImage(String url,Context context,ImageView imageView) {
        String base_url = "https://club.padelbook.gr/public/assets/images/";
        if(url.startsWith("http") || url.startsWith("https")){
            Glide.with(context).asBitmap().load(url).error(R.drawable.ic_user)
                    .placeholder(R.drawable.ic_user).into(imageView);
        }
        else{
            Glide.with(context).asBitmap().load(base_url+url).error(R.drawable.ic_user)
                    .placeholder(R.drawable.ic_user).into(imageView);
        }

    }

    public static void  loadImageForProfile(String url,Context context,ImageView imageView) {
        String base_url = "https://club.padelbook.gr/public/assets/images/users/";
        if(url.startsWith("http") || url.startsWith("https")){
            Glide.with(context).asBitmap().load(url).error(R.drawable.ic_user)
                    .placeholder(R.drawable.ic_user).into(imageView);
        }
        else{
            Glide.with(context).asBitmap().load(base_url+url).error(R.drawable.ic_user)
                    .placeholder(R.drawable.ic_user).into(imageView);
        }

    }
    public static String displayWeekDay(int day) {
        String dayy = "";
        if (day == 0) {
            dayy = "Sunday";
        } else if (day == 1) {
            dayy = "Monday";
        } else if (day == 2) {
            dayy = "Tuesday";
        } else if (day == 3) {
            dayy = "Wednesday";
        } else if (day == 4) {
            dayy = "Thursday";
        } else if (day == 5) {
            dayy = "Friday";
        } else if (day == 6) {
            dayy = "Saturday";
        }
        return dayy;

    }
    public static void dismissProgress() {
        if (progressDialog == null) {
            return ;
        }
        progressDialog.dismiss();
        progressDialog = null;
    }

    public static void showProgress(Context context) {
        dismissProgress();
        progressDialog = new ProgressDialog(context, R.style.TransparentProgressDialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static Boolean isDateMatched(Date date) {
        Boolean matched = false;

        String dateArr[] = String.valueOf(date).split(" ");
        String currentArr[] = String.valueOf(Calendar.getInstance().getTime()).split(" ");

        String month = dateArr[1];
        String datee = dateArr[2];
        String year = dateArr[dateArr.length - 1];
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String strDate = datee + month + year;
        String currentDate = currentArr[2] + currentArr[1] + currentArr[currentArr.length - 1];
        if (strDate.equals(currentDate)) {
            matched = true;
        }
        return matched;
    }

    //    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type) || "raw".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                String id = DocumentsContract.getDocumentId(uri);
//                id = id.replaceAll("\\D+","");
//                 Uri contentUri=null;
//                try{
//                   contentUri = ContentUris.withAppendedId(
//                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//                }catch(NumberFormatException e){
//                    Log.e("exception",e.getMessage());
//                    contentUri=uri;
//                }
                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");

                    }
                    try {
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                        return getDataColumn(context, contentUri, null, null);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }


                //  return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return "";
    }
    public static void setStrokeColorDynamic(View view,Context context,int colorData){
        GradientDrawable drawable = (GradientDrawable)view.getBackground();
        drawable.setStroke(1,ContextCompat.getColor(context,colorData));
    }

    public static void highlightView(Context context, TextView textView, ArrayList<TextView> textViews) {

        for (int i = 0; i < 4; i++) {


            if (textView == textViews.get(i)) {
                int color = context.getResources().getColor(R.color.btn_clr_blue_light);
                textView.setTextColor(color);
                for (Drawable drawable : textView.getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
                    }
                }
            } else {
                int color = context.getResources().getColor(R.color.btn_clr_blue_light);
                textViews.get(i).setTextColor(color);
                for (Drawable drawable : textViews.get(i).getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
                    }
                }
            }
        }
    }
    public static void highlightViewPinkTwo(Context context, TextView textView, ArrayList<TextView> textViews) {

        for (int i = 0; i < 4; i++) {


            if (textView == textViews.get(i)) {
                int color = context.getResources().getColor(R.color.btn_clr_blue_light);
                textView.setTextColor(color);
                for (Drawable drawable : textView.getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
                    }
                }
            } else {
                int color = context.getResources().getColor(R.color.btn_clr_blue_light);
                textViews.get(i).setTextColor(color);
                for (Drawable drawable : textViews.get(i).getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
                    }
                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String toPath(Context context, Uri uri) {
        if (uri == null) {
            //   LogUtils.verbose("uri is null");
            return "";
        }
        // LogUtils.verbose("uri: " + uri.toString());
        String path = uri.getPath();
        String scheme = uri.getScheme();
        String authority = uri.getAuthority();
        //是否是4.4及以上版本
        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if (uri.getScheme().equals("content")) {
                return getDataColumn(context, uri, null, null);
            } else if (RawDocumentsHelper.isRawDocId(docId)) {
                return RawDocumentsHelper.getAbsoluteFilePath(docId);
            } else if (RawDocumentsHelper.isPrimaryDocId(docId)) {
                return RawDocumentsHelper.getPrimaryAbsoluteFilePath(docId);
            } else {
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                return getDataColumn2(context, contentUri, null, null);
            }

        }
        // MediaStore (and general)
        else {
            if ("content".equalsIgnoreCase(scheme)) {
                // Return the remote address
                if (authority.equals("com.google.android.apps.photos.content")) {
                    return uri.getLastPathSegment();
                }
                return _queryPathFromMediaStore(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(scheme)) {
                return uri.getPath();
            }
        }
        //   LogUtils.verbose("uri to path: " + path);
        return path;
    }

    public static String getDataColumn2(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {
        Cursor cursor = null;
        final String[] projection = {
                MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int columnIndex =
                        (uri.toString().startsWith("content://com.google.android.gallery3d"))
                                ? cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                                : cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                if (columnIndex != -1) {
                    return cursor.getString(columnIndex);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private static String _queryPathFromMediaStore(Context context, Uri uri, String selection, String[] selectionArgs) {
        String filePath = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                filePath = cursor.getString(column_index);
                cursor.close();
            }
        } catch (IllegalArgumentException e) {
            //  LogUtils.error(e);
        }
        return filePath;
    }

    public static class RawDocumentsHelper {
        public static final String RAW_PREFIX = "raw:";
        public static final String PRIMARY_PREFIX = "primary:";

        public static boolean isRawDocId(String docId) {
            return docId != null && docId.startsWith(RAW_PREFIX);
        }

        public static boolean isPrimaryDocId(String docId) {
            return docId != null && docId.startsWith(PRIMARY_PREFIX);
        }

        public static String getDocIdForFile(File file) {
            return RAW_PREFIX + file.getAbsolutePath();
        }

        public static String getAbsoluteFilePath(String rawDocumentId) {
            return rawDocumentId.substring(RAW_PREFIX.length());

        }

        public static String getPrimaryAbsoluteFilePath(String rawDocumentId) {
            return rawDocumentId.substring(PRIMARY_PREFIX.length());

        }
    }


}