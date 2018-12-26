package app.gametec.com.gametec.FragmentsPackages;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.ActivityPackages.SignInActivity;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class InsertPinFragment extends Fragment {


    EditText pin;
    Button set_btn;
    int count = 0;

    public InsertPinFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insert_pin, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View in = getView();


        if (in != null) {

            pin = in.findViewById(R.id.pin_number);

            set_btn = in.findViewById(R.id.btn_set);

        }

        final Storage storage = new Storage(getActivity());

        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Toast.makeText(getActivity(), String.valueOf(pin.getText().toString()), Toast.LENGTH_SHORT).show();

                ((FragmentContainerActivity) getActivity()).FragmentTransition(new AdminFragment());

                */

                if (validatePin(pin)) {

                    if (pin.getText().toString().equals(storage.getPin())) {

                        if (storage.getLogInState()) {

                            Intent intent = new Intent(getActivity(), FragmentContainerActivity.class);
                            intent.putExtra("flag", "admin");
                            startActivity(intent);
                            if (getActivity() != null)
                                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);


                        } else {

                            Intent intent = new Intent(getActivity(), SignInActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getActivity().finish();
                            startActivity(intent);
                            if (getActivity() != null)
                                getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                        }

                    } else {

                        Toast.makeText(getActivity(), R.string.incorrect_pin, Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
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
