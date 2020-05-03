package com.example.diskfrete;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.diskfrete.Motorista.LoginMotorista;
import com.example.diskfrete.Usuario.LoginActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btCliente =  (Button)findViewById(R.id.btCliente);
        Button btMotorista = (Button) findViewById(R.id.btMotorista);


        btCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, LoginActivity.class);
               startActivity(intent);
            }
        });

        btMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginMotorista.class);
                startActivity(intent);
            }
        });



    }



    }

