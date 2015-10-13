package com.leontheprofessional.test.whorepresentsyou.activity.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

import java.util.ArrayList;

/**
 * Created by Leon on 10/13/2015.
 */
public class CustomListFragmentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MemberModel> members;

    public CustomListFragmentAdapter(Context context, ArrayList<MemberModel> members) {
        this.context = context;
        this.members = members;
    }


    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public MemberModel getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (members != null && members.size() > 0) {
            View rootView = inflater.inflate(R.layout.listview_item, null);

            TextView textViewName = (TextView) rootView.findViewById(R.id.textview_name);
            TextView textViewState = (TextView) rootView.findViewById(R.id.textview_state);

            textViewName.setText(getItem(position).getName());
            textViewState.setText(getItem(position).getState());

            return rootView;
        } else {
            TextView emptyTextView = new TextView(context);
            emptyTextView.setText(R.string.empty_textview_text);
            return emptyTextView;
        }
    }
}
