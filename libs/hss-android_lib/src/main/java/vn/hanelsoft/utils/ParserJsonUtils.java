package vn.hanelsoft.utils;

import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by dinhdv on 2/2/2018.
 */

public class ParserJsonUtils {

    /**
     *
     * @param name : Class name need return : Example A.class.getName();
     * @param jsonObject : Input json Object.
     * @param param : array key you want return.
     * @return Object.
     */
    public static Object getObject(String name , JSONObject jsonObject, String... param) {
        Object object = null;
        try {
            Class c = Class.forName(name);
            object = c.newInstance();
            Class<?> clazz = object.getClass();
            // returns the array of Field objects
            if (null != clazz && param != null && param.length > 0) {
                for (int i = 0; i < param.length; i++) {
                    Field field = clazz.getDeclaredField(param[i]);
                    field.setAccessible(true);
                    field.set(object, jsonObject.optString(param[i]));
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return object;
    }
}
