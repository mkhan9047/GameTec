package app.gametec.com.gametec.Helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

public class Utility {

    public static boolean EmailValidator(String email) {

        String regex = "^[\\p{L}\\p{N}\\._%+-]+@[\\p{L}\\p{N}\\.\\-]+\\.[\\p{L}]{2,}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();

    }

    public static ACProgressFlower StartProgressDialog(Activity context, String message) {
        ACProgressFlower dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(message)
                .fadeColor(Color.DKGRAY).build();
        dialog.show();

        return dialog;
    }

    public static void DismissDialog(ACProgressFlower dialog) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static void showDialog(Activity context, String message) {
        if (context != null) {
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Info");
            alertDialog.setMessage(message);
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkFingerprintSettings(Context context) {
        final String TAG = "FP-Check";

        //Check for android version, FingerPrint is not available below Marshmallow
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.d(TAG, "This Android version does not support fingerprint authentication.");
            return false;
        }

        //Check whether the security option for phone is set.
        //ie. LockScreen is enabled or not.
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);

        if (fingerprintManager != null && fingerprintManager.isHardwareDetected()) {


            //check if user have registered any fingerprints
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                Log.d(TAG, "User hasn't registered any fingerprints");

                return false;
            }

            //check for app permissions
            //Make sure you have mentioned the permission on AndroidManifest.xml
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "User hasn't granted permission to use Fingerprint");

                return false;
            }

            return true;

        } else {


            return false;
        }


    }

}
