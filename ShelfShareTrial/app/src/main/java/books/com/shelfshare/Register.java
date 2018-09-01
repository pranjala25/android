package books.com.shelfshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import books.com.shelfshare.config.ShelfShareContract;
import books.com.shelfshare.config.User;

public class Register extends BaseActivity implements  View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = Register.class.getSimpleName();
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mUsernameField;
    private EditText mUserPhoneField;
    private EditText mUserLocField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mEmailField = (EditText) findViewById(R.id.editEmail);
        mPasswordField = (EditText) findViewById(R.id.editPwd);
        mUsernameField = (EditText) findViewById(R.id.username);
        mUserPhoneField = (EditText) findViewById(R.id.editPhone);
        mUserLocField = (EditText) findViewById(R.id.editLoc);
        findViewById(R.id.register).setOnClickListener(this);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "Setting Username:" + mUsernameField.getText().toString());
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(mUsernameField.getText().toString())
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });
                            Log.d(TAG, "Is Username set:" + mAuth.getCurrentUser().getDisplayName());
                            int c = 0;
                            while(user.getDisplayName()==null){
                                c++;
                                Log.d(TAG,"Still not set"+c);
                            }
                            saveUser();
                            finish();
                            startActivity(new Intent(getApplicationContext(), AllBooks.class));
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        else if (!task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Failed=" + task.getException().getMessage());
                            Toast.makeText(Register.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.register) {
            //  updateSignUpUI();
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());

        }
    }

    public void saveUser(){
        saveUser(mUsernameField.getText().toString(),mEmailField.getText().toString(),mUserPhoneField.getText().toString(),mUserLocField.getText().toString());
    }

    public void saveUser(String name, String email, String number, String loc){
        String uid = mAuth.getCurrentUser().getUid();
        Log.d(TAG,"Uid"+uid);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        // Creating new user node, which returns the unique key value
        // new user node would be /users/$userid/
        String userId = mDatabase.push().getKey();
        // creating user object
        User user = new User(uid,name,email,number,loc);
        // pushing user to 'users' node using the userId
        mDatabase.child(userId).setValue(user);
    }
}
