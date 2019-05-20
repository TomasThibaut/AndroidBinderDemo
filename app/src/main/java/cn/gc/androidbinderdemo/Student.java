package cn.gc.androidbinderdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 宫成 on 2019/5/20 上午10:58.
 */
public class Student implements Parcelable {
    public static String NAME = "THINK";
    private int s_id;
    private String s_name;
    private String s_gender;

    protected Student(Parcel in) {
        s_id = in.readInt();
        s_name = in.readString();
        s_gender = in.readString();
    }

    public Student(int s_id, String s_name, String s_gender) {
        this.s_id = s_id;
        this.s_name = s_name;
        this.s_gender = s_gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(s_id);
        dest.writeString(s_name);
        dest.writeString(s_gender);
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public String toString() {
        return "Student{" +
                "s_id=" + s_id +
                ", s_name='" + s_name + '\'' +
                ", s_gender='" + s_gender + '\'' +
                '}';
    }
}
