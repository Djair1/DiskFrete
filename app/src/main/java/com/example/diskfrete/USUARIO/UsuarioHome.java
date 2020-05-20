package com.example.diskfrete.USUARIO;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diskfrete.LoginActivity;
import com.example.diskfrete.MOTORISTA.Motorista;
import com.example.diskfrete.R;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.*;

import static com.example.diskfrete.LoginActivity.Usuario_PREFERENCES;

public class UsuarioHome extends AppCompatActivity {


    private DatabaseReference database;
    private ProgressDialog barraDeProgresso;
    private  GroupAdapter adapter;
    private RecyclerView lista;
    private List<Motorista> listaMotorista = new ArrayList<>();
    int posicao;
    private FloatingActionMenu BotaoMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_home);




        database= FirebaseDatabase.getInstance().getReference("Motoristas");
        barraDeProgresso = new ProgressDialog(this);

        BotaoMenu =findViewById(R.id.floatingActionButton1);
        BotaoMenu.setClosedOnTouchOutside(true);
        lista = findViewById(R.id.ListaMotorista);
        adapter = new GroupAdapter();
        lista.setAdapter(adapter);
        lista.setLayoutManager(new LinearLayoutManager(this));



        carregarMotoristas();





        //  Snackbar.make(v, "Seu " ,Snackbar.LENGTH_LONG).setAction("Action",null).show();




         adapter.setOnItemClickListener(new OnItemClickListener() {
             @Override
             public void onItemClick(@NonNull Item item, @NonNull View view) {
                 chamaMotorista(item);


             }
         });

}

  //  @Override
  //  public void onBackPressed() {
   //     super.finish();
   // }

public void logoutUsuario(View view){
    SharedPreferences.Editor editor = getSharedPreferences(Usuario_PREFERENCES,MODE_PRIVATE).edit();
    editor.putString("EMAIL",null);
    editor.putString("ATOR",null);
    editor.commit();
    FirebaseAuth.getInstance().signOut();
    Intent it = new Intent(UsuarioHome.this, LoginActivity.class);
    startActivity(it);
    finish();
}



public void historicoDfretes(View view){

}




    private void chamaMotorista(Item item) {
        posicao=adapter.getAdapterPosition(item);
        Motorista mt = listaMotorista.get(posicao);
        Intent it = new Intent(UsuarioHome.this, CadastrarFrete.class) ;
        it.putExtra("MOTORISTAEMAIL",mt.getEmail());
        it.putExtra("NOME",mt.getNomeCompleto());
        it.putExtra("PLACA",mt.getPlacaDoVeiculo());
        it.putExtra("TELEFONE",mt.getTelefone());
        it.putExtra("VOLUME",mt.getVolumeMaximo());
        it.putExtra("FOTOPERFIL",mt.getFotoPerfil());
        it.putExtra("VEICULO",mt.getTipoDeVeiculo());
        startActivity(it);
       // String nome= mt.getNomeCompleto();
       // Toast.makeText(UsuarioHome.this, "Adapter ->"+posicao+" posicao na lista ->"+nome,
        //        Toast.LENGTH_SHORT).show();
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

public void barradProgresso(boolean resposta,String title){
        if(resposta==true){

            barraDeProgresso.setTitle(title);
            barraDeProgresso.setMessage("carregando...");
            barraDeProgresso.setCanceledOnTouchOutside(false);
            barraDeProgresso.show();

        }else if(resposta==false){barraDeProgresso.dismiss();}

}




    private void carregarMotoristas() {

        barradProgresso(true,"BEM VINDO...");
        database.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaMotorista.clear();
                adapter.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {


                    Motorista motorista = objSnapshot.getValue(Motorista.class);
                    listaMotorista.add(motorista);
                    adapter.add(new itemMotorista( motorista));

 }


                barradProgresso(false,"");
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
           TextView texto1= viewHolder.itemView.findViewById(R.id.textView4);
           TextView texto2= viewHolder.itemView.findViewById(R.id.textView16);
           TextView texto3= viewHolder.itemView.findViewById(R.id.textView17);
         ImageView fotoMotorista = viewHolder.itemView.findViewById(R.id.imageView2);

         texto2.setText(motorista.getTipoDeVeiculo());
         texto1.setText(motorista.getNomeCompleto());
         texto3.setText(motorista.getVolumeMaximo());
            Picasso.get().load(motorista.getFotoPerfil()).resize(500,500).centerCrop().into( fotoMotorista);



        }

        @Override
        public int getLayout() {
            return  R.layout.item_motorista;
        }
    }

}
