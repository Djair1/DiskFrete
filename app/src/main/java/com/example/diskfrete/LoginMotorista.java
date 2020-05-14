package com.example.diskfrete;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class LoginMotorista extends AppCompatActivity {

    EditText Email, Senha,Cpf;
    Button Entrar;
    private String email,senha,cpf;
    FirebaseAuth firebaseAuth;
    private DatabaseReference database;
    private ProgressDialog barraDeProgresso;
    public static final  String Motorista_PREFERENCES="login automatico";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_motorista);


        Email =(EditText)findViewById(R.id.editText2);
        Senha =(EditText)findViewById(R.id.editText3);
        Cpf =(EditText)findViewById(R.id.editText4);
        Entrar=(Button)findViewById(R.id.button2);
        Button cadastrar = (Button)findViewById(R.id.button4);
        Button ResetarSenha=(Button)findViewById(R.id.button3);



        firebaseAuth=FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance().getReference("Motoristas");
        barraDeProgresso = new ProgressDialog(this);


        SharedPreferences prefs=getSharedPreferences(Motorista_PREFERENCES,MODE_PRIVATE);
        cpf=prefs.getString("cpf",null);
        if(cpf!=null){

            Intent it = new Intent(LoginMotorista.this, MotoristaHome.class);
            it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        }



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
                 cpf = Cpf.getText().toString().trim();


                if(email.length()== 0){
                    Toast.makeText(LoginMotorista.this, "Email Invalido ou vazio!",
                            Toast.LENGTH_SHORT).show();
                }
             else if(cpf.length()==0){
                    Toast.makeText(LoginMotorista.this, "CPF Invalido ou vazio!",
                            Toast.LENGTH_SHORT).show();
                }


                else if (senha.length()== 0){
                    Toast.makeText(LoginMotorista.this, " inserir senha. ",
                            Toast.LENGTH_SHORT).show();
                }else{ carregarMotoristasDoFirebase();}

            }

            private void validarCpf(String e,String c) {
                if (email.equals(e) && cpf.equals(c)){
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

                            validarCpf(motorista.getEmail(),motorista.getCpf());

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }



            private void LogarMotorista(){
                barraDeProgresso.setTitle("");
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
                SharedPreferences.Editor editor = getSharedPreferences( Motorista_PREFERENCES,MODE_PRIVATE).edit();
                editor.putString("cpf",cpf);
                editor.commit();
                Intent it = new Intent(LoginMotorista.this, MotoristaHome.class);
                it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK| it.FLAG_ACTIVITY_NEW_TASK);
                startActivity(it);

            }



});
    }




}