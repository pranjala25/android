package books.com.shelfshare.config;

import android.provider.BaseColumns;

/**
 * Created by ppranjal on 11/30/2016.
 */

public final class ShelfShareContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ShelfShareContract() {
    }

    /* Inner class that defines the table contents */
    public static class ShelfEntry implements BaseColumns {
        public static final String USER = "user";
        public static final String TABLE_NAME = "shelf";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_DESC = "desc";
        public static final String COLUMN_NAME_CONDITION = "condition";
    }

    public static String USER_ID ;
}