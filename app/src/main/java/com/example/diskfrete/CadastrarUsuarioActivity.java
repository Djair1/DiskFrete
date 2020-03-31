package com.example.diskfrete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastrarUsuarioActivity extends AppCompatActivity {

private FirebaseAuth mAuth;
private String nome ,sobrenome ,email,senha,confiSenha;
private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference("Usuarios");




        final EditText ednome = (EditText)findViewById(R.id.editText2);
        final EditText edSobrenome=(EditText)findViewById(R.id.editText6);
        final EditText edmail = (EditText)findViewById(R.id.editText3) ;
         final EditText edSenha=(EditText)findViewById(R.id.editText);
         final EditText edConfSenha = (EditText)findViewById(R.id.editText5);


      Button cadastrar = (Button)findViewById(R.id.button2);



        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edmail.getText().toString().trim();
                senha = edSenha.getText().toString().trim();
                nome = ednome.getText().toString().trim();
                sobrenome= edSobrenome.getText().toString().trim();
                confiSenha=edConfSenha.getText().toString().trim();


                if(nome.length()<=3){
                    Toast.makeText(CadastrarUsuarioActivity.this, "usuario inválido ou vazio.",
                            Toast.LENGTH_SHORT).show();
                }
                else if(sobrenome.length()>25||sobrenome.length()==0){
                    Toast.makeText(CadastrarUsuarioActivity.this, "sobrenome inválido ou vazio",
                            Toast.LENGTH_SHORT).show();
                }

                else if(email.length()== 0){
                    Toast.makeText(CadastrarUsuarioActivity.this, "Email vazio!",
                            Toast.LENGTH_SHORT).show();
                }

                else if (senha.length()== 0){
                    Toast.makeText(CadastrarUsuarioActivity.this, " insera a senha. ",
                            Toast.LENGTH_SHORT).show();
                }

               else if (!senha.equals(confiSenha)){
                    Toast.makeText(CadastrarUsuarioActivity.this, "as senhas não correspondem. ",
                            Toast.LENGTH_SHORT).show();
                }
               else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    CadastrarUsuario();

                }else{Toast.makeText(CadastrarUsuarioActivity.this, "formato de email inválido ! ",
                        Toast.LENGTH_SHORT).show();}





            }

            private void CadastrarUsuario(){
                mAuth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(CadastrarUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    carregarUsuario();

                                } else {
                                    Toast.makeText(CadastrarUsuarioActivity.this, "Erro ao cadastrar!.",
                                            Toast.LENGTH_SHORT).show();

                                }

                               
                            }
                        });
            }



            private void carregarUsuario() {
                Intent it = new Intent(CadastrarUsuarioActivity.this, UsuarioHome.class);
                it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                Toast.makeText(CadastrarUsuarioActivity.this, "Usuario Cadastrado com susseso", Toast.LENGTH_SHORT).show();
                CadastrarUsuarioNoBanco();

            }

            private void CadastrarUsuarioNoBanco() {
                String id = database.push().getKey();
                Usuario usuario = new Usuario(id,nome,sobrenome,email,senha);
                database.child(id).setValue(usuario);


            }
        });

        }
}