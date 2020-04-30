package com.example.diskfrete.Usuario;

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
import com.example.diskfrete.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastrarUsuarioActivity extends AppCompatActivity {

private FirebaseAuth mAuth;
private String nomeCompleto ,cidade,email,senha,confiSenha;
private DatabaseReference database;
    public static final  String Usuario_PREFERENCES="login automatico";
    private ProgressDialog barraDeProgresso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference("Usuarios");
        barraDeProgresso = new ProgressDialog(this);



        final EditText ednomeCompleto = (EditText)findViewById(R.id.editText2);
        final EditText edCidade=(EditText)findViewById(R.id.editText6);

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
               cidade= edCidade.getText().toString().trim();
                confiSenha=edConfSenha.getText().toString().trim();



                if(nomeCompleto.length()<=3){
                    Toast.makeText(CadastrarUsuarioActivity.this, "por favor inserir nome completo.",
                            Toast.LENGTH_SHORT).show();
                }
                else if(cidade.length()==0){
                    Toast.makeText(CadastrarUsuarioActivity.this, "Campo cidade vazio",
                            Toast.LENGTH_SHORT).show();
                }


                else if(email.length()== 0){
                    Toast.makeText(CadastrarUsuarioActivity.this, "Campo Email vazio!",
                            Toast.LENGTH_SHORT).show();
                }

                else if (senha.length()== 0){
                    Toast.makeText(CadastrarUsuarioActivity.this, "entre com a senha. ",
                            Toast.LENGTH_SHORT).show();
                }
                else if (senha.length()< 6){
                    Toast.makeText(CadastrarUsuarioActivity.this, "inserir senha com pelo menos 6 Digitos. ",
                            Toast.LENGTH_SHORT).show();
                }

               else if (!senha.equals(confiSenha)){
                    Toast.makeText(CadastrarUsuarioActivity.this, "as senhas não correspondem. ",
                            Toast.LENGTH_SHORT).show();
                }
               else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    CadastrarUsuario();

                }else{Toast.makeText(CadastrarUsuarioActivity.this, " email inválido ! ",
                        Toast.LENGTH_SHORT).show();}





            }

            private void  CadastrarUsuario(){
                barraDeProgresso.setTitle("");
                barraDeProgresso.setMessage("conectando...");
                barraDeProgresso.setCanceledOnTouchOutside(false);
                barraDeProgresso.show();
                mAuth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(CadastrarUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    carregarUsuario();

                                } else {
                                    barraDeProgresso.dismiss();
                                    Toast.makeText(CadastrarUsuarioActivity.this, "Erro no cadastro !.",
                                            Toast.LENGTH_SHORT).show();

                                }


                            }
                        });
            }



            private void carregarUsuario() {
                SharedPreferences.Editor editor = getSharedPreferences(Usuario_PREFERENCES,MODE_PRIVATE).edit();
                editor.putString("email",email);
                editor.commit();
                Intent it = new Intent(CadastrarUsuarioActivity.this, UsuarioHome.class);
                it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                Toast.makeText(CadastrarUsuarioActivity.this, "Cadastro efetuado com susseso", Toast.LENGTH_SHORT).show();
                CadastrarUsuarioNoBanco();
            }

            private void CadastrarUsuarioNoBanco() {
                String id = database.push().getKey();
                Usuario usuario = new Usuario(id,nomeCompleto,cidade,email);
                database.child(id).setValue(usuario);


            }
        });

        }


}