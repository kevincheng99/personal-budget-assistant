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
        holder.abTv.setText(temp.GetAcntTotal());
        holder.anTv.setText(temp.GetAcntName());
        holder.pb.setProgress(temp.GetProg());
        return row;
    }
    
    private class ViewHolder
    {
    	public TextView anTv = null;
    	public TextView abTv = null;
    	public ProgressBar pb = null;
    	ViewHolder(View v)
    	{
    		anTv = (TextView) v.findViewById(R.id.acnt_li_tv);
    		abTv = (TextView) v.findViewById(R.id.acnt_li_tv1);
    		pb = (ProgressBar) v.findViewById(R.id.acnt_li_pb);
    	}
    	
    }
}


