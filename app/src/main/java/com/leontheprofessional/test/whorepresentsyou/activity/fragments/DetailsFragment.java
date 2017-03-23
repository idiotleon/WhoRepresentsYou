package com.leontheprofessional.test.whorepresentsyou.activity.fragments;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by Leon on 10/12/2015.
 */
public class DetailsFragment extends Fragment {

    private static final String LOG_TAG = DetailsFragment.class.getSimpleName();

    private MemberModel member;

    private TextView textViewName;
    private TextView textViewParty;
    private TextView textViewState;
    private TextView textViewDistrict;
    private TextView textViewPhone;
    private TextView textViewOffice;
    private TextView textViewLink;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        member = getArguments().getParcelable(getString(R.string.fragment_argument_identifier2));
        Log.v(LOG_TAG, "member name: " + member.getName());

        View view = inflater.inflate(R.layout.details, null);

        textViewName = (TextView) view.findViewById(R.id.textview_detail_name);
        textViewParty = (TextView) view.findViewById(R.id.textview_detail_party);
        textViewState = (TextView) view.findViewById(R.id.textview_detail_state);
        textViewDistrict = (TextView) view.findViewById(R.id.textview_detail_district);
        textViewPhone = (TextView) view.findViewById(R.id.textview_detail_phone);
        textViewOffice = (TextView) view.findViewById(R.id.textview_detail_office);
        textViewLink = (TextView) view.findViewById(R.id.textview_detail_link);


        textViewName.setText(member.getName());
        textViewParty.setText(member.getParty());
        textViewState.setText(member.getState());
        textViewDistrict.setText(member.getDistrict());
        final String phoneNumber = member.getPhoneNumber();
        textViewPhone.setText(phoneNumber);
        textViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(phoneNumber);
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        Log.e(LOG_TAG, getString(R.string.call_permission_rejected_by_user));
                        return;
                    }
                }
                startActivity(intent);
            }
        });
        textViewOffice.setText(member.getOfficeAddress());
        final String linkUrl = member.getLinkUrl();
        textViewLink.setText(linkUrl);
        textViewLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(linkUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        return view;
    }
}

