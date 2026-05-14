package com.example.wrconveniencia.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.wrconveniencia.model.ItemCarrinho;
import com.example.wrconveniencia.model.Produto;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "wr_conveniencia.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuario (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, cpf TEXT NOT NULL, telefone TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, endereco TEXT NOT NULL, senha TEXT NOT NULL)");

        db.execSQL("CREATE TABLE produto (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, descricao TEXT, categoria TEXT, " +
                "preco REAL NOT NULL, estoque INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE carrinho (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, produto_id INTEGER NOT NULL, quantidade INTEGER NOT NULL, " +
                "FOREIGN KEY(produto_id) REFERENCES produto(id))");

        db.execSQL("CREATE TABLE pedido (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, usuario_id INTEGER, endereco TEXT NOT NULL, pagamento TEXT NOT NULL, " +
                "status TEXT NOT NULL, total REAL NOT NULL, data_pedido TEXT DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE TABLE pedido_item (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, pedido_id INTEGER, produto_id INTEGER, nome_produto TEXT, " +
                "preco REAL, quantidade INTEGER, subtotal REAL)");

        inserirProdutosIniciais(db);
    }

    private void inserirProdutosIniciais(SQLiteDatabase db) {
        inserirProduto(db, "Amstel Caixa", "Caixa de cerveja Amstel lata", "Cervejas", 38.00, 20);
        inserirProduto(db, "Smirnoff", "Vodka Smirnoff", "Destilados", 35.00, 15);
        inserirProduto(db, "Vulcano Tradicional", "Energético Vulcano", "Energéticos", 12.00, 30);
        inserirProduto(db, "Gelo 4kg", "Saco de gelo 4kg", "Gelo", 10.00, 40);
        inserirProduto(db, "Red Bull", "Energético Red Bull", "Energéticos", 12.00, 25);
        inserirProduto(db, "Monster", "Energético Monster 473ml", "Energéticos", 11.00, 25);
    }

    private void inserirProduto(SQLiteDatabase db, String nome, String desc, String cat, double preco, int estoque) {
        ContentValues cv = new ContentValues();
        cv.put("nome", nome); cv.put("descricao", desc); cv.put("categoria", cat);
        cv.put("preco", preco); cv.put("estoque", estoque);
        db.insert("produto", null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS pedido_item");
        db.execSQL("DROP TABLE IF EXISTS pedido");
        db.execSQL("DROP TABLE IF EXISTS carrinho");
        db.execSQL("DROP TABLE IF EXISTS produto");
        db.execSQL("DROP TABLE IF EXISTS usuario");
        onCreate(db);
    }

    public boolean cadastrarUsuario(String nome, String cpf, String telefone, String email, String endereco, String senha) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome", nome); cv.put("cpf", cpf); cv.put("telefone", telefone);
        cv.put("email", email); cv.put("endereco", endereco); cv.put("senha", senha);
        long result = db.insert("usuario", null, cv);
        return result != -1;
    }

    public int login(String email, String senha) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM usuario WHERE email=? AND senha=?", new String[]{email, senha});
        int id = -1;
        if (c.moveToFirst()) id = c.getInt(0);
        c.close();
        return id;
    }

    public List<Produto> listarProdutos() {
        List<Produto> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id,nome,descricao,categoria,preco,estoque FROM produto WHERE estoque > 0", null);
        while (c.moveToNext()) {
            lista.add(new Produto(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getDouble(4), c.getInt(5)));
        }
        c.close();
        return lista;
    }

    public void adicionarAoCarrinho(int produtoId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id, quantidade FROM carrinho WHERE produto_id=?", new String[]{String.valueOf(produtoId)});
        if (c.moveToFirst()) {
            int id = c.getInt(0);
            int qtd = c.getInt(1) + 1;
            ContentValues cv = new ContentValues();
            cv.put("quantidade", qtd);
            db.update("carrinho", cv, "id=?", new String[]{String.valueOf(id)});
        } else {
            ContentValues cv = new ContentValues();
            cv.put("produto_id", produtoId);
            cv.put("quantidade", 1);
            db.insert("carrinho", null, cv);
        }
        c.close();
    }

    public List<ItemCarrinho> listarCarrinho() {
        List<ItemCarrinho> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT c.id, p.id, p.nome, p.preco, c.quantidade FROM carrinho c INNER JOIN produto p ON p.id=c.produto_id", null);
        while (c.moveToNext()) {
            lista.add(new ItemCarrinho(c.getInt(0), c.getInt(1), c.getString(2), c.getDouble(3), c.getInt(4)));
        }
        c.close();
        return lista;
    }

    public void removerItemCarrinho(int carrinhoId) {
        getWritableDatabase().delete("carrinho", "id=?", new String[]{String.valueOf(carrinhoId)});
    }

    public double totalCarrinho() {
        double total = 0;
        for (ItemCarrinho item : listarCarrinho()) total += item.getSubtotal();
        return total;
    }

    public int finalizarPedido(int usuarioId, String endereco, String pagamento) {
        SQLiteDatabase db = getWritableDatabase();
        List<ItemCarrinho> itens = listarCarrinho();
        if (itens.isEmpty()) return -1;

        double total = totalCarrinho();
        ContentValues pedido = new ContentValues();
        pedido.put("usuario_id", usuarioId);
        pedido.put("endereco", endereco);
        pedido.put("pagamento", pagamento);
        pedido.put("status", "Pedido em preparo");
        pedido.put("total", total);
        long pedidoId = db.insert("pedido", null, pedido);

        for (ItemCarrinho item : itens) {
            ContentValues pi = new ContentValues();
            pi.put("pedido_id", pedidoId);
            pi.put("produto_id", item.getProdutoId());
            pi.put("nome_produto", item.getNomeProduto());
            pi.put("preco", item.getPreco());
            pi.put("quantidade", item.getQuantidade());
            pi.put("subtotal", item.getSubtotal());
            db.insert("pedido_item", null, pi);

            db.execSQL("UPDATE produto SET estoque = estoque - ? WHERE id = ?",
                    new Object[]{item.getQuantidade(), item.getProdutoId()});
        }

        db.delete("carrinho", null, null);
        return (int) pedidoId;
    }

    public Cursor buscarPedido(int pedidoId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT id,status,total,data_pedido,endereco,pagamento FROM pedido WHERE id=?",
                new String[]{String.valueOf(pedidoId)});
    }

    public Cursor listarPedidos() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT id,status,total,data_pedido,endereco,pagamento FROM pedido ORDER BY id DESC", null);
    }

    public void atualizarStatusPedido(int pedidoId, String status) {
        ContentValues cv = new ContentValues();
        cv.put("status", status);
        getWritableDatabase().update("pedido", cv, "id=?", new String[]{String.valueOf(pedidoId)});
    }
}
