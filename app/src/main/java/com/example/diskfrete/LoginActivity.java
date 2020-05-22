package com.example.diskfrete;

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


import com.example.diskfrete.MOTORISTA.*;
import com.example.diskfrete.USUARIO.TeladeEspera;
import com.example.diskfrete.USUARIO.Usuario;
import com.example.diskfrete.USUARIO.UsuarioHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import static com.example.diskfrete.USUARIO.CadastrarFrete.Usuario_Solicitacao_concluida;


public class LoginActivity extends AppCompatActivity {

    EditText Email, Senha;
    private String email,senha;
    private String emailM,ator;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseMotorista,databaseUsuario;
    public static final  String Usuario_PREFERENCES="preferencias do usuario";
    public static final  String Motorista_PREFERENCES="preferencias do motorista";
    private ProgressDialog barraDeProgresso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("DiskFrete");
        firebaseAuth = FirebaseAuth.getInstance();


        databaseUsuario = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseMotorista= FirebaseDatabase.getInstance().getReference("Motoristas");

        Button cadastrar = (Button) findViewById(R.id.button4);
        Button entrar = (Button) findViewById(R.id.button2);
        Button ResetarSenha = (Button) findViewById(R.id.button11);
        Email = (EditText) findViewById(R.id.editText2);
        Senha = (EditText) findViewById(R.id.editText3);
        barraDeProgresso = new ProgressDialog(this);


        carregarSolicitacaoDoUsuario();
        chamarMotoristajaCadastrado();



        //ação do botão cadastre-se
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this, DialogoCadastro.class);
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

                validarcampos();


            }





        });



 }



 public void  validarcampos(){

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
                            definirUsuario();

                        } else {

                            barraDeProgresso.dismiss();
                            Toast.makeText(LoginActivity.this, "Erro ao realizar Login.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

  public void  definirUsuario(){
      carregarUsuarios();
      carregarMotoristas();


  }

    private void carregarUsuarios() {

        databaseUsuario.addValueEventListener(new ValueEventListener() {

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
        if(email.equals(this.email)){
            carregarUsuario();
        }
    }

    private void carregarUsuario() {
        //    startActivity(new Intent(getBaseContext(),Trasicao.class));
        this.ator="usuario";
        SharedPreferences.Editor editor = getSharedPreferences(Usuario_PREFERENCES,MODE_PRIVATE).edit();
        editor.putString("EMAIL",email);
        editor.putString("ATOR",ator);
        editor.commit();
        Intent it = new Intent(LoginActivity.this, UsuarioHome.class);
        it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK | it.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);

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
        email=prefs.getString("EMAIL",null);
        ator=prefs.getString("ATOR",null);
        if(email!=null&&ator!=null){

            Intent it = new Intent(LoginActivity.this, UsuarioHome.class);
            //  it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK | it.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            finish();
        }
     }


    private void carregarMotoristas() {

        databaseMotorista.addValueEventListener(new ValueEventListener() {

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
        if(email.equals(this.email)){
            carregarMotorista();
        }
    }




    private void  carregarMotorista() {

        this.ator="motorista";
        SharedPreferences.Editor editor = getSharedPreferences( Motorista_PREFERENCES,MODE_PRIVATE).edit();
        editor.putString("EMAIL",email);
        editor.putString("ATOR",ator);
        editor.commit();
        Intent it = new Intent(LoginActivity.this, MotoristaHome.class);
      //  it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
        finish();
    }

    private void  chamarMotoristajaCadastrado(){
        SharedPreferences prefs=getSharedPreferences(Motorista_PREFERENCES,MODE_PRIVATE);
        email=prefs.getString("EMAIL",null);
        ator=prefs.getString("ATOR",null);
        if(email!=null&&ator!=null){

            Intent it = new Intent(LoginActivity.this, MotoristaHome.class);
          //  it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            finish();
        }

}
}






