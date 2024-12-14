package com.example.lp6;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class armor_list extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private ListView listView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_armor_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper=new DBHelper(getApplicationContext());
        try {
            database=dbHelper.getWritableDatabase();
        } catch (Exception e){
            e.printStackTrace();
        }

        listView=findViewById(R.id.ListView2);
        textView =findViewById(R.id.textView2);

        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("position", 0) + 1;
        textView.setText(mIntent.getStringExtra("armor_type"));


        ArrayList<HashMap<String,String>>armors =new ArrayList<>();
        HashMap <String,String> armor;
        Cursor cursor = database.rawQuery("SELECT name, description, passive, price FROM armor_list WHERE type_id = ?", new String[] { String.valueOf(intValue) } );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            armor=new HashMap<>();
            armor.put("name", cursor.getString(0));
            armor.put("info", "\n" + "Description: " + cursor.getString(1) + "\n\nPassive: "+ cursor.getString(2)+"\n\nPrice: " + cursor.getString(3));
            armors.add(armor);
            cursor.moveToNext();
        }
        cursor.close();

        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                armors, android.R.layout.simple_list_item_2,
                new String[]{"name","info"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );
        listView.setAdapter(adapter);

    }
}