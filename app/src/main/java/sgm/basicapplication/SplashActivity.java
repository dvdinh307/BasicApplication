package sgm.basicapplication;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.firebase.iid.FirebaseInstanceId;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import sgm.basicapplication.control.activity.LoginActivity;
import sgm.basicapplication.utils.AppConstants;
import sgm.basicapplication.utils.AppUtils;
import sgm.basicapplication.utils.RequestDataUtils;
import vn.hanelsoft.control.NormalBaseActivity;
import vn.hanelsoft.utils.AccountUtils;
import vn.hanelsoft.utils.PreferenceUtils;

import static android.R.attr.name;

public class SplashActivity extends NormalBaseActivity {
    @BindView(R.id.tv_name)
    TextView mTvName;

    private AccountManager mManagerAccount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        mTvName.setText("AAAAAA");
        mManagerAccount = AccountManager.get(this);
    }

    @Override
    protected void initEvents() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            Dexter.withActivity(SplashActivity.this)
                    .withPermission(Manifest.permission.READ_PHONE_STATE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            getAccount();
                            sendToken();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            finish();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .onSameThread()
                    .check();
        } else {
            getAccount();
            sendToken();
        }
    }

    private void getAccount() {
        AccountManager manager = AccountManager.get(this);
        Account accountSave = null;
        accountSave = AccountUtils.getAccountUser(SplashActivity.this, manager);
        if (accountSave != null) {
            String name = accountSave.name;
            String password = manager.getPassword(accountSave);
            login(name, password, "");
        } else {
            goLoginScreen();
        }
    }

    private void goLoginScreen() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    protected void sendToken() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        if (refreshedToken != null && refreshedToken.length() > 0)
        sendTokenToServer(refreshedToken != null ? refreshedToken : "");
        Log.e("TOKEN", "Values : " + refreshedToken);
    }

    protected void sendTokenToServer(String token) {
        Map<String, String> params = new HashMap();
        params.put(AppConstants.KEY_PARAMS.CLIENT_ID.toString(), String.valueOf(AppConstants.CLIENT_ID));
        params.put(AppConstants.KEY_PARAMS.DEVICE_ID.toString(), AppUtils.getDeviceID(SplashActivity.this));
        params.put(AppConstants.KEY_PARAMS.TOKEN.toString(), token);
        params.put(AppConstants.KEY_PARAMS.TYPE.toString(), "2");
        params.put(AppConstants.KEY_PARAMS.DEVICE_NAME.toString(), AppUtils.getDeviceName(SplashActivity.this));
        params.put(AppConstants.KEY_PARAMS.OS_VERSION.toString(), AppUtils.getOsVersion());
        String isFirstRun = PreferenceUtils.getInstance(SplashActivity.this).getString(AppConstants.KEY_PREFERENCE.IS_FIRST_RUN.toString(), "");
        if (isFirstRun.length() == 0)
            PreferenceUtils.getInstance(SplashActivity.this).putString(AppConstants.KEY_PREFERENCE.IS_FIRST_RUN.toString(), "has_run");
        params.put(AppConstants.KEY_PARAMS.NEW_APP.toString(), isFirstRun.length() == 0 ? "1" : "2");
        RequestDataUtils.requestData(Request.Method.POST, SplashActivity.this, AppConstants.SERVER_PATH.SPLASH.toString(), params, new RequestDataUtils.onResult() {
            @Override
            public void onSuccess(JSONObject object, String msg) {
                System.out.println(msg);
            }

            @Override
            public void onFail() {
                Log.e("ERROR", "Error");
            }
        });
    }

    protected void login(final String email, final String password, final String id_purchase) {
        Log.e("ACcount", "user-name : " + name + "--- password : " + password);
        Map<String, String> params = new HashMap();
        params.put(AppConstants.KEY_PARAMS.CLIENT_ID.toString(), String.valueOf(AppConstants.CLIENT_ID));
        params.put(AppConstants.KEY_PARAMS.DEVICE_ID.toString(), AppUtils.getDeviceID(SplashActivity.this));
        params.put(AppConstants.KEY_PARAMS.EMAIL.toString(), email);
        params.put(AppConstants.KEY_PARAMS.PASSWORD.toString(), password);
        if (id_purchase.length() > 0)
            params.put(AppConstants.KEY_PARAMS.ID_PURCHASE.toString(), password);
        JSONObject parameters = new JSONObject(params);
        RequestDataUtils.requestData(Request.Method.POST, SplashActivity.this, AppConstants.SERVER_PATH.LOGIN.toString(), params, new RequestDataUtils.onResult() {
            @Override
            public void onSuccess(JSONObject object, String msg) {
                if (object.length() > 0) {
                    int status = object.optInt(AppConstants.KEY_PARAMS.STATUS.toString(), 0);
                    String mCurrentCodePurchase = "";
                    String msgg = object.optString(AppConstants.KEY_PARAMS.MESSAGE.toString(), "");
                    if (status == AppConstants.REQUEST_SUCCESS) {
                        try {
                            JSONObject objectData = object.getJSONObject(AppConstants.KEY_PARAMS.DATA.toString());
                            String auth = objectData.optString(AppConstants.KEY_PARAMS.AUTH_TOKEN.toString(), "");
                            if (auth.length() > 0) {
                                PreferenceUtils.getInstance(SplashActivity.this).putString(AppConstants.KEY_PREFERENCE.AUTH_TOKEN.toString(), auth);
                            }
//                            User user = User.parse(objectData.getJSONObject("info"));
//                            User.getInstance().setCurrentUser(user);
//                            JSONObject objectPremiumInfo = objectData.getJSONObject(AppConstants.KEY_PARAMS.PREMIUM_INFO.toString());
//                            if (objectPremiumInfo.length() > 0) {
//                                mCurrentCodePurchase = objectPremiumInfo.optString(AppConstants.KEY_PARAMS.PRICE_CODE.toString(), "");
//                            }
//                            if (mIdNews.length() > 0) {
//                                // Go from new notification.
//                                NewsItem item = new NewsItem();
//                                item.setId(Integer.valueOf(mIdNews));
//                                item.setmIsFromNotification(true);
//                                startActivity(new Intent(SplashActivity.this, NewDetailActivity.class).putExtra(AppConstants.KEY_SEND.KEY_SEND_NEW_OBJECT, item));
//                                finish();
//                            } else if (mIdCampaign.length() > 0) {
//                                startActivity(new Intent(SplashActivity.this, TopActivity.class)
//                                        .putExtra(AppConstants.KEY_SEND.KEY_ID_CAMPAIGN, mIdCampaign)
//                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                            } else {
//                                // Test send id purchase to  top. Need remove params when done.
//                                startActivity(mCurrentCodePurchase.length() > 0 ? new Intent(SplashActivity.this, TopActivity.class)
//                                        .putExtra(AppConstants.KEY_SEND.KEY_SEND_ID_PURCHASE, mCurrentCodePurchase).
//                                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                                        : new Intent(SplashActivity.this, TopActivity.class)
//                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
//                    else if (status == AppConstants.STATUS_REQUEST.SERVER_MAINTAIN) {
//                        goMaintainScreen(SplashActivity.this, msg);
//                    } else if (status == AppConstants.STATUS_REQUEST.TOKEN_EXPIRED) {
//                        HSSDialog.show(SplashActivity.this, getString(R.string.msg_session_expire), getString(R.string.txt_ok), new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                HSSDialog.dismissDialog();
//                                sendRequestLoginSkipUser(AppConstants.TEST.USER_NAME, AppConstants.TEST.PASSWORD);
//                            }
//                        });
//                    } else {
//                        HSSDialog.show(SplashActivity.this, getString(R.string.msg_request_error_try_again), new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                finish();
//                            }
//                        });
//                    }
                }
            }

            @Override
            public void onFail() {

            }
        });
    }
}
