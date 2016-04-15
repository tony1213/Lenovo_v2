package com.overtech.lenovo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.fragment.InformationFragment;
import com.overtech.lenovo.activity.fragment.KnowledgeFragment;
import com.overtech.lenovo.activity.fragment.PersonalFragment;
import com.overtech.lenovo.activity.fragment.TaskListFragment;
import com.overtech.lenovo.activity.fragment.callback.FragmentCallback;
import com.overtech.lenovo.utils.FragmentUtils;
import com.overtech.lenovo.widget.dialogeffects.Effectstype;
import com.overtech.lenovo.widget.tabview.TabView;
import com.overtech.lenovo.widget.tabview.TabView.OnTabChangeListener;

public class MainActivity extends BaseActivity implements OnTabChangeListener, FragmentCallback {

    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    public TaskListFragment taskListFragment;
    public KnowledgeFragment knowledgeFragment;
    public InformationFragment informationFragment;
    public PersonalFragment personalFragment;
    private TabView mTabView;
    private int mPreviousTabIndex = 0;//上一次的状态
    private int mCurrentTabIndex = 0;//当前状态

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();
        mCurrentTabIndex = 0;
        mPreviousTabIndex = -1;
        setupViews();
    }

    private void setupViews() {
        mTabView = (TabView) findViewById(R.id.view_tab);
        mTabView.setOnTabChangeListener(this);
        mTabView.setCurrentTab(mCurrentTabIndex);
    }

    @Override
    public void onFragmentCallback(Fragment fragment, int id, Bundle args) {
        mTabView.setCurrentTab(0);
    }

    @Override
    public void onTabChange(String tag) {

        if (tag != null) {
            if (tag.equals("tasklist")) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 0;
                replaceFragment(TaskListFragment.class);
            } else if ("knowledge".equals(tag)) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 1;
                replaceFragment(KnowledgeFragment.class);
            } else if (tag.equals("information")) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 2;
                replaceFragment(InformationFragment.class);
            } else if (tag.equals("personal")) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 3;
                replaceFragment(PersonalFragment.class);
            }
        }
    }

    private void replaceFragment(Class<? extends Fragment> newFragment) {
        mCurrentFragment = FragmentUtils.switchFragment(mFragmentManager, R.id.layout_content, mCurrentFragment, newFragment, null, false);
        if(newFragment.getSimpleName().equals(TaskListFragment.class.getSimpleName())){
            taskListFragment=(TaskListFragment) mCurrentFragment;
        }else if(newFragment.getSimpleName().equals(KnowledgeFragment.class.getSimpleName())){
            knowledgeFragment= (KnowledgeFragment) mCurrentFragment;
        }else if(newFragment.getSimpleName().equals(InformationFragment.class.getSimpleName())){
            informationFragment= (InformationFragment) mCurrentFragment;
        }else{
            personalFragment= (PersonalFragment) mCurrentFragment;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            Effectstype effect = Effectstype.Shake;
            dialogBuilder.withTitle("温馨提示")
                    .withTitleColor(R.color.main_primary)
                    .withDividerColor("#11000000").withMessage("您是否要退出？")
                    .withMessageColor(R.color.main_primary)
                    .withDialogColor("#FFFFFFFF")
                    .isCancelableOnTouchOutside(true).withDuration(700)
                    .withEffect(effect).withButtonDrawable(R.color.main_white)
                    .withButton1Text("否").withButton1Color("#DD47BEE9")
                    .withButton2Text("是").withButton2Color("#DD47BEE9")
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            }).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
