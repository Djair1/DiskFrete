package com.example.diskfrete.Controller.USUARIO;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.diskfrete.Model.Usuario;
import com.example.diskfrete.R;
import com.example.diskfrete.db.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.diskfrete.Model.DadosPreferences.dadosDoLoginUsuario;

public class CadastrarUsuario extends AppCompatActivity {

private String nomeCompleto ,telefone,email,senha,confiSenha;
private ProgressDialog barraDeProgresso;
private Firebase firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);
        setTitle("Cadastro Usuario");

        barraDeProgresso = new ProgressDialog(this);

        final EditText ednomeCompleto = (EditText)findViewById(R.id.editText2);
        final EditText edTelefone=(EditText)findViewById(R.id.editText6);
        final EditText edmail = (EditText)findViewById(R.id.editText3) ;
         final EditText edSenha=(EditText)findViewById(R.id.editText);
         final EditText edConfSenha = (EditText)findViewById(R.id.editText5);


      Button cadastrar = (Button)findViewById(R.id.button2);



        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edmail.getText().toString().trim();
                senha = edSenha.getText().toString().trim();
                nomeCompleto = ednomeCompleto.getText().toString().trim();
               telefone= edTelefone.getText().toString().trim();
                confiSenha=edConfSenha.getText().toString().trim();



                if(nomeCompleto.length()<=3){
                    Toast.makeText(CadastrarUsuario.this, "por favor inserir nome completo.",
                            Toast.LENGTH_SHORT).show();
                }
                else if(telefone.length()==0){
                    Toast.makeText(CadastrarUsuario.this, "Campo telefone vazio",
                            Toast.LENGTH_SHORT).show();
                }


                else if(email.length()== 0){
                    Toast.makeText(CadastrarUsuario.this, "Campo Email vazio!",
                            Toast.LENGTH_SHORT).show();
                }

                else if (senha.length()== 0){
                    Toast.makeText(CadastrarUsuario.this, "entre com a senha. ",
                            Toast.LENGTH_SHORT).show();
                }
                else if (senha.length()< 6){
                    Toast.makeText(CadastrarUsuario.this, "inserir senha com pelo menos 6 Digitos. ",
                            Toast.LENGTH_SHORT).show();
                }

               else if (!senha.equals(confiSenha)){
                    Toast.makeText(CadastrarUsuario.this, "as senhas não correspondem. ",
                            Toast.LENGTH_SHORT).show();
                }
               else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    CadastrarUsuario();

                }else{Toast.makeText(CadastrarUsuario.this, " email inválido ! ",
                        Toast.LENGTH_SHORT).show();}





            }

            private void  CadastrarUsuario(){
                barraDeProgresso.setTitle("");
                barraDeProgresso.setMessage("conectando...");
                barraDeProgresso.setCanceledOnTouchOutside(false);
                barraDeProgresso.show();
                firebase = new Firebase();
                firebase.getFirebaseAuth().createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(CadastrarUsuario.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    CadastrarUsuarioNoBanco();

                                } else {
                                    barraDeProgresso.dismiss();
                                    Toast.makeText(CadastrarUsuario.this, "Erro no cadastro !.",
                                            Toast.LENGTH_SHORT).show();

                                }


                            }
                        });
            }



            private void carregarUsuario() {

                Intent it = new Intent(CadastrarUsuario.this, UsuarioHome.class);
                it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                Toast.makeText(CadastrarUsuario.this, "Cadastro efetuado com susseso", Toast.LENGTH_SHORT).show();

            }

            private void CadastrarUsuarioNoBanco() {
                String ator="usuario";
                SharedPreferences.Editor editor = getSharedPreferences(dadosDoLoginUsuario,MODE_PRIVATE).edit();
                editor.putString("EMAIL",email);
                editor.putString("ATOR",ator);
                editor.commit();

                firebase= new Firebase();



                String id = firebase.getDatabaseUsuario().push().getKey();
                Usuario usuario = new Usuario(id,nomeCompleto,telefone,email);
                firebase.persistirUsuario(usuario);

                carregarUsuario();

            }
        });

        }


}