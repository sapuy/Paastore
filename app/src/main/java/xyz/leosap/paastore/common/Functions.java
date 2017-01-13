package xyz.leosap.paastore.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.leosap.paastore.R;
import xyz.leosap.paastore.helpers.SQLHelper;


/**
 * Created by LeoSap on 27/11/2016.
 */

public class Functions {

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("ls_pref", Context.MODE_PRIVATE);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public static String epochToDate(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date(time * 1000));
    }

    public static String numberToFormat(int number) {
        NumberFormat nf = NumberFormat.getInstance();
        return nf.format(number);

    }

    public static String cleanContent(String content) {
        return content.replace("/r/", "").replace("r/", "");
    }
    public static String convertPrice(String price, String currency, Context context) {
        if(Float.parseFloat(price)>0)
            return currency+" $ " +price;
        else
            return context.getString(R.string.price_free);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, Context context, final View container, final View progress) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

            container.setVisibility(show ? View.GONE : View.VISIBLE);
            container.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    container.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            container.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static SQLiteDatabase getDB(Context context) {
        SQLHelper admin = new SQLHelper(context, "ls_rappi", Constants.versionDB);
        return admin.getWritableDatabase();

    }

    public static void checkOrientaton(Context context){

        Boolean isTablet=  context.getResources().getBoolean(R.bool.isTablet);
        if(isTablet)
            ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }
}

