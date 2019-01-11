package com.example.mr_robot.studentapp7;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    EditText id,name,cgpa,number;
    Button btn1,btn2;
    DBOpenHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = new DBOpenHelper(this);
        id = findViewById(R.id.id);
        name = findViewById(R.id.name);
        cgpa = findViewById(R.id.cgpa);
        number = findViewById(R.id.number);
        btn1 = findViewById(R.id.submit);
        btn2 = findViewById(R.id.view);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = name.getText().toString();
                String id1 = id.getText().toString();
                String cgpa1 = cgpa.getText().toString();
                String number1 = number.getText().toString();
                ContentValues values = new ContentValues();
                values.put(DBOpenHelper.COL_1,id1);
                values.put(DBOpenHelper.COL_2,name1);
                values.put(DBOpenHelper.COL_3,cgpa1);
                values.put(DBOpenHelper.COL_4,number1);
                Uri uri = getContentResolver().insert(ContactsProvider.Content_URI,values);
                Toast.makeText(getBaseContext(),"New data Added",Toast.LENGTH_SHORT).show();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = mydb.getAllData();
                if(res.getCount()==0) {
                    //
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()) {
                    buffer.append("ID :"+res.getString(0)+"\n");
                    buffer.append("NAME :"+res.getString(1)+"\n");
                    buffer.append("CGPA :"+res.getString(2)+"\n");
                    buffer.append("NUMBER :"+res.getString(3)+"\n");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("DATA");
                builder.setMessage(buffer.toString());
                builder.show();
                }
        });
    }
}
