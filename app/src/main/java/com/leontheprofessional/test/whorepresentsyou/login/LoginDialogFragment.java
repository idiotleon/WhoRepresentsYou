package com.leontheprofessional.test.whorepresentsyou.login;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.helper.GeneralHelper;

import java.io.IOException;

/**
 * Created by Leon on 10/14/2015.
 */
public class LoginDialogFragment extends DialogFragment {

    private static final String LOG_TAG = LoginDialogFragment.class.getSimpleName();

    private static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    private static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;

    private String tokenFetched;

    private String mEmail; // Received from newChooseAccountIntent(); passed to getToken()
    private final static String USER_INFO_SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    private final static String BOOKS_API_SCOPE = "https://www.googleapis.com/auth/books";
    private final static String GPLUS_SCOPE = "https://www.googleapis.com/auth/plus.login";
    private final static String scopes = "oauth2:" + BOOKS_API_SCOPE + " " + GPLUS_SCOPE + " " + USER_INFO_SCOPE;

    private EditText usernameEditText;
    private EditText usernamePassword;
    private Button btnConfirm;
    private Button btnSignUp;
    private ImageButton imageButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, null);

        usernameEditText = (EditText) rootView.findViewById(R.id.edittext_username);
        usernamePassword = (EditText) rootView.findViewById(R.id.edittext_password);
        btnConfirm = (Button) rootView.findViewById(R.id.login_confirm);
        btnSignUp = (Button) rootView.findViewById(R.id.login_signup);
        imageButton = (ImageButton) rootView.findViewById(R.id.google_log_in_imagebutton);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LoginDialogFragment.this.getDialog().dismiss();

                RegistrationFragment registrationFragment = new RegistrationFragment();
                registrationFragment.show(getFragmentManager(), "RegistrationFragment");
/*                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_main_activity, registrationFragment);
                fragmentTransaction.commit();*/
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickUserAccount();
            }
        });

        return rootView;
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == Activity.RESULT_OK) {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Log.v(LOG_TAG, "mEmail: " + mEmail);
                getUsername();
            } else if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getActivity(), R.string.fail_in_picking_account, Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getUsername() {
        if (mEmail == null) {
            pickUserAccount();
        } else {
            if (GeneralHelper.isNetworkConnectionAvailable(getActivity())) {
                new GetUsernameTask(getActivity(), mEmail, scopes).execute();
                dismiss();
            } else {
                Toast.makeText(getActivity(), getString(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class GetUsernameTask extends AsyncTask {
        Activity activity;
        String scope;
        String email;

        public GetUsernameTask(Activity activity, String name, String scope) {
            this.activity = activity;
            this.email = name;
            this.scope = scope;
        }

        @Override
        protected String doInBackground(Object[] params) {

            try {
                String token = fetchToken();
                if (token != null) {

                } else {
                    return token;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        protected String fetchToken() throws IOException {
            try {
                return GoogleAuthUtil.getToken(activity, email, scope);
            } catch (UserRecoverableAuthException ex) {
                ex.printStackTrace();
                handleException(ex);
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void handleException(final Exception ex) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ex instanceof GooglePlayServicesAvailabilityException) {
                    int statusCode = ((GooglePlayServicesAvailabilityException) ex).getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode, getActivity(), REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (ex instanceof UserRecoverableAuthException) {
                    Intent intent = ((UserRecoverableAuthException) ex).getIntent();
                    startActivityForResult(intent, REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }
}
