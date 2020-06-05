package com.example.diskfrete.Controller.USUARIO;

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
import com.example.diskfrete.Controller.HistoricoDeFretes;
import com.example.diskfrete.Controller.Login;
import com.example.diskfrete.Model.Motorista;
import com.example.diskfrete.R;
import com.example.diskfrete.db.Firebase;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.*;

import static com.example.diskfrete.Model.DadosPreferences.*;


public class UsuarioHome extends AppCompatActivity {


    private Firebase fb;
    private ProgressDialog barraDeProgresso;
    private  GroupAdapter adapter;
    private RecyclerView lista;
    private List<Motorista> listaMotorista = new ArrayList<>();
    int posicao;
    private FloatingActionMenu BotaoMenu;
    private String frete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_home);
        setTitle("DISK FRETE");


        barraDeProgresso = new ProgressDialog(this);

        BotaoMenu =findViewById(R.id.floatingActionButton1);
        BotaoMenu.setClosedOnTouchOutside(true);
        lista = findViewById(R.id.listaDeMotoristas);
        adapter = new GroupAdapter();
        lista.setAdapter(adapter);
        lista.setLayoutManager(new LinearLayoutManager(this));

        carregarMotoristas();

         adapter.setOnItemClickListener(new OnItemClickListener() {
             @Override
             public void onItemClick(@NonNull Item item, @NonNull View view) {
                 BotaoMenu.close(true);
                 chamaMotorista(item);


             }
         });
 }

    //  @Override
  //  public void onBackPressed() {
   //     super.finish();
   // }

public void logoutUsuario(View view){
    SharedPreferences.Editor editor = getSharedPreferences(dadosDoLoginUsuario,MODE_PRIVATE).edit();
    editor.putString("EMAIL",null);
    editor.putString("ATOR",null);
    editor.apply();
    fb = new Firebase();
    FirebaseAuth f = fb.getFirebaseAuth();
    f.getInstance().signOut();
    Intent it = new Intent(UsuarioHome.this, Login.class);
    startActivity(it);
    finish();
}



public void historicoDfretes(View view){
    Intent it = new Intent(UsuarioHome.this, HistoricoDeFretes.class);
    fb = new Firebase();
    FirebaseAuth f = fb.getFirebaseAuth();
    String usuario = f.getCurrentUser().getEmail();
    it.putExtra("USUARIO",usuario);
    BotaoMenu.close(true);
    startActivity(it);
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
        fb=new Firebase();
   DatabaseReference df = fb.getDatabaseMotorista();

        barradProgresso(true,"BEM VINDO...");

        df.addValueEventListener(new ValueEventListener() {

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
