package com.example.wrconveniencia.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;
import com.example.wrconveniencia.R;
import com.example.wrconveniencia.model.Produto;
import java.util.List;

public class ProdutoAdapter extends BaseAdapter {
    Context context;
    List<Produto> produtos;
    OnProdutoClick listener;

    public interface OnProdutoClick { void adicionar(Produto produto); }

    public ProdutoAdapter(Context context, List<Produto> produtos, OnProdutoClick listener) {
        this.context = context; this.produtos = produtos; this.listener = listener;
    }

    public int getCount() { return produtos.size(); }
    public Object getItem(int i) { return produtos.get(i); }
    public long getItemId(int i) { return produtos.get(i).getId(); }

    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) view = LayoutInflater.from(context).inflate(R.layout.item_produto, parent, false);
        Produto p = produtos.get(i);
        ((TextView)view.findViewById(R.id.txtNomeProduto)).setText(p.getNome());
        ((TextView)view.findViewById(R.id.txtDescricao)).setText(p.getDescricao());
        ((TextView)view.findViewById(R.id.txtPreco)).setText(String.format("R$ %.2f", p.getPreco()));
        ((TextView)view.findViewById(R.id.txtEstoque)).setText("Estoque: " + p.getEstoque());
        view.findViewById(R.id.btnAdicionar).setOnClickListener(v -> listener.adicionar(p));
        return view;
    }
}
