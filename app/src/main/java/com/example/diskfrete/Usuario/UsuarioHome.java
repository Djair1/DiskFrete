package com.example.diskfrete.Usuario;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diskfrete.Motorista.Motorista;
import com.example.diskfrete.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.*;

public class UsuarioHome extends AppCompatActivity {


    private DatabaseReference database;
    private ProgressDialog barraDeProgresso;
    private  GroupAdapter adapter;
    private RecyclerView lista;
    private List<Motorista> listaMotorista = new ArrayList<>();
    int posicao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_home);
        database= FirebaseDatabase.getInstance().getReference("Motoristas");
        barraDeProgresso = new ProgressDialog(this);


        lista = findViewById(R.id.ListaMotorista);

       adapter = new GroupAdapter();
       lista.setAdapter(adapter);
       lista.setLayoutManager(new LinearLayoutManager(this));


         carregarMotoristas();

         adapter.setOnItemClickListener(new OnItemClickListener() {
             @Override
             public void onItemClick(@NonNull Item item, @NonNull View view) {
                 carregarDadosDoMotorista(item);
                // posicao=adapter.getAdapterPosition(item);
               //  Motorista mt = listaMotorista.get(posicao);
               //  String nome= mt.getNomeCompleto();
               //  Toast.makeText(UsuarioHome.this, posicao+" posicao na lista -->"+nome,
                 //        Toast.LENGTH_SHORT).show();

             }
         });

}

    private void carregarDadosDoMotorista(Item item) {
        posicao=adapter.getAdapterPosition(item);
        Motorista mt = listaMotorista.get(posicao);
        String nome= mt.getNomeCompleto();
        Toast.makeText(UsuarioHome.this, posicao+" posicao na lista -->"+nome,
                Toast.LENGTH_SHORT).show();
    }

    private void buscarCurrentUser() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // String name = user.getDisplayName();
            String email = user.getEmail();
            //  Uri photoUrl = user.getPhotoUrl();
            boolean emailVerified = user.isEmailVerified();
            String uid = user.getUid();

        } else {
            Toast.makeText(UsuarioHome.this, "Erro ao buscar informações.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void progressobarra() {
        barraDeProgresso.setTitle("Bem Vindo");
        barraDeProgresso.setMessage("carregando...");
        barraDeProgresso.setCanceledOnTouchOutside(false);
        barraDeProgresso.show();
    }

    private void carregarMotoristas() {
        progressobarra();
        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaMotorista.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {

                    Motorista motorista = objSnapshot.getValue(Motorista.class);
                    listaMotorista.add(motorista);
                    adapter.add(new itemMotorista( motorista));

 }

                barraDeProgresso.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public class itemMotorista extends Item<ViewHolder>{
private Motorista motorista;

private itemMotorista (Motorista motorista ){
    this.motorista=motorista;
}

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int i) {
           TextView texto= viewHolder.itemView.findViewById(R.id.textView4);
         ImageView fotoMotorista = viewHolder.itemView.findViewById(R.id.imageView2);

         texto.setText(motorista.getNomeCompleto());
            Picasso.get().load(motorista.getFotoPerfil()).into( fotoMotorista);



        }

        @Override
        public int getLayout() {
            return  R.layout.item_motorista;
        }
    }


}
