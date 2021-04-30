/*
    Written by newreza🍷
    Telegram: @newreza
    ✉ Mail: newreza7@gmail.com
    Version 3.3
    Updates: Added method saveBitmap();
    Copyright © All rights reserved for newreza
    ***For legal use, don't remove this comment***
*/
package ir.newreza.nicodeadventure.Modules;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import static android.content.Context.MODE_PRIVATE;

public class T {
    public static int w, h, inch, minLength, actionHeight, actionPadding, statusHeight, size1, size2, corner, p0, p1, p2, p3, p4;
    public static int c11, c12, c13, c21, c22, c23, c31, c32, c33, c41, c42, c43;
    public static float dimens, widthDimens, heightDimens;
    public static Typeface tf01, tf02;
    public static GradientDrawable mainBg;
    public static SharedPreferences shp;
    public static SharedPreferences.Editor shpe;
    private Context context;
    private static String[] pn;
    private static List<String> pnList;
    private static Toast toast;
    Resources resources;

    public T(Context context) {
        this.context = context;
        resources = context.getResources();
        shp = context.getSharedPreferences("shp", MODE_PRIVATE);
        shpe = shp.edit();
        shpe.apply();
        w = Resources.getSystem().getDisplayMetrics().widthPixels;
        h = Resources.getSystem().getDisplayMetrics().heightPixels;
        inch = Resources.getSystem().getDisplayMetrics().densityDpi;
        pn = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};
        pnList = Arrays.asList(pn);
        minLength = Math.min(w, h);
        widthDimens = (float) w / inch;
        heightDimens = (float) h / inch;
        dimens = (float) Math.sqrt(Math.pow(widthDimens, 2) + Math.pow(heightDimens, 2));
        size1 = 14;
        size2 = 16;
        corner = inch / 10;
        p0 = w / 16;
        p1 = w / 32;
        p2 = w / 64;
        p3 = w / 128;
        p4 = w / 256;
        actionHeight = getActionBarHeight();
        statusHeight = getStatusBarHeight();
        actionPadding = actionHeight / 5;

        tf01 = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans.ttf");
        tf02 = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSans-Bold.ttf");

        //Green
        c11 = Color.rgb(46, 125, 50);
        c12 = Color.argb(200, Color.red(c11), Color.green(c11), Color.blue(c11));
        c13 = Color.argb(50, Color.red(c11), Color.green(c11), Color.blue(c11));

