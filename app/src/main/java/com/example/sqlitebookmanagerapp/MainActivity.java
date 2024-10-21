package com.example.sqlitebookmanagerapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText isbn, titre;
    Button button;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for the activity
        setContentView(R.layout.activity_main);

        // Initialize EditText and Button views
        isbn = findViewById(R.id.isbn);
        titre = findViewById(R.id.titre);
        button = findViewById(R.id.button);

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom);
            return insets;
        });

        // Open or create a database
        db = openOrCreateDatabase("BDLivres", MODE_PRIVATE, null);

        // Create the table if it doesn't exist
        db.execSQL("CREATE TABLE IF NOT EXISTS livres (Id INTEGER PRIMARY KEY AUTOINCREMENT, Isbn TEXT, Titre TEXT);");

        // Get the number of rows in the "livres" table
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM livres;", null);
        cursor.moveToFirst();
        long newId = cursor.getLong(0);
        cursor.close();

        // If there are no rows in the table, insert default values
        if (newId == 0) {
            db.execSQL("INSERT INTO livres (Isbn, Titre) VALUES (?, ?)", new Object[]{"619-23654", "Le langage C"});
            db.execSQL("INSERT INTO livres (Isbn, Titre) VALUES (?, ?)", new Object[]{"235-78965", "Systèmes Temps Réel"});
        }

        // Set the click listener for the button to insert new book data
        button.setOnClickListener(v -> {
            String isbnValue = isbn.getText().toString();
            String titreValue = titre.getText().toString();

            if (!isbnValue.isEmpty() && !titreValue.isEmpty()) {
                // Insert the new book data into the "livres" table
                db.execSQL("INSERT INTO livres (Isbn, Titre) VALUES (?, ?)", new Object[]{isbnValue, titreValue});

                // Clear the input fields
                isbn.setText("");
                titre.setText("");
            }
        });
    }
}
