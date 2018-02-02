package sgm.basicapplication.utils;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sgm.basicapplication.R;
import sgm.basicapplication.control.activity.LoginActivity;
import sgm.basicapplication.control.activity.RegisterActivity;
import sgm.basicapplication.control.activity.TermsActivity;
import sgm.basicapplication.modul.User;
import sgm.basicapplication.utils.custom.TextViewApp;
import vn.hanelsoft.utils.AccountUtils;
import vn.hanelsoft.utils.NetworkUtils;
import vn.hanelsoft.utils.PreferenceUtils;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    DrawerLayout drawerLayout;
    FrameLayout layoutContentContainer;
    RelativeLayout layoutContainer;
    ImageView btnMenu, btnShop;
    ImageView btnBack;
    protected TextView tvTitle;
    protected ImageView ivLogo;
    TextViewApp menuHome, menuMyLibrary, menuProfile, menuHelp, menuTerm, menuSpcifiedTransaction, mTvDeviceManager, mTvLogin;
    TextViewApp mTvPurchaseList, mTvHistory, mTvFavoriteList, mTvVersion, mTvPrivacy , mTvSetting;
    protected ActionBarDrawerToggle mDrawerToggle;
    Activity activity;
    public static Point screenSize = null;
    public static User user;
    private LinearLayout mLlExpand;
    private boolean isShowExpand = false;
    private ImageView mImvArrow;
    private ProgressDialog mPrg;
    private AccountManager mManagerAccount;
    public Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_right_to_left_enter,
                R.anim.anim_right_to_left_leave);
        setContentView(R.layout.activity_base);

        if (screenSize == null) {
            screenSize = new Point();
            getWindowManager().getDefaultDisplay().getSize(screenSize);
        }
        user = User.getInstance().getCurrentUser();
        activity = this;
        isShowExpand = false;
        inflateView();
        // Progress Bar
        mPrg = new ProgressDialog(activity);
        mPrg.setMessage(getString(R.string.txt_loading));
        mPrg.setCanceledOnTouchOutside(false);
        mPrg.setCancelable(false);
        mPrg.setMax(100);
        mManagerAccount = AccountManager.get(this);
    }

    protected int getLayoutId() {
        return 0;
    }


    protected void inflateView() {
        initView();
        if (getLayoutId() > 0)
            View.inflate(activity, getLayoutId(), layoutContentContainer);
        initEvent();
    }

    protected void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        layoutContentContainer = (FrameLayout) findViewById(R.id.container);
        layoutContainer = (RelativeLayout) findViewById(R.id.layout_container);
        btnMenu = (ImageView) findViewById(R.id.btn_menu);
        btnShop = (ImageView) findViewById(R.id.btn_shop);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        tvTitle = (TextView) findViewById(R.id.tv_title_screen);
        ivLogo = (ImageView) findViewById(R.id.iv_logo);

        menuHome = (TextViewApp) findViewById(R.id.tv_home);
        menuMyLibrary = (TextViewApp) findViewById(R.id.tv_library);
        menuProfile = (TextViewApp) findViewById(R.id.tv_profile);
        menuHelp = (TextViewApp) findViewById(R.id.tv_help);
        menuTerm = (TextViewApp) findViewById(R.id.tv_term);
        menuSpcifiedTransaction = (TextViewApp) findViewById(R.id.tv_specified_transaction);

        mLlExpand = (LinearLayout) findViewById(R.id.ll_expand_sub);
        mTvPurchaseList = (TextViewApp) findViewById(R.id.tv_purchase_list);
        mTvHistory = (TextViewApp) findViewById(R.id.tv_purchase_history);
        mTvFavoriteList = (TextViewApp) findViewById(R.id.tv_favorite_list);
        mImvArrow = (ImageView) findViewById(R.id.imv_arrow_down);
        mTvVersion = (TextViewApp) findViewById(R.id.tv_version);
        mTvDeviceManager = (TextViewApp) findViewById(R.id.tv_device_manager);
        mTvLogin = (TextViewApp) findViewById(R.id.tv_login);
        mTvPrivacy = (TextViewApp) findViewById(R.id.tv_privacy);
        mTvSetting = (TextViewApp) findViewById(R.id.tv_setting);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            mTvVersion.setText("version " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void expandSubViewPurchase() {
        if (isShowExpand) {
            AppUtils.collapse(mLlExpand);
        } else {
            AppUtils.expand(mLlExpand);
        }
//        mTvVersion.animate().translationY(isShowExpand ? 0 : 120);
        mImvArrow.setRotation(isShowExpand ? 0 : 180);
        isShowExpand = !isShowExpand;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mTvDeviceManager.setVisibility(user.isSkipUser() ? View.GONE : View.VISIBLE);
        if (user != null)
            mTvLogin.setVisibility(user.isSkipUser() ? View.VISIBLE : View.GONE);
    }

    protected void setupTitleScreen(String title) {
        ivLogo.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        btnBack.setVisibility(View.VISIBLE);
    }

    protected HashMap<String, String> getAuthHeader() {
        HashMap<String, String> header = new HashMap<>();
        String auth = PreferenceUtils.getInstance().getString(AppConstants.KEY_PREFERENCE.AUTH_TOKEN.toString(), "");
        header.put("Authorization", auth);
        return header;
    }

    private void initEvent() {
        btnMenu.setOnClickListener(this);
        btnShop.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        ivLogo.setOnClickListener(this);

        menuHome.setOnClickListener(menu);
        menuMyLibrary.setOnClickListener(menu);
        menuProfile.setOnClickListener(menu);
        menuHelp.setOnClickListener(menu);
        menuTerm.setOnClickListener(menu);
        mTvDeviceManager.setOnClickListener(menu);
        menuSpcifiedTransaction.setOnClickListener(menu);
        mTvPurchaseList.setOnClickListener(menu);
        mTvHistory.setOnClickListener(menu);
        mTvFavoriteList.setOnClickListener(menu);
        mTvLogin.setOnClickListener(menu);
        mTvPrivacy.setOnClickListener(menu);
        mTvSetting.setOnClickListener(menu);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                layoutContainer.setTranslationX(-slideOffset * drawerView.getWidth());
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }
        };
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_menu:
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                else drawerLayout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.iv_logo:
                if (activity.getClass().getName().equals(TopActivity.class.getName()))
                    return;
                else
                    startActivity(new Intent(activity, TopActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
            case R.id.btn_shop:
                if (activity.getClass().getName().equals(TermsActivity.class.getName()))
                    return;
                startActivity(new Intent(activity, ShopContentActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }

    View.OnClickListener menu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_home:
                    closeRightMenu();
//                    if (activity.getClass().getName().equals(TopActivity.class.getName()))
//                        return;
                    /**
                     * Nga yeu cau load lai khi click vao.
                     */
                    startActivity(new Intent(activity, TopActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    break;
                case R.id.tv_library:
                    expandSubViewPurchase();
//                    if (activity.getClass().getName().equals(MyLibraryActivity.class.getName()))
//                        return;
//                    startActivity(new Intent(activity, MyLibraryActivity.class));
                    break;
                case R.id.tv_profile:
                    closeRightMenu();
                    if (activity.getClass().getName().equals(ProfileActivity.class.getName()))
                        return;
//                    startActivity(new Intent(activity, ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    startActivity(new Intent(activity, TeacherProfileCardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
                case R.id.tv_term:
                    closeRightMenu();
                    if (activity.getClass().getName().equals(PolicyActivity.class.getName()))
                        return;
                    startActivity(new Intent(activity, PolicyActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
                case R.id.tv_help:
                    closeRightMenu();
                    if (activity.getClass().getName().equals(PaidMemberGuiderActivity.class.getName()))
                        return;
                    startActivity(new Intent(activity, PaidMemberGuiderActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(PaidMemberGuiderActivity.KEY_SHOW_BACK, false));
                    break;
                case R.id.tv_specified_transaction:
                    closeRightMenu();
                    if (activity.getClass().getName().equals(TermsActivity.class.getName()))
                        return;
                    startActivity(new Intent(activity, TermsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
                case R.id.tv_purchase_list:
                    closeRightMenu();
                    if (activity.getClass().getName().equals(MyLibraryActivity.class.getName())) {
                        sendBroadcast(new Intent(AppConstants.BROAD_CAST.CHANGE_TAB).putExtra(AppConstants.KEY_SEND.KEY_SEND_TAB, 0));
                        return;
                    }
                    startActivity(new Intent(activity, MyLibraryActivity.class)
                            .putExtra(KeyParser.KEY.DATA.toString(), 0)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
                case R.id.tv_purchase_history:
                    closeRightMenu();
                    if (activity.getClass().getName().equals(MyLibraryActivity.class.getName())) {
                        sendBroadcast(new Intent(AppConstants.BROAD_CAST.CHANGE_TAB).putExtra(AppConstants.KEY_SEND.KEY_SEND_TAB, 1));
                        return;
                    }
                    startActivity(new Intent(activity, MyLibraryActivity.class)
                            .putExtra(KeyParser.KEY.DATA.toString(), 1)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
                case R.id.tv_favorite_list:
                    closeRightMenu();
                    if (activity.getClass().getName().equals(MyLibraryActivity.class.getName())) {
                        sendBroadcast(new Intent(AppConstants.BROAD_CAST.CHANGE_TAB).putExtra(AppConstants.KEY_SEND.KEY_SEND_TAB, 2));
                        return;
                    }
                    startActivity(new Intent(activity, MyLibraryActivity.class)
                            .putExtra(KeyParser.KEY.DATA.toString(), 2)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
                case R.id.tv_device_manager:
                    closeRightMenu();
                    if (activity.getClass().getName().equals(ManagerDeviceActivity.class.getName())) {
                        return;
                    }
                    startActivity(new Intent(activity, ManagerDeviceActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
                case R.id.tv_login:
                    closeRightMenu();
                    startActivity(new Intent(activity, LoginActivity.class));
                    break;
                case R.id.tv_privacy:
                    closeRightMenu();
                    if (activity.getClass().getName().equals(JackTermsActivity.class.getName()))
                        return;
                    startActivity(new Intent(activity, JackTermsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
                case R.id.tv_setting:
                    closeRightMenu();
                    if (activity.getClass().getName().equals(SettingActivity.class.getName()))
                        return;
                    startActivity(new Intent(activity, SettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    break;
            }
        }
    };

    protected void closeRightMenu() {
        drawerLayout.closeDrawer(Gravity.RIGHT);
        toggleExpand();
    }

    private void toggleExpand() {
        if (mLlExpand.getVisibility() == View.VISIBLE) {
            mImvArrow.setRotation(0);
        }
        isShowExpand = false;
        AppUtils.collapse(mLlExpand);
    }

    public void attachFragment(Fragment fragment, @IdRes int containerViewID, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewID, fragment);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    protected View getFooterCopyRight() {
        View view = LayoutInflater.from(activity).inflate(R.layout.footer_copy_right, null, false);
        return view;
    }

    private boolean isBackPressed;

    @Override
    public void onBackPressed() {
        if (!isTaskRoot()) {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_left_to_right_enter, R.anim.anim_left_to_right_leave);
            return;
        }
        if (isBackPressed) {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_left_to_right_enter, R.anim.anim_left_to_right_leave);
        } else {
            Toast.makeText(this, R.string.back_again_to_exit,
                    Toast.LENGTH_SHORT).show();
            isBackPressed = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBackPressed = false;
                }
            }, 2000);
        }
    }

    public void goMaintainScreen(Activity activity, String msg) {
        startActivity(new Intent(activity, MaintainActivity.class).putExtra(AppConstants.KEY_SEND.KEY_MSG_MAINTAIN, msg).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    public void goRegisterScreenUserFree() {
        startActivity(new Intent(activity, RegisterActivity.class).putExtra(AppConstants.KEY_INTENT.IS_REGISTER_USER.toString(), true));
    }

    public void sessionExpire() {
        HSSDialog.show(activity, getString(R.string.msg_session_expire), "Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestLoginSkipUser(AppConstants.TEST.USER_NAME, AppConstants.TEST.PASSWORD);
            }
        });
    }

    private void sendRequestLoginSkipUser(final String email, final String password) {
        showLoading();
        Map<String, String> params = new HashMap<>();
        params.put(AppConstants.KEY_PARAMS.CLIENT_ID.toString(), String.valueOf(AppConstants.CLIENT_ID));
        params.put(AppConstants.KEY_PARAMS.DEVICE_ID.toString(), AppUtils.getDeviceID(activity));
        params.put(AppConstants.KEY_PARAMS.EMAIL.toString(), email);
        params.put(AppConstants.KEY_PARAMS.PASSWORD.toString(), password);
        JSONObject parameters = new JSONObject(params);
        HashMap<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConstants.SERVER_PATH.LOGIN.toString(), parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.length() > 0) {
                            int status = response.optInt(AppConstants.KEY_PARAMS.STATUS.toString(), 1);
                            String msg = response.optString(AppConstants.KEY_PARAMS.MESSAGE.toString(), "");
                            if (status == AppConstants.REQUEST_SUCCESS) {
                                try {
                                    JSONObject objectData = response.getJSONObject(AppConstants.KEY_PARAMS.DATA.toString());
                                    String auth = objectData.optString(AppConstants.KEY_PARAMS.AUTH_TOKEN.toString(), "");
                                    if (auth.length() > 0) {
                                        HSSPreference.getInstance(activity).putString(AppConstants.KEY_PREFERENCE.AUTH_TOKEN.toString(), auth);
                                    }
                                    User user = User.parse(objectData.getJSONObject("info"));
                                    User.getInstance().setCurrentUser(user);
                                    AccountUtils.saveAccountInformation(activity, mManagerAccount, email, password);
                                    dismissLoading();
                                    startActivity(new Intent(activity, TopActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else if (status == AppConstants.STATUS_REQUEST.SERVER_MAINTAIN) {
                                goMaintainScreen(activity, msg);
                            } else {
                                dismissLoading();
                                HSSDialog.show(activity, getString(R.string.msg_login_error_password_id_error));
                            }
                        }
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissLoading();
                NetworkUtils.showDialogError(activity, error);
            }
        });
        request.setHeaders(header);
        ForestApplication.getInstance().addToRequestQueue(request);
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
