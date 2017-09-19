package kr.hs.emirim.iuki1.firebasedbexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    static final String TAG = "파베:mainActivity";

    private TextView mNameTextView;
    private TextView mGithubTextView;
    private ImageView mProfilImageView;
    FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNameTextView = (TextView)findViewById(R.id.profile_name);
        mGithubTextView = (TextView)findViewById(R.id.profile_github);
        mProfilImageView = (ImageView)findViewById(R.id.profile_image);

        Picasso.with(this)
                .load("http://i.imgur.com/DvpvklR.png")
                .centerCrop()
                .resize(100,100)
                .into(mProfilImageView);

        mDatabase = FirebaseDatabase.getInstance();


        DatabaseReference profileNameRef = mDatabase.getReference("profile/name");

        ValueEventListener profileNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                mNameTextView.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        profileNameRef.addValueEventListener(profileNameListener);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                Toast.makeText(getApplication(), value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_github:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse( mGithubTextView.getText().toString() );
                intent.setData(uri);
                startActivity(intent);
            break;
        }

    }
}
