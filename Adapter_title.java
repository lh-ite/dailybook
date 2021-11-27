package com.example.dailybook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.LayoutInflaterCompat;

import java.util.List;

public class Adapter_title extends ArrayAdapter<dailybook> {
    private int resourceId;
    public Adapter_title(Context context, int texwviewResourced, List<dailybook> objects)
    {
        super(context,texwviewResourced,objects);
        resourceId=texwviewResourced;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        dailybook d=getItem(position);//通过getItem获得点击事件的内容
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);//加载我们传入的布局
        TextView title=(TextView) view.findViewById(R.id.title2);
        TextView time=(TextView) view.findViewById(R.id.time2);
        ImageView image1=(ImageView) view.findViewById(R.id.img);
        time.setText(d.getTime());//给对应的控件赋值
        title.setText(d.getTitle());//给对应的控件赋值

        return view;
    }
}
