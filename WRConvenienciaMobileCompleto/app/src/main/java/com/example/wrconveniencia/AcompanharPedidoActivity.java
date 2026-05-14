package com.example.wrconveniencia;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.*;
import com.example.wrconveniencia.database.DatabaseHelper;

public class AcompanharPedidoActivity extends Activity {
    DatabaseHelper db;
    TextView txtPedido, txtStatus;
    int pedidoId;
    Handler handler = new Handler();
    String[] status = {"Pedido em preparo", "Saiu para entrega", "Entregue"};
    int pos = 0;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_acompanhar);
        db = new DatabaseHelper(this);
        txtPedido = findViewById(R.id.txtPedido);
        txtStatus = findViewById(R.id.txtStatus);
        pedidoId = getIntent().getIntExtra("pedido_id", -1);
        carregarPedido();
        simularStatus();
    }

    private void carregarPedido() {
        Cursor c = db.buscarPedido(pedidoId);
        if (c.moveToFirst()) {
            txtPedido.setText("Pedido #" + c.getInt(0) + " - Total R$ " + String.format("%.2f", c.getDouble(2)));
            txtStatus.setText(c.getString(1));
        }
        c.close();
    }

    private void simularStatus() {
        handler.postDelayed(() -> {
            if (pedidoId != -1 && pos < status.length) {
                db.atualizarStatusPedido(pedidoId, status[pos]);
                txtStatus.setText(status[pos]);
                Toast.makeText(this, "Status atualizado: " + status[pos], Toast.LENGTH_SHORT).show();
                pos++;
                simularStatus();
            }
        }, 5000);
    }
}
