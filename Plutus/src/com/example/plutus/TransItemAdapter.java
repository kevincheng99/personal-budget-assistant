package com.example.plutus;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TransItemAdapter extends ArrayAdapter<TransMenuItem>
{
	
	public TransItemAdapter(Context context, ArrayList<TransMenuItem> listItems) 
	{
        super(context, R.layout.trns_li, listItems);
    }
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder;
        if(row==null)
        {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.trns_li, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }
        TransMenuItem temp = getItem(position);
        holder.titleTv.setText(temp.trnsTitle);
        holder.dateTv.setText(temp.trnsDate);
        holder.amntTv.setText(temp.trnsTotal);
        holder.typeTv.setText(temp.trnsType);
        return row;
    }
    
    private class ViewHolder
    {
    	public TextView typeTv = null;
    	public TextView dateTv = null;
    	public TextView amntTv = null;
    	public TextView titleTv = null;
    	ViewHolder(View v)
    	{
    		typeTv = (TextView) v.findViewById(R.id.trns_type_tv);
    		dateTv = (TextView) v.findViewById(R.id.trns_date_tv);
    		amntTv = (TextView) v.findViewById(R.id.trns_amnt_tv);
    		titleTv = (TextView) v.findViewById(R.id.trns_item_tv);
    	}
    	
    }
}


