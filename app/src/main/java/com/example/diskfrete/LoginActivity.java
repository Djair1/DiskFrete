package com.example.diskfrete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText Email, Senha;
    private String email,senha;
   FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        firebaseAuth=FirebaseAuth.getInstance();

        Button cadastrar = (Button)findViewById(R.id.button4);
        Button entrar = (Button)findViewById(R.id.button2);
        Button ResetarSenha =(Button)findViewById(R.id.button3);
        Email =(EditText)findViewById(R.id.editText2);
        Senha =(EditText)findViewById(R.id.editText3);


        //ação do botão cadastre-se
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this , CadastrarUsuarioActivity.class);
                startActivity(it);

            }
        });

        ResetarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this , RedefinirSenha.class);
                startActivity(it);

            }
        });


        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              email = Email.getText().toString().trim();
              senha = Senha.getText().toString().trim();


                if(email.length()== 0){
                    Toast.makeText(LoginActivity.this, "Email Invalido ou vazio!",
                            Toast.LENGTH_SHORT).show();
                }
                else if (senha.length()== 0){
                    Toast.makeText(LoginActivity.this, " inserir senha. ",
                            Toast.LENGTH_SHORT).show();
                }else{ usuarioLogar();}






            }
            private void usuarioLogar(){
                firebaseAuth.signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    carregarUsuario();

                                } else {


                                    Toast.makeText(LoginActivity.this, "Erro ao realizar Login.",
                                            Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

            }

            private void carregarUsuario() {
                Intent it = new Intent(LoginActivity.this, UsuarioHome.class);
                it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
            }
        });


}}
