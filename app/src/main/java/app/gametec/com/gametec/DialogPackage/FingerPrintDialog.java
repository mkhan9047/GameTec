package app.gametec.com.gametec.DialogPackage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ajalt.reprint.core.AuthenticationFailureReason;
import com.github.ajalt.reprint.core.AuthenticationListener;
import com.github.ajalt.reprint.core.Reprint;

import app.gametec.com.gametec.ActivityPackages.AuthnicateActivity;
import app.gametec.com.gametec.ActivityPackages.FragmentContainerActivity;
import app.gametec.com.gametec.ActivityPackages.SignInActivity;
import app.gametec.com.gametec.FragmentsPackages.AdminFragment;
import app.gametec.com.gametec.FragmentsPackages.InsertPinFragment;
import app.gametec.com.gametec.FragmentsPackages.SetPinFragment;
import app.gametec.com.gametec.LocalStorage.Storage;
import app.gametec.com.gametec.R;

public class FingerPrintDialog extends android.support.v4.app.DialogFragment {


    Button cancel;
    Storage storage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.finger_print_dialog_layout, container, false);
    }


    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View in = inflater.inflate(R.layout.finger_print_dialog_layout, null);


        if (in != null) {

            cancel = in.findViewById(R.id.cancel);

        }

        storage = new Storage(getActivity());

        cancel.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                dismiss();

                Reprint.cancelAuthentication();

                if (storage.getHasPin()) {
                    Toast.makeText(getActivity(), "Canceled by user!", Toast.LENGTH_SHORT).show();
                    ((AuthnicateActivity) getActivity()).FragmentTransition(new InsertPinFragment());
                } else {
                    Toast.makeText(getActivity(), "Canceled by user!", Toast.LENGTH_SHORT).show();
                    ((AuthnicateActivity) getActivity()).FragmentTransition(new SetPinFragment());
                }


            }
        });


        Reprint.initialize(getActivity());


        Reprint.authenticate(new AuthenticationListener() {

            Storage storage = new Storage(getActivity());

            public void onSuccess(int moduleTag) {

                if (storage.getLogInState()) {

                    Intent intent = new Intent(getActivity(), FragmentContainerActivity.class);
                    intent.putExtra("flag", "admin");
                    startActivity(intent);
                    if (getActivity() != null)
                        getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);


                } else {

                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().finish();
                    startActivity(intent);
                    if (getActivity() != null)
                        getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                }
            }


            public void onFailure(AuthenticationFailureReason failureReason, boolean fatal,
                                  CharSequence errorMessage, int moduleTag, int errorCode) {

                Toast.makeText(getActivity(), failureReason.name(), Toast.LENGTH_SHORT).show();
            }
        });



        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {

                if (i == KeyEvent.KEYCODE_BACK) {

                    dismiss();

                    if (storage.getHasPin()) {

                        ((AuthnicateActivity) getActivity()).FragmentTransition(new InsertPinFragment());

                    } else {

                        ((AuthnicateActivity) getActivity()).FragmentTransition(new SetPinFragment());

                    }
                }

                return false;
            }
        });

        builder.setView(in);


        return builder.create();
    }


}
