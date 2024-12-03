package lab.insecuredroid.challenges.utils;

import static lab.insecuredroid.challenges.utils.SHA256HashHelper.hashPassword;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "user_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (uid INTEGER PRIMARY KEY AUTOINCREMENT, " + "username TEXT UNIQUE, password TEXT)");
        db.execSQL("INSERT INTO users (username, password) VALUES ('test_user', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f')");
        db.execSQL("INSERT INTO users (username, password) VALUES ('admin', 'de75a1e13642c7462b6332ae18f168a6ade57dcd84766f42e3a1f9650f4c6d01')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean vulnerable_login(String username, String password) {
        boolean exists = false;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            Cursor cursor = db.rawQuery(sql, new String[]{username, hashPassword(password)});
            exists = (cursor.getCount() > 0);
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exists;
    }
}

