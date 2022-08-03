package com.gudeok.gudeokapp.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gudeok.gudeokapp.R;
import com.gudeok.gudeokapp.data.DayModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter {

    private SimpleDateFormat onlyDate=new SimpleDateFormat("yyyy-MM-dd");

    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;
    public boolean isTime = true;

    public List<String> holoday = new ArrayList<>();
    public List<DayModel> mCalendarList;

    private Context mContext;

    private NumberFormat numformat = NumberFormat.getIntegerInstance();
    String today;

    public CalendarAdapter(Context context) {
        numformat.setMinimumIntegerDigits(2);
        mContext = context;

        Date currentTime = Calendar.getInstance().getTime();
        today = onlyDate.format(currentTime);
    }

    @Override
    public int getItemViewType(int position) { //뷰타입 나누기
        DayModel item = mCalendarList.get(position);

        if (item.getType() == 102031) {
            return EMPTY_TYPE; // 비어있는 일자 타입
        } else {
            return DAY_TYPE; // 일자 타입

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new DayViewHolder(inflater.inflate(R.layout.viewholder_day, parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        DayViewHolder holder = (DayViewHolder) viewHolder;

        if (viewType == EMPTY_TYPE)
        {
            if(position%7 == 0)
            {
                holder.mainLL.setBackgroundResource(R.drawable.background_border_top);

//                if(today.equals(onlyDate.format(mCalendarList.get(position).getCalendarModel().getTime())))
//                {
//                    holder.mainLL.setBackgroundResource(R.drawable.background_border_top_blue);
//                }
//                else {
//
//                    holder.mainLL.setBackgroundResource(R.drawable.background_border_top);
//                }
            }
            else
            {
                holder.mainLL.setBackgroundResource(R.drawable.background_border_left_top);

//                if(today.equals(onlyDate.format(mCalendarList.get(position).getCalendarModel().getTime())))
//                {
//                    holder.mainLL.setBackgroundResource(R.drawable.background_border_left_top_blue);
//                }
//                else {
//
//                    holder.mainLL.setBackgroundResource(R.drawable.background_border_left_top);
//                }
            }


            holder.lunchText.setBackground(null);
            holder.lunchText.setTextColor(mContext.getResources().getColor(R.color.lowestColor));
            holder.dinnerText.setBackground(null);
            holder.dinnerText.setTextColor(mContext.getResources().getColor(R.color.lowestColor));



            try {
                SimpleDateFormat formatter = new SimpleDateFormat("d");
                Date d = new Date(mCalendarList.get(position).getCalendarModel().getTimeInMillis());
                holder.dayText.setText(formatter.format(d).toUpperCase());


                Calendar cal = Calendar.getInstance();
                cal.setTime(d);

                int dayNum = cal.get(Calendar.DAY_OF_WEEK);

                holder.dayText.setTextColor(mContext.getResources().getColor(R.color.lowestColor));
//                if(holoday.contains(onlyDate.format(d)) || dayNum == 1)
//                {
//                    holder.dayText.setTextColor(Color.RED);
//                } else if(holoday.contains(onlyDate.format(d)) || dayNum == 6) {
//                    holder.dayText.setTextColor(mContext.getResources().getColor(R.color.primaryColor));
//                }
//                else
//                {
//                    holder.dayText.setTextColor(mContext.getResources().getColor(R.color.lowestColor));
//                }
            } catch (Exception e) {
                holder.dayText.setText("");
            }
        }
        else if (viewType == DAY_TYPE) {

            if(position%7 == 0)
            {
                holder.mainLL.setBackgroundResource(R.drawable.background_border_top);

//                if(today.equals(onlyDate.format(mCalendarList.get(position).getCalendarModel().getTime())))
//                {
//                    holder.mainLL.setBackgroundResource(R.drawable.background_border_top_blue);
//                }
//                else {
//
//                    holder.mainLL.setBackgroundResource(R.drawable.background_border_top);
//                }
            }
            else
            {
                holder.mainLL.setBackgroundResource(R.drawable.background_border_left_top);

//                if(today.equals(onlyDate.format(mCalendarList.get(position).getCalendarModel().getTime())))
//                {
//                    holder.mainLL.setBackgroundResource(R.drawable.background_border_left_top_blue);
//                }
//                else {
//
//                    holder.mainLL.setBackgroundResource(R.drawable.background_border_left_top);
//                }
            }
            holder.lunchText.setText("점심");
            holder.lunchText.setBackgroundColor(mContext.getResources().getColor(R.color.subPrimaryColor));
            holder.lunchText.setTextColor(mContext.getResources().getColor(R.color.white));

            holder.dinnerText.setText("저녁");
            holder.dinnerText.setBackgroundColor(mContext.getResources().getColor(R.color.middleColor));
            holder.dinnerText.setTextColor(mContext.getResources().getColor(R.color.white));

            try {
                SimpleDateFormat formatter = new SimpleDateFormat("d");
                Date d = new Date(mCalendarList.get(position).getCalendarModel().getTimeInMillis());
                holder.dayText.setText(formatter.format(d).toUpperCase());


                Calendar cal = Calendar.getInstance();
                cal.setTime(d);

                int dayNum = cal.get(Calendar.DAY_OF_WEEK);

                if(holoday.contains(onlyDate.format(d)) || dayNum == 1)
                {
                    holder.dayText.setTextColor(Color.RED);
                } else if(holoday.contains(onlyDate.format(d)) || dayNum == 7) {
                    holder.dayText.setTextColor(mContext.getResources().getColor(R.color.primaryColor));
                }
                else
                {
                    holder.dayText.setTextColor(mContext.getResources().getColor(R.color.black));
                }
            } catch (Exception e) {
                holder.dayText.setText("");
            }

        }

    }

    @Override
    public int getItemCount() {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }

}
