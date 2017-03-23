package com.leontheprofessional.test.whorepresentsyou.activity.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.activity.fragments.adapter.CustomGridViewAdapter;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

import java.util.ArrayList;

/**
 * Created by Leon on 10/12/2015.
 */
public class DisplayFragment extends Fragment {

    private static final String LOG_TAG = DisplayFragment.class.getSimpleName();

    private GridView gridView;

    private ArrayList<MemberModel> members;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.display_fragment, null);
        gridView = (GridView) rootView.findViewById(R.id.gridview_display_fragment);
        members = getArguments().getParcelableArrayList(getString(R.string.fragment_argument_identifier));
        gridView.setAdapter(new CustomGridViewAdapter(getActivity(), members));

        if (members != null && members.size() > 0)
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle arguments = new Bundle();
                    Log.v(LOG_TAG, "member name: " + members.get(position).getName());
                    arguments.putParcelable(getString(R.string.fragment_argument_identifier2), members.get(position));
                    DetailsFragment detailsFragment = new DetailsFragment();
                    detailsFragment.setArguments(arguments);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container2, detailsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

        TextView emptyTextView = new TextView(getActivity());
        emptyTextView.setText(getString(R.string.empty_textview_text));
        gridView.setEmptyView(emptyTextView);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
