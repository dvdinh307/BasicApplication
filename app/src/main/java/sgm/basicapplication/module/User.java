package sgm.basicapplication.module;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;

import org.json.JSONObject;

import me.leolin.shortcutbadger.ShortcutBadger;
import sgm.basicapplication.R;
import sgm.basicapplication.control.activity.LoginActivity;
import sgm.basicapplication.utils.AppConstants;
import sgm.basicapplication.utils.BaseActivity;

/**
 * Created by Tuấn Sơn on 31/7/2017.
 */

public class User {
    int id;
    String email;
    int remainPremium;
    boolean isPremiumUser;
    String userId;
    int status = AppConstants.STATUS_USER.NORMAL;
    boolean isSocial;


    private static User instance = new User();

    public static User getInstance() {
        return instance;
    }

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPremiumUser() {
        return isPremiumUser;
    }

    public void setPremiumUser(boolean premiumUser) {
        isPremiumUser = premiumUser;
    }

    public int getRemainPremium() {
        return remainPremium;
    }

    public void setRemainPremium(int remainPremium) {
        this.remainPremium = remainPremium;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isSocial() {
        return isSocial;
    }

    public void setSocial(boolean social) {
        isSocial = social;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Using if user register when user is skip user.
     * Khi sử dụng hàm này. User đã hoàn thành việc đăng kí || Login để sử dụng app. chuyển vể User bình thường.
     *
     * @return
     */
    public boolean isSkipUser() {
        User user = getInstance().getCurrentUser();
        return user != null && user.getEmail() != null && user.getEmail().length() > 0 && user.getEmail().equalsIgnoreCase(AppConstants.TEST.USER_NAME);
    }

    public static User parse(JSONObject item) {
        User user = new User();
        user.setSocial(false);
        user.setId(item.optInt(AppConstants.KEY_PARAMS.ID.toString()));
        user.setUserId(item.optString(AppConstants.KEY_PARAMS.ID.toString()));
        user.setEmail(item.optString("email"));
        int premiumRemain = item.optInt("premium_remain");
        user.setRemainPremium(premiumRemain);
        if (premiumRemain >= 0)
            user.setPremiumUser(true);
        else user.setPremiumUser(false);
        return user;
    }

    public void logout(Activity activity) {
        AccountManager mAccountManager = AccountManager.get(activity);
        Account[] accounts = mAccountManager.getAccountsByType(activity.getString(R.string.account_type));
        if (accounts.length > 0) {
            if (android.os.Build.VERSION.SDK_INT >= 22) {
                // use new account manager code
                mAccountManager.removeAccount(accounts[0], activity, null, null);
            } else {
                mAccountManager.removeAccount(accounts[0], null, null);
            }

        }
        ShortcutBadger.applyCount(activity, 0);
        User.getInstance().setCurrentUser(null);
        BaseActivity.user = null;
        activity.startActivity(new Intent(activity, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
