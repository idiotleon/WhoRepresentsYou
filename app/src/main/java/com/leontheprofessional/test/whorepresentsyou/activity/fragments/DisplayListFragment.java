package com.leontheprofessional.test.whorepresentsyou.activity.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daimajia.swipe.SwipeLayout;
import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.activity.MemberDetailsActivity;
import com.leontheprofessional.test.whorepresentsyou.adapters.ListViewAdapter;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

import java.util.ArrayList;

/**
 * Created by Leon on 10/13/2015.
 */
public class DisplayListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = DisplayListFragment.class.getSimpleName();

    private ArrayList<MemberModel> members;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview, null);

        context = getActivity();
        members = new ArrayList<>();

        listView = (ListView) view.findViewById(R.id.listview);
        members = getArguments().getParcelableArrayList(getString(R.string.fragment_argument_identifier3));
        listViewAdapter = new ListViewAdapter(context, members);

        listView.setAdapter(listViewAdapter);
        listViewAdapter.setMode(com.daimajia.swipe.util.Attributes.Mode.Single);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailsIntent = new Intent(context, MemberDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(context.getString(R.string.parcelable_identifier), members.get(position));
                Log.i(TAG, members.get(position).getPhoneNumber());

                detailsIntent.putExtra(context.getString(R.string.bundle_identifier), bundle);
                context.startActivity(detailsIntent);
            }
        });

        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(TAG, "onLoadFinished()");

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


/*    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (getArguments() != null) {

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
    }*/
}
