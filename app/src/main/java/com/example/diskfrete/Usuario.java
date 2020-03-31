package com.example.diskfrete;



public class Usuario {

  private String id,nome,sobrenome,email,senha;


  public Usuario() {

  }

  public Usuario(String id, String nome,String sobrenome, String email, String senha) {
    this.id= id;
    this.nome = nome;
    this.sobrenome=sobrenome;
    this.email = email;
    this.senha = senha;
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

  public String getSenha() {
    return senha;
  }
}
