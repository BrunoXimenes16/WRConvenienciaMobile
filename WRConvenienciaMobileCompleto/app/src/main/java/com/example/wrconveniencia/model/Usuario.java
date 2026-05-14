package com.example.wrconveniencia.model;

public class Usuario {
    private int id;
    private String nome, cpf, telefone, email, endereco, senha;

    public Usuario(int id, String nome, String cpf, String telefone, String email, String endereco, String senha) {
        this.id = id; this.nome = nome; this.cpf = cpf; this.telefone = telefone;
        this.email = email; this.endereco = endereco; this.senha = senha;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public String getEndereco() { return endereco; }
    public String getSenha() { return senha; }
}
