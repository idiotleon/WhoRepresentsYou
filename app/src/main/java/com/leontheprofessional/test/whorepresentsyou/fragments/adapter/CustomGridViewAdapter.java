package com.leontheprofessional.test.whorepresentsyou.fragments.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leontheprofessional.test.whorepresentsyou.R;
import com.leontheprofessional.test.whorepresentsyou.model.MemberModel;

import java.util.ArrayList;

/**
 * Created by Leon on 10/12/2015.
 */
public class CustomGridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MemberModel> members;

    public CustomGridViewAdapter(Context context, ArrayList<MemberModel> members) {
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

        View rootView = inflater.inflate(R.layout.gridview_item, null);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageview_gridview_item);
        TextView textView = (TextView) rootView.findViewById(R.id.textview_gridview_item);

        imageView.setImageResource(R.mipmap.no_image_available);
        textView.setText(getItem(position).getName());

        return rootView;
    }
}
