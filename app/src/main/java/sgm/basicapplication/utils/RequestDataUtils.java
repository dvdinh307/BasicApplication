package sgm.basicapplication.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import sgm.basicapplication.AppApplication;
import vn.hanelsoft.dialog.DialogUtils;
import vn.hanelsoft.utils.NetworkUtils;

/**
 * Created by dinhdv on 11/14/2017.
 */

public class RequestDataUtils {
    /**
     * Using request data from server
     *
     * @param activity
     * @param params
     * @param action
     */
    public static void requestData(int method, final Activity activity, String url, final Map<String, String> params, final onResult action) {
        Log.e("SERVICE", "LINK :" + params.toString());
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    try {
                        JSONObject objectResponse = new JSONObject(response);
                        int status = objectResponse.optInt(AppConstants.KEY_PARAMS.STATUS.toString(), 1);
                        String msg = objectResponse.optString(AppConstants.KEY_PARAMS.MESSAGE.toString());
                        if (status == AppConstants.REQUEST_SUCCESS) {
                            try {
                                JSONObject objectData = objectResponse.getJSONObject(AppConstants.KEY_PARAMS.DATA.toString());
                                if (action != null) {
                                    action.onSuccess(objectData, msg);
                                }
                            } catch (JSONException e) {
                                if (action != null)
                                    action.onFail();
                                e.printStackTrace();
                            }
                        } else {
                            if (action != null)
                                action.onFail();
                            DialogUtils.show(activity, msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (action != null)
                        action.onFail();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (action != null)
                            action.onFail();
                        NetworkUtils.showDialogError(activity, error, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogUtils.dismissDialog();
                            }
                        });
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        AppApplication.getInstance().addToRequestQueue(request);
    }

    public interface onResult {
        void onSuccess(JSONObject object, String msg);

        void onFail();
    }
}
