package com.rectapp.metro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.rectapp.metro.base.BaseUI;
import com.rectapp.metro.frgm.AccountFrgm;
import com.rectapp.metro.frgm.HomeFrgm;
import com.rectapp.metro.frgm.StationFrgm;

public class MainUI extends BaseUI {

    private String mLastFragmentTag;
    private FragmentManager mFragmentManager;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment("home");
                    return true;
                case R.id.navigation_station:
                    changeFragment("station");
                    return true;
                case R.id.navigation_account:
                    changeFragment("account");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_main);
        mFragmentManager = getSupportFragmentManager();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void changeFragment(String flag) {

        if (!flag.equals(mLastFragmentTag)) {
            Fragment lastFragment = null;
            Fragment currentFragment = mFragmentManager.findFragmentByTag(flag);
            FragmentTransaction transaction = mFragmentManager.beginTransaction();

            if (mLastFragmentTag != null) {
                lastFragment = mFragmentManager.findFragmentByTag(mLastFragmentTag);
            }

            if (lastFragment != null) {
                transaction.hide(lastFragment);
            }

            if (currentFragment != null) {
                transaction.show(currentFragment);
            } else {
                currentFragment = createFragment(flag);
                transaction.add(R.id.main_container, currentFragment, flag);
            }
            mLastFragmentTag = flag;
            transaction.commit();
        }
    }

    private Fragment createFragment(String flag) {
        Fragment fragment = null;
        switch (flag) {
            case "home":
                fragment = new HomeFrgm();
                break;
            case "station":
                fragment = new StationFrgm();
                break;
            case "account":
                fragment = new AccountFrgm();
                break;
        }
        return fragment;
    }


    //退出时的时间
    private long mExitTime;
    private boolean isCloseApp;

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(context, "再按一次退出"+getString(R.string.app_name), Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            isCloseApp = true;
            //MobclickAgent.onKillProcess(mContext);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isCloseApp) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

}