        //Red
        c21 = Color.rgb(198, 40, 40);
        c22 = Color.argb(200, Color.red(c21), Color.green(c21), Color.blue(c21));
        c23 = Color.argb(50, Color.red(c21), Color.green(c21), Color.blue(c21));
    }

    private int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
    }

    private int getStatusBarHeight() {
        return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"));
    }

    public static String splitter(String text) {
        int k = 0;
        if (text.equals("")) {
            return "0";
        }
        String res = "";
        int index = text.length() % 3;
        int part = text.length() / 3;
        if (index > 0) {
            part++;
        } else {
            index = 3;
        }
        for (int j = 0; j < part; j++) {
            res = res + text.substring(0, index) + ",";
            text = text.substring(index);
            index = 3;
        }
        return res.substring(0, res.length() - 1);
    }

    public static String splitter(int i) {
        return splitter(String.valueOf(i));
    }

    public static String splitter(long l) {
        return splitter(String.valueOf(l));
    }

    public static String english(String text) {
        if (text.equals("")) {
            return "0";
        }
        String res = "";
        int count = text.length();
        for (int i = 0; i < count; i++) {
            if (text.charAt(i) != ',') {
                int j = 0;
                for (; j < 10; j++) {
                    if (text.charAt(i) == pn[j].charAt(0)) {
                        res += String.valueOf(j);
                        break;
                    }
                }
                if (j == 10) {
                    res += String.valueOf(text.charAt(i));
                }
            }
        }
        return res;
    }

    public static String persian(int i) {
        int archive = i;
        String s = "";
        do {
            s = pn[Math.abs(i % 10)] + s;
            i = i / 10;
        } while (i != 0);
        if (archive < 0) {
            s = "-" + s;
        }
        return s;
    }

    public static String persian(float f) {
        String temp = String.valueOf(f);
        int integer = (int) f;
        int decimal = Integer.parseInt(temp.substring(temp.indexOf('.') + 1));
        return persian(integer) + "/" + persian(decimal);
    }

    public static String persian(String text) {
        String res = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) >= 48 && text.charAt(i) <= 57) {
                res += pn[text.charAt(i) - 48];
            } else {
                res += String.valueOf(text.charAt(i));
            }
        }
        return res;
    }

    public static String persian(long l) {
        long archive = l;
        String res = "";
        do {
            res = pn[(int) Math.abs(l % 10)] + res;
            l = l / 10;
        } while (l != 0);
        if (archive < 0) {
            res = "-" + res;
        }
        return res;
    }

    public static String[] persian(String[] texts) {
        String[] res = new String[texts.length];
        for (int i = 0; i < texts.length; i++) {
            res[i] = persian(texts[i]);
        }
        return res;
    }

    public static String persianSplitter(String text) {
        if (text.equals("")) {
            return "۰";
        }
        String res = "";
        int index = text.length() % 3;
        int part = text.length() / 3;
        if (index > 0) {
            part++;
        } else {
            index = 3;
        }
        for (int j = 0; j < part; j++) {
            res = res + persian(text.substring(0, index)) + ",";
            text = text.substring(index);
            index = 3;
        }
        return res.substring(0, res.length() - 1);
    }

    public static String[] persianSplitter(String[] texts) {
        String[] res = new String[texts.length];
        for (int i = 0; i < texts.length; i++) {
            res[i] = persianSplitter(texts[i]);
        }
        return res;
    }

    public static String persianSplitter(int i) {
        return persianSplitter(String.valueOf(i));
    }

    public static String persianSplitter(long l) {
        return persianSplitter(String.valueOf(l));
    }

    public static boolean isNumeric(String s) {
        if (s.length() == 0) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < 48 || s.charAt(i) > 57) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumericSplitter(String text) {
        if (text.length() == 0) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            if (!((text.charAt(i) >= 48 && text.charAt(i) <= 57) || text.charAt(i) == ',' || pnList.contains(String.valueOf(text.charAt(i))))) {
                return false;
            }
        }
        return true;
    }

    public static String elapsedTime(long l) {
        long timeDiff = (System.currentTimeMillis() - l) / 1000;
        if (timeDiff < 60) {
            return persian(timeDiff) + " ثانیه پیش";
        }
        if (timeDiff < 3600) {
            return persian(timeDiff / 60) + " دقیقه پیش";
        }
        if (timeDiff < 86400) {
            return persian(timeDiff / 3600) + " ساعت پیش";
        }
        if (timeDiff < 604800) {
            return persian(timeDiff / 86400) + " روز پیش";
        }
        if (timeDiff < 2592000) {
            return persian(timeDiff / 604800) + " هفته پیش";
        }
        if (timeDiff < 31558150) {
            return persian(timeDiff / 2592000) + " ماه پیش";
        }
        return persian(timeDiff / 31558150) + " سال پیش";
    }

    public static String persianNumberToLetter(String number) {
        if (number.equals("")) {
            return "۰ تومان";
        }
        try {
            Long.parseLong(number);
            String[] symbols = new String[]{"", " هزار", " میلیون", " میلیارد", " بیلیون", " بیلیارد"};
            int digits = number.length();
            int temp2 = digits % 3 == 0 ? 3 : digits % 3;
            int digitPacks = digits / 3 + (temp2 == 3 ? 0 : 1);
            String res = "";
            int temp1 = 0, piece;
            for (int i = digitPacks; i > 0; i--) {
                piece = Integer.parseInt(number.substring(temp1, temp1 + temp2));
                if (piece > 0) {
                    res = res + T.persian(piece) + symbols[i - 1] + " و ";
                }
                temp1 += temp2;
                temp2 = 3;
            }
            return res.substring(0, res.length() - 2) + "تومان";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setImageCorner(ImageView iv, int resource, int radius) {
        setImageCorner(iv, BitmapFactory.decodeResource(context.getResources(), resource), radius, false);
    }

    public static void setImageCorner(final ImageView iv, final Bitmap bitmap, final int radius, boolean isBackground) {
        iv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        Bitmap bm = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int minBitmap = Math.min(bm.getWidth(), bm.getHeight());
        int minIv = Math.min(iv.getWidth(), iv.getHeight());
        int r = minBitmap / (minIv / radius);
        Canvas canvas = new Canvas(bm);
        Paint pClear = new Paint();
        pClear.setColor(Color.RED);
        pClear.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        pClear.setAntiAlias(true);
        pClear.setStrokeWidth(1);
        double r2 = Math.pow(r, 2);
        for (int i = 0; i < r; i++) {
            float temp = (float) (r - Math.sqrt(r2 - Math.pow(r - i, 2)));
            for (int j = 0; j < 4; j++) {
                canvas.drawLine(0, i, temp, i, pClear);
                canvas.drawLine(bm.getWidth(), i, bm.getWidth() - temp, i, pClear);
                canvas.drawLine(0, bm.getHeight() - i, temp, bm.getHeight() - i, pClear);
                canvas.drawLine(bm.getWidth(), bm.getHeight() - i, bm.getWidth() - temp, bm.getHeight() - i, pClear);
            }
        }
        if (isBackground) {
            iv.setBackground(new BitmapDrawable(bitmap));
        } else {
            iv.setImageBitmap(bm);
        }
    }

    public void setLocale(String localeText) {
        Locale locale = new Locale(localeText);
        Locale.setDefault(locale);
        Configuration config = resources.getConfiguration();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public static void setColor(View v, int c1, int c2) {
        StateListDrawable sld1 = new StateListDrawable();
        sld1.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(c2));
        sld1.addState(new int[]{}, new ColorDrawable(c1));
        v.setBackground(sld1);
    }

    public static void setGradient(View v, GradientDrawable gd1, GradientDrawable gd2) {
        StateListDrawable sld1 = new StateListDrawable();
        sld1.addState(new int[]{android.R.attr.state_pressed}, gd2);
        sld1.addState(new int[]{}, gd1);
        v.setBackground(sld1);
    }

    public static void setGradient(View v, int color1, int color2, int corner) {
        GradientDrawable gd1 = new GradientDrawable();
        gd1.setColor(color1);
        gd1.setCornerRadius(corner);
        GradientDrawable gd2 = new GradientDrawable();
        gd2.setColor(color2);
        gd2.setCornerRadius(corner);
        setGradient(v, gd1, gd2);
    }

    public static void setGradient(View v, int color1, int color2) {
        setGradient(v, color1, color2, corner);
    }

    public static Bitmap getColored(Context context, int res, int color) {
        return getColored(BitmapFactory.decodeResource(context.getResources(), res), color);
    }

    public static Bitmap getColored(Bitmap bitmap, int color) {
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int dimen = width * height;
        for (int i = 0; i < dimen; i++) {
            pixels[i] = Color.argb(Color.alpha(pixels[i]), red, green, blue);
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static Drawable getDrawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }

    public static Bitmap getBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static void setShadow(final View view, final int length, final int[] shadowColors) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int previousW = view.getWidth();
                int previousH = view.getHeight();
                int w = previousW + 2 * length;
                int h = previousH + 2 * length;
                Bitmap previousBitmap = Bitmap.createBitmap(getBitmap(view.getBackground()));
                if (previousBitmap.getWidth() == 1 && previousBitmap.getHeight() == 1) {
                    int color = previousBitmap.getPixel(0, 0);
                    previousBitmap = Bitmap.createBitmap(previousW, previousH, Bitmap.Config.ARGB_8888);
                    for (int i = 0; i < previousH; i++) {
                        for (int j = 0; j < previousW; j++) {
                            previousBitmap.setPixel(j, i, color);
                        }
                    }
                }
                Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);

                LinearGradient linearGradient = new LinearGradient(w / 2, h / 2, w / 2, 0, shadowColors, null, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setDither(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setAntiAlias(true);
                paint.setShader(linearGradient);
                canvas.drawRect(h / 2, 0, w - h / 2, h / 2, paint);

                linearGradient = new LinearGradient(w / 2, h / 2, w / 2, h, shadowColors, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                canvas.drawRect(h / 2, h / 2, w - h / 2, h, paint);

                RadialGradient radialGradient = new RadialGradient(h / 2, h / 2, h / 2, shadowColors, null, Shader.TileMode.CLAMP);
                paint.setShader(radialGradient);
                Path path = new Path();
                path.moveTo(h / 2, 0);
                path.quadTo(0, 0, 0, h / 2);
                path.quadTo(0, h, h / 2, h);
                canvas.drawPath(path, paint);

                radialGradient = new RadialGradient(w - h / 2, h / 2, h / 2, shadowColors, null, Shader.TileMode.CLAMP);
                paint.setShader(radialGradient);
                path = new Path();
                path.moveTo(w - h / 2, 0);
                path.quadTo(w, 0, w, h / 2);
                path.quadTo(w, h, w - h / 2, h);
                canvas.drawPath(path, paint);

                canvas.drawBitmap(previousBitmap, length, length, null);
                view.setBackground(getDrawable(bitmap));
                view.setPadding(view.getPaddingLeft() + length, view.getPaddingTop() + length, view.getPaddingRight() + length, view.getPaddingBottom() + length);
            }
        });
    }

    public static void setShadow(final View view, final int length, final int[] shadowColors, final int viewColor1, final int viewColor2, final int corner) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                GradientDrawable gd1 = new GradientDrawable();
                gd1.setColor(viewColor1);
                gd1.setCornerRadius(corner);
                GradientDrawable gd2 = new GradientDrawable();
                gd2.setColor(viewColor2);
                gd2.setCornerRadius(corner);
                setGradient(view, gd1, gd2);

                int previousW = view.getWidth();
                int previousH = view.getHeight();
                int w = previousW + 2 * length;
                int h = previousH + 2 * length;
                Bitmap previousBitmap1 = Bitmap.createBitmap(previousW, previousH, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(previousBitmap1);
                gd1.setBounds(0, 0, previousW, previousH);
                gd1.draw(canvas);
                Bitmap previousBitmap2 = Bitmap.createBitmap(previousW, previousH, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(previousBitmap2);
                gd2.setBounds(0, 0, previousW, previousH);
                gd2.draw(canvas);

                Bitmap newBitmap1 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Bitmap newBitmap2 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                Canvas canvas1 = new Canvas(newBitmap1);
                Canvas canvas2 = new Canvas(newBitmap2);

                LinearGradient linearGradient = new LinearGradient(w / 2, h / 2, w / 2, 0, shadowColors, null, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setDither(true);
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setAntiAlias(true);
                paint.setShader(linearGradient);
                canvas1.drawRect(h / 2, 0, w - h / 2, h / 2, paint);
                canvas2.drawRect(h / 2, 0, w - h / 2, h / 2, paint);

                linearGradient = new LinearGradient(w / 2, h / 2, w / 2, h, shadowColors, null, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                canvas1.drawRect(h / 2, h / 2, w - h / 2, h, paint);
                canvas2.drawRect(h / 2, h / 2, w - h / 2, h, paint);

                RadialGradient radialGradient = new RadialGradient(h / 2, h / 2, h / 2, shadowColors, null, Shader.TileMode.CLAMP);
                paint.setShader(radialGradient);
                Path path = new Path();
                path.moveTo(h / 2, 0);
                path.quadTo(0, 0, 0, h / 2);
                path.quadTo(0, h, h / 2, h);
                canvas1.drawPath(path, paint);
                canvas2.drawPath(path, paint);

                radialGradient = new RadialGradient(w - h / 2, h / 2, h / 2, shadowColors, null, Shader.TileMode.CLAMP);
                paint.setShader(radialGradient);
                path = new Path();
                path.moveTo(w - h / 2, 0);
                path.quadTo(w, 0, w, h / 2);
                path.quadTo(w, h, w - h / 2, h);
                canvas1.drawPath(path, paint);
                canvas2.drawPath(path, paint);

                canvas1.drawBitmap(previousBitmap1, length, length, null);
                canvas2.drawBitmap(previousBitmap2, length, length, null);
                StateListDrawable sld = new StateListDrawable();
                sld.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(newBitmap2));
                sld.addState(new int[]{}, new BitmapDrawable(newBitmap1));
                view.setBackground(sld);
                view.setPadding(view.getPaddingLeft() + length, view.getPaddingTop() + length, view.getPaddingRight() + length, view.getPaddingBottom() + length);
            }
        });
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void toast(Context context, String s) {
        try {
            toast.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        toast = Toast.makeText(context, s, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void toast(Context context, int i) {
        try {
            toast.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        toast = Toast.makeText(context, i, Toast.LENGTH_LONG);
        toast.show();
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void installAPK(Context context, String path) {
        if (Build.VERSION.SDK_INT >= 24) {
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setData(FileProvider.getUriForFile(context.getApplicationContext(), context.getPackageName() + ".fileprovider", new File(path)));
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static void setOrientation(Context context, int i) {
        try {
            ((AppCompatActivity) context).setRequestedOrientation(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isRunning(Context context, Class c) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (c.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<ArrayList<String>> cursor2list(Cursor cursor) {
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        int count = cursor.getCount();
        int columnCount = cursor.getColumnCount();
        cursor.moveToFirst();
        ArrayList<String> temp;
        for (int i = 0; i < count; i++) {
            temp = new ArrayList<>();
            for (int j = 0; j < columnCount; j++) {
                temp.add(cursor.getString(j));
            }
            res.add(temp);
            cursor.moveToNext();
        }
        return res;
    }

    public static ArrayList<String> array2arrayList(String[] array) {
        ArrayList<String> res = new ArrayList<>();
        Collections.addAll(res, array);
        return res;
    }

    public static ArrayList<Bitmap> array2arrayList(Bitmap[] array) {
        ArrayList<Bitmap> res = new ArrayList<>();
        Collections.addAll(res, array);
        return res;
    }

    public static ArrayList<ArrayList<String>> array2arrayList(String[][] array) {
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        ArrayList<String> temp;
        for (String[] strings : array) {
            temp = new ArrayList<>();
            Collections.addAll(temp, strings);
            res.add(temp);
        }
        return res;
    }

    public static JSONArray arrayList2jsonArrayForJSONObject(ArrayList<JSONObject> arrayList) {
        int size = arrayList.size();
        JSONArray res = new JSONArray();
        for (int i = 0; i < size; i++) {
            res.put(arrayList.get(i));
        }
        return res;
    }

    public static JSONArray arrayList2jsonArrayForString(ArrayList<String> arrayList) {
        int size = arrayList.size();
        JSONArray res = new JSONArray();
        for (int i = 0; i < size; i++) {
            res.put(arrayList.get(i));
        }
        return res;
    }

    public static ArrayList<JSONObject> jsonArray2arrayListForJSONObject(JSONArray jsonArray) {
        int length = jsonArray.length();
        ArrayList<JSONObject> res = new ArrayList<>();
        try {
            for (int i = 0; i < length; i++) {
                res.add(jsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static ArrayList<String> jsonArray2arrayListForString(JSONArray jsonArray) {
        int length = jsonArray.length();
        ArrayList<String> res = new ArrayList<>();
        try {
            for (int i = 0; i < length; i++) {
                res.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static JSONArray array2jsonArray(String[] array) {
        JSONArray res = new JSONArray();
        for (String s : array) {
            res.put(s);
        }
        return res;
    }

    public static int findIndex(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean saveBitmap(Context context, String name, Bitmap bitmap) {
        deleteBitmap(context, name);
        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0 && name.substring(dotIndex + 1).equalsIgnoreCase("png")) {
            compressFormat = Bitmap.CompressFormat.PNG;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, 100, byteArrayOutputStream);
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(name, MODE_PRIVATE);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Bitmap loadBitmap(Context context, String name) {
        try {
            return BitmapFactory.decodeStream(context.openFileInput(name));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteBitmap(Context context, String name) {
        context.deleteFile(name);
    }

    public static void showNotification(Context context, String title, String text, int logoRes, Class c, JSONObject data) {
        String channelId = context.getPackageName();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(logoRes)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true);
        if (c != null) {
            Intent intent = new Intent(context, c);
            intent.putExtra("data", data.toString());
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(c);
            stackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBuilder.setContentIntent(pendingIntent);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            String channelDescription = "New Message!";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, title, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        } else {
            notificationBuilder.setVibrate(new long[]{0, 250, 250, 250});
            notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        }
        assert notificationManager != null;
        notificationManager.notify(1, notificationBuilder.build());
    }

}
