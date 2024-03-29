package com.example.firebasech3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button writeButton;
    private TextView viewText;
    private Button readButton;
    private Button updateButton;
    private Button deleteButton;
    private EditText messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("portfolios");
        writeButton = findViewById(R.id.writeButton);
        viewText = findViewById(R.id.viewText);
        readButton = findViewById(R.id.readButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        messageEditText = findViewById(R.id.messageEditText);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<StockPortfolio>>
                        t = new GenericTypeIndicator<ArrayList<StockPortfolio>>() {};
                ArrayList<StockPortfolio> myFolios = dataSnapshot.getValue(t);
                String pName="";
                if(myFolios != null){
                    pName = myFolios.get(0).portfolioName;
                }
                viewText.setText(pName);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast toast=Toast.makeText(getApplicationContext(),"Error" + error.toString(),
                        Toast.LENGTH_SHORT);
                toast.setMargin(50,50);
                toast.show();
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = myRef.orderByChild("portfolioName").equalTo("demoFolio");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {}

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<StockPortfolio> myFolios = new ArrayList<StockPortfolio>();
                StockPortfolio myFolio = new StockPortfolio("demoFolio", "lmoroney", "lm@hotmail.com");
                ArrayList<Stock> myFolioHoldings = new ArrayList<Stock>();
                myFolioHoldings.add(new Stock("GOOG", "Google", 100));
                myFolioHoldings.add(new Stock("AAPL", "Apple", 50));
                myFolioHoldings.add(new Stock("MSFT", "Microsoft", 10));
                myFolio.portfolioHoldings = myFolioHoldings;
                StockPortfolio myOtherFolio = new StockPortfolio("realFolio", "lmoroney",
                        "lmwork@hotmail.com");
                ArrayList<Stock> myOtherFolioHoldings = new ArrayList<Stock>();
                myOtherFolioHoldings.add(new Stock("IBM", "IBM", 50));
                myOtherFolioHoldings.add(new Stock("MMM", "3M", 10));
                myOtherFolio.portfolioHoldings = myOtherFolioHoldings;
                myFolios.add(myFolio);
                myFolios.add(myOtherFolio);
                myRef.setValue(myFolios);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = myRef.orderByChild("portfolioName").equalTo("demoFolio");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot nodeShot = dataSnapshot.getChildren().iterator().next();
                        String key = nodeShot.getKey();
                        HashMap<String, Object> update = new HashMap<>();
                        update.put("portfolioOwner", messageEditText.getText().toString());
                        myRef.child(key).updateChildren(update);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = myRef.orderByChild("portfolioName").equalTo("demoFolio");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot nodeShot = dataSnapshot.getChildren().iterator().next();
                        String key = nodeShot.getKey();
                        myRef.child(key).removeValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }
}
