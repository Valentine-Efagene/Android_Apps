package com.example.valentyne.databases;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class DatabasesMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_databases_main);

        DBAdapter db = new DBAdapter(this);

        // --add a contact--
        try{
            db.open();
        }catch (Exception ex) {
            Toast.makeText(this, "id: " + ex.toString(), Toast.LENGTH_LONG).show();
            Log.w("error", ex.toString());
            return;
        }
        long id = db.insertContact("Jennifer Ann","jenniferann@jfmarzio.com");
        id = db.insertContact("Oscar Diggs", "oscar@oscardiggs.com");
        db.close();
        db.open();
        Cursor c = db.getAllContacts();

        if (c.moveToFirst()) {
            do {
                DisplayContact(c);
            } while (c.moveToNext());
        }

        db.close();
    }

    public void DisplayContact(Cursor c) {
        Toast.makeText(this, "id: " + c.getString(0) +
        "\n" + "Name: " + c.getString(1) + "\n" +
        "Email: " + c.getString(2), Toast.LENGTH_LONG).show();
    }
}
