package com.example.wrconveniencia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.example.wrconveniencia.adapter.ProdutoAdapter;
import com.example.wrconveniencia.database.DatabaseHelper;

public class ProdutosActivity extends Activity {
    DatabaseHelper db;
    ListView lista;
    Button btnCarrinho, btnPedidos;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_produtos);
        db = new DatabaseHelper(this);
        lista = findViewById(R.id.listaProdutos);
        btnCarrinho = findViewById(R.id.btnCarrinho);
        btnPedidos = findViewById(R.id.btnPedidos);

        lista.setAdapter(new ProdutoAdapter(this, db.listarProdutos(), produto -> {
            db.adicionarAoCarrinho(produto.getId());
            Toast.makeText(this, produto.getNome() + " adicionado ao carrinho", Toast.LENGTH_SHORT).show();
        }));

        btnCarrinho.setOnClickListener(v -> startActivity(new Intent(this, CarrinhoActivity.class)));
        btnPedidos.setOnClickListener(v -> startActivity(new Intent(this, MeusPedidosActivity.class)));
    }
}
