package com.example.mistareas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText et_tarea;
    Button btn_agregar, btn_reiniciar;
    RecyclerView recyclerView;
    List<MainData> informacion =new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    MainAdapter mainAdapter;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_tarea =findViewById(R.id.et_tarea);
        btn_agregar =findViewById(R.id.btn_agregar);
        btn_reiniciar =findViewById(R.id.btn_reiniciar);
        recyclerView=findViewById(R.id.recycler_view);

        database=RoomDB.getInstance(this);

        informacion =database.mainDao().getAll();

        linearLayoutManager =new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        mainAdapter=new MainAdapter(informacion,MainActivity.this);

        recyclerView.setAdapter(mainAdapter);

        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sText= et_tarea.getText().toString().trim();
                if(!sText.equals("")){
                    MainData data=new MainData();
                    data.setText(sText);
                    database.mainDao().insertar(data);


                    et_tarea.setText("");

                    informacion.clear();
                    Toast.makeText(MainActivity.this,"Tarea agregada",Toast.LENGTH_LONG).show();

                    informacion.addAll(database.mainDao().getAll());
                    mainAdapter.notifyDataSetChanged();

                }else{
                    builder= new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Ingresa una tarea")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Error");
                    alert.show();
                }
            }
        });
        btn_reiniciar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               builder= new AlertDialog.Builder(v.getContext());
               builder.setMessage("¿Deseas eliminar todas tus tareas?")
                       .setCancelable(false)
                       .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {

                               database.mainDao().reiniciar(informacion);
                               informacion.clear();
                               informacion.addAll(database.mainDao().getAll());
                               mainAdapter.notifyDataSetChanged();
                           }
                       })
                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               dialog.cancel();
                           }
                       });
               AlertDialog alert = builder.create();
               alert.setTitle("Confirmar reinicio");
               alert.show();

           }
        });
    }
}