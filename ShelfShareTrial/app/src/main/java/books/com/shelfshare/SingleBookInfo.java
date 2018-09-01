package books.com.shelfshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import books.com.shelfshare.config.User;


public class SingleBookInfo extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    String userName, phone,location;
    String uid, title, author, desc, condition;
    boolean isRented;
    TextView titleVal,authorVal,descVal,conditionVal,userVal,phoneVal,locVal;
    Switch rentedVal;
    private static final String TAG = AddBook.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_book_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        retrieveDate();

        titleVal = (TextView)  findViewById(R.id.editTitle);
        titleVal.setText(title);
        authorVal = (TextView)  findViewById(R.id.editAuth);
        authorVal.setText(author);
        descVal = (TextView)  findViewById(R.id.editDesc);
        descVal.setText(desc);
        conditionVal = (TextView)  findViewById(R.id.chooseCond);
        conditionVal.setText(condition);
        userVal= (TextView)  findViewById(R.id.userName);
        userVal.setText(userName);
        phoneVal = (TextView)  findViewById(R.id.phoneNo);
        phoneVal.setText(phone);
        locVal= (TextView)  findViewById(R.id.location);
        locVal.setText(location);
        rentedVal = (Switch) findViewById(R.id.isRented);
        rentedVal.setChecked(isRented);
    }

    public  void retrieveDate(){
         uid = getIntent().getExtras().getString("uid");
         title = getIntent().getExtras().getString("title");
         author = getIntent().getExtras().getString("author");
         desc = getIntent().getExtras().getString("desc");
         condition = getIntent().getExtras().getString("condition");
         isRented = getIntent().getExtras().getBoolean("rented");
        Log.d(TAG,"have some info kya?"+title);
        getUserInfo();

    }

    public void getUserInfo(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        Query query = myRef.orderByChild("userId").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataShot:dataSnapshot.getChildren())
                {
                    User user = dataShot.getValue(User.class);
                    userName = user.getName();
                    phone = user.getPhone();
                    location = user.getLocation();
                }
              //  Log.d(TAG, "Got here for "+bookList.toString());
                Log.d(TAG,"have some user info kya?"+userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
            Intent i = new Intent(SingleBookInfo.this, MyBooks.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.miShelf) {
            Intent i = new Intent(SingleBookInfo.this, AllBooks.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
