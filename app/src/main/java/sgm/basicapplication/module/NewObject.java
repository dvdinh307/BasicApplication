package sgm.basicapplication.module;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import sgm.basicapplication.utils.AppConstants;
import sgm.basicapplication.utils.AppObject;
import vn.hanelsoft.utils.ParserJsonUtils;

/**
 * Created by dinhdv on 2/2/2018.
 */

public class NewObject implements Parcelable, AppObject<NewObject> {
    String id;
    String name;

    public NewObject() {
    }

    protected NewObject(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final Creator<NewObject> CREATOR = new Creator<NewObject>() {
        @Override
        public NewObject createFromParcel(Parcel in) {
            return new NewObject(in);
        }

        @Override
        public NewObject[] newArray(int size) {
            return new NewObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    @Override
    public NewObject parserData(JSONObject object) {
        return (NewObject) ParserJsonUtils.getObject(NewObject.class.getName(), object,
                AppConstants.KEY_PARAMS.ID.toString(),
                AppConstants.KEY_PARAMS.NAME.toString());
    }
}
