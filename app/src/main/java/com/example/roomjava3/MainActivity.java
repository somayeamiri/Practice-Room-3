package com.example.roomjava3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import java.io.File;

public class MainActivity extends AppCompatActivity  implements UnzipListener {

    public static final String TAG = "Roomjava3";
    String DATABASE_NAME = "endb.db";
    String FILE = "/data/data/com.example.roomjava3/databases/";


    EditText word;
    Button btn;
    TextView show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        word=findViewById(R.id.edt_word);
        btn=findViewById(R.id.btn);
        show=findViewById(R.id.edt_show);

        File file = new File(FILE + DATABASE_NAME);
        if (!file.exists()) {

            DownloadDataBase downloadDataBase = new DownloadDataBase(MainActivity.this, "dic.zip", this);
            downloadDataBase.execute("http://195.248.242.73/androidteam/p1/database.zip", "/data/data/com.example.roomjava3/databases/");

        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //query to database
                readData(word.getText().toString());


            }
        });


    }


    @Override
    public void unzipFinished() {

        Toast.makeText(this, "unzipping finished", Toast.LENGTH_SHORT).show();

    }



    private void readData(String sample){

        WordRoomDatabase.getDatabase(MainActivity.this, DATABASE_NAME).wordDao().GetWord(sample) // observable
                .subscribeOn(Schedulers.io()) // observable where done
                .observeOn(AndroidSchedulers.mainThread()) // result where show
                .subscribe(new DisposableSingleObserver<Dic>() {
                    @Override
                    public void onSuccess(Dic dic) {

                        show.setText(dic.translate);
//                        Toast.makeText(MainActivity.this, dic.translate, Toast.LENGTH_SHORT).show();
                    } 
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });
}}