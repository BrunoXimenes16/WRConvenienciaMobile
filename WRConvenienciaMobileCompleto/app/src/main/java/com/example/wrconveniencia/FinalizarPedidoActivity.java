package com.example.wrconveniencia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import com.example.wrconveniencia.database.DatabaseHelper;

public class FinalizarPedidoActivity extends Activity {
    DatabaseHelper db;
    EditText edtEndereco;
    Spinner spPagamento;
    TextView txtTotal;
    Button btnConfirmar;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_finalizar);
        db = new DatabaseHelper(this);

        edtEndereco = findViewById(R.id.edtEnderecoEntrega);
        spPagamento = findViewById(R.id.spPagamento);
        txtTotal = findViewById(R.id.txtTotalFinalizar);
        btnConfirmar = findViewById(R.id.btnConfirmar);

        spPagamento.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"PIX", "Cartão", "Dinheiro na entrega"}));

        txtTotal.setText(String.format("Total do pedido: R$ %.2f", db.totalCarrinho()));

        btnConfirmar.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("sessao", MODE_PRIVATE);
            int usuarioId = sp.getInt("usuario_id", -1);
            int pedidoId = db.finalizarPedido(usuarioId, edtEndereco.getText().toString(), spPagamento.getSelectedItem().toString());

            if (pedidoId != -1) {
                Intent i = new Intent(this, AcompanharPedidoActivity.class);
                i.putExtra("pedido_id", pedidoId);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "Carrinho vazio", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
