package com.project.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 于德海 on 2018/3/22.
 * package com.project.base
 * email : yudehai0204@163.com
 *
 * @describe
 */

public abstract class BaseActivity extends RxAppCompatActivity implements View.OnClickListener{
    private Unbinder unbinder;
    private MaterialDialog progress_Dialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam(getIntent().getExtras());
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    protected void showLoading(){
        if(progress_Dialog==null){
            progress_Dialog = new MaterialDialog.Builder(this)
                    .content("Loading")
                    .progress(true,100,false)
                    .build();
        }
        progress_Dialog.show();
    }

    protected void dismissLoading(){
        if(progress_Dialog!=null){
            progress_Dialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissLoading();
    }

    protected abstract void initParam(Bundle param);
    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initListener();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getRefWatcher().watch(this);
        if(progress_Dialog!=null){
            progress_Dialog =null;
        }
        if(unbinder!=null){
            unbinder.unbind();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS
                );
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
