package com.gudeok.gudeokapp.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gudeok.gudeokapp.R;

public class DayViewHolder extends RecyclerView.ViewHolder {// 요일 입 ViewHolder

    public TextView dayText;
    public TextView lunchText;
    public TextView dinnerText;
    public LinearLayout mainLL;

    public DayViewHolder(@NonNull View itemView) {
        super(itemView);


        dayText = itemView.findViewById(R.id.dayText);
        lunchText = itemView.findViewById(R.id.lunch);
        dinnerText = itemView.findViewById(R.id.dinner);
        mainLL = itemView.findViewById(R.id.mainLL);
    }


}
