package com.example.diskfrete.Controller;

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


import com.example.diskfrete.Controller.ADMINISTRADOR.AdministradorHome;
import com.example.diskfrete.Controller.USUARIO.AvaliarMotorista;
import com.example.diskfrete.Model.Administrador;
import com.example.diskfrete.db.Firebase;
import com.example.diskfrete.Controller.MOTORISTA.*;
import com.example.diskfrete.Controller.USUARIO.TeladeEspera;
import com.example.diskfrete.Model.Motorista;
import com.example.diskfrete.Model.Usuario;
import com.example.diskfrete.Controller.USUARIO.UsuarioFreteAceito;
import com.example.diskfrete.Controller.USUARIO.UsuarioHome;
import com.example.diskfrete.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.*;


import static com.example.diskfrete.Preferencias.DadosPreferences.*;


public class Login extends AppCompatActivity {

    EditText Email, Senha;
    private String email, senha;
    private String ator;
    private ProgressDialog barraDeProgresso;
    private Button cadastrar, entrar, ResetarSenha;
    private Firebase fb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("DISK FRETE");


        cadastrar = (Button) findViewById(R.id.button4);
        entrar = (Button) findViewById(R.id.button2);
        ResetarSenha = (Button) findViewById(R.id.button11);
        Email = (EditText) findViewById(R.id.editText2);
        Senha = (EditText) findViewById(R.id.editText3);
        barraDeProgresso = new ProgressDialog(this);


        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Login.this, DialogoCadastro.class);
                startActivity(it);

            }
        });

        ResetarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(Login.this, RedefinirSenha.class);
                startActivity(it);

            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarcampos();


            }

        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        loginUsuarioSalvo();
        loginMotoristaSalvo();
    }

    private void validarcampos() {

        email = Email.getText().toString().trim();
        senha = Senha.getText().toString().trim();


        if (email.length() == 0) {
            Toast.makeText(Login.this, "Email Invalido ou vazio!",
                    Toast.LENGTH_SHORT).show();
        } else if (senha.length() == 0) {
            Toast.makeText(Login.this, " inserir senha. ",
                    Toast.LENGTH_SHORT).show();
        } else {

            usuarioLogar();
        }


    }

    private void usuarioLogar() {

        barraDeProgresso.setTitle("");
        barraDeProgresso.setMessage("conectando...");
        barraDeProgresso.setCanceledOnTouchOutside(false);
        barraDeProgresso.show();

        fb = new Firebase();

        fb.getFirebaseAuth().signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            definirUsuario();

                        } else {

                            limparCampos();
                            barraDeProgresso.dismiss();
                            Toast.makeText(Login.this, "Erro ao realizar Login.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    private void definirUsuario() {
        carregarUsuarios();
        carregarMotoristas();
        buscarAdministrador();


    }

    private void carregarUsuarios() {
        fb = new Firebase();

        fb.getDatabaseUsuario().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {

                    Usuario usuario = objSnapshot.getValue(Usuario.class);

                    String email = usuario.getEmail();
                    verificarTipoDeUsuario(email);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void verificarTipoDeUsuario(String email) {
        if (email.equals(this.email)) {
            carregarUsuario();
        }
    }

    private void carregarUsuario() {

        this.ator = "usuario";
        SharedPreferences.Editor editor = getSharedPreferences(dadosDoLoginUsuario, MODE_PRIVATE).edit();
        editor.putString("EMAIL", email);
        editor.putString("ATOR", ator);
        editor.commit();
        Intent it = new Intent(Login.this, UsuarioHome.class);
        it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK | it.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);

    }

    private void loginUsuarioSalvo() {

        SharedPreferences pref = getSharedPreferences(AppInicializar, MODE_PRIVATE);
        boolean telaEspera = pref.getBoolean("ESPERA", false);
        boolean telaFreteAceito = pref.getBoolean("ACEITO", false);


        if (telaEspera) {

            Intent it = new Intent(Login.this, TeladeEspera.class);
            //  it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK | it.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            finish();

        } else if (telaFreteAceito) {
            Intent it = new Intent(Login.this, UsuarioFreteAceito.class);
            // it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK | it.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            finish();
        } else {
            carregarUsuarioHome();
        }

    }

    private void carregarUsuarioHome() {
        SharedPreferences prefs = getSharedPreferences(dadosDoLoginUsuario, MODE_PRIVATE);
        email = prefs.getString("EMAIL", null);
        ator = prefs.getString("ATOR", null);
        if (email != null && ator != null) {

            Intent it = new Intent(Login.this, UsuarioHome.class);
            // it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK | it.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            finish();
        }

    }


    private void carregarMotoristas() {
        fb = new Firebase();
        fb.getDatabaseMotorista().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {

                    Motorista motorista = objSnapshot.getValue(Motorista.class);

                    String email = motorista.getEmail();
                    verificarMotorista(email);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void verificarMotorista(String email) {
        if (email.equals(this.email)) {
            carregarMotorista();
        }
    }


    private void carregarMotorista() {

        this.ator = "motorista";
        SharedPreferences.Editor editor = getSharedPreferences(dadosDologinMotorista, MODE_PRIVATE).edit();
        editor.putString("EMAIL", email);
        editor.putString("ATOR", ator);
        editor.commit();
        Intent it = new Intent(Login.this, MotoristaHome.class);
        it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK | it.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);

    }

    public void loginMotoristaSalvo() {

        SharedPreferences prefs = getSharedPreferences(dadosDologinMotorista, MODE_PRIVATE);
        email = prefs.getString("EMAIL", null);
        ator = prefs.getString("ATOR", null);

        SharedPreferences pref = getSharedPreferences(AppInicializar, MODE_PRIVATE);
        String id = pref.getString("ID", null);

        if (id != null) {

            Intent it = new Intent(Login.this, FreteMotorista.class);
            // it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            finish();

        } else if (email != null && ator != null) {

            Intent it = new Intent(Login.this, MotoristaHome.class);
            // it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            finish();
        }
    }


    private void buscarAdministrador() {

        fb = new Firebase();

        fb.getDatabaseAdministrador().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objsnapshot:dataSnapshot.getChildren()){


                    Administrador administrador=objsnapshot.getValue(Administrador.class);

                   String em = administrador.getEmail();
                   String se = administrador.getSenha();

                   if(em.equals(email)&&se.equals(senha)){

                       carregarTelaAdministrador();


                   }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void carregarTelaAdministrador() {

       Intent intent = new Intent(Login.this, AdministradorHome.class);
       startActivity(intent);
       finish();
    }

    public void limparCampos(){

        Senha.setText(" ");

    }
}






