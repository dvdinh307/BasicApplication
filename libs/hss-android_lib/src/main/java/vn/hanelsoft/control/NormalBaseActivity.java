package vn.hanelsoft.control;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import vn.hanelsoft.mylibrary.R;

/**
 * Created by dinhdv on 2/1/2018.
 */

public abstract class NormalBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_right_to_left_enter,
                R.anim.anim_right_to_left_leave);
        if (getLayoutId() == 0)
            throw new NullPointerException("Please input your layout here !!");
        setContentView(getLayoutId());
        initViews();
        initEvents();
    }

    protected abstract int getLayoutId();

    protected abstract void initViews();

    protected abstract void initEvents();

}
