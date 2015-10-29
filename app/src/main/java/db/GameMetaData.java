package db;

import android.provider.BaseColumns;

/**
 * Created by zhangjb on 2015/10/26.
 */
public class GameMetaData {

    private GameMetaData() {
    };

    //定义表 和 列字段名
    public static abstract class GamePlay implements BaseColumns {
        public static final String TABLE_NAME = "player_table";
        public static final String PLAYER = "player";
        public static final String SCORE = "score";
        public static final String LEVEL = "level";
    }
}
