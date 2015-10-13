package com.leontheprofessional.test.whorepresentsyou.activity;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

/**
 * Created by Leon on 10/12/2015.
 */
public class ViewHolder {

    private View view;

    private String name;
    private String party;
    private String state;
    private String district;
    private String phoneNumber;
    private String officeAddress;
    private String urlLink;

    public ViewHolder(Context context, MemberModel member) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = inflater.inflate(R.layout.details, null);
        name = member.getName();
        party = member.getParty();
        state = member.getState();
        district = member.getDistrict();
        phoneNumber = member.getPhoneNumber();
        officeAddress = member.getOfficeAddress();
        urlLink = member.getLinkUrl();
    }

    TextView textViewName = (TextView) view.findViewById(R.id.textview_detail_name);
    TextView textViewParty = (TextView) view.findViewById(R.id.textview_detail_party);
    TextView textViewState = (TextView) view.findViewById(R.id.textview_detail_state);
    TextView textViewDistrict = (TextView) view.findViewById(R.id.textview_detail_district);
    TextView textViewPhone = (TextView) view.findViewById(R.id.textview_detail_phone);
    TextView textViewOffice = (TextView) view.findViewById(R.id.textview_detail_office);
    TextView textViewLink = (TextView) view.findViewById(R.id.textview_detail_link);

    public void setTextViews() {
        textViewName.setText(name);
        textViewParty.setText(party);
        textViewState.setText(state);
        textViewDistrict.setText(district);
        textViewPhone.setText(phoneNumber);
        textViewOffice.setText(officeAddress);
        textViewLink.setText(urlLink);
    }
}
