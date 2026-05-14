package com.example.wrconveniencia.model;

public class ItemCarrinho {
    private int id, produtoId, quantidade;
    private String nomeProduto;
    private double preco;

    public ItemCarrinho(int id, int produtoId, String nomeProduto, double preco, int quantidade) {
        this.id = id; this.produtoId = produtoId; this.nomeProduto = nomeProduto;
        this.preco = preco; this.quantidade = quantidade;
    }

    public int getId() { return id; }
    public int getProdutoId() { return produtoId; }
    public String getNomeProduto() { return nomeProduto; }
    public double getPreco() { return preco; }
    public int getQuantidade() { return quantidade; }
    public double getSubtotal() { return preco * quantidade; }
}
