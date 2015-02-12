package com.example.BreakinLogger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.BreakinLogger.R;
import com.example.BreakinLogger.domain.Person;

import java.util.List;


public class ListDataAdapter extends BaseAdapter
{
    private List<Person> persons;//绑定的数据
    private int resource;//绑定的条目界面
    private LayoutInflater inflater;

    public ListDataAdapter(Context context, List<Person> persons, int resource)
    {
        this.persons = persons;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return persons.size();//数据总数
    }

    @Override
    public Object getItem(int position)
    {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView text_Time = null;
        TextView text_PMname = null;
        TextView text_number = null;
        if (convertView == null)
        {
            convertView = inflater.inflate(resource, null);//生成条目界面对象
            text_Time = (TextView) convertView.findViewById(R.id.text_two_time);
            text_PMname = (TextView) convertView.findViewById(R.id.text_two_PMName);
            text_number = (TextView) convertView.findViewById(R.id.text_two_number);

            ViewCache cache = new ViewCache();
            cache.text_Time = text_Time;
            cache.text_PMname = text_PMname;
            cache.text_number = text_number;
            convertView.setTag(cache);
        } else
        {
            ViewCache cache = (ViewCache) convertView.getTag();
            text_Time = cache.text_Time;
            text_PMname = cache.text_PMname;
            text_number = cache.text_number;
        }

        Person person = persons.get(position);
        //下面代码实现数据绑定
        text_Time.setText(person.getTime());
        text_PMname.setText(person.getPMName());
        text_number.setText(person.getNumber().toString());

        return convertView;
    }

    private final class ViewCache
    {
        public TextView text_Time;
        public TextView text_PMname;
        public TextView text_number;
    }
}
