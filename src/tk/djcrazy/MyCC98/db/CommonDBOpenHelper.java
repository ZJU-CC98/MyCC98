package tk.djcrazy.MyCC98.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CommonDBOpenHelper extends SQLiteOpenHelper {
    public static final String TAG = "DB";

    public static final String COMMON_DB = "common_db";
    public static final String PM_TABLE = "pm";
    public static final String PM_ID = "pm_id";
    public static final String SENDER = "sender";
    public static final String TOPIC = "topic";
    public static final String PM_TIME = "pm_time";
    public static final String PM_CONTENT = "pm_content";

    public static final String PERSON_TABLE = "person";
    public static final String PERSON_NAME = "name";
    public static final String PERSON_IMG_URL = "person_img_url";
    public static final String PERSON_IMG = "person_img";

    private static final String CREATE_PM_TABLE = "create table " + PM_TABLE
            + "(" + PM_ID + " int primary key, " + SENDER + " varchar(11), "
            + TOPIC + " varchar(51), " + PM_TIME + " char(19), " + PM_CONTENT
            + " varchar(3001))";
    private static final String CREATE_PERSON_TABLE = "create table "
            + PERSON_TABLE + "(" + PERSON_NAME + " varchar(11) primary key, "
            + PERSON_IMG_URL + " varchar(60), " + PERSON_IMG + " blob)";

    public CommonDBOpenHelper(Context context, String name,
            CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_PM_TABLE);
        db.execSQL(CREATE_PERSON_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
