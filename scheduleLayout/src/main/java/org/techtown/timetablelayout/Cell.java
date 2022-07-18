package org.techtown.timetablelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.techtown.timetablelayout.R;

import java.util.ArrayList;

public class Cell extends FrameLayout {

    private int row,column;
    private boolean isScheduled = false;
    private ArrayList<Cell> spannedCells = new ArrayList<>();//스케줄 추가하면서 View.GONE 처리된 Cells
    private TextView textView;

    public Cell(Context context) {
        super(context);
        initView(context,null);
    }

    public Cell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public Cell(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        textView = new TextView(context);

        //ripple effect 적용
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(androidx.appcompat.R.attr.selectableItemBackground, typedValue,true);
        int resId = typedValue.resourceId;
        textView.setBackgroundResource(resId);

        setClickable(false);
        addView(textView);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.width = LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.MATCH_PARENT;
        textView.setLayoutParams(layoutParams);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    //병합되어서 지워진 cell 추가
    public void addSpannedCells(Cell cell)
    {
        spannedCells.add(cell);
    }

    //
    public void removeSpannedCells(int index)
    {
        spannedCells.get(index).setVisibility(View.VISIBLE);
        spannedCells.remove(index);

    }

    public void removeSpannedCells(Object o)
    {
        spannedCells.get(spannedCells.indexOf(o)).setVisibility(View.VISIBLE);
        spannedCells.remove(o);
    }


    public ArrayList<Cell> getSpannedCells() {
        return spannedCells;
    }

    public boolean isScheduled()
    {
        return isScheduled;
    }
    public void setScheduled(boolean isScheduled)
    {
        this.isScheduled = isScheduled;
    }
    public TextView getTextView() {
        return textView;
    }


    public void setGravity(int gravity)
    {
        textView.setGravity(gravity);
    }

    public void setTextColor(int color)
    {
        textView.setTextColor(color);
    }

    public void setText(String text)
    {
        textView.setText(text);
    }

    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        textView.setClickable(clickable);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        textView.setVisibility(visibility);
    }

    @Override
    public void setOnClickListener(View.OnClickListener l) {
        setClickable(true);
        textView.setOnClickListener(l);
    }

    public String getText()
    {
        return textView.getText().toString();
    }
}
