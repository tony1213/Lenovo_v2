package com.overtech.lenovo.widget.tabview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.overtech.lenovo.R;


public class TabView extends LinearLayout implements OnClickListener {

    private OnTabChangeListener mOnTabChangedListener;

    private int mState =-1;

    private final TextView mStateTextView1;
    private final TextView mStateTextView2;
    private final TextView mStateTextView3;
    private final TextView mStateTextView4;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public TabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.layout_tab_view, this);
        mStateTextView1 = (TextView) findViewById(R.id.textview_state1);
        mStateTextView2 = (TextView) findViewById(R.id.textview_state2);
        mStateTextView3 = (TextView) findViewById(R.id.textview_state3);
        mStateTextView4 = (TextView) findViewById(R.id.textview_state4);

        mStateTextView1.setOnClickListener(this);
        mStateTextView2.setOnClickListener(this);
        mStateTextView3.setOnClickListener(this);
        mStateTextView4.setOnClickListener(this);
    }

    public void setOnTabChangeListener(OnTabChangeListener listener) {
        mOnTabChangedListener = listener;
    }

    public void setCurrentTab(int index) {
        switchState(index);
    }

    private void switchState(int state) {
        if (mState == state) {
            return;
        } // else continue

        mState = state;
        mStateTextView1.setSelected(false);
        mStateTextView2.setSelected(false);
        mStateTextView3.setSelected(false);
        mStateTextView4.setSelected(false);

        Object tag = null;

        switch (mState) {
            case 0:
                mStateTextView1.setSelected(true);
                tag = mStateTextView1.getTag();
                break;

            case 1:
                mStateTextView2.setSelected(true);
                tag = mStateTextView2.getTag();
                break;

            case 2:
                mStateTextView3.setSelected(true);
                tag = mStateTextView3.getTag();
                break;

            case 3:
                mStateTextView4.setSelected(true);
                tag = mStateTextView4.getTag();
                break;

            default:
                break;
        }

        if (mOnTabChangedListener != null) {
            if (tag != null && mOnTabChangedListener != null) {
                mOnTabChangedListener.onTabChange(tag.toString());
            } else {
                mOnTabChangedListener.onTabChange(null);
            }
        } // else ignored
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_state1:
                switchState(0);
                break;
            case R.id.textview_state2:
                switchState(1);
                break;
            case R.id.textview_state3:
                switchState(2);
                break;
            case R.id.textview_state4:
                switchState(3);
                break;
            default:
                break;
        }
    }

    public static interface OnTabChangeListener {
        public void onTabChange(String tag);
    }
}
