package com.example.diskfrete;

import android.app.ProgressDialog;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LoginMotorista extends AppCompatActivity {

    EditText Email, Senha,Cnh;
    Button Entrar;
    private String email,senha,CNH;
    FirebaseAuth firebaseAuth;
    private DatabaseReference database;
    private ProgressDialog barraDeProgresso;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_motorista);

        Email =(EditText)findViewById(R.id.editText2);
        Senha =(EditText)findViewById(R.id.editText3);
        Cnh =(EditText)findViewById(R.id.editText4);
        Entrar=(Button)findViewById(R.id.button2);
        Button cadastrar = (Button)findViewById(R.id.button4);
        Button ResetarSenha=(Button)findViewById(R.id.button3);



        firebaseAuth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference("Motoristas");
        barraDeProgresso = new ProgressDialog(this);






        ResetarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginMotorista.this , RedefinirSenha.class);
                startActivity(it);

            }
        });



        // ação do botão cadastrar
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginMotorista.this , CadastroMotoristas.class);
                startActivity(it);
            }
        });

        Entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = Email.getText().toString().trim();
                senha = Senha.getText().toString().trim();
                 CNH = Cnh.getText().toString().trim();


                if(email.length()== 0){
                    Toast.makeText(LoginMotorista.this, "Email Invalido ou vazio!",
                            Toast.LENGTH_SHORT).show();
                }
             else if(CNH.length()==0){
                    Toast.makeText(LoginMotorista.this, "CNH Invalido ou vazio!",
                            Toast.LENGTH_SHORT).show();
                }


                else if (senha.length()== 0){
                    Toast.makeText(LoginMotorista.this, " inserir senha. ",
                            Toast.LENGTH_SHORT).show();
                }else{ carregarMotoristasDoFirebase();}

            }

            private void validarCnh(String emeil,String cnh) {
                if (email.equals(emeil) && CNH.equals(cnh)){
                    LogarMotorista();
                }else{ Toast.makeText(LoginMotorista.this, " Motorista não encontrado ! ",
                        Toast.LENGTH_SHORT).show();}


            }

            private void carregarMotoristasDoFirebase() {

                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for(DataSnapshot objSnapshot :dataSnapshot.getChildren()){

                            Motorista motorista = objSnapshot.getValue(Motorista.class);

                            validarCnh(motorista.getEmail(),motorista.getCnh());

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }



            private void LogarMotorista(){
                barraDeProgresso.setTitle("Bem Vindo");
                barraDeProgresso.setMessage("conectando...");
                barraDeProgresso.setCanceledOnTouchOutside(false);
                barraDeProgresso.show();
                 firebaseAuth.signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(LoginMotorista.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    carregarMotorista();

                                } else {

                                    barraDeProgresso.dismiss();
                                    Toast.makeText(LoginMotorista.this, "Erro ao realizar Login.",
                                            Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

            }


            private void  carregarMotorista() {
               // progresso.setProgress(60);
                Intent it = new Intent(LoginMotorista.this, MotoristaDiskFrete.class);
                it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
              //  progresso.setProgress(0);
            }



});
    }




}