package app.gametec.com.gametec.ActivityPackages;

import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.UniversalTimeScale;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import app.gametec.com.gametec.Helper.Utility;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.SignIn;
import app.gametec.com.gametec.Networking.NetworkInterface;
import app.gametec.com.gametec.Networking.RetrofitClient;
import app.gametec.com.gametec.R;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        InitView();

    }

    public void onSignIn(View view) {

        if (IsOk(email, password)) {

            SignInCall(email.getText().toString(), password.getText().toString());

        }
    }

    private boolean IsOk(EditText email, EditText password) {

        if (email.getText().toString().length() > 0 && password.getText().toString().length() > 0) {

            if (Utility.EmailValidator(email.getText().toString())) {

                return true;

            } else {

                Toast.makeText(this, "Email is not Valid", Toast.LENGTH_SHORT).show();

                return false;

            }

        } else {

            Toast.makeText(this, "Input all field first!", Toast.LENGTH_SHORT).show();

            return false;

        }


    }

    private void SignInCall(String email, String password) {

        final ACProgressFlower dialog = Utility.StartProgressDialog(this, "Logging in....");

        final Storage storage = new Storage(this);

        NetworkInterface networkInterface = RetrofitClient.getRetrofitOfScalar().create(NetworkInterface.class);

        Call<String> signInCall = networkInterface.getSignedIN(email, password);

        signInCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {




                /*       String json  = new Gson().toJson(response.body());*/

                Log.d("MKJSON", response.body());

                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    boolean isSuccess = jsonObject.getBoolean("success");

                    String message = jsonObject.getString("message");

                    if (isSuccess) {

                        Gson gson = new Gson();

                        SignIn signIn = gson.fromJson(response.body(), SignIn.class);

                        /*save credentials*/
                        storage.SaveAccessType(signIn.getData().getCredentials().getTokenType());
                        storage.SaveAccessToken(signIn.getData().getCredentials().getAccessToken());
                        storage.SaveLogInSate(true);
                        storage.SaveRefreshToken(signIn.getData().getCredentials().getRefreshToken());
                        storage.SaveRole(signIn.getData().getUser().getRole());

                        GOtoDash();

                        Utility.DismissDialog(dialog, SignInActivity.this);

                    } else {

                        Toast.makeText(SignInActivity.this, message, Toast.LENGTH_SHORT).show();
                        Utility.DismissDialog(dialog, SignInActivity.this);
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

/*
                if (signIn != null) {

                    if (signIn.isSuccess()) {

                        Toast.makeText(SignInActivity.this, "Success", Toast.LENGTH_SHORT).show();

                        GOtoDash();

                    } else {

                        Toast.makeText(SignInActivity.this, "Not Success", Toast.LENGTH_SHORT).show();

                    }

                }*/


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }


    private void GOtoDash() {
        Intent intent = new Intent(this, FragmentContainerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
        overridePendingTransition( R.anim.left_to_right, R.anim.right_to_left );

    }

    private void InitView() {
        email = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
    }


    @Override
    public void onBackPressed() {

         super.onBackPressed();

        overridePendingTransition( 0, R.anim.right_to_left );

    }



}
