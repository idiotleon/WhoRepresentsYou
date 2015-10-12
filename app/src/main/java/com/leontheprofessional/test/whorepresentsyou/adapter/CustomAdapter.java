package com.leontheprofessional.test.whorepresentsyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

import java.util.ArrayList;


public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MemberModel> members;

    public CustomAdapter(Context context, ArrayList<MemberModel> members) {
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
        View rootView = inflater.inflate(R.layout.listview_item, null);

        TextView textViewName = (TextView) rootView.findViewById(R.id.textview_name);
        TextView textViewState = (TextView) rootView.findViewById(R.id.textview_state);

        textViewName.setText(getItem(position).getName());
        textViewState.setText(getItem(position).getState());

        return rootView;
    }
}
