package com.leontheprofessional.test.whorepresentsyou.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Leon on 10/12/2015.
 */
public class MemberModel implements Parcelable {

    private String name;
    private String party;
    private String state;
    private String district;
    private String phoneNumber;
    private String officeAddress;
    private String linkUrl;

    public MemberModel(String name, String party, String state, String district, String phoneNumber, String officeAddress, String linkUrl) {
        this.name = name;
        this.party = party;
        this.state = state;
        this.district = district;
        this.phoneNumber = phoneNumber;
        this.officeAddress = officeAddress;
        this.linkUrl = linkUrl;
    }


    protected MemberModel(Parcel in) {
        name = in.readString();
        party = in.readString();
        state = in.readString();
        district = in.readString();
        phoneNumber = in.readString();
        officeAddress = in.readString();
        linkUrl = in.readString();
    }

    public static final Creator<MemberModel> CREATOR = new Creator<MemberModel>() {
        @Override
        public MemberModel createFromParcel(Parcel in) {
            return new MemberModel(in);
        }

        @Override
        public MemberModel[] newArray(int size) {
            return new MemberModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(party);
        dest.writeString(state);
        dest.writeString(district);
        dest.writeString(phoneNumber);
        dest.writeString(officeAddress);
        dest.writeString(linkUrl);
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public String getLinkUrl() {
        return linkUrl;
    }
}
