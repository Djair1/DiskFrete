package com.example.diskfrete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroMotoristas extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String nome ,sobrenome ,email,cnh,senha,confiSenha;
    private DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motoristas);

        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference("Motoristas");


        final EditText ednome = (EditText)findViewById(R.id.editText2);
        final EditText edSobrenome=(EditText)findViewById(R.id.editText4);
        final EditText edCnh=(EditText)findViewById(R.id.editText5);
        final EditText edmail = (EditText)findViewById(R.id.editText3) ;
        final EditText edSenha=(EditText)findViewById(R.id.editText6);
        final EditText edConfSenha = (EditText)findViewById(R.id.editText7);
        final  Button btcadastrar =(Button)findViewById(R.id.button2);

        btcadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edmail.getText().toString().trim();
                senha = edSenha.getText().toString().trim();
                nome = ednome.getText().toString().trim();
                cnh=edCnh.getText().toString().trim();
                sobrenome= edSobrenome.getText().toString().trim();
                confiSenha=edConfSenha.getText().toString().trim();



                if(nome.length()<=3){
                    Toast.makeText(CadastroMotoristas.this, "nome inválido ou vazio.",
                            Toast.LENGTH_SHORT).show();
                }
                else if(sobrenome.length()>25||sobrenome.length()==0){
                    Toast.makeText(CadastroMotoristas.this, "sobrenome inválido ou vazio",
                            Toast.LENGTH_SHORT).show();
                }
                else if(cnh.length()>11||cnh.length()==0){
                    Toast.makeText(CadastroMotoristas.this, "CNH vazio!",
                            Toast.LENGTH_SHORT).show();
                }

                else if(email.length()== 0){
                    Toast.makeText(CadastroMotoristas.this, "Email vazio!",
                            Toast.LENGTH_SHORT).show();
                }

                else if (senha.length()== 0){
                    Toast.makeText(CadastroMotoristas.this, " insera a senha. ",
                            Toast.LENGTH_SHORT).show();
                }

                else if (!senha.equals(confiSenha)){
                    Toast.makeText(CadastroMotoristas.this, "as senhas não correspondem. ",
                            Toast.LENGTH_SHORT).show();
                }
                else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    CadastrarMotorista();

                }else{Toast.makeText(CadastroMotoristas.this, "formato de email inválido ! ",
                        Toast.LENGTH_SHORT).show();}





            }

            private void  CadastrarMotorista(){
                mAuth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(CadastroMotoristas.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    carregarMotorista();

                                } else {
                                    Toast.makeText(CadastroMotoristas.this, "Erro ao cadastrar!.",
                                            Toast.LENGTH_SHORT).show();

                                }


                            }
                        });
            }



            private void carregarMotorista() {
                Intent it = new Intent(CadastroMotoristas.this, UsuarioHome.class);
                it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                Toast.makeText(CadastroMotoristas.this, "Motorista Cadastrado com susseso", Toast.LENGTH_SHORT).show();
                CadastrarMotoristaNoBanco();

            }

            private void CadastrarMotoristaNoBanco() {
                String id = database.push().getKey();
                Motorista motorista = new Motorista(id,nome,sobrenome,cnh,email);
                database.child(id).setValue(motorista);


            }
        });





    }
}
