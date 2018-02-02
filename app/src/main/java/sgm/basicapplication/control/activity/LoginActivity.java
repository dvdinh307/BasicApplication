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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sgm.basicapplication.R;
import sgm.basicapplication.utils.AppConstants;
import sgm.basicapplication.utils.AppUtils;
import sgm.basicapplication.utils.RequestDataUtils;
import sgm.basicapplication.utils.custom.EditTextApp;
import vn.hanelsoft.control.NormalBaseActivity;
import vn.hanelsoft.utils.AccountUtils;
import vn.hanelsoft.utils.PreferenceUtils;

/**
 * Created by dinhdv on 2/1/2018.
 */

public class LoginActivity extends NormalBaseActivity {
    @BindView(R.id.rl_main)
    RelativeLayout mRlMain;
    @BindView(R.id.edt_email)
    EditTextApp mEdtEmail;
    @BindView(R.id.edt_password)
    EditTextApp mEdtPassword;
    @BindView(R.id.tv_error_password)
    TextView mTvErrorPassword;
    @BindView(R.id.tv_error_email)
    TextView mTvErrorEmail;
    @BindView(R.id.rl_email)
    RelativeLayout mRlEmail;
    @BindView(R.id.rl_password)
    RelativeLayout mRlPassword;
    private AccountManager mManagerAccount;
    private ProgressDialog mPrg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        initControls();
        mManagerAccount = AccountManager.get(this);
        // Progress Bar
        mPrg = new ProgressDialog(LoginActivity.this);
        mPrg.setMessage(getString(R.string.txt_loading));
        mPrg.setCanceledOnTouchOutside(false);
        mPrg.setCancelable(false);
        mPrg.setMax(100);
    }

    @Override
    protected void initEvents() {

    }

    @OnClick({R.id.btn_create_account, R.id.btn_login, R.id.tv_forgot_password, R.id.tv_privacy})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create_account:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                break;
            case R.id.btn_login:
                String email = mEdtEmail.getText().toString();
                String password = mEdtPassword.getText().toString();
                if (checkValidate(email, password)) {
                    sendRequestLogin(email, password, "");
                }
                break;
            case R.id.tv_forgot_password:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.tv_privacy:
                startActivity(new Intent(LoginActivity.this, TermsActivity.class));
                break;
        }
    }

    private void initControls() {
        mRlMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mRlMain.getWindowVisibleDisplayFrame(r);
                int screenHeight = mRlMain.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    mRlMain.animate().translationY(-100f).start();
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

    private boolean checkValidate(String email, String password) {
        boolean valuesEmail = checkErrorEmail(email);
        boolean valuePassword = checkErrorPassword(password);
        return valuesEmail && valuePassword;
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

    protected void clearError() {
        mRlPassword.setBackgroundResource(R.drawable.bg_edittext);
        mRlEmail.setBackgroundResource(R.drawable.bg_edittext);
        mTvErrorEmail.setVisibility(View.GONE);
        mTvErrorPassword.setVisibility(View.GONE);
    }

    public void showLoading() {
        if (!mPrg.isShowing() && mPrg != null)
            mPrg.show();
    }

    public void dismissLoading() {
        if (mPrg.isShowing())
            mPrg.dismiss();
    }

    private void sendRequestLogin(final String email, final String password, final String id_purchase) {
        showLoading();
        Map<String, String> params = new HashMap<>();
        params.put(AppConstants.KEY_PARAMS.CLIENT_ID.toString(), String.valueOf(AppConstants.CLIENT_ID));
        params.put(AppConstants.KEY_PARAMS.DEVICE_ID.toString(), AppUtils.getDeviceID(LoginActivity.this));
        params.put(AppConstants.KEY_PARAMS.EMAIL.toString(), email);
        params.put(AppConstants.KEY_PARAMS.PASSWORD.toString(), password);
        if (id_purchase.length() > 0)
            params.put(AppConstants.KEY_PARAMS.ID_PURCHASE.toString(), password);
        JSONObject parameters = new JSONObject(params);
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        RequestDataUtils.requestData(Request.Method.POST, LoginActivity.this, AppConstants.SERVER_PATH.LOGIN.toString(), params, new RequestDataUtils.onResult() {
            @Override
            public void onSuccess(JSONObject object, String msg) {
                if (object.length() > 0) {
                    boolean isSkipUser = false;
                    int status = object.optInt(AppConstants.KEY_PARAMS.STATUS.toString(), 1);
//                    String msg = object.optString(AppConstants.KEY_PARAMS.MESSAGE.toString(), "");
                    String mCurrentCodePurchase = "";
                    if (status == AppConstants.REQUEST_SUCCESS) {
                        try {
                            JSONObject objectData = object.getJSONObject(AppConstants.KEY_PARAMS.DATA.toString());
                            String auth = objectData.optString(AppConstants.KEY_PARAMS.AUTH_TOKEN.toString(), "");
                            if (auth.length() > 0) {
                                PreferenceUtils.getInstance(LoginActivity.this).putString(AppConstants.KEY_PREFERENCE.AUTH_TOKEN.toString(), auth);
                            }
                            // check current user is skip user ?
//                                    isSkipUser = User.getInstance().isSkipUser();
//                                    User user = User.parse(objectData.getJSONObject("info"));
//                                    User.getInstance().setCurrentUser(user);
//                                    JSONObject objectPremiumInfo = objectData.getJSONObject(AppConstants.KEY_PARAMS.PREMIUM_INFO.toString());
//                                    if (objectPremiumInfo.length() > 0) {
//                                        mCurrentCodePurchase = objectPremiumInfo.optString(AppConstants.KEY_PARAMS.PRICE_CODE.toString(), "");
//                                    }
                            AccountUtils.saveAccountInformation(LoginActivity.this, mManagerAccount, email, password);
                            dismissLoading();
                            AppUtils.setIsFirstLogin(LoginActivity.this);
                            /**
                             * Neu a skip user chi chi can finish activity de quay tro lai man hinh truoc do.
                             */
                            if (!isSkipUser)
//                                        startActivity(mCurrentCodePurchase.length() > 0 ? new Intent(LoginActivity.this, TopActivity.class).putExtra(AppConstants.KEY_SEND.KEY_SEND_ID_PURCHASE, mCurrentCodePurchase) : new Intent(LoginActivity.this, TopActivity.class));
                                finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                            else if (status == AppConstants.STATUS_REQUEST.SERVER_MAINTAIN) {
//                                goMaintainScreen(LoginActivity.this, msg);
//                            } else if (status == AppConstants.STATUS_REQUEST.LIMIT_DEVICE) {
//                                dismissLoading();
//                                HSSDialog.show(LoginActivity.this, getString(R.string.msg_error_limit_device));
//                            } else {
//                                dismissLoading();
//                                HSSDialog.show(LoginActivity.this, getString(R.string.msg_login_error_password_id_error));
//                            }
                }
                System.out.println(object);
            }

            @Override
            public void onFail() {

            }
        });
    }
}
