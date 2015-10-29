package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import entity.GamePlayer;

/**
 * Created by zhangjb on 2015/10/26.
 */
public class DatabaseAdapter {
    private DatabaseHelper dbHelper = null;

    public DatabaseAdapter(Context context) {
        dbHelper = new DatabaseHelper(context);
    }


    //添加操作
    public void add(GamePlayer gamePlayer) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GameMetaData.GamePlay.PLAYER, gamePlayer.getPlayer());
        values.put(GameMetaData.GamePlay.SCORE, gamePlayer.getScore());
        values.put(GameMetaData.GamePlay.LEVEL, gamePlayer.getLevel());

        db.insert(GameMetaData.GamePlay.TABLE_NAME, null, values);
        db.close();
    }

    //删除操作
    public void delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = GameMetaData.GamePlay._ID + "=?";
        String[] whereArgs = {String.valueOf(id)};

        db.delete(GameMetaData.GamePlay.TABLE_NAME, whereClause, whereArgs);
        db.close();
    }


    //更新操作
    public void update(GamePlayer gamePlayer) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GameMetaData.GamePlay.PLAYER, gamePlayer.getPlayer());

        String whereClause = GameMetaData.GamePlay._ID + "=?";
        String[] whereArgs = {String.valueOf(gamePlayer.getId())};
        db.update(GameMetaData.GamePlay.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    //查询
    public GamePlayer findById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        Cursor cursor = db.query(true, GameMetaData.GamePlay.TABLE_NAME, null, GameMetaData.GamePlay._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        GamePlayer player = null;
        if (cursor.moveToNext()) {
            player = new GamePlayer();

            player.setPlayer(cursor.getString(cursor.getColumnIndexOrThrow(GameMetaData.GamePlay.PLAYER)));
            player.setId(cursor.getInt(cursor.getColumnIndexOrThrow(GameMetaData.GamePlay._ID)));
            player.setLevel(cursor.getInt(cursor.getColumnIndexOrThrow(GameMetaData.GamePlay.LEVEL)));
            player.setScore(cursor.getInt(cursor.getColumnIndexOrThrow(GameMetaData.GamePlay.SCORE)));
        }
        cursor.close();
        db.close();
        return player;
    }


    public ArrayList<GamePlayer> findAll() {

        String sql = "select _id,player,score,level from player_table order by score desc";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);


        ArrayList<GamePlayer> gamePlayers = new ArrayList<>();
        GamePlayer gamePlayer = null;

        while (c.moveToNext()) {
            gamePlayer = new GamePlayer();
            gamePlayer.setPlayer(c.getString(c.getColumnIndexOrThrow(GameMetaData.GamePlay.PLAYER)));
            gamePlayer.setId(c.getInt(c.getColumnIndexOrThrow(GameMetaData.GamePlay._ID)));
            gamePlayer.setScore(c.getInt(c.getColumnIndexOrThrow(GameMetaData.GamePlay.SCORE)));
            gamePlayer.setLevel(c.getInt(c.getColumnIndexOrThrow(GameMetaData.GamePlay.LEVEL)));

            gamePlayers.add(gamePlayer);
        }
        c.close();
        db.close();

        return gamePlayers;
    }


    //查询总记录数
    public int getCount() {
        int count = 0;
        String sql = "select count(_id) from player_table";
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(sql, null);

        c.moveToFirst();
        count = c.getInt(0);//取第一条
        c.close();
        db.close();
        return count;
    }
}
