package app.gametec.com.gametec.ActivityPackages;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import app.gametec.com.gametec.DialogPackage.FingerPrintDialog;
import app.gametec.com.gametec.FragmentsPackages.ConnectBluetoothFragment;
import app.gametec.com.gametec.FragmentsPackages.InsertPinFragment;
import app.gametec.com.gametec.FragmentsPackages.SetPinFragment;
import app.gametec.com.gametec.FragmentsPackages.SwitchNetworkFragment;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.R;

public class AuthnicateActivity extends AppCompatActivity {

    Fragment fragment;
    public RelativeLayout layout;
    FragmentTransaction transaction;
    int count = 0;
    boolean isFromBackgroud;
    public String got;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authnicate);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        layout = findViewById(R.id.layout);


        got = getIntent().getStringExtra("has");

        isFromBackgroud = getIntent().getBooleanExtra("FROMBG", false);

        if (isFromBackgroud) {

            Storage storage = new Storage(this);

            if (!storage.getHasPin()) {
                if (storage.getHasPin()) {

                    FragmentTransition(new InsertPinFragment());

                } else {

                    FragmentTransition(new SetPinFragment());
                }

            } else {

                if (got != null) {

                    if (got.equals("finger_print")) {
                        layout.setBackgroundResource(R.drawable.splash_bg);
                        FingerPrintDialog dialog = new FingerPrintDialog();
                        dialog.show(getSupportFragmentManager(), null);
                    }

                } else {

                    if (storage.getHasPin()) {

                        FragmentTransition(new InsertPinFragment());

                    } else {

                        FragmentTransition(new SetPinFragment());
                    }

                }

            }

        } else {
            FragmentTransition(new SwitchNetworkFragment());
        }


    }


    public void FragmentTransition(Fragment fragment) {
        layout.setBackgroundResource(0);
        this.fragment = fragment;
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.auth_container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        if (fragment instanceof ConnectBluetoothFragment || fragment instanceof InsertPinFragment || fragment instanceof SetPinFragment) {

            FragmentTransition(new SwitchNetworkFragment());

        } else {

            count++;

            switch (count) {

                case 1:
                    Toast.makeText(this, "Press again to exit!", Toast.LENGTH_SHORT).show();
                    break;

                case 2:
                    finish();
                    break;
            }

        }


    }

}
