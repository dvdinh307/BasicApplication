package sgm.basicapplication.utils;

import org.json.JSONObject;

/**
 * Created by dinhdv on 2/2/2018.
 */

public interface AppObject<T> {
    T parserData(JSONObject object);
}
