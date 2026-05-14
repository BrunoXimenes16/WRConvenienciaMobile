package com.example.wrconveniencia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.example.wrconveniencia.database.DatabaseHelper;

public class CadastroActivity extends Activity {
    EditText nome, cpf, telefone, email, endereco, senha;
    Button btnCadastrar;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_cadastro);
        db = new DatabaseHelper(this);

        nome = findViewById(R.id.edtNome);
        cpf = findViewById(R.id.edtCpf);
        telefone = findViewById(R.id.edtTelefone);
        email = findViewById(R.id.edtEmail);
        endereco = findViewById(R.id.edtEndereco);
        senha = findViewById(R.id.edtSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(v -> {
            if (nome.getText().toString().isEmpty() || email.getText().toString().isEmpty() || senha.getText().toString().isEmpty()) {
                Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean ok = db.cadastrarUsuario(
                    nome.getText().toString(), cpf.getText().toString(), telefone.getText().toString(),
                    email.getText().toString(), endereco.getText().toString(), senha.getText().toString());

            if (ok) {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "E-mail já cadastrado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
