package app.gametec.com.gametec.FragmentsPackages;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.ActivityPackages.SignInActivity;
import app.gametec.com.gametec.ActivityPackages.SplashActivity;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.ModelPackages.SignIn;
import app.gametec.com.gametec.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SetPinFragment extends Fragment {

    Button setBtn;
    EditText pin;

    public SetPinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_pin, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        initView();

        final Storage storage = new Storage(getActivity());

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validatePin(pin)) {
                    storage.savePin(pin.getText().toString());
                    storage.saveHasPin(true);

                    Toast.makeText(getActivity(), R.string.pint_set_success, Toast.LENGTH_SHORT).show();

                    if (storage.getLogInState()) {

                        Intent intent = new Intent(getActivity(), FragmentContainerActivity.class);
                        intent.putExtra("flag", "admin");
                        startActivity(intent);
                        if (getActivity() != null)
                            getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                    } else {

                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Objects.requireNonNull(getActivity()).finish();
                        }
                        startActivity(new Intent(intent));
                        if (getActivity() != null)
                            getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                    }
                }

            }
        });

    }

    private void initView() {

        View view = getView();
        if (view != null) {
            setBtn = view.findViewById(R.id.set_btn);
            pin = view.findViewById(R.id.pin_number);
        }
    }

    private boolean validatePin(EditText editText) {

        boolean ok;

        if (editText.getText().toString().length() < 4) {

            Toast.makeText(getActivity(), R.string.pin_digit, Toast.LENGTH_SHORT).show();

            ok = false;

        } else {

            ok = true;

        }

        return ok;
    }

}
