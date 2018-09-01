package books.com.shelfshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import books.com.shelfshare.config.Book;
import books.com.shelfshare.config.BookAdapter;


public class MyBooks extends AppCompatActivity  {


    boolean isData= false;
    private TextView greetings;
    private RecyclerView recyclerView;
    private List<Book> bookList = new ArrayList<>();
    private BookAdapter bookAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private static final String TAG = MyBooks.class.getSimpleName();
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        greetings = (TextView) findViewById(R.id.greeting);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isData = true;
                Intent i = new Intent(MyBooks.this, AddBook.class);
                startActivity(i);
                finish();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        // User is signed in
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            greetings.setText("Hey "+name+"!");
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("books");
        bookAdapter = new BookAdapter(bookList);
        displayData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_books, menu);
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
            Intent i = new Intent(MyBooks.this, MyBooks.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.miShelf) {
            Intent i = new Intent(MyBooks.this, AllBooks.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayData(){

                    Query query = myRef.orderByChild("userId").equalTo(uid);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataShot:dataSnapshot.getChildren())
                            {
                            Book book = dataShot.getValue(Book.class);
                            bookList.add(book);}
                            Log.d(TAG, "Got here for "+bookList.toString());


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
