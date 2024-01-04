package com.example.proyectohuella;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperHuella extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Fingerprint.db";
    private static final String TABLE_NAME = "FingerprintTable";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FINGERPRINT_ID = "fingerprint_id";

    public DBHelperHuella(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_FINGERPRINT_ID + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertFingerprint(String fingerprintId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FINGERPRINT_ID, fingerprintId);
        return db.insert(TABLE_NAME, null, contentValues);
    }
    public Boolean isFingerprintExist(String fingerprintId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_FINGERPRINT_ID + " = ?", new String[]{fingerprintId});
        return cursor.getCount() > 0;
    }
}
