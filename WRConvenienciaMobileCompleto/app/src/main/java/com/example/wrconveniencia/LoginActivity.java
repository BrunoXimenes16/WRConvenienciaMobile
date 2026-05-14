package com.example.wrconveniencia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import android.app.Activity;
import com.example.wrconveniencia.database.DatabaseHelper;

public class LoginActivity extends Activity {
    EditText edtEmail, edtSenha;
    Button btnLogin;
    TextView txtCadastro;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_login);
        db = new DatabaseHelper(this);

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnLogin = findViewById(R.id.btnLogin);
        txtCadastro = findViewById(R.id.txtCadastro);

        btnLogin.setOnClickListener(v -> {
            int usuarioId = db.login(edtEmail.getText().toString(), edtSenha.getText().toString());
            if (usuarioId != -1) {
                SharedPreferences sp = getSharedPreferences("sessao", MODE_PRIVATE);
                sp.edit().putInt("usuario_id", usuarioId).apply();
                startActivity(new Intent(this, ProdutosActivity.class));
                finish();
            } else {
                Toast.makeText(this, "E-mail ou senha inválidos", Toast.LENGTH_SHORT).show();
            }
        });

        txtCadastro.setOnClickListener(v -> startActivity(new Intent(this, CadastroActivity.class)));
    }
}
