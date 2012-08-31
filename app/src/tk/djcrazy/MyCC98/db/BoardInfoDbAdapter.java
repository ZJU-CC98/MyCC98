package tk.djcrazy.MyCC98.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BoardInfoDbAdapter {

	public static final String KEY_INDEX_NAME = "index_name";
	public static final String KEY_BOARD_NAME = "board_name";
	public static final String KEY_BOARD_ID = "board_id";

	private static final String TAG = "BoardInfoDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String BOARD_IDFO_DATABASE_CREATE = "create table board_info (_id integer primary key autoincrement, "
			+ " index_name text not null, board_name text not null, board_id text not null);";

	private static final String BOARD_INFO_DATABASE_NAME = "board_info_db";
	private static final String BOARD_INFO_DATABASE_TABLE = "board_info";
	private static final int BOARD_INFO_DATABASE_VERSION = 1;

	private final Context mCtx;
	private final int mVersion;

	private  class DatabaseHelper extends SQLiteOpenHelper {

		// The Android's default system path of your application database.
		private final String DB_PATH = "/data/data/tk.djcrazy.MyCC98/databases/";
		private final String DB_NAME = BOARD_INFO_DATABASE_NAME;
		private final Context myContext;
		private SQLiteDatabase myDataBase;

		DatabaseHelper(Context context, int DBVersion) {
			super(context, BOARD_INFO_DATABASE_NAME, null, DBVersion);
			this.myContext = context;

		}

		public void createDataBase() throws IOException {

			myDataBase = this.getWritableDatabase();
			try {
				copyDataBase();
				if (!checkDataBase()) {
					throw new Error("Error creating database");
				} else {
					Log.d(TAG, "success!");
				}
			} catch (IOException e) {
				Log.d(TAG, e.getMessage());
				throw new Error("Error copying database");
			}
		}

		public SQLiteDatabase openDataBase() throws SQLException {

			// Open the database
			String myPath = DB_PATH + DB_NAME;
			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			return myDataBase;
		}

		public boolean checkDataBase() {

			SQLiteDatabase checkDB = null;

			try {

				String myPath = DB_PATH + DB_NAME;

				checkDB = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READONLY);

			} catch (SQLiteException e) {
				// database does't exist yet.
			}

			if (checkDB != null) {
				checkDB.close();
			}

			return checkDB != null ? true : false;
		}

		private void copyDataBase() throws IOException {

 			InputStream myInput = myContext.getAssets()
					.open("board_info_db.db");
			String outFileName = DB_PATH + DB_NAME;

			OutputStream myOutput = new FileOutputStream(outFileName);

			byte[] buffer = new byte[1024];

			int length;

			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);

			}
			myOutput.flush();
			myOutput.close();
			myInput.close();
		}

		public synchronized void close() {

			if (myDataBase != null)

				myDataBase.close();

			super.close();

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS "+BOARD_INFO_DATABASE_TABLE);
			db.execSQL(BOARD_IDFO_DATABASE_CREATE);
			Log.d(TAG, db.getPath() + " " + db.toString());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE");
			Log.d(TAG, DB_NAME.toString());
			Log.d(TAG, "version:"+newVersion+" "+oldVersion);
			try {
				createDataBase();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public BoardInfoDbAdapter(Context ctx, int Version) {
		this.mCtx = ctx;
		this.mVersion = Version;
	}

	public void createDataBase() throws IOException {
		mDbHelper = new DatabaseHelper(mCtx, mVersion);
		mDbHelper.createDataBase();
	}

	public BoardInfoDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx, mVersion);
		mDb = mDbHelper.openDataBase();
		return this;
	}

	public void closeclose() {
		mDbHelper.close();
	}

	public long createBoard(String indexName, String boardName, String boardID) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_INDEX_NAME, indexName);
		initialValues.put(KEY_BOARD_NAME, boardName);
		initialValues.put(KEY_BOARD_ID, boardID);
		return mDb.insert(BOARD_INFO_DATABASE_TABLE, null, initialValues);
	}

	public Cursor getBoardInfo(String indexName) throws SQLException {

		String sqlString = "select " + KEY_BOARD_NAME + ", " + KEY_BOARD_ID
				+ " from " + BOARD_INFO_DATABASE_TABLE + " where "
				+ KEY_INDEX_NAME + " like " + "'%" + indexName + "%'";
		Log.d(TAG, sqlString);
		Cursor mCursor = mDb.rawQuery(sqlString, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		Log.d(TAG, mCursor.getCount() + "");
		return mCursor;

	}

	public void getAllInfo() {

		Cursor cursor = mDb.query(BOARD_INFO_DATABASE_TABLE, null, null, null,
				null, null, null);
		Log.d(TAG, cursor.getColumnCount() + " " + cursor.getCount());

		StringBuffer sf = new StringBuffer();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			sf.append(cursor.getInt(0)).append(" : ")
					.append(cursor.getString(1)).append(" : ")
					.append(cursor.getString(2)).append(" : ")
					.append(cursor.getInt(3)).append("\n");
			cursor.moveToNext();
		}
		Log.d(TAG, sf.toString());
	}
}
