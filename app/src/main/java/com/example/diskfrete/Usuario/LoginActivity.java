package com.example.diskfrete.Usuario;

import android.app.ProgressDialog;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.diskfrete.R;
import com.example.diskfrete.RedefinirSenha;
import com.example.diskfrete.TeladeEspera;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.diskfrete.Usuario.CadastrarFrete.Usuario_Solicitacao_concluida;


public class LoginActivity extends AppCompatActivity {

    EditText Email, Senha;
    private String email,senha;
    private String emailM,nomeM;
    private FirebaseAuth firebaseAuth;
    public static final  String Usuario_PREFERENCES="login automatico";
    private ProgressDialog barraDeProgresso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();



        Button cadastrar = (Button) findViewById(R.id.button4);
        Button entrar = (Button) findViewById(R.id.button2);
        Button ResetarSenha = (Button) findViewById(R.id.button11);
        Email = (EditText) findViewById(R.id.editText2);
        Senha = (EditText) findViewById(R.id.editText3);
        barraDeProgresso = new ProgressDialog(this);


        carregarSolicitacaoDoUsuario();



        //ação do botão cadastre-se
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this, CadastrarUsuarioActivity.class);
                startActivity(it);

            }
        });

        ResetarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this, RedefinirSenha.class);
                startActivity(it);

            }
        });





        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = Email.getText().toString().trim();
                senha = Senha.getText().toString().trim();


                if (email.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Email Invalido ou vazio!",
                            Toast.LENGTH_SHORT).show();
                } else if (senha.length() == 0) {
                    Toast.makeText(LoginActivity.this, " inserir senha. ",
                            Toast.LENGTH_SHORT).show();
                } else {
                    usuarioLogar();
                }


            }

            public void usuarioLogar() {

                barraDeProgresso.setTitle("");
                barraDeProgresso.setMessage("conectando...");
                barraDeProgresso.setCanceledOnTouchOutside(false);
                barraDeProgresso.show();

                firebaseAuth.signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    carregarUsuario();

                                } else {

                                    barraDeProgresso.dismiss();
                                    Toast.makeText(LoginActivity.this, "Erro ao realizar Login.",
                                            Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

            }

            private void carregarUsuario() {
            //    startActivity(new Intent(getBaseContext(),Trasicao.class));
                SharedPreferences.Editor editor = getSharedPreferences(Usuario_PREFERENCES,MODE_PRIVATE).edit();
                editor.putString("email",email);
                editor.commit();
                Intent it = new Intent(LoginActivity.this, UsuarioHome.class);
                it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK | it.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);

            }

        });



 }

    private void carregarSolicitacaoDoUsuario() {
        SharedPreferences pref=getSharedPreferences( Usuario_Solicitacao_concluida,MODE_PRIVATE);
        emailM =pref.getString("EMAIL",null);
        if (emailM != null) {

            Intent it = new Intent(LoginActivity.this, TeladeEspera.class);
            //  it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK | it.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            finish();

        }else{carregarUsuarioHome();}

    }
        private void carregarUsuarioHome(){
            SharedPreferences prefs=getSharedPreferences(Usuario_PREFERENCES,MODE_PRIVATE);
            email=prefs.getString("email",null);
            if(email!=null){

                Intent it = new Intent(LoginActivity.this, UsuarioHome.class);
                //  it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK | it.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                finish();
            }
        }
    }





