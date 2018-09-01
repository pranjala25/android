package books.com.shelfshare.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import books.com.shelfshare.MyBooks;

/**
 * Created by ppranjal on 11/30/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "ShelfShare";
    private static String TABLE = "Shelf";
    private static String DB_PATH = "/data/data/books.com.shelfshare/databases/"; ;

    private SQLiteDatabase myDataBase;
    private static final String TAG = MyBooks.class.getSimpleName();
    private final Context myContext;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ShelfShareContract.ShelfEntry.TABLE_NAME + " (" +
                    ShelfShareContract.ShelfEntry._ID + " INTEGER PRIMARY KEY," +
                    ShelfShareContract.ShelfEntry.USER + TEXT_TYPE + COMMA_SEP +
                    ShelfShareContract.ShelfEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    ShelfShareContract.ShelfEntry.COLUMN_NAME_AUTHOR + TEXT_TYPE + COMMA_SEP +
                    ShelfShareContract.ShelfEntry.COLUMN_NAME_DESC + TEXT_TYPE + COMMA_SEP +
                    ShelfShareContract.ShelfEntry.COLUMN_NAME_CONDITION + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ShelfShareContract.ShelfEntry.TABLE_NAME;


    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, 1);// 1? Its database Version
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            Log.i(TAG, "Database exists");
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDb = null;
        String myPath = DB_PATH + DB_NAME;
        checkDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        if(checkDb != null){
            checkDb.close();
        }
        return checkDb != null ? true : false;
    }
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase()  {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


}
