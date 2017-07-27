package com.leontheprofessional.test.whorepresentsyou.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.leontheprofessional.test.whorepresentsyou.activity.MainActivity;
import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.activity.MemberDetailsActivity;
import com.leontheprofessional.test.whorepresentsyou.adapters.ListViewAdapter;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;
import com.leontheprofessional.test.whorepresentsyou.provider.MemberContract;

import java.util.ArrayList;

/**
 * Created by Leon on 10/13/2015.
 */
public class DisplayListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = DisplayListFragment.class.getSimpleName();
    private static final int LOADER_ID = 0;

    private ArrayList<MemberModel> members;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView() is called");
        View view = inflater.inflate(R.layout.listview, null);

        context = getActivity();
        members = new ArrayList<>();

        listView = (ListView) view.findViewById(R.id.listview);

        getLoaderManager().initLoader(LOADER_ID, null, this);
        if (!getLoaderManager().getLoader(LOADER_ID).isReset()) {
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }

        return view;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(TAG, "onCreateLoader() is called");
        CursorLoader loader = new CursorLoader(
                getActivity(),
                MemberContract.MemberEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(TAG, "onLoadFinished() is called");

        ArrayList<MemberModel> allFavoriteMembers = new ArrayList<>();
        if (data.getCount() > 0) {
            Log.v(TAG, "Count of favoriteMembers: " + data.getCount());
            data.moveToFirst();
            while (!data.isAfterLast()) {
                String name = data.getString(data.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_NAME));
                String party = data.getString(data.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_PARTY));
                String state = data.getString(data.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_STATE));
                String district = data.getString(data.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_DISTRICT));
                String phoneNumber = data.getString(data.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_PHONE));
                String officeAddress = data.getString(data.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_OFFICE));
                String website = data.getString(data.getColumnIndex(MemberContract.MemberEntry.COLUMN_MEMBER_WEBSITE));

                MemberModel member = new MemberModel(name, party, state, district, phoneNumber, officeAddress, website);
                allFavoriteMembers.add(member);
                data.moveToNext();
            }
        }
        ArrayList<MemberModel> allMembers = new ArrayList<>();
        ArrayList<MemberModel> tempMembers = getArguments().getParcelableArrayList(getString(R.string.fragment_argument_identifier3));
        allMembers.addAll(allFavoriteMembers);
        Log.v(TAG, "Size1 of allMembers: " + allMembers.size());
        allMembers.addAll(tempMembers);
        Log.v(TAG, "Size2 of allMembers: " + allMembers.size());


        members.clear();
        members = allMembers;

        // members = getArguments().getParcelableArrayList(getString(R.string.fragment_argument_identifier3));
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
                detailsIntent.setAction(MainActivity.CUSTOM_SEARCH_INTENT_FILTER);
                context.startActivity(detailsIntent);
            }
        });
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
