package com.example.diskfrete;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static com.example.diskfrete.LoginActivity.Motorista_PREFERENCES;

public class CadastroMotoristas extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private String nomeCompleto, cpf, telefone, email, placaDoVeiculo, tipoDeVeiculo, VolumeMaximo, senha, confiSenha, fotoPerfil, fotoCNH=" ", fotoCRLV=" ";
    private DatabaseReference database;
    private ProgressDialog barraDeProgresso;
    private ValidarCpf validarCpf;
    EditText ednomeCompleto,edCpf, edTelefone,edmail,edPlaca,edSenha,edveiculo,edpesoVolumeMaximo,edConfSenha;
    Button capturarDaGaleria,btcadastrar;
    ImageView imagem;
    Uri perfilImagem;
    StorageReference refPerfil;
    Bitmap bit;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_motoristas);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Motoristas");
        filename = database.push().getKey();
        refPerfil = FirebaseStorage.getInstance().getReference("/MotoristasPerfil/" + filename);
        barraDeProgresso = new ProgressDialog(this);


         ednomeCompleto = (EditText)findViewById(R.id.editText2);
         edCpf=(EditText)findViewById(R.id.editText4);
         edTelefone=(EditText)findViewById(R.id.editText5);
         edmail = (EditText)findViewById(R.id.editText3) ;
         edPlaca = (EditText)findViewById(R.id.editText10) ;
         edSenha=(EditText)findViewById(R.id.editText6);
         edveiculo = (EditText)findViewById(R.id.editText9) ;
         edpesoVolumeMaximo = (EditText)findViewById(R.id.editText11) ;
         edConfSenha = (EditText)findViewById(R.id.editText7);
         btcadastrar =(Button)findViewById(R.id.button2);

        capturarDaGaleria = (Button) findViewById(R.id.button7);
        imagem = (ImageView) findViewById(R.id.imageView3);



        capturarDaGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                carregarImagemPerfil();

            }


        });




        btcadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();


            }
        });


    }

    private void validarCampos() {

        nomeCompleto = ednomeCompleto.getText().toString().trim();
        cpf=edCpf.getText().toString().trim();
        telefone=edTelefone.getText().toString().trim();
        email = edmail.getText().toString().trim();
        placaDoVeiculo= edPlaca.getText().toString().trim();
        tipoDeVeiculo=edveiculo.getText().toString().trim();
        VolumeMaximo=edpesoVolumeMaximo.getText().toString().trim();
        senha = edSenha.getText().toString().trim();
        confiSenha=edConfSenha.getText().toString().trim();





        if(nomeCompleto.length()<=8){
            Toast.makeText(CadastroMotoristas.this, "por favor inserir nome completo.",
                    Toast.LENGTH_SHORT).show();

        }else if (perfilImagem == null ) {

            Toast.makeText(CadastroMotoristas.this, "Definir foto de Perfil",
                    Toast.LENGTH_SHORT).show();

        }else if (validarCpf.isCPF(cpf) == false) {

            Toast.makeText(CadastroMotoristas.this, "cpf Inválido",
                    Toast.LENGTH_SHORT).show();

        }else if(telefone.length()<9) {
            Toast.makeText(CadastroMotoristas.this, "telefone incompleto",
                    Toast.LENGTH_SHORT).show();

        }else if(placaDoVeiculo.length()==0){
            Toast.makeText(CadastroMotoristas.this, "inserir placa do veiculo!",
                    Toast.LENGTH_SHORT).show();


        }else if(tipoDeVeiculo.length()==0) {
            Toast.makeText(CadastroMotoristas.this, "por favor descreva o modelo do seu veiculo",
                    Toast.LENGTH_SHORT).show();

        }else if(VolumeMaximo.length()==0){
            Toast.makeText(CadastroMotoristas.this, "por favor descreva peso/volume máximo do veículo",
                    Toast.LENGTH_SHORT).show();

        }else if(email.length()== 0){
            Toast.makeText(CadastroMotoristas.this, "Email vazio!",
                    Toast.LENGTH_SHORT).show();

        }else if (senha.length()== 0){
            Toast.makeText(CadastroMotoristas.this, " insera a senha. ",
                    Toast.LENGTH_SHORT).show();

        }else if (!senha.equals(confiSenha)){
            Toast.makeText(CadastroMotoristas.this, "as senhas não correspondem. ",
                    Toast.LENGTH_SHORT).show();

        }else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            CadastrarMotorista();

        }else{Toast.makeText(CadastroMotoristas.this, "email inválido ! ",
                Toast.LENGTH_SHORT).show();}


    }



    private void carregarImagemPerfil() {

        Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
       // it.setType("images/*");
        startActivityForResult(Intent.createChooser(it, "Selecione uma imagem"), 0);



    }



    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_OK) {
            if (requestCode == 0) {
                perfilImagem = data.getData();

                //  subirParaFirestore();
                capturarDaGaleria.setBackgroundColor(Color.TRANSPARENT);



                try {
                   //  bit = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemSelecionada);
                    // imagem.setImageDrawable(new BitmapDrawable(bit));

                     Picasso.get()
                             .load(perfilImagem)
                             .resize(500,500)
                             .centerCrop()
                             .into(imagem);

                }catch (Exception e){}


            }
        }
    }

    private void CadastrarMotorista() {
        barraDeProgresso.setTitle("CONECTANDO");
        barraDeProgresso.setMessage("aguarde...");
        barraDeProgresso.setCanceledOnTouchOutside(false);
        barraDeProgresso.show();
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(CadastroMotoristas.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            subirPerfilMotoristaFirestore();

                        } else {
                            barraDeProgresso.dismiss();
                            Toast.makeText(CadastroMotoristas.this, "Erro ao iniciar cadastro!",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });


    }




    private void subirPerfilMotoristaFirestore() {

        // String filename = UUID.randomUUID().toString();
        refPerfil.putFile(perfilImagem).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                refPerfil.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        fotoPerfil = uri.toString();
                        CadastrarMotoristaNoBanco();
                    }

                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CadastroMotoristas.this, " Erro ao processar Imagem do perfil",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void CadastrarMotoristaNoBanco() {
        String id = database.push().getKey();
        Motorista motorista = new Motorista(id,nomeCompleto,cpf,telefone,email,placaDoVeiculo,tipoDeVeiculo,VolumeMaximo,fotoPerfil,fotoCNH,fotoCRLV);
        database.child(id).setValue(motorista);


        motoristaNext(id);
    }

    private void motoristaNext(String uid) {

        Intent it = new Intent(CadastroMotoristas.this, CadastroMotoristas1.class);
        it.setFlags(it.FLAG_ACTIVITY_CLEAR_TASK | it.FLAG_ACTIVITY_NEW_TASK);
        it.putExtra("ID",uid);
        startActivity(it);

    }
}
