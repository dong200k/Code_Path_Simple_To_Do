package com.example.codepathsimpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    Button btnSave;
    EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setTitle("Edit Text");

        btnSave = findViewById(R.id.btnSave);
        etText = findViewById(R.id.etText);

        // Show text before edit
        String itemText = getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT);
        etText.setText(itemText);

        // Done editing
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                // Pass edited text and position back
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, etText.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                // Set result
                setResult(RESULT_OK, intent);

                // Finish and return to MainActivity
                finish();
            }
        });

    }
}