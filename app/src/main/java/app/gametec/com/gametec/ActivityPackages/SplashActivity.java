package app.gametec.com.gametec.ActivityPackages;

import android.accessibilityservice.GestureDescription;
import android.app.FragmentContainer;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import javax.security.auth.login.LoginException;

import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.SignIn;
import app.gametec.com.gametec.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


        /**
         * handler for showing splash screen for 4 seconds and after then finishing this activity and staring the login activity
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 * finishing this activity after 4 seconds
                 */
                finish();
                /**
                 * starting login activity after 4 seconds
                 */


                Storage storage = new Storage(SplashActivity.this);

                if (storage.getLogInState()) {

                    Intent intent = new Intent(SplashActivity.this, FragmentContainerActivity.class);
                    intent.putExtra("flag", "machine");
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                } else {

                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                }


            }
        }, 4000);//this will finish after 4 seconds
    }


}
