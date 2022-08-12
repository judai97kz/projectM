package com.judai.projectm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends BaseAdapter {
    Context context;
    List<User> arrayList;
    int layout;

    public UserAdapter(Context context, List<User> arrayList, int layout) {
        this.context = context;
        this.arrayList = arrayList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder = new ViewHolder();
             holder.textView = convertView.findViewById(R.id.nameac);
             convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        User user = arrayList.get(position);
        holder.textView.setText(user.get_username());
        return convertView;
    }
}
