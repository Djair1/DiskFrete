package com.example.diskfrete;



public class Usuario {

  private String id,nome,sobrenome,email;


  public Usuario() {

  }

  public Usuario(String id, String nome,String sobrenome, String email) {
    this.id= id;
    this.nome = nome;
    this.sobrenome=sobrenome;
    this.email = email;

  }
  public String getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getSobrenome() {
    return sobrenome;
  }

  public String getEmail() {
    return email;
  }


}
