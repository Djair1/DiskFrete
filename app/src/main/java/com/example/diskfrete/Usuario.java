package com.example.diskfrete;



public class Usuario {

  private String id,nomeCompleto,telefone,email;


  public Usuario() {

  }

  public Usuario(String id, String nomeCompleto,String telefone, String email) {
    this.id= id;
    this.nomeCompleto = nomeCompleto;
    this.telefone=telefone;
    this.email = email;

  }
  public String getId() {
    return id;
  }

  public String getNomeCompleto() { return nomeCompleto;
  }

  public String getTelefone() {
    return telefone;
  }

  public String getEmail() {
    return email;
  }


}
