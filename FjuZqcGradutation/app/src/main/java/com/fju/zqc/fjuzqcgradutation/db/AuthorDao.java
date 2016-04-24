package com.fju.zqc.fjuzqcgradutation.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2015/12/5.
 */
public class AuthorDao {
    private AReadDbOpenHelp aReadDbOpenHelp;
    public final static String AUTHOR_TABLE_NAME="_author_tb";
    public final static String AUTHOR_NAME="author_name";

    public AuthorDao(Context context) {
       aReadDbOpenHelp=AReadDbOpenHelp.getInstance(context);
    }

    /**
     * 插入作者
     * @param authorName
     * @return
     */
    public long insertAuthor(String authorName){
        SQLiteDatabase db=aReadDbOpenHelp.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(AUTHOR_NAME,authorName);
        long insertId=db.insert(AUTHOR_TABLE_NAME,null,values);
        db.close();
        aReadDbOpenHelp.closeDB();
        return insertId;
    }

    /**
     * 获取所有作者的名字
     * @return
     */
   public List<String> findAllAuthor(){
       SQLiteDatabase db=aReadDbOpenHelp.getWritableDatabase();
       List<String> listAuthor=new ArrayList<>();
       Cursor cursor=db.query(AUTHOR_TABLE_NAME,null,null,null,null,null,null,null);
       while(cursor.moveToNext()){
           listAuthor.add(cursor.getString(cursor.getColumnIndex(AUTHOR_NAME)));
       }
       db.close();
       aReadDbOpenHelp.closeDB();
       return  listAuthor;
   }

    /**
     * 删除所以的作者
     * @return
     */
   public int deleteAuthor(){
       SQLiteDatabase db=aReadDbOpenHelp.getWritableDatabase();
       int deleteNum=db.delete(AUTHOR_TABLE_NAME,null,null);
       db.close();
       return deleteNum;
   }
}
