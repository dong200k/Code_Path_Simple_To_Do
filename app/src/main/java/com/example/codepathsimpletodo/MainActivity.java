package com.example.codepathsimpletodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    static ItemsAdapter itemsAdapter;

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Simple To Do");

        // Mock data
        items = new ArrayList<>();

        // Load items
        loadItems();

        // Wiring
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);


        // Listener Remove items
        ItemsAdapter.onLongClickListener onLongClickListener = new ItemsAdapter.onLongClickListener() {
            @Override

            public void onItemLongClick(int position) {
                // Get item to remove
                String item = items.get(position);
                // Remove item
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                saveItems();
                Toast.makeText(getApplicationContext(), "Item Removed", Toast.LENGTH_SHORT).show();
            }
        };
         // Listener to Edit Items
        ItemsAdapter.onClickListener onClickListener = new ItemsAdapter.onClickListener() {
            @Override
            public void onItemClick(int position) {
                // Get item to edit
                String item = items.get(position);
                // Create new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // Pass item to edit
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // Display activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        // Set up adapter
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // Listener to Insert item
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Get user input
                String item = etItem.getText().toString();
                // If user input is not the empty string
                if(!item.equals("")) {
                    // Add item to model
                    items.add(item);
                    // Notify adapter of insertion
                    itemsAdapter.notifyItemInserted(items.size() - 1);
                    // Set text to empty string for next input
                    etItem.setText("");
                    saveItems();
                    Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            // Retreive the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // Update item
            items.set(position, itemText);
            itemsAdapter.notifyItemChanged(position);
            
            saveItems();
            Toast.makeText(getApplicationContext(), "Item Saved", Toast.LENGTH_SHORT).show();
        }  else{
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), String.valueOf(Charset.defaultCharset())));
        } catch (IOException e) {
            items = new ArrayList<>();
            Log.e("MainActivity", "Error loading items", e);
        }
    }
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error saving items", e);
        }
    }
}