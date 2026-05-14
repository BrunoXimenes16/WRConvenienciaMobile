package com.example.wrconveniencia;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import com.example.wrconveniencia.database.DatabaseHelper;

public class MeusPedidosActivity extends Activity {
    DatabaseHelper db;
    LinearLayout areaPedidos;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_meus_pedidos);
        db = new DatabaseHelper(this);
        areaPedidos = findViewById(R.id.areaPedidos);
        carregarPedidos();
    }

    private void carregarPedidos() {
        Cursor c = db.listarPedidos();
        while (c.moveToNext()) {
            TextView tv = new TextView(this);
            tv.setText("Pedido #" + c.getInt(0) +
                    "\nStatus: " + c.getString(1) +
                    "\nTotal: R$ " + String.format("%.2f", c.getDouble(2)) +
                    "\nData: " + c.getString(3) +
                    "\nPagamento: " + c.getString(5) + "\n");
            tv.setTextSize(16);
            tv.setPadding(20, 20, 20, 20);
            areaPedidos.addView(tv);
        }
        c.close();
    }
}
