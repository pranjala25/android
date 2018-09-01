package books.com.shelfshare;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import books.com.shelfshare.config.Book;
import books.com.shelfshare.config.DatabaseHelper;
import books.com.shelfshare.config.ShelfShareContract;
import books.com.shelfshare.config.User;


public class AddBook extends AppCompatActivity {

    DatabaseHelper helper;
    EditText title;
    EditText author;
    EditText desc;
    Spinner condition;
    String uid;
    private static final String TAG = AddBook.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddBook.this, MyBooks.class);
                startActivity(i);
                finish();
            }
        });

        title = (EditText)  findViewById(R.id.editTitle);
        author = (EditText)  findViewById(R.id.editAuth);
        desc = (EditText)  findViewById(R.id.editDesc);
        condition = (Spinner)  findViewById(R.id.chooseCond);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertBookInfo(title.getText().toString(), author.getText().toString(), desc.getText().toString(), condition.getSelectedItem().toString()/*, getApplicationContext()*/);
                Intent i = new Intent(AddBook.this, MyBooks.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void insertBookInfo(String title,String author, String desc, String condition){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("books");
        // Creating new user node, which returns the unique key value
        // new user node would be /users/$userid/
        String bookId = mDatabase.push().getKey();
        // creating book object
        Book book = new Book(uid, title,author,condition);
        // pushing book to 'books' node using the bookId
        mDatabase.child(bookId).setValue(book);
    }

}
