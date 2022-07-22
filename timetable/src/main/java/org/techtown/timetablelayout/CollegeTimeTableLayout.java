package org.techtown.timetablelayout;

import android.content.Context;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;

public class CollegeTimeTableLayout extends GridLayout {

    private static String TAG = "CollegeTimeTableLayout";
    private ArrayList<TextView> cells = new ArrayList<>();

    private String[] row_names,column_names;

    protected int cellMarginTop, cellMarginBottm, cellMarginLeft, cellMarginRight;
    private int cellTextColor;
    private int cellColor;//cell 배경 색

    public CollegeTimeTableLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public CollegeTimeTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CollegeTimeTableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public CollegeTimeTableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.collegetimetable_layout, this);

        if (attrs != null) {
            //attrs.xml에 정의한 스타일을 가져온다
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CollegeTimeTableLayout);
            cellColor = a.getColor(R.styleable.CollegeTimeTableLayout_cellColor, getResources().getColor(R.color.white,null));
            cellTextColor = a.getColor(R.styleable.CollegeTimeTableLayout_cellTextColor, getResources().getColor(R.color.black,null));
            cellMarginTop = a.getInt(R.styleable.CollegeTimeTableLayout_cellMarginTop, 5);
            cellMarginBottm = a.getInt(R.styleable.CollegeTimeTableLayout_cellMarginBottom, 5);
            cellMarginRight = a.getInt(R.styleable.CollegeTimeTableLayout_cellMarginRight, 5);
            cellMarginLeft = a.getInt(R.styleable.CollegeTimeTableLayout_cellMarginLeft, 5);
            a.recycle(); // 이용이 끝났으면 recycle() 호출
        }
        removeAllViews();
        //setBackgroundColor(getResources().getColor(R.color.cell_backgroud_color,null));
        //setOrientation(GridLayout.VERTICAL);

        createInitCells();
    }

    //초기 cell들 생성
    private void createInitCells()
    {
        initRowColumnNames();
        addCells();
    }

    //row, column 이름 초기화
    private void initRowColumnNames()
    {
        row_names = new String[getRowCount()];
        column_names = new String[getColumnCount()];

        for(int i=0;i<getRowCount();i++)
            row_names[i] = i+"";
        for(int i=0;i<getColumnCount();i++)
            column_names[i] = i+"";
    }

    //row, column만큼 cell 추가
    private void addCells()
    {
        for(int i=0;i<getRowCount();i++)
        {
            for(int j=0;j<getColumnCount();j++)
            {
                Cell cell = new Cell(getContext());
                cell.setTag(row_names[i]+"-"+column_names[j]);//행열로 태그 설정

                cell.setRow(i);
                cell.setColumn(j);

                cell.setBackgroundColor(cellColor);//cell 배경색 설정
                cell.setTextColor(cellTextColor);//cell text 색 설정
                cell.setGravity(Gravity.CENTER);
                cell.setText("");

                if(i==0 && j!=0)
                    cell.setText(column_names[j]);
                if(j==0 && i!=0)
                    cell.setText(row_names[i]);

                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(i,1.0f), GridLayout.spec(j,1.0f));
                layoutParams.setGravity(Gravity.FILL);
                layoutParams.width = 0;//스케줄 추가해도 너비 일정하게 유지
                layoutParams.height = LayoutParams.WRAP_CONTENT;
                layoutParams.setMargins(cellMarginLeft,cellMarginTop,cellMarginRight,cellMarginBottm);
                cell.setLayoutParams(layoutParams);

                addView(cell);
            }
        }
    }

    //스케줄 추가
    public void addSchedule(String text, String row_name, String column_name, int blocks)
    {
        //스케줄 추가 cell
        Cell schedule_cell = findCell(row_name,column_name);

        if(schedule_cell.getVisibility() == View.GONE || schedule_cell.isScheduled())//스케줄 있는 시간
        {
            Toast.makeText(getContext(),"already exists schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> row_list = new ArrayList<>(Arrays.asList(row_names));
        int origin_index = row_list.indexOf(row_name);

        //cell 삭제
        for(int i=1;i<blocks;i++)
        {
            Cell cell= findCell(row_names[origin_index+i],column_name);
            cell.setVisibility(View.GONE);

            schedule_cell.addSpannedCells(cell);
        }

        schedule_cell.setText(text);
        schedule_cell.setScheduled(true);
        schedule_cell.setClickable(true);
        //병합
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(origin_index, blocks,1.0f);
        schedule_cell.setLayoutParams(layoutParams);
    }

    //스케줄 추가
    public void addSchedule(String text, String row_name, String column_name, int blocks, int backgroundColor)
    {
        //스케줄 추가 cell
        Cell schedule_cell = findCell(row_name,column_name);

        if(schedule_cell.getVisibility() == View.GONE || schedule_cell.isScheduled())//스케줄 있는 시간
        {
            Toast.makeText(getContext(),"already exists schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> row_list = new ArrayList<>(Arrays.asList(row_names));
        int origin_index = row_list.indexOf(row_name);

        //삭제 cell
        for(int i=1;i<blocks;i++)
        {
            Cell cell= findCell(row_names[origin_index+i],column_name);
            cell.setVisibility(View.GONE);

            schedule_cell.addSpannedCells(cell);
        }

        //스케줄 추가 cell
        schedule_cell.setText(text);
        schedule_cell.setScheduled(true);
        schedule_cell.setClickable(true);
        //cell 색깔 지정
        schedule_cell.setBackgroundColor(backgroundColor);

        //병합
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(origin_index, blocks,1.0f);
        schedule_cell.setLayoutParams(layoutParams);
    }

    //스케줄 추가
    public void addSchedule(String text, String row_name, String column_name, int blocks, int backgroundColor, int textColor)
    {
        //스케줄 추가 cell
        Cell schedule_cell = findCell(row_name,column_name);

        if(schedule_cell.getVisibility() == View.GONE || schedule_cell.isScheduled())//스케줄 있는 시간
        {
            Toast.makeText(getContext(),"already exists schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<String> row_list = new ArrayList<>(Arrays.asList(row_names));
        int origin_index = row_list.indexOf(row_name);

        //삭제 cell
        for(int i=1;i<blocks;i++)
        {
            Cell cell= findCell(row_names[origin_index+i],column_name);
            cell.setVisibility(View.GONE);

            schedule_cell.addSpannedCells(cell);
        }

        //스케줄 추가 cell
        schedule_cell.setText(text);
        schedule_cell.setScheduled(true);
        schedule_cell.setClickable(true);
        //색지정
        schedule_cell.setBackgroundColor(backgroundColor);
        schedule_cell.setTextColor(textColor);

        //병합
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(origin_index, blocks,1.0f);
        schedule_cell.setLayoutParams(layoutParams);
    }

    //스케줄 추가
    public void addSchedule(String text, int row, int column, int blocks)
    {
        //스케줄 추가 cell
        Cell schedule_cell = findCell(row,column);

        if(schedule_cell.getVisibility() == View.GONE || schedule_cell.isScheduled())//스케줄 있는 시간
        {
            Toast.makeText(getContext(),"already exists schedule",Toast.LENGTH_SHORT).show();
            return;
        }
        //cell 삭제
        for(int i=1;i<blocks;i++)
        {
            Cell cell= findCell(row + i,column);
            cell.setVisibility(View.GONE);

            schedule_cell.addSpannedCells(cell);
        }

        //스케줄 추가 cell
        schedule_cell.setText(text);
        schedule_cell.setScheduled(true);
        schedule_cell.setClickable(true);
        //병합
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row, blocks,1.0f);
        schedule_cell.setLayoutParams(layoutParams);
    }

    //스케줄 추가
    public void addSchedule(String text, int row, int column, int blocks, int backgroundColor)
    {
        //스케줄 추가 cell
        Cell schedule_cell = findCell(row,column);

        if(schedule_cell.getVisibility() == View.GONE || schedule_cell.isScheduled())//스케줄 있는 시간
        {
            Toast.makeText(getContext(),"already exists schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        //cell 삭제
        for(int i=1;i<blocks;i++)
        {
            Cell cell= findCell(row + i,column);
            cell.setVisibility(View.GONE);

            schedule_cell.addSpannedCells(cell);
        }

        //스케줄 추가 cell
        schedule_cell.setText(text);
        schedule_cell.setScheduled(true);
        schedule_cell.setClickable(true);
        //cell 색깔 지정
        schedule_cell.setBackgroundColor(backgroundColor);

        //병합
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row, blocks,1.0f);
        schedule_cell.setLayoutParams(layoutParams);
    }

    //스케줄 추가
    public void addSchedule(String text, int row, int column, int blocks, int backgroundColor, int textColor)
    {
        //스케줄 추가 cell
        Cell schedule_cell = findCell(row,column);

        if(schedule_cell.getVisibility() == View.GONE || schedule_cell.isScheduled())//스케줄 있는 시간
        {
            Toast.makeText(getContext(),"already exists schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        //cell 삭제
        for(int i=1;i<blocks;i++)
        {
            Cell cell= findCell(row + i,column);
            cell.setVisibility(View.GONE);

            schedule_cell.addSpannedCells(cell);
        }

        //스케줄 추가 cell
        schedule_cell.setText(text);
        schedule_cell.setScheduled(true);
        schedule_cell.setClickable(true);
        //색지정
        schedule_cell.setBackgroundColor(backgroundColor);
        schedule_cell.setTextColor(textColor);

        //병합
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row, blocks,1.0f);
        schedule_cell.setLayoutParams(layoutParams);
    }

    public void deleteSchedule(int row, int column)
    {
        Cell schedule_cell = findCell(row,column);

        if(!schedule_cell.isScheduled())//스케줄 없는경우
        {
            Toast.makeText(getContext(),"not exists any schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        //스케줄 삭제처리
        schedule_cell.setText("");
        schedule_cell.setScheduled(false);//스케줄 삭제됨
        schedule_cell.setClickable(false);
        //색 복구
        schedule_cell.setBackgroundColor(cellColor);
        schedule_cell.setTextColor(cellTextColor);

        //병합 복구
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row, 1,1.0f);
        schedule_cell.setLayoutParams(layoutParams);

        //지워진 cell들 원래대로
        ArrayList<Cell> spannedCells = schedule_cell.getSpannedCells();

        for(int i=0;i<spannedCells.size();i++)
        {
            spannedCells.get(i).setVisibility(View.VISIBLE);
        }
        spannedCells.clear();

    }

    public void deleteSchedule(String row_name, String column_name)
    {
        Cell schedule_cell = findCell(row_name, column_name);

        int row = schedule_cell.getRow();

        if(!schedule_cell.isScheduled())//스케줄 없는경우
        {
            Toast.makeText(getContext(),"not exists any schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        //스케줄 삭제처리
        schedule_cell.setText("");
        schedule_cell.setScheduled(false);//스케줄 삭제됨
        schedule_cell.setClickable(false);
        //색 복구
        schedule_cell.setBackgroundColor(cellColor);
        schedule_cell.setTextColor(cellTextColor);

        //병합 복구
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row, 1,1.0f);
        schedule_cell.setLayoutParams(layoutParams);

        //지워진 cell들 원래대로
        ArrayList<Cell> spannedCells = schedule_cell.getSpannedCells();

        for(int i=0;i<spannedCells.size();i++)
        {
            spannedCells.get(i).setVisibility(View.VISIBLE);
        }
        spannedCells.clear();

    }

    public void deleteScheduleWithText(String text)
    {
        Cell schedule_cell = findCellWithText(text);

        int row = schedule_cell.getRow();

        if(!schedule_cell.isScheduled())//스케줄 없는경우
        {
            Toast.makeText(getContext(),"not exists any schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        //스케줄 삭제처리
        schedule_cell.setText("");
        schedule_cell.setScheduled(false);//스케줄 삭제됨
        schedule_cell.setClickable(false);
        //색 복구
        schedule_cell.setBackgroundColor(cellColor);
        schedule_cell.setTextColor(cellTextColor);

        //병합 복구
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row, 1,1.0f);
        schedule_cell.setLayoutParams(layoutParams);

        //지워진 cell들 원래대로
        ArrayList<Cell> spannedCells = schedule_cell.getSpannedCells();

        for(int i=0;i<spannedCells.size();i++)
        {
            spannedCells.get(i).setVisibility(View.VISIBLE);
        }
        spannedCells.clear();
    }

    public void modifySchedule(int row, int column,String change_text,int blocks)
    {
        Cell schedule_cell = findCell(row, column);

        if(!schedule_cell.isScheduled())//스케줄 없는경우
        {
            Toast.makeText(getContext(),"not exists any schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        schedule_cell.setText(change_text);

        ArrayList<Cell> spannedCells = schedule_cell.getSpannedCells();
        for(int i=0;i<spannedCells.size();i++)
        {
            spannedCells.get(i).setVisibility(View.VISIBLE);
        }
        spannedCells.clear();

        //cell 삭제
        for(int i=1;i<blocks;i++)
        {
            Cell cell= findCell(row + i,column);
            cell.setVisibility(View.GONE);

            schedule_cell.addSpannedCells(cell);
        }

        //병합
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row, blocks,1.0f);
        schedule_cell.setLayoutParams(layoutParams);
    }

    public void modifySchedule(int row, int column,String change_text,int blocks, int backgroundColor, int textColor)
    {
        Cell schedule_cell = findCell(row, column);

        if(!schedule_cell.isScheduled())//스케줄 없는경우
        {
            Toast.makeText(getContext(),"not exists any schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        schedule_cell.setText(change_text);
        schedule_cell.setBackgroundColor(backgroundColor);
        schedule_cell.setTextColor(textColor);

        ArrayList<Cell> spannedCells = schedule_cell.getSpannedCells();
        for(int i=0;i<spannedCells.size();i++)
        {
            spannedCells.get(i).setVisibility(View.VISIBLE);
        }
        spannedCells.clear();

        //cell 삭제
        for(int i=1;i<blocks;i++)
        {
            Cell cell= findCell(row + i,column);
            cell.setVisibility(View.GONE);

            schedule_cell.addSpannedCells(cell);
        }

        //병합
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row, blocks,1.0f);
        schedule_cell.setLayoutParams(layoutParams);
    }

    public void modifySchedule(String row_name, String column_name, String change_text, int blocks)
    {
        Cell schedule_cell = findCell(row_name, column_name);

        int row = schedule_cell.getRow();
        int column = schedule_cell.getColumn();

        if(!schedule_cell.isScheduled())//스케줄 없는경우
        {
            Toast.makeText(getContext(),"not exists any schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        schedule_cell.setText(change_text);

        ArrayList<Cell> spannedCells = schedule_cell.getSpannedCells();
        for(int i=0;i<spannedCells.size();i++)
        {
            spannedCells.get(i).setVisibility(View.VISIBLE);
        }
        spannedCells.clear();

        //cell 삭제
        for(int i=1;i<blocks;i++)
        {
            Cell cell= findCell(row + i,column);
            cell.setVisibility(View.GONE);

            schedule_cell.addSpannedCells(cell);
        }

        //병합
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row, blocks,1.0f);
        schedule_cell.setLayoutParams(layoutParams);
    }

    public void modifySchedule(String row_name, String column_name, String change_text, int blocks, int backgroundColor, int textColor)
    {
        Cell schedule_cell = findCell(row_name, column_name);

        int row = schedule_cell.getRow();
        int column = schedule_cell.getColumn();

        if(!schedule_cell.isScheduled())//스케줄 없는경우
        {
            Toast.makeText(getContext(),"not exists any schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        schedule_cell.setText(change_text);
        schedule_cell.setBackgroundColor(backgroundColor);
        schedule_cell.setTextColor(textColor);

        ArrayList<Cell> spannedCells = schedule_cell.getSpannedCells();
        for(int i=0;i<spannedCells.size();i++)
        {
            spannedCells.get(i).setVisibility(View.VISIBLE);
        }
        spannedCells.clear();

        //cell 삭제
        for(int i=1;i<blocks;i++)
        {
            Cell cell= findCell(row + i,column);
            cell.setVisibility(View.GONE);

            schedule_cell.addSpannedCells(cell);
        }

        //병합
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row, blocks,1.0f);
        schedule_cell.setLayoutParams(layoutParams);
    }

    public void modifyScheduleWithText(String origin_text, String change_text, int blocks)
    {
        Cell schedule_cell = findCellWithText(origin_text);

        int row = schedule_cell.getRow();
        int column = schedule_cell.getColumn();

        if(!schedule_cell.isScheduled())//스케줄 없는경우
        {
            Toast.makeText(getContext(),"not exists any schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        schedule_cell.setText(change_text);

        ArrayList<Cell> spannedCells = schedule_cell.getSpannedCells();
        for(int i=0;i<spannedCells.size();i++)
        {
            spannedCells.get(i).setVisibility(View.VISIBLE);
        }
        spannedCells.clear();

        //cell 삭제
        for(int i=1;i<blocks;i++)
        {
            Cell cell= findCell(row + i,column);
            cell.setVisibility(View.GONE);

            schedule_cell.addSpannedCells(cell);
        }

        //병합
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row, blocks,1.0f);
        schedule_cell.setLayoutParams(layoutParams);
    }

    public void modifyScheduleWithText(String origin_text, String change_text, int blocks, int backgroundColor, int textColor)
    {
        Cell schedule_cell = findCellWithText(origin_text);

        int row = schedule_cell.getRow();
        int column = schedule_cell.getColumn();

        if(!schedule_cell.isScheduled())//스케줄 없는경우
        {
            Toast.makeText(getContext(),"not exists any schedule",Toast.LENGTH_SHORT).show();
            return;
        }

        schedule_cell.setText(change_text);
        schedule_cell.setBackgroundColor(backgroundColor);
        schedule_cell.setTextColor(textColor);

        ArrayList<Cell> spannedCells = schedule_cell.getSpannedCells();
        for(int i=0;i<spannedCells.size();i++)
        {
            spannedCells.get(i).setVisibility(View.VISIBLE);
        }
        spannedCells.clear();

        //cell 삭제
        for(int i=1;i<blocks;i++)
        {
            Cell cell= findCell(row + i,column);
            cell.setVisibility(View.GONE);

            schedule_cell.addSpannedCells(cell);
        }

        //병합
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)schedule_cell.getLayoutParams();
        layoutParams.rowSpec = GridLayout.spec(row, blocks,1.0f);
        schedule_cell.setLayoutParams(layoutParams);
    }

    //모든 cell 가져옴
    public Cell[][] getAllCell()
    {
        Cell[][] list = new Cell[getRowCount()] [getColumnCount()];

        for(int i=0;i<getRowCount();i++)
        {
            for(int j=0;j<getColumnCount();j++)
                list[i][j] = findCell(row_names[i],column_names[j]);
        }
        return list;
    }

    public int getCellTextColor() {
        return cellTextColor;
    }

    public void setCellTextColor(int cellTextColor) {
        this.cellTextColor = cellTextColor;

        for(int i=0;i<getRowCount();i++)
        {
            for(int j=0;j<getColumnCount();j++)
            {
                Cell cell = (Cell)findViewWithTag(row_names[i]+"-"+column_names[j]);
                cell.setTextColor(cellTextColor);
            }
        }
    }

    //cell 배경 색
    public int getCellColor() {
        return cellColor;
    }

    //cell 배경 색
    public void setCellColor(int cellColor) {
        this.cellColor = cellColor;

        for(int i=0;i<getRowCount();i++)
        {
            for(int j=0;j<getColumnCount();j++)
            {
                Cell cell = (Cell) findViewWithTag(row_names[i]+"-"+column_names[j]);
                cell.setBackgroundColor(cellColor);//배경설정
            }
        }
    }

    //처음 세로줄
    public void setRowNames(String[] row_names)
    {
        for(int i=0;i<getRowCount();i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                Cell cell = (Cell) findViewWithTag(this.row_names[i] + "-" + this.column_names[j]);
                cell.setTag(row_names[i] + "-" + this.column_names[j]);

                if(j==0 && i!=0)//첫줄
                    cell.setText(row_names[i]);
            }
        }

        this.row_names = row_names.clone();

    }

    //처음 가로줄
    public void setColumnNames(String[] column_names)
    {
        for(int i=0;i<getRowCount();i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                Cell cell = (Cell) findViewWithTag(this.row_names[i] + "-" + this.column_names[j]);
                cell.setTag(this.row_names[i] + "-" + column_names[j]);

                if(i==0 && j!=0)//첫줄
                    cell.setText(column_names[j]);
            }
        }

        this.column_names = column_names;
    }
    //특정 row, column의 cell 반환
    public Cell findCell(String row_name, String column_name)
    {
        Cell cell = (Cell)findViewWithTag(row_name+"-"+column_name);
        return cell;
    }

    //특정 row, column의 cell 반환
    public Cell findCell(int row, int column)
    {
        Cell cell = (Cell)findViewWithTag(row_names[row]+"-"+column_names[column]);
        return cell;
    }
    //해당 cell의 text를 보고 찾음
    public Cell findCellWithText(String text)
    {
        for(int i=0;i<getRowCount();i++)
        {
            for(int j=0;j<getColumnCount();j++)
            {
                Cell cell = (Cell)findViewWithTag(row_names[i]+"-"+column_names[j]);
                if(cell.getText().equals(text))
                    return cell;
            }
        }
        return null;
    }


    //마진 값
    public void setCellsMargin(int left, int top, int right, int bottom)
    {
        cellMarginLeft = left;
        cellMarginRight = right;
        cellMarginTop = top;
        cellMarginBottm = bottom;

        for(int i=0;i<getRowCount();i++)
        {
            for(int j=0;j<getColumnCount();j++)
            {
                Cell cell = (Cell)findViewWithTag(row_names[i]+"-"+column_names[j]);
                GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)cell.getLayoutParams();

                layoutParams.setMargins(left,top,right,bottom);
                cell.setLayoutParams(layoutParams);
            }
        }
    }


    @Override
    public void setRowCount(int rowCount) {
        removeAllViews();

        super.setRowCount(rowCount);

        initRowColumnNames();
        addCells();
    }

    @Override
    public void setColumnCount(int columnCount) {
        removeAllViews();

        super.setColumnCount(columnCount);

        initRowColumnNames();
        addCells();
    }


}
