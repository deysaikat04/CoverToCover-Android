package com.example.cover;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepakr on 3/29/2016.
 */
public class MyAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<Books> beanList;
    private LayoutInflater inflater;
    List<Books> mStringFilterList;
    ValueFilter valueFilter;


    public MyAdapter(Context context, List<Books> beanList) {
        this.context = context;
        this.beanList = beanList;
        mStringFilterList = beanList;
    }


    @Override
    public int getCount() {
        return beanList.size();
    }

    @Override
    public Object getItem(int i) {
        return beanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = inflater.inflate(R.layout.search_list, null);
        }

        TextView txtName = (TextView) view.findViewById(R.id.name);
        //TextView txtAge = (TextView) view.findViewById(R.id.age);

        Books bean = beanList.get(i);
        String name = bean.getBookname();
        int id = bean.getBookid();
        String age = String.valueOf(id);

        txtName.setText(name);
        //txtAge.setText(age);
        return view;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        notifyDataSetChanged();
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<Books> filterList = new ArrayList<Books>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getBookname().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        Books bean = new Books(mStringFilterList.get(i)
                                .getBookname(), mStringFilterList.get(i)
                                .getBookid());
                        filterList.add(bean);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            beanList = (ArrayList<Books>) results.values;
            notifyDataSetChanged();
        }

    }
}
