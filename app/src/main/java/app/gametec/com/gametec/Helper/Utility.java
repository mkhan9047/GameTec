package app.gametec.com.gametec.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class Utility {

    public static boolean EmailValidator(String email) {

        String regex = "^[\\p{L}\\p{N}\\._%+-]+@[\\p{L}\\p{N}\\.\\-]+\\.[\\p{L}]{2,}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();

    }

    public static ACProgressFlower StartProgressDialog(Context context, String message) {

  /*      ProgressDialog dialog = new ProgressDialog(context);

        dialog.setIndeterminate(true);

        dialog.setMessage(message);

        dialog.setCanceledOnTouchOutside(false);

        dialog.show();*/

        ACProgressFlower dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(message)
                .fadeColor(Color.DKGRAY).build();
        dialog.show();

        return dialog;
    }

    public static void DismissDialog(ACProgressFlower dialog, Context context) {

        if (dialog.isShowing()) {

            dialog.dismiss();


        }
    }

    public static void ShowToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }

    public static boolean isInternetAvailable(Context context) {

        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = null;
        if (connMgr != null) {
            activeNetworkInfo = connMgr.getActiveNetworkInfo();
        }

        if (activeNetworkInfo != null) { // connected to the internet

            // connected to wifi
            return activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI || activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        }

        return false;
    }

}
