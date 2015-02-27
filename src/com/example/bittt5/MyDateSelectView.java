
package com.example.bittt5;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

class MyDateSelectView extends RelativeLayout {

    public Context context;

    public MyDateSelectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public int  hhhh = 0;
    public int  wwww = 0;
    public View mask_tv;
    public MyDateSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.mydataselect, null);
        addView(view, new RelativeLayout.LayoutParams(-1, -1));
        mylistView1 = (ListView) view.findViewById(R.id.mylist1);
        mylistView2 = (ListView) view.findViewById(R.id.mylist2);
        mylistView3 = (ListView) view.findViewById(R.id.mylist3);
        mask_tv = view.findViewById(R.id.mask_tv);
        
        this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                hhhh = getMeasuredHeight();
                wwww = getMeasuredWidth();
                Log.i("mimi", hhhh+" "+wwww);
                RelativeLayout.LayoutParams ppp= (LayoutParams) mask_tv.getLayoutParams();
                ppp.height = hhhh/3;
                mask_tv.setLayoutParams(ppp);
                init();
                handler.postDelayed(new Runnable() {
                    
                    @Override
                    public void run() {
                        setNums(11,11,11);
                       // setNums(11,11,11);
                       // getNums();
                    }
                }, 4000);
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
    
    public Handler handler = new Handler();

    public MyDateSelectView(Context context) {
        super(context);
    }

    private ListView mylistView1;
    private ListView mylistView2;
    private ListView mylistView3;

    private int mfirstVisibleItem = 0;
    private int mvisibleItemCount = 0;

    public void init() {
        for (int i = 0; i < 12; i++) {
            hours_12.add(i + 1);
        }
        for (int i = 0; i < 24; i++) {
            hours_24.add(i + 1);
        }
        for (int i = 0; i < 60; i++) {
            minute_60.add(i + 1);
        }

        mylistView1.setDividerHeight(0);
        mylistView1.setDivider(null);
        mylistView2.setDividerHeight(0);
        mylistView2.setDivider(null);
        mylistView3.setDividerHeight(0);
        mylistView3.setDivider(null);
        mylistView1.setAdapter(new MyAdapter1());
        mylistView2.setAdapter(new MyAdapter2());
        mylistView3.setAdapter(new MyAdapter3());
        mylistView1.setSelection(Integer.MAX_VALUE / 2);
        mylistView2.setSelection(Integer.MAX_VALUE / 2);
        mylistView3.setSelection(Integer.MAX_VALUE / 2);
        mylistView1.setSelected(true);
        mylistView2.setSelected(true);
        mylistView3.setSelected(true);
        
      OnScrollListener bbb =   new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        ListView ttt = (ListView) view;
                        ttt.setSelection(mfirstVisibleItem);
                        
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:

                        break;
                    case SCROLL_STATE_FLING:

                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                Log.i("bbb", "" + visibleItemCount);
                mfirstVisibleItem = firstVisibleItem;
                mvisibleItemCount = visibleItemCount;
            }
        };
        mylistView1.setOnScrollListener(bbb);
        mylistView2.setOnScrollListener(bbb);
        mylistView3.setOnScrollListener(bbb);
    }
    
    
    
    public int[] getNums(){
        int item1 = (Integer) mylistView1.getAdapter().getItem(mylistView1.getSelectedItemPosition()+1);
        int item2 = (Integer) mylistView2.getAdapter().getItem(mylistView2.getSelectedItemPosition()+1);
        int item3 = (Integer) mylistView3.getAdapter().getItem(mylistView3.getSelectedItemPosition()+1);
        
        Log.i("mimi", item1 +"  "+item2 + "  "+item3 );
        return new int[]{item1, item2, item3};
    }
    
    //设置数字
    public void setNums(int num1, int num2, int num3){
        
        Object item2 = mylistView1.getSelectedItem();
        
        int item = (Integer) mylistView1.getAdapter().getItem(mylistView1.getSelectedItemPosition());
        
        Log.i("mimi", "::::"+mylistView1.getSelectedItemPosition());
        if(item != num1){
            int ddd = Math.abs(num1 - item -2);
            mylistView1.setSelection(mylistView1.getSelectedItemPosition()+ddd);
        }

        item = (Integer) mylistView2.getAdapter().getItem(mylistView2.getSelectedItemPosition());
        if(item != num1){
            int ddd = Math.abs(num1 - item -2);
            mylistView2.setSelection(mylistView2.getSelectedItemPosition()+ddd);
        }
        
        item = (Integer) mylistView3.getAdapter().getItem(mylistView3.getSelectedItemPosition());
        if(item != num1){
            int ddd = Math.abs(num1 - item -2);
            mylistView3.setSelection(mylistView3.getSelectedItemPosition()+ddd);
        }
        Log.i("mimi", item+"");
    }

    public ArrayList<Integer> hours_12 = new ArrayList<Integer>();
    public ArrayList<Integer> hours_24 = new ArrayList<Integer>();
    public ArrayList<Integer> minute_60 = new ArrayList<Integer>();

    class MyAdapter3 extends BaseAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return hours_12.get(position % 12);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = null;
            if (convertView != null) {
                tv = (TextView) convertView;
            } else {
                tv = new TextView(context);
            }
            AbsListView.LayoutParams params = (android.widget.AbsListView.LayoutParams) tv
                    .getLayoutParams();
            if (params == null) {
                params = new AbsListView.LayoutParams(-1, hhhh/3);
                params.height = hhhh/3;
            } else {
                params.height = hhhh/3;
            }
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(params);
            tv.setTextSize(25);
            tv.setText("" + hours_12.get(position % 12));
            return tv;
        }
    }

    class MyAdapter2 extends BaseAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return minute_60.get(position % 60);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = null;
            if (convertView != null) {
                tv = (TextView) convertView;
            } else {
                tv = new TextView(context);
            }
            AbsListView.LayoutParams params = (android.widget.AbsListView.LayoutParams) tv
                    .getLayoutParams();
            if (params == null) {
                params = new AbsListView.LayoutParams(-1, hhhh/3);
                params.height = hhhh/3;
            } else {
                params.height = hhhh/3;
            }
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(params);
            tv.setTextSize(25);
            tv.setText("" + minute_60.get(position % 60));
            return tv;
        }
    }

    class MyAdapter1 extends BaseAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return hours_24.get(position % 24);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = null;
            if (convertView != null) {
                tv = (TextView) convertView;
            } else {
                tv = new TextView(context);
            }
            AbsListView.LayoutParams params = (android.widget.AbsListView.LayoutParams) tv
                    .getLayoutParams();
            if (params == null) {
                params = new AbsListView.LayoutParams(-1, hhhh/3);
                params.height = hhhh/3;
            } else {
                params.height = hhhh/3;
            }
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(params);
            tv.setTextSize(25);
            tv.setText("" + hours_24.get(position % 24));
            return tv;
        }
    }
}
