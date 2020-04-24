package com.example.diskfrete.Usuario;



public class Usuario {

  private String id,nomeCompleto,cidade,email;


  public Usuario() {

  }

  public Usuario(String id, String nomeCompleto,String cidade, String email) {
    this.id= id;
    this.nomeCompleto = nomeCompleto;
    this.cidade=cidade;
    this.email = email;

  }
  public String getId() {
    return id;
  }

  public String getNomeCompleto() { return nomeCompleto;
  }

  public String getCidade() {
    return cidade;
  }

  public String getEmail() {
    return email;
  }


}
