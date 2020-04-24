package com.example.diskfrete.Motorista;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diskfrete.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroMotoristas extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String nomeCompleto ,cpf,telefone,email,placaDoVeiculo,tipoDeVeiculo,tipoDeCarroceria,senha,confiSenha;
    private DatabaseReference database;
    private ProgressDialog barraDeProgresso;
    public static final  String Motorista_PREFERENCES="login automatico";
    private ValidarCpf validarCpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motoristas);

        mAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference("Motoristas");
        barraDeProgresso = new ProgressDialog(this);


        final EditText ednomeCompleto = (EditText)findViewById(R.id.editText2);
        final EditText edCpf=(EditText)findViewById(R.id.editText4);
        final EditText edTelefone=(EditText)findViewById(R.id.editText5);
        final EditText edmail = (EditText)findViewById(R.id.editText3) ;
        final EditText edPlaca = (EditText)findViewById(R.id.editText10) ;
        final EditText edSenha=(EditText)findViewById(R.id.editText6);
        final EditText edveiculo = (EditText)findViewById(R.id.editText9) ;
        final EditText edcarroceria = (EditText)findViewById(R.id.editText11) ;
        final EditText edConfSenha = (EditText)findViewById(R.id.editText7);
        final  Button btcadastrar =(Button)findViewById(R.id.button2);


        SharedPreferences prefs=getSharedPreferences(Motorista_PREFERENCES,MODE_PRIVATE);
        cpf=prefs.getString("cpf",null);
        if(cpf!=null){

            Intent it = new Intent(CadastroMotoristas.this, MotoristaDiskFrete.class);
            it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        }


        btcadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomeCompleto = ednomeCompleto.getText().toString().trim();
                cpf=edCpf.getText().toString().trim();
                telefone=edTelefone.getText().toString().trim();
                email = edmail.getText().toString().trim();
                placaDoVeiculo= edPlaca.getText().toString().trim();
                tipoDeVeiculo=edveiculo.getText().toString().trim();
                tipoDeCarroceria=edcarroceria.getText().toString().trim();
                senha = edSenha.getText().toString().trim();
                confiSenha=edConfSenha.getText().toString().trim();





                if(nomeCompleto.length()<=3){
                    Toast.makeText(CadastroMotoristas.this, "por favor inserir nome completo.",
                            Toast.LENGTH_SHORT).show();
                }

                   else if (validarCpf.isCPF(cpf) == false) {

                    Toast.makeText(CadastroMotoristas.this, "cpf Inválido",
                            Toast.LENGTH_SHORT).show();

                }

                else if(telefone.length()<9) {
                    Toast.makeText(CadastroMotoristas.this, "telefone incompleto",
                            Toast.LENGTH_SHORT).show();

                }else if(placaDoVeiculo.length()==0){
                    Toast.makeText(CadastroMotoristas.this, "inserir placa do veiculo!",
                            Toast.LENGTH_SHORT).show();

                }
                else if(tipoDeVeiculo.length()==0) {
                    Toast.makeText(CadastroMotoristas.this, "por favor descreva o modelo do seu veiculo",
                            Toast.LENGTH_SHORT).show();

                }
                else if(tipoDeCarroceria.length()==0){
                    Toast.makeText(CadastroMotoristas.this, "por favor descreva o tipo de carroceria do seu veiculo",
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

                }else{Toast.makeText(CadastroMotoristas.this, "email inválido ! ",
                        Toast.LENGTH_SHORT).show();}





            }

            private void  CadastrarMotorista(){
                barraDeProgresso.setTitle("");
                barraDeProgresso.setMessage("conectando...");
                barraDeProgresso.setCanceledOnTouchOutside(false);
                barraDeProgresso.show();
                mAuth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(CadastroMotoristas.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    carregarMotorista();

                                } else {
                                    barraDeProgresso.dismiss();
                                    Toast.makeText(CadastroMotoristas.this, "Erro no cadastro!.",
                                            Toast.LENGTH_SHORT).show();

                                }


                            }
                        });
            }



            private void carregarMotorista() {
                SharedPreferences.Editor editor = getSharedPreferences( Motorista_PREFERENCES,MODE_PRIVATE).edit();
                editor.putString("cpf",cpf);
                editor.commit();
                Intent it = new Intent(CadastroMotoristas.this, MotoristaDiskFrete.class);
                it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);
                Toast.makeText(CadastroMotoristas.this, "Cadastro efetuado com susseso", Toast.LENGTH_SHORT).show();
                CadastrarMotoristaNoBanco();

            }

            private void CadastrarMotoristaNoBanco() {
                String id = database.push().getKey();
                Motorista motorista = new Motorista(id,nomeCompleto ,cpf,telefone,email,placaDoVeiculo,tipoDeVeiculo,tipoDeCarroceria);
                database.child(id).setValue(motorista);


            }
        });





    }


}
