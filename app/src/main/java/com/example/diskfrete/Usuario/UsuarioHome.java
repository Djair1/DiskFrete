package com.example.diskfrete.Usuario;

import android.app.ProgressDialog;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.diskfrete.Motorista.Motorista;
import com.example.diskfrete.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.*;

public class UsuarioHome extends AppCompatActivity {


    private DatabaseReference database;
    private List<String> listaDemotoristas= new ArrayList<>();
    private ArrayAdapter<String>adapterMotorista;
    private ProgressDialog barraDeProgresso;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_home);
        database= FirebaseDatabase.getInstance().getReference("Motoristas");



         lista =(ListView)findViewById(R.id.lista);
         barraDeProgresso = new ProgressDialog(this);


         carregarMotoristas();



}

    private void buscarCurrentUser() {


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

    private void progressobarra() {
        barraDeProgresso.setTitle("Bem Vindo");
        barraDeProgresso.setMessage("carregando...");
        barraDeProgresso.setCanceledOnTouchOutside(false);
        barraDeProgresso.show();
    }

    private void carregarMotoristas() {
        progressobarra();
        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaDemotoristas.clear();


                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {

                    Motorista motorista = objSnapshot.getValue(Motorista.class);

                    listaDemotoristas.add(motorista.getNomeCompleto()+"\n"+motorista.getEmail()+"\n"+motorista.getTelefone());


                }
                adapterMotorista = new ArrayAdapter<String>(UsuarioHome.this, android.R.layout.simple_list_item_1, listaDemotoristas);

                lista.setAdapter(adapterMotorista);
                barraDeProgresso.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
