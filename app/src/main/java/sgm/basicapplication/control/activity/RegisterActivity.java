package sgm.basicapplication.control.activity;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;
import sgm.basicapplication.R;
import sgm.basicapplication.utils.AppConstants;
import sgm.basicapplication.utils.AppUtils;
import sgm.basicapplication.utils.RequestDataUtils;
import sgm.basicapplication.utils.custom.EditTextApp;
import sgm.basicapplication.utils.custom.TextViewApp;
import vn.hanelsoft.control.NormalBaseActivity;

/**
 * Created by dinhdv on 2/2/2018.
 */

public class RegisterActivity extends NormalBaseActivity {
    @BindView(R.id.edt_email)
    EditTextApp mEdtEmail;
    @BindView(R.id.edt_password)
    EditTextApp mEdtPassword;
    @BindView(R.id.edt_re_password)
    EditTextApp mEdtRePassword;
    @BindView(R.id.rl_main)
    RelativeLayout mRlMain;
    @BindView(R.id.rl_email)
    RelativeLayout mRlEmail;
    @BindView(R.id.rl_password)
    RelativeLayout mRlPassword;
    @BindView(R.id.rl_re_password)
    RelativeLayout mRlRePassword;
    @BindView(R.id.tv_error_re_password)
    TextView mTvErrorRePassword;
    @BindView(R.id.tv_error_password)
    TextView mTvErrorPassword;
    @BindView(R.id.tv_error_email)
    TextView mTvErrorEmail;
    @BindView(R.id.tv_skip)
    TextViewApp mTvSkip;
    private AccountManager mManagerAccount;
    private ProgressDialog mPrg;
    private ProgressDialog mProgressDialog;
    private String email;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        initView();
        mManagerAccount = AccountManager.get(this);
        // Progress Bar
        mPrg = new ProgressDialog(RegisterActivity.this);
        mPrg.setMessage(getString(R.string.txt_loading));
        mPrg.setCanceledOnTouchOutside(false);
        mPrg.setCancelable(false);
        mPrg.setMax(100);
        //Clear shortcut badger.
        ShortcutBadger.applyCount(getApplicationContext(), 0);
        // Using to check visibility of skip function
//        if (!User.getInstance().isSkipUser())
    }

    @Override
    protected void initEvents() {

    }


    private void initView() {
        mRlMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mRlMain.getWindowVisibleDisplayFrame(r);
                int screenHeight = mRlMain.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    mRlMain.animate().translationY(-230f).start();
                } else {
                    mRlMain.animate().translationY(0f).start();
                }
            }
        });

        mEdtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0)
                    clearError();
            }
        });

        mEdtRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0)
                    clearError();
            }
        });

        mEdtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0)
                    clearError();
            }
        });
    }

    @OnClick({R.id.btn_submit, R.id.btn_login, R.id.tv_skip, R.id.tv_privacy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                String email = mEdtEmail.getText().toString();
                String password = mEdtPassword.getText().toString();
                String rePassword = mEdtRePassword.getText().toString();
                if (checkValidate(email, password, rePassword)) {
                    registerAccount(email, password);
                }
                break;
            case R.id.btn_login:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.tv_skip:
                finish();
                break;
            case R.id.tv_privacy:
                startActivity(new Intent(RegisterActivity.this, TermsActivity.class));
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        clearError();
    }

    private void registerAccount(final String email, final String password) {
        showLoading();
        Map<String, String> params = new HashMap<>();
        params.put(AppConstants.KEY_PARAMS.CLIENT_ID.toString(), String.valueOf(AppConstants.CLIENT_ID));
        params.put(AppConstants.KEY_PARAMS.EMAIL.toString(), email);
        params.put(AppConstants.KEY_PARAMS.DEVICE_ID.toString(), AppUtils.getDeviceID(RegisterActivity.this));
        params.put(AppConstants.KEY_PARAMS.PASSWORD.toString(), password);
        RequestDataUtils.requestData(Request.Method.POST, RegisterActivity.this, AppConstants.SERVER_PATH.REGISTER.toString(), params, new RequestDataUtils.onResult() {
            @Override
            public void onSuccess(JSONObject object, String msg) {
                System.out.println(object);
            }

            @Override
            public void onFail() {
                System.out.println("Error");
            }
        });
    }

    private boolean checkValidate(String email, String password, String rePassword) {
        boolean valuesEmail = checkErrorEmail(email);
        boolean valuePassword = checkErrorPassword(password);
        boolean valueRePassword = checkErrorRePassword(password, rePassword);
        return valuesEmail && valuePassword && valueRePassword;
    }

    private boolean checkErrorEmail(String email) {
        if (email.length() == 0) {
            setErrorMsg(getString(R.string.msg_email_empty));
            return false;
        } else if (email.length() > 0 && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setErrorMsg(getString(R.string.msg_email_not_validate));
            return false;
        } else {
            setErrorMsg("");
            return true;
        }
    }

    protected void setErrorMsg(String msg) {
        mRlEmail.setBackgroundResource(msg.length() > 0 ? R.drawable.bg_edittext_error : R.drawable.bg_edittext);
        mTvErrorEmail.setVisibility(msg.length() > 0 ? View.VISIBLE : View.GONE);
        if (msg.length() > 0) {
            mTvErrorEmail.setText(msg);
        }
    }

    private boolean checkErrorPassword(String password) {
        if (password.length() == 0) {
            setErrorMsgPassword(getString(R.string.msg_password_empty));
            return false;
        } else if (password.length() > 0 && password.length() < 6) {
            setErrorMsgPassword(getString(R.string.msg_password_not_enough_length));
            return false;
        } else {
            setErrorMsgPassword("");
            return true;
        }
    }

    protected void setErrorMsgPassword(String msg) {
        mRlPassword.setBackgroundResource(msg.length() > 0 ? R.drawable.bg_edittext_error : R.drawable.bg_edittext);
        mTvErrorPassword.setVisibility(msg.length() > 0 ? View.VISIBLE : View.GONE);
        if (msg.length() > 0) {
            mTvErrorPassword.setText(msg);
        }
    }

    private boolean checkErrorRePassword(String password, String rePassword) {
        if (rePassword.length() == 0) {
            setErrorReMsgPassword(getString(R.string.msg_re_password_empty));
            return false;
        } else if (rePassword.length() > 0 && password.length() < 6) {
            setErrorReMsgPassword(getString(R.string.msg_password_not_enough_length));
            return false;
        } else if (!password.equalsIgnoreCase(rePassword)) {
            setErrorReMsgPassword(getString(R.string.msg_password_not_equal_re_password));
            return false;
        } else {
            setErrorReMsgPassword("");
            return true;
        }
    }

    protected void setErrorReMsgPassword(String msg) {
        mRlRePassword.setBackgroundResource(msg.length() > 0 ? R.drawable.bg_edittext_error : R.drawable.bg_edittext);
        mTvErrorRePassword.setVisibility(msg.length() > 0 ? View.VISIBLE : View.GONE);
        if (msg.length() > 0) {
            mTvErrorRePassword.setText(msg);
        }
    }

    protected void clearError() {
        mRlRePassword.setBackgroundResource(R.drawable.bg_edittext);
        mRlPassword.setBackgroundResource(R.drawable.bg_edittext);
        mRlEmail.setBackgroundResource(R.drawable.bg_edittext);
        mTvErrorRePassword.setVisibility(View.GONE);
        mTvErrorPassword.setVisibility(View.GONE);
        mTvErrorEmail.setVisibility(View.GONE);
    }

    public void showLoading() {
        if (!mPrg.isShowing() && mPrg != null)
            mPrg.show();
    }

    public void dismissLoading() {
        if (mPrg.isShowing())
            mPrg.dismiss();
    }

}
