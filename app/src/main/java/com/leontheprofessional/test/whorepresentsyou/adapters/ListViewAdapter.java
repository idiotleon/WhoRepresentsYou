package com.leontheprofessional.test.whorepresentsyou.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.activity.MemberDetailsActivity;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

import java.util.ArrayList;

/**
 * Created by LeonthePro7 on 1/16/2017.
 */

public class ListViewAdapter extends BaseSwipeAdapter {
    private static final String TAG = ListViewAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<MemberModel> members;

    public ListViewAdapter(Context context, ArrayList<MemberModel> members) {
        this.context = context;
        this.members = members;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.listview_swipe_item;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_item_swipe, null);
        SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                super.onOpen(layout);
            }
        });

        // todo: single click
/*        swipeLayout.setItem(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsIntent = new Intent(context, MemberDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(context.getString(R.string.parcelable_identifier), members.get(position));
                Log.i(TAG, members.get(position).getPhoneNumber());

                detailsIntent.putExtra(context.getString(R.string.bundle_identifier), bundle);
                context.startActivity(detailsIntent);
            }
        });*/

        // todo: double click
        swipeLayout.setOnDoubleClickListener(
                new SwipeLayout.DoubleClickListener() {

                    @Override
                    public void onDoubleClick(SwipeLayout layout, boolean surface) {
                        Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // todo: favorite

        // todo: deletion
        view.findViewById(R.id.iv_trash).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Deletion clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView textView = (TextView) convertView.findViewById(R.id.tv_member_name);
        textView.setText(members.get(position).getName());

        TextView textViewMemberState = (TextView) convertView.findViewById(R.id.tv_member_state);
        textViewMemberState.setText(members.get(position).getState());
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public MemberModel getItem(int i) {
        return members.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
