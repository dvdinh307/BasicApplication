package sgm.basicapplication.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.leolin.shortcutbadger.ShortcutBadger;
import sgm.basicapplication.R;
import sgm.basicapplication.utils.view.span.TopAlignSuperscriptSpan;
import vn.hanelsoft.utils.PreferenceUtils;


/**
 * Created by dinhdv on 7/20/2017.
 */

public class AppUtils {
    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    /**
     * Get device name.
     *
     * @param context
     * @return
     */
    public static String getDeviceName(Context context) {
        return android.os.Build.MODEL;
    }

    /**
     * Get android version.
     *
     * @return
     */
    public static String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * Get device id of this phone.
     *
     * @param context
     * @return
     */
    public static String getDeviceID(Context context) {
        TelephonyManager TM = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        return TM.getDeviceId();
    }

    public static void setNumberNotification(Context context, int number) {
        ShortcutBadger.applyCount(context, number);
    }

    public static boolean checkPermissionPhoneState(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_PHONE_STATE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    public static void expand(final View v) {
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    /**
     * Get time in milisecond to format yyyy-MM-dd
     *
     * @param milliSeconds
     * @param dateFormat
     * @return
     */
    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {
        Date parsed = null;
        String outputDate = "";
        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());
        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    public static void createNewViewTextView(final TextView tv, final int maxLine, final String expandText) {
        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine <= 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                } else if (tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                }
            }
        });
    }

    public static void makeTextViewResizable(final Context context, final TextView tv, final String textContent, final int maxLine, final String expandText, final boolean isShowNew) {
        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                String dot = context.getString(R.string.txt_dot);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " ";
                    if (!isShowNew)
                        tv.setText(getNormalNewText(text, expandText, context), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() > maxLine) {
                    // Kiem tra truong hop tu 3 dong tro len
                    int lengthInLine = tv.getLayout().getLineEnd(1);
                    String content = tv.getText().toString();
                    String subString = content.substring(0, lengthInLine);
                    if (!isShowNew) {
                        String result = subString.substring(4, subString.length() - dot.length() - 1).trim() + dot;
                        SpannableStringBuilder builder = getNormalNewText(result, expandText, context);
                        tv.setText(builder, TextView.BufferType.SPANNABLE);
                    } else {
                        String result = subString.substring(dot.length(), subString.length() - 1).trim() + dot;
                        SpannableStringBuilder builder = getNormalNewText(result, "", context);
                        tv.setText(builder, TextView.BufferType.SPANNABLE);
                    }
                } else {
                    String content = tv.getText().toString();
                    if (!isShowNew) {
                        String result = content.substring(4, content.length());
                        SpannableStringBuilder builder = getNormalNewText(result, expandText, context);
                        tv.setText(builder, TextView.BufferType.SPANNABLE);
                    } else {
//                        String result = content.substring(dot.length(), content.length());
                        tv.setText(textContent);
                    }
                }
            }
        });
    }


    public static SpannableStringBuilder getNormalNewText(String text, String lastText, Context context) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString str2 = new SpannableString(text);
        str2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str2.length(), 0);
        builder.append(str2);
        /**
         * Add NEW TO LAST.
         */
        builder.append(getSpanndNew(lastText));
        return builder;
    }

    /**
     * Add new text to first.
     *
     * @param text
     * @param lastText
     * @param context
     * @return
     */
    public static SpannableStringBuilder getAddNewTextToFirst(String text, String lastText, Context context) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString str2 = new SpannableString(text);
        str2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str2.length(), 0);
        // Add new
        builder.append(getSpanndNew(lastText));
        //Add content
        builder.append(str2);
        return builder;
    }


    /**
     * Get width of spanned.
     *
     * @param sb
     * @param context
     * @return
     */
    public static Float getWidthSpannable(SpannableStringBuilder sb, Context context) {
        TextView tmp = new TextView(context);
        tmp.setText(sb, TextView.BufferType.SPANNABLE);
        Float dime = tmp.getPaint().measureText(sb, 0, sb.length());
        return dime;
    }

    /**
     * Create NEW text spanned.
     *
     * @param lastText
     * @return
     */
    public static SpannableStringBuilder getSpanndNew(String lastText) {
        SpannableStringBuilder sb = new SpannableStringBuilder(lastText);
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);
        StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        sb.setSpan(fcs, 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bss, 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        sb.setSpan(new RelativeSizeSpan(0.5f), 0, sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//resize size
        sb.setSpan(new TopAlignSuperscriptSpan((float) 0.45), 0, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    public static void setIsFirstLogin(Context context) {
        PreferenceUtils.getInstance(context).putString(AppConstants.KEY_PREFERENCE.IS_LOGIN.toString(), "login");
    }
}
