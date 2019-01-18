package app.gametec.com.gametec.ActivityPackages;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import app.gametec.com.gametec.FragmentsPackages.AdminFragment;
import app.gametec.com.gametec.FragmentsPackages.AlarmFragment;
import app.gametec.com.gametec.FragmentsPackages.BalanceFragment;
import app.gametec.com.gametec.FragmentsPackages.BlockFragment;
import app.gametec.com.gametec.FragmentsPackages.BluetoothFragment;
import app.gametec.com.gametec.FragmentsPackages.ClockFragment;
import app.gametec.com.gametec.FragmentsPackages.ConnectBluetoothFragment;
import app.gametec.com.gametec.FragmentsPackages.DoorFragment;
import app.gametec.com.gametec.FragmentsPackages.GpsFragment;
import app.gametec.com.gametec.FragmentsPackages.MachineFragment;
import app.gametec.com.gametec.FragmentsPackages.PercentFragement;
import app.gametec.com.gametec.FragmentsPackages.ResetFragment;
import app.gametec.com.gametec.FragmentsPackages.SetPinFragment;
import app.gametec.com.gametec.FragmentsPackages.TicketFragment;
import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.R;

import static android.os.Build.VERSION.SDK;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.KITKAT;

public class FragmentContainerActivity extends AppCompatActivity {

    Fragment currentFragment;
    int count = 0;
    String flag;
    Storage storage = new Storage(this);
    private boolean wasInBackground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);


        /*default*/
     /*   currentFragment = new MachineFragment();
        FragmentTransition();
*/

        flag = getIntent().getStringExtra("flag");

        if (flag != null) {
            Log.d("MKFalg", flag);
            MakeTransactionFromIntent(flag);
        }
    }


    /*make the fragment transition with current fragment and the previous one, replace the fragment with new fragment */
    private void FragmentTransition() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, currentFragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    /*make the fragment transition with current fragment and the previous one, replace the fragment with new fragment */
    public void FragmentTransition(Fragment fragment) {
        currentFragment = fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, currentFragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {

        if (!(currentFragment instanceof AdminFragment)) {
            currentFragment = new AdminFragment();
            FragmentTransition();

        } else {

            final android.support.v7.app.AlertDialog.Builder dailog = new android.support.v7.app.AlertDialog.Builder(this);
            dailog.setMessage(R.string.wanna_exit);
            dailog.setIcon(R.mipmap.ic_launcher_round);
            dailog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    finishAffinity();
                    moveTaskToBack(true);
                    System.exit(1);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });

            dailog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dailog.show();

        }


    }

    private void MakeTransactionFromIntent(String got) {

        switch (got) {

            case "balance":
                flag = null;
                currentFragment = new BalanceFragment();
                FragmentTransition();
                break;

            case "admin":
                flag = null;
                currentFragment = new AdminFragment();
                FragmentTransition();
                break;

            case "machine":
                flag = null;
                currentFragment = new MachineFragment();
                FragmentTransition();
                break;

            case "alarm":
                flag = null;
                currentFragment = new AlarmFragment();
                FragmentTransition();
                break;

            case "tickets":
                flag = null;
                currentFragment = new TicketFragment();
                FragmentTransition();
                break;

            case "gps":
                flag = null;
                currentFragment = new GpsFragment();
                FragmentTransition();
                break;

            case "clock":
                flag = null;
                currentFragment = new ClockFragment();
                FragmentTransition();
                break;

            case "door_opening":
                flag = null;
                currentFragment = new DoorFragment();
                FragmentTransition();
                break;

            case "block_machine":

                currentFragment = new BlockFragment();
                FragmentTransition();
                break;

            case "bluetooth":
                flag = null;
                currentFragment = new BluetoothFragment();
                FragmentTransition();
                break;

            case "reset_machine":
                flag = null;
                currentFragment = new ResetFragment();
                FragmentTransition();
                break;

            case "percent_control":
                flag = null;
                currentFragment = new PercentFragement();
                FragmentTransition();
                break;
            case "logout":
                Storage storage = new Storage(this);
                storage.SaveLogInSate(false);
                storage.saveCurrentMachineName("");
                storage.saveCurrentMachine(0);

                Intent intent1 = new Intent(this, SignInActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent1);
                break;

            default:
                currentFragment = new AdminFragment();
                FragmentTransition();
                break;
        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        wasInBackground = true;
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (!(currentFragment instanceof ConnectBluetoothFragment)) {

            if (wasInBackground) {

                wasInBackground = false;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (Utility.checkFingerprintSettings(FragmentContainerActivity.this)) {

                        Intent intent = new Intent(FragmentContainerActivity.this, AuthnicateActivity.class);
                        intent.putExtra("FROMBG", true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("has", "finger_print");
                        startActivity(intent);
                        finish();
                        //overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);

                    } else {
                        Intent intent = new Intent(FragmentContainerActivity.this, AuthnicateActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("FROMBG", true);
                        startActivity(intent);
                        finish();
                        //overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);

                    }
                }

            }

        }


    }
}
