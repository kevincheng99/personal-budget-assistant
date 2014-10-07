package com.example.plutus;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MenuItemAdapter extends ArrayAdapter<AccountMenuItem>
{
	
	public MenuItemAdapter(Context context, ArrayList<AccountMenuItem> listItems) 
	{
        super(context, R.layout.acnt_li, listItems);
    }
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder;
        if(row==null)
        {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.acnt_li, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }
        AccountMenuItem temp = getItem(position);
        holder.abTv.setText(temp.acntTotal);
        holder.anTv.setText(temp.acntName);
        holder.pb.setProgress(temp.progress);
        holder.threshTv.setText(temp.threshText);
        holder.titleTv.setText(temp.acntTitle);
        return row;
    }
    
    private class ViewHolder
    {
    	public TextView anTv = null;
    	public TextView abTv = null;
    	public TextView threshTv = null;
    	public TextView titleTv = null;
    	public TextView emailTv = null;
    	public TextView phoneTv = null;
    	public ProgressBar pb = null;
    	ViewHolder(View v)
    	{
    		anTv = (TextView) v.findViewById(R.id.acnt_li_tv2);
    		abTv = (TextView) v.findViewById(R.id.acnt_li_tv1);
    		pb = (ProgressBar) v.findViewById(R.id.acnt_li_pb);
    		threshTv = (TextView) v.findViewById(R.id.acnt_li_tv3);
    		titleTv = (TextView) v.findViewById(R.id.acnt_li_tv);
    		emailTv = (TextView) v.findViewById(R.id.acnt_email_tv);
    		phoneTv = (TextView) v.findViewById(R.id.acnt_phone_tv);
    	}
    	
    }
}


