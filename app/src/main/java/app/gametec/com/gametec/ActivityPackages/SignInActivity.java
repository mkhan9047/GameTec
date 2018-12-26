package app.gametec.com.gametec.ActivityPackages;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private boolean wasInBackground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        InitView();

    }

    public void onSignIn(View view) {

        if (IsOk(email, password)) {

            if (Utility.isInternetAvailable(this)) {
                SignInCall(email.getText().toString(), password.getText().toString());
            } else {
                Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            }


        }
    }

    private boolean IsOk(EditText email, EditText password) {

        if (email.getText().toString().length() > 0 && password.getText().toString().length() > 0) {

            if (Utility.EmailValidator(email.getText().toString())) {

                return true;

            } else {

                Toast.makeText(this, R.string.email_no_valid, Toast.LENGTH_SHORT).show();

                return false;

            }

        } else {

            Toast.makeText(this, R.string.input_field, Toast.LENGTH_SHORT).show();

            return false;

        }


    }

    private void SignInCall(String email, String password) {

        final ACProgressFlower dialog = Utility.StartProgressDialog(this, getString(R.string.logging_in));

        final Storage storage = new Storage(this);

        NetworkInterface networkInterface = RetrofitClient.getRetrofitOfScalar().create(NetworkInterface.class);

        Call<String> signInCall = networkInterface.getSignedIN(email, password);

        signInCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {




                /*       String json  = new Gson().toJson(response.body());*/

             //;   Log.d("MKJSON", response.body());

                if(response.body() != null){


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

                            Utility.DismissDialog(dialog);

                        } else {

                            Toast.makeText(SignInActivity.this, message, Toast.LENGTH_SHORT).show();
                            Utility.DismissDialog(dialog);
                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(SignInActivity.this, "No Response found", Toast.LENGTH_SHORT).show();
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

                Log.d("Mim", t.getMessage());

            }
        });


    }


    private void GOtoDash() {
        Intent intent = new Intent(this, FragmentContainerActivity.class);
        intent.putExtra("flag", "machine");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);

    }

    private void InitView() {
        email = findViewById(R.id.user_name);
        password = findViewById(R.id.password);

    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();


    }

    @Override
    protected void onResume() {

        super.onResume();

        if(wasInBackground){

            wasInBackground =false;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if(Utility.checkFingerprintSettings(SignInActivity.this)){

                    Intent intent = new Intent(SignInActivity.this, AuthnicateActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("has", "finger_print");
                    startActivity(intent);
                    finish();
                    //overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);

                }
                else{
                    Intent intent = new Intent(SignInActivity.this, AuthnicateActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    //overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);

                }
            }

        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        wasInBackground = true;
    }
}
