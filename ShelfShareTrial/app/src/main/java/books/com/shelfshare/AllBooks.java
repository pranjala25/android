package books.com.shelfshare;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import books.com.shelfshare.config.Book;
import books.com.shelfshare.config.BookAdapter;
import books.com.shelfshare.config.DatabaseHelper;
import books.com.shelfshare.config.ShelfShareContract;
import books.com.shelfshare.helper.Quotes;

public class AllBooks extends AppCompatActivity  {

    DatabaseHelper helper;
    boolean isData= false;
    SimpleCursorAdapter adapter;
    private TextView greetings;
    private TextView quotes;
    private RecyclerView recyclerView;
    private List<Book> bookList = new ArrayList<>();
    private BookAdapter bookAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    //EditText inputSearch;
    private static final String TAG = AllBooks.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        greetings = (TextView) findViewById(R.id.greeting);
        quotes = (TextView) findViewById(R.id.quote);
       // inputSearch = (EditText) findViewById(R.id.inputSearch);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // User is signed in
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

           // Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
            greetings.setText("Hey "+name+"!");
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("books");
        bookAdapter = new BookAdapter(bookList);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Quotes quotesHelper = new Quotes();
        String newQuote = quotesHelper.getQuote();
        quotes.setText(newQuote);
        displayData();

       /* inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                AllBooks.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_books, menu);
        // Associate searchable configuration with the SearchView
    /*    SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.miProfile) {
            Log.d(TAG,"Trying to start Profile");
            Intent i = new Intent(AllBooks.this, MyBooks.class);
            startActivity(i);
            finish();
            //return true;
        }
        if (id == R.id.miShelf) {
            Intent i = new Intent(AllBooks.this, AllBooks.class);
            startActivity(i);
            finish();
           // return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayData(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataShot:dataSnapshot.getChildren())
                {
                    Book book = dataShot.getValue(Book.class);
                    bookList.add(book);
                }
                Log.d(TAG, "Got here for "+bookList.toString()+" "+bookList.get(3));


                bookAdapter.notifyDataSetChanged();
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(bookAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
