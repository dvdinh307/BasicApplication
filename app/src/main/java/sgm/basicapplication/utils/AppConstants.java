package sgm.basicapplication.utils;

/**
 * Created by Hss on 8/19/2015.
 */
public class AppConstants {
    /**
     * @Variable : isTestMode
     * Using with billing.
     * If True : Using code test purchase. Server : Dev.
     * If Fail : Using real purchase. Server : UAT
     */
    public static boolean isTestMode = false;
    public static int CLIENT_ID = 1;
    public static int REQUEST_SUCCESS = 200;
    public static String mUrlResultQuestion = "";

    public static class TEST {
        public static String USER_NAME = "guest@guest.forest";
        public static String PASSWORD = "guestguest";
    }

    public static String getUrlResultQuestion() {
        return mUrlResultQuestion;
    }

    public static void setUrlResultQuestion(String mUrlResultQuestion) {
        AppConstants.mUrlResultQuestion = mUrlResultQuestion;
    }

    public static String getLinkAdmin() {
        return isTestMode ? "http://alexapp.ito.vn/service/" : "http://alexapp.ito.vn/service/";
    }

    public static class STATUS_USER {
        public static final int NORMAL = 1;
        public static final int REQUESTING = 2;
        public static final int DONE = 3;
        public static final int ERROR = 4;
    }

    public enum KEY_INTENT {
        URL_DIAGNOSIS("KEY_URL_DIAGNOSIS"),
        SEARCH_VALUES("SEARCH_VALUES"),
        ID_USER("ID_USER"),
        IS_REGISTER_USER("IS_REGISTER_USER"),
        IS_SKIP_USER("IS_SKIP_USER"),
        LIST_FREE_VIDEO("video/listfree");

        String link = "";

        KEY_INTENT(String values) {
            link = values;
        }

        @Override
        public String toString() {
            return link;
        }
    }

    public enum SERVER_PATH {
        LOGIN("user/login"),
        REGISTER("user/register"),
        SPLASH("token"),
        FORGOT_PASSWORD("user/forgotPassword"),
        TOP("top");

        String link = "";

        SERVER_PATH(String values) {
            link = values;
        }

        @Override
        public String toString() {
            return getLinkAdmin() + link;
        }
    }

    public enum KEY_PARAMS {
        DEVICE_ID("device_id"),
        TOKEN("token"),
        NAME("name"),
        DATA("data"),
        CLIENT_ID("client_id"),
        PASSWORD("password"),
        EMAIL("email"),
        ID_PURCHASE("id_purchase"),
        AUTH_TOKEN("auth_token"),
        TYPE("type"),
        DEVICE_NAME("device_name"),
        OS_VERSION("os_version"),
        STATUS("status"),
        USER_INFO("userInfo"),
        USER_CLIENT_ID("user_client_id"),
        PROGRESS_WATCHES("progress_watches"),
        TOTAL("total"),
        VIEWED("viewed"),
        PREMIUM_REMAIN("premium_remain"),
        TITLE("title"),
        OLD_PASSWORD("old_password"),
        CREATE_AT("created_at"),
        UPDATE_AT("updated_at"),
        PRICE("price"),
        PRICE_CODE("price_code"),
        REMAIN("remain"),
        ID("id"),
        DETAIL("detail"),
        PREMIUM_ID("premiumid"),
        DATE_FROM("datefrom"),
        DATE_TO("dateto"),
        LIST("list"),
        PREMIUM_TO_DATE("premium_to_date"),
        MY_PREMIUM("mypremium"),
        PREMIUM_INFO("premiumInfo"),
        PRICE_ID("price_id"),
        MONTH("month"),
        CURRENT_PRICE("current_price"),
        CURRENT_END_DATE("current_enddate"),
        NEXT_PRICE("next_price"),
        NEXT_START_DATE("next_startdate"),
        MESSAGE("message"),
        PREMIUM_CODE("premium_code"),
        ORDER_NO("order_no"),
        COST("cost"),
        CURRENCY("currency"),
        PURCHASE_INFO("purchase_info"),
        PURCHASE_TOKEN("purchase_token"),
        NEW_APP("newapp"),
        LIST_SUB_CATE("listSubCate"),
        HIDE_NEWS("hide_news"),
        LIST_NEWS("list_news"),
        REGISTER_REQUIRE("register_require"),
        DEVICE_OS("device_os"),
        IS_CURRENT_DEVICE("isCurrent"),
        INFO("info"),
        IMAGE("image"),
        BODY("body"),
        ORDER("order"),
        LINK("link"),
        LIST_CATEGORY("list_category"),
        LIST_BANNER("list_banner"),
        LIST_CAMPAIGN("list_campaign"),
        DESCRIPTION("description"),
        TOP("top"),
        ACCESS_TOKEN("access_token"),
        REGISTER_TYPE("register_type"),
        LOGIN_TYPE("login_type"),
        NEW_PASSWORD("new_password");

        private String mName = "";

        KEY_PARAMS(String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }

    public enum KEY_PREFERENCE {
        AUTH_TOKEN("auth_token"),
        IS_FIRST_RUN("IS_FIRST_RUN"),
        IS_LOGIN("IS_LOGIN"),
        KEY_DEFAULT_QUESTION("Nica_question_");

        private final String mName;

        KEY_PREFERENCE(String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }
}
