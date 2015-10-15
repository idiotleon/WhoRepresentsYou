package com.leontheprofessional.test.whorepresentsyou.login;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.helper.GeneralHelper;

import java.io.IOException;

/**
 * Created by Leon on 10/14/2015.
 */
public class LoginFragment extends DialogFragment {

    private static final String LOG_TAG = LoginFragment.class.getSimpleName();

    private static final int REQUEST_CODE_PICK_ACCOUNT = 1000;

    private String mEmail; // Received from newChooseAccountIntent(); passed to getToken()
    private final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, null);

        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.google_log_in_imagebutton);

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
                getUsername();
            } else if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getActivity(), R.string.fail_in_picking_account, Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getUsername(){
        if(mEmail==null){
            pickUserAccount();
        }else {
            if (GeneralHelper.isNetworkConnectionAvailable(getActivity())) {
                new GetUsernameTask(getActivity(), mEmail, SCOPE).execute();
            }else{
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
        protected Object doInBackground(Object[] params) {

            try {
                String token = fetchToken();
                if (token != null) {

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
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
