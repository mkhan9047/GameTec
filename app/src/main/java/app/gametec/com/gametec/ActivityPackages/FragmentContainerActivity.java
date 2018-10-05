package app.gametec.com.gametec.ActivityPackages;

import android.accessibilityservice.GestureDescription;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import app.gametec.com.gametec.FragmentsPackages.AdminFragment;
import app.gametec.com.gametec.FragmentsPackages.AlarmFragment;
import app.gametec.com.gametec.FragmentsPackages.BalanceFragment;
import app.gametec.com.gametec.FragmentsPackages.BlockFragment;
import app.gametec.com.gametec.FragmentsPackages.BloquearFragment;
import app.gametec.com.gametec.FragmentsPackages.BluetoothFragment;
import app.gametec.com.gametec.FragmentsPackages.ClientFragment;
import app.gametec.com.gametec.FragmentsPackages.ClockFragment;
import app.gametec.com.gametec.FragmentsPackages.DoorFragment;
import app.gametec.com.gametec.FragmentsPackages.GpsFragment;
import app.gametec.com.gametec.FragmentsPackages.PercentFragement;
import app.gametec.com.gametec.FragmentsPackages.ResetFragment;
import app.gametec.com.gametec.FragmentsPackages.TicketFragment;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.R;

public class FragmentContainerActivity extends AppCompatActivity {

    Fragment currentFragment;
    int count = 0;
    String flag;
    Storage storage = new Storage(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);


        /*default*/
        currentFragment = new AdminFragment();
        FragmentTransition();



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
            dailog.setMessage("Do you want to exit?");
            dailog.setIcon(R.mipmap.ic_launcher_round);
            dailog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });

            dailog.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
                flag = null;
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
                Intent intent1 = new Intent(this, SignInActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent1);
                break;
        }

    }
}