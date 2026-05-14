package com.example.wrconveniencia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.example.wrconveniencia.adapter.CarrinhoAdapter;
import com.example.wrconveniencia.database.DatabaseHelper;

public class CarrinhoActivity extends Activity {
    DatabaseHelper db;
    ListView lista;
    TextView txtTotal;
    Button btnFinalizar, btnVoltar;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_carrinho);
        db = new DatabaseHelper(this);
        lista = findViewById(R.id.listaCarrinho);
        txtTotal = findViewById(R.id.txtTotal);
        btnFinalizar = findViewById(R.id.btnFinalizar);
        btnVoltar = findViewById(R.id.btnVoltar);
        carregar();

        btnFinalizar.setOnClickListener(v -> startActivity(new Intent(this, FinalizarPedidoActivity.class)));
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void carregar() {
        lista.setAdapter(new CarrinhoAdapter(this, db.listarCarrinho(), item -> {
            db.removerItemCarrinho(item.getId());
            carregar();
        }));
        txtTotal.setText(String.format("Total: R$ %.2f", db.totalCarrinho()));
    }
}
