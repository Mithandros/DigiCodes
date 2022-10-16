package com.maksof.linkapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class commonClass {

    static String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;
    static String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);
    static String number = "", whatsApp = "";
    int PICK_IMAGE_MULTIPLE = 2;
    static String host = "http://lynk-app.com/api/";
    static String imgUrl = host + "cdn/images/";

    public static void apiIntegration(Context context, String url, String data, String header, String screen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new APIServices(context, host + url, data, header, screen).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{null});
        else
            new APIServices(context, host + url, data, header, screen).execute(new String[]{null});
    }

    public static void apiIntegration(Context context, String url, MultipartEntity data, String header, String screen,String cls) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new APIServices(context, host + url, data, header, screen,cls).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{null});
        else
            new APIServices(context, host + url, data, header, screen,cls).execute(new String[]{null});
    }


    public static void apiIntegration(Context context, String url, String data, String header, String screen, String groupID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new APIServices(context, host + url, data, header, screen, groupID).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{null});
        else
            new APIServices(context, host + url, data, header, screen, groupID).execute(new String[]{null});
    }


    public static void apiIntegrationGet(Context context, String url, String header, String screen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new APIServices("GET", context, host + url, header, screen).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{null});
        else
            new APIServices("GET", context, host + url, header, screen).execute(new String[]{null});
    }

    public static String randomColors() {
        String[] colors = {"#ADE6D0", "#F1F0CF", "#EBCEED", "#DEB3EB", "#5f227f", "#cd96cf", "#315440", "#0d271c", "#3C8E41", "#8AC249", "#A6D46E", "#835E9B"};
        int r = new Random().nextInt(colors.length);
        return colors[r];
    }

    public static void openWhatsApp(Activity context, String contactNumber) {
        String wp = contactNumber;
        TelephonyManager telephonyMngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String contryId = telephonyMngr.getSimCountryIso().toUpperCase();
        String[] arrContryCode=context.getResources().getStringArray(R.array.DialingCountryCode);
        for(int i=0; i<arrContryCode.length; i++){
            String[] arrDial = arrContryCode[i].split(",");
            if(arrDial[1].trim().equals(contryId.trim())){
                if(contactNumber.charAt(0)=='0')
                {
                    wp = "+"+arrDial[0]+contactNumber.replaceFirst("0", "");
                }
                break;
            }
        }

        String url = "http://api.whatsapp.com/send?phone=" + wp;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
                Toast.makeText(context, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    public static void call(String number, Activity activity) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));

        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        activity.startActivity(callIntent);
    }


    public static void sms(Activity activity, String contctnumber) {
        try {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("sms_body", "");
            sendIntent.putExtra("address", contctnumber);
            sendIntent.setType("vnd.android-dir/mms-sms");
            activity.startActivity(sendIntent);

        } catch (Exception e) {
            Toast.makeText(activity,
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void downloadContact(final Activity activity, final String newContactName, final String contctnumber, final String email, final String occupation) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        if (newContactName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            newContactName).build());
        }

        // MOBILE NUMBER
        if (contctnumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contctnumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }
        // EMAIL
        if (email != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        // OCCUPATION
        if (!occupation.equals("")) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, occupation)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        try {
            activity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Contact sved", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public static void selectImage(final Activity activity,String type) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder sBuilder = new AlertDialog.Builder(activity);
        sBuilder.setTitle("Add Photo!");
        sBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (options[item].equals("Choose from Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    if(type.equals("profileImage"))
                    activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
                    else
                        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                } else if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT >= 24) {
                        try {
                            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                            m.invoke(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Constants.fileUri = Uri.fromFile(getOutputMediaFile(activity));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Constants.fileUri);
                    if(type.equals("profileImage"))
                        activity.startActivityForResult(intent, 120);
                    else
                        activity.startActivityForResult(intent, 100);

                }
            }
        });
        sBuilder.show();
    }

    static File getOutputMediaFile(Activity activity) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File f = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        return f;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    public static File rotate(File f) throws IOException {
        ExifInterface ei = new ExifInterface(f.getAbsolutePath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        Bitmap bitmap;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = fileToBitmap(f);
                rotatedBitmap = rotateImage(bitmap, 90);
                f = bitmapTofile(rotatedBitmap,f);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = fileToBitmap(f);
                rotatedBitmap = rotateImage(bitmap, 180);
                f = bitmapTofile(rotatedBitmap,f);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmap = fileToBitmap(f);
                rotatedBitmap = rotateImage(bitmap, 270);
                f = bitmapTofile(rotatedBitmap,f);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
                break;
            default:
        }
            return f;
    }
    public static Bitmap fileToBitmap(File file){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public static File bitmapTofile(Bitmap bitmap,File f){
        try {
            FileOutputStream fout = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, fout);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public static void imageAPI(int requestCode, int resultCode, Intent data, final Activity activity,String cls) {
        if (requestCode == 100||requestCode==120) {
            if (resultCode == activity.RESULT_OK) {
                if (Build.VERSION.SDK_INT >= 24) {
                    try {
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                File file = new File(Constants.fileUri.getPath());
                try {
                    file = rotate(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                ContentBody cbFile = new FileBody(file);
                mpEntity.addPart("fileToUpload[]", cbFile);
              /*  if(requestCode==100)
                    commonClass.apiIntegration(activity, "profile/uploadImage", mpEntity, "", "uploadImage",cls);
                if(requestCode==120)
                    commonClass.apiIntegration(activity, "profile/uploadImage", mpEntity, "", "uploadProfile",cls);
*/
            }
        }
        String imageEncoded;
        if (resultCode != activity.RESULT_CANCELED)
            if ((requestCode == 2||requestCode == 3) && resultCode == activity.RESULT_OK
                    && null != data) {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    File[] fileArray = new File[mClipData.getItemCount()];

                    if (mClipData.getItemCount() > 5) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                APIServices.toastMessage("Please select at most 5 images at a time.",activity);
                            }
                        });
                    } else {
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            String wholeID = DocumentsContract.getDocumentId((Uri) uri);
                            String id = wholeID.split(":")[1];
                            String[] column = {MediaStore.Images.Media.DATA};
                            String sel = MediaStore.Images.Media._ID + "=?";
                            Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    column, sel, new String[]{id}, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(column[0]);
                            imageEncoded = cursor.getString(columnIndex);
                            File file = new File(imageEncoded);
                            if (cursor != null)
                                cursor.moveToFirst();
                            ContentBody cbFile = new FileBody(file);
                            mpEntity.addPart("fileToUpload[]", cbFile);
                            fileArray[i] = file;
                            cursor.close();
                        }
                       /* if (requestCode==3)
                            commonClass.apiIntegration(activity, "profile/uploadImage", mpEntity, "", "uploadProfile",cls);
                        else
                            commonClass.apiIntegration(activity, "profile/uploadImage", mpEntity, "", "uploadImage",cls);
                  */  }
                } else if (data.getData() != null) {

                      Uri selectedImage = data.getData();
                    try {
                      String wholeID = DocumentsContract.getDocumentId(selectedImage);
                      String id = wholeID.split(":")[1];
                      String[] column = {MediaStore.Images.Media.DATA};
                      String sel = MediaStore.Images.Media._ID + "=?";
                      Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                              column, sel, new String[]{id}, null);
                      cursor.moveToFirst();
                      int columnIndex = cursor.getColumnIndex(column[0]);
                      imageEncoded = cursor.getString(columnIndex);
                      cursor.close();
                   } catch (Exception e){
                      Cursor cursor = null;
                      try {
                          String[] proj = { MediaStore.Images.Media.DATA };
                          cursor = activity.getContentResolver().query(selectedImage, proj, null, null, null);
                          int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                          cursor.moveToFirst();
                          imageEncoded = cursor.getString(column_index);
                      } finally {
                          if (cursor != null) {
                              cursor.close();
                          }
                      }

                  }
                    MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                    File file = new File(imageEncoded);
                    ContentBody cbFile = new FileBody(file);
                    mpEntity.addPart("fileToUpload[]", cbFile);
                 /*   if (requestCode == 3)
                        commonClass.apiIntegration(activity, "profile/uploadImage", mpEntity, "", "uploadProfile", cls);
                    else
                        commonClass.apiIntegration(activity, "profile/uploadImage", mpEntity, "", "uploadImage", cls);
*/
                }
            }
    }


    public static String urlBitmap(Activity activity, int n) {
        String url = null;
        try {
            url = PreferenceUtils.getGroupNamesJSON("imgURLSlist", activity).get(n).getString("" + n + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static void email(String email, Activity activity) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        emailIntent.setType("message/rfc822");
        activity.startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
    }

    public static void location(String address, Activity activity){
        String map = "http://maps.google.co.in/maps?q=" + address;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        activity.startActivity(i);
    }
    public static void website(String address, Activity activity) {
        String url = address;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://" + url));
        activity.startActivity(i);
    }

    public static void permissionPermanentlyDenied(Activity activity, int[] grantResults,String screen) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage(activity,"images");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (!activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)||!activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                // Comment 2.
                snackBar(activity);
            }
        }
    }

    public static void snackBar(Activity activity) {
        Snackbar s = Snackbar.make(activity.findViewById(android.R.id.content), "We require camera access permission. Please allow it in Settings.", Snackbar.LENGTH_INDEFINITE)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivityForResult(intent, 1000);     // Comment 3.
                    }
                });
        View snackbarView = s.getView();
        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        textView.setMaxLines(3);    // Comment 4.
        s.show();
    }


    public static String whtasAppNum(Activity activity, String cNumber) {
        whatsApp = cNumber;
        Cursor cursor = activity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ORDER);

        if (cursor != null && cursor.getCount() > 0) {
            int j = 0;
            while (cursor != null && cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor pCur = activity.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = ?", new String[]{id}, null);
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                    if (pCur != null && pCur.moveToNext()) {
                        number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                if (number.equals(cNumber)) {
                    final String[] projection = {
                            ContactsContract.Data.CONTACT_ID,
                            ContactsContract.Data.DISPLAY_NAME,
                            ContactsContract.Data.MIMETYPE,
                            "account_type",
                            ContactsContract.Data.DATA3,
                    };

                    final String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=? and " + ContactsContract.Data.MIMETYPE + " =? and account_type=?";
                    final String[] selectionArgs = {id,
                            "vnd.android.cursor.item/vnd.com.whatsapp.profile",
                            "com.whatsapp"
                    };

                    ContentResolver cr = activity.getContentResolver();
                    Cursor c = cr.query(
                            ContactsContract.Data.CONTENT_URI,
                            projection,
                            selection,
                            selectionArgs,
                            ORDER);

                    if (c != null && c.moveToNext()) {
                        String wNumber = c.getString(c.getColumnIndex(ContactsContract.Data.DATA3));
                        if (wNumber != null) {
                            whatsApp = wNumber;
                        }
                    } else whatsApp = cNumber;
                    break;
                }
                pCur.close();
            }
            cursor.close();
        }
        return whatsApp;
    }

    static Animator currentAnimator;
    static int shortAnimationDuration;
  static void zoomImageFromThumb2(final View thumbView, String imageResId, ImageView expandedImageView, Activity activity) {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
      CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(activity);
      circularProgressDrawable.setStrokeWidth(5f);
      circularProgressDrawable.setCenterRadius(30f);
      circularProgressDrawable.start();
      GlideApp.with(activity)
                .load(commonClass.host + "cdn/images/" + imageResId)
                .placeholder(circularProgressDrawable)
                .into(expandedImageView);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();
        activity.findViewById(R.id.labUserContainer).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);
        float startScale;

        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        expandedImageView.setVisibility(View.VISIBLE);

        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale+200, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;
    }


    public static String getContactNumber(Activity activity, String name) {
        String num = null;
        Cursor cursor = activity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ORDER);

        if (cursor != null && cursor.getCount() > 0) {
            int j = 0;
            while (cursor != null && cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name1 = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if(name1!=null)
                if ((name1.equals(name)) || name1 == name) {
                    Cursor pCur = activity.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[]{id}, null);
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                        if (pCur != null && pCur.moveToNext()) {
                            num = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                    if(pCur != null)
                    pCur.close();
                        break;
                }
            }
            cursor.close();
        }
    return num;
  }
  public static JSONObject myContacts(Context context) throws JSONException {
      JSONObject job = new JSONObject();
      new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... voids) {
              Cursor cursor= context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, "HAS_PHONE_NUMBER <> 0", null, ORDER);
              if (cursor!= null) {
                  final int displayNameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                  final int idIndex= cursor.getColumnIndex(ContactsContract.Contacts._ID);
                  String displayName, number = null, idValue;
                  while (cursor.moveToNext()) {
                      displayName = cursor.getString(displayNameIndex);
                      idValue= cursor.getString(idIndex);
                      Cursor phones  = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id = '" + idValue + "'", null, null);
                      if(phones!=null){
                          phones.moveToFirst();
                          try {
                              number = phones.getString(phones.getColumnIndex("data1"));
                              if(!Constants.numList.contains(number)){
                                  Constants.nameList.add(displayName);
                                  Constants.numList.add(number);
                                  Constants.colorList.add(commonClass.randomColors());
                                  Constants.listHashMap.put(displayName, null);
                              }
                          }
                          catch (CursorIndexOutOfBoundsException e)
                          {}
                      }
                  }
                  cursor.close();
                  PreferenceUtils.saveGroupString(Constants.nameList,"nameList",(Activity) context);
                  PreferenceUtils.saveGroupString(Constants.numList,"numList",(Activity) context);
                  PreferenceUtils.saveGroupString(Constants.colorList,"colorList",(Activity) context);
                  JSONObject arrayOfNum = new JSONObject();
                 /* try {
                      arrayOfNum.put("phoneNumber", new JSONArray( Constants.numList));
//                      commonClass.apiIntegration(context, "user/checkuserregistration", arrayOfNum.toString(), PreferenceUtils.getToken(context), "checkUserRegistration");
                  }catch (JSONException je){
                  }*/
              }

              try {
                  job.put("name",Constants.nameList);
                  job.put("num",Constants.numList);
                  job.put("color",Constants.colorList);
              } catch (JSONException e) {
                  e.printStackTrace();
              }
              return null;
          }

          @Override
          protected void onPostExecute(Void aVoid) {
              super.onPostExecute(aVoid);
              if(Constants.startApp==false)
              {
                  APIServices.updateLab((Activity) context);
                  Constants.startApp = true;
              }
          }
      }.execute();
      return job;
  }
  public static void updateConstantsImageUrl(ArrayList<String> img){
      Constants.imgsURL.removeAll(Constants.imgsURL);
      for (int i = 0; i < img.size(); i++) {
          Constants.imgsURL.add(img.get(i));
      }
  }

}