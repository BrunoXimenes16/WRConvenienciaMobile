package com.example.wrconveniencia.model;

public class Produto {
    private int id, estoque;
    private String nome, descricao, categoria;
    private double preco;

    public Produto(int id, String nome, String descricao, String categoria, double preco, int estoque) {
        this.id = id; this.nome = nome; this.descricao = descricao; this.categoria = categoria;
        this.preco = preco; this.estoque = estoque;
    }

    public int getId() { return id; }
    public int getEstoque() { return estoque; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public double getPreco() { return preco; }
}
