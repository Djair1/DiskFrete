package com.example.diskfrete;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioHome extends AppCompatActivity {

EditText informacoes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_home);


        informacoes =(EditText)findViewById(R.id.editText4);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
           // String name = user.getDisplayName();

            String email = user.getEmail();
          //  Uri photoUrl = user.getPhotoUrl();

            boolean emailVerified = user.isEmailVerified();

            String uid = user.getUid();
informacoes.setText(email+" "+uid );

        } else {
            Toast.makeText(UsuarioHome.this, "Erro ao buscar informações.",
                    Toast.LENGTH_SHORT).show();
        }




}
}
