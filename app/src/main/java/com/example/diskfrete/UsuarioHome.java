package com.example.diskfrete;

import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.collection.ArraySet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.*;

public class UsuarioHome extends AppCompatActivity {


    private DatabaseReference database;
    private List<String> listaDemotoristas= new ArrayList<>();
    private ArrayAdapter<String>adapterMotorista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_home);

        database= FirebaseDatabase.getInstance().getReference("Motoristas");



        final ListView lista =(ListView)findViewById(R.id.lista);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaDemotoristas.clear();

                for(DataSnapshot objSnapshot :dataSnapshot.getChildren()){

                    Motorista motorista = objSnapshot.getValue(Motorista.class);

                    listaDemotoristas.add(motorista.getNome()+" "+motorista.getSobrenome());



                }
adapterMotorista = new ArrayAdapter<String>(UsuarioHome.this,android.R.layout.simple_list_item_1,listaDemotoristas);

                lista.setAdapter(adapterMotorista);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
           // String name = user.getDisplayName();
              String email = user.getEmail();
          //  Uri photoUrl = user.getPhotoUrl();
           boolean emailVerified = user.isEmailVerified();
              String uid = user.getUid();

 } else {
            Toast.makeText(UsuarioHome.this, "Erro ao buscar informações.",
                    Toast.LENGTH_SHORT).show();
        }












}
}
