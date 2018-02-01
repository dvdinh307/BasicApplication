package vn.hanelsoft.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import java.util.HashMap;
import java.util.Map;

import vn.hanelsoft.view.LoadingView;

public class LoadingDialog extends Dialog {
    private Context context;
    private static Map<Context, LoadingDialog> cache = new HashMap<>();

    private LoadingDialog(Context context) {
        super(context);
        init(context);
    }

    public static LoadingDialog getDialog(Context context) {
        LoadingDialog dialog = cache.get(context);
        if (dialog == null) {
            dialog = new LoadingDialog(context);
            cache.put(context, dialog);
        }
        return dialog;
    }

    public static Map<Context, LoadingDialog> getCache() {
        return cache;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        cache.remove(context);
    }

    private void init(Context context) {
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(new LoadingView(context));

//        LinearLayout view = new LinearLayout(context);
//        view.setGravity(Gravity.CENTER);
//        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        view.setLayoutParams(params);
//        ProgressBar progress = new ProgressBar(context);
//        progress.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.prg_loading));
//        setContentView(progress);
    }
}
