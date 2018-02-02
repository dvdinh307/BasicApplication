package sgm.basicapplication.control.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sgm.basicapplication.R;
import sgm.basicapplication.utils.AppConstants;
import sgm.basicapplication.utils.RequestDataUtils;
import sgm.basicapplication.utils.custom.EditTextApp;
import vn.hanelsoft.control.NormalBaseActivity;
import vn.hanelsoft.dialog.DialogUtils;

/**
 * Created by dinhdv on 2/2/2018.
 */

public class ForgotPasswordActivity extends NormalBaseActivity {
    @BindView(R.id.edt_email)
    EditTextApp mEdtEmail;
    @BindView(R.id.tv_error_email)
    TextView mTvErrorEmail;
    private ProgressDialog mPrg;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        // Progress Bar
        mPrg = new ProgressDialog(ForgotPasswordActivity.this);
        mPrg.setMessage(getString(R.string.txt_loading));
        mPrg.setCanceledOnTouchOutside(false);
        mPrg.setCancelable(false);
        mPrg.setMax(100);
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
    }

    @Override
    protected void initEvents() {

    }

    @OnClick({R.id.btn_send, R.id.imv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                String email = mEdtEmail.getText().toString();
                if (checkValidate(email)) {
                    sendRequestForgot(email);
                }
                break;
            case R.id.imv_back:
                finish();
                break;
        }
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
        mEdtEmail.setBackgroundResource(msg.length() > 0 ? R.drawable.bg_edittext_error : R.drawable.bg_edittext);
        mTvErrorEmail.setVisibility(msg.length() > 0 ? View.VISIBLE : View.GONE);
        if (msg.length() > 0) {
            mTvErrorEmail.setText(msg);
        }
    }

    protected void clearError() {
        mEdtEmail.setBackgroundResource(R.drawable.bg_edittext);
        mTvErrorEmail.setVisibility(View.GONE);
    }

    private void sendRequestForgot(final String email) {
        showLoading();
        Map<String, String> params = new HashMap();
        params.put(AppConstants.KEY_PARAMS.CLIENT_ID.toString(), String.valueOf(AppConstants.CLIENT_ID));
        params.put(AppConstants.KEY_PARAMS.EMAIL.toString(), email);
        JSONObject parameters = new JSONObject(params);
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        RequestDataUtils.requestData(Request.Method.POST, ForgotPasswordActivity.this, AppConstants.SERVER_PATH.FORGOT_PASSWORD.toString(), params, new RequestDataUtils.onResult() {
            @Override
            public void onSuccess(JSONObject object, String msg) {
                if (object.length() > 0) {
                    int status = object.optInt(AppConstants.KEY_PARAMS.STATUS.toString(), 1);
                    if (status == AppConstants.REQUEST_SUCCESS) {
                        startActivity(new Intent(ForgotPasswordActivity.this, HomeActivity.class));
                        dismissLoading();
                        finish();
                    } else {
                        dismissLoading();
                        DialogUtils.show(ForgotPasswordActivity.this, getString(R.string.msg_login_error_try_again));
                    }
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    private boolean checkValidate(String email) {
        return checkErrorEmail(email);
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
