package com.leontheprofessional.test.whorepresentsyou.activity.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.MainActivity;
import com.leontheprofessional.test.whorepresentsyou.activity.MemberDetailsActivity;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

import java.util.ArrayList;

/**
 * Created by Leon on 10/13/2015.
 */
public class DisplayListFragment extends ListFragment {

    private ArrayList<MemberModel> members;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        members = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            members = getArguments().getParcelableArrayList(getString(R.string.fragment_argument_identifier3));

            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(getString(R.string.parcelable_identifier), members.get(position));
                    Intent intent = new Intent(getActivity(), MemberDetailsActivity.class);
                    intent.putExtra(getString(R.string.bundle_identifier), bundle);
                    intent.setAction(MainActivity.CUSTOM_SEARCH_INTENT_FILTER);
                    startActivity(intent);
                }
            });
        }
    }
}
