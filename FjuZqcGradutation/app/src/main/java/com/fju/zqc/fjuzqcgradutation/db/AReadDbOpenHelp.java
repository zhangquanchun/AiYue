package com.fju.zqc.fjuzqcgradutation.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhang on 2015/12/5.
 */
public class AReadDbOpenHelp extends SQLiteOpenHelper {

    private final static int DB_VERSION=3;
   private static AReadDbOpenHelp instance;
    private static final String ARTICLE_AUTHOR="CREATE TABLE "
            + AuthorDao.AUTHOR_TABLE_NAME+"("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            +AuthorDao.AUTHOR_NAME + " TEXT)";

    private AReadDbOpenHelp(Context context) {
        super(context,getDbName(), null, DB_VERSION);
    }
    public static AReadDbOpenHelp getInstance(Context context){
        if(instance==null){
            instance=new AReadDbOpenHelp(context);
        }
        return instance;
    }

    private static String getDbName(){
        return  "ARead.db";
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ARTICLE_AUTHOR);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }
}
