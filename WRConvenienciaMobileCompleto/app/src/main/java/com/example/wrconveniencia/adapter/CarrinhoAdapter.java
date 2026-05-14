package com.example.wrconveniencia.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;
import com.example.wrconveniencia.R;
import com.example.wrconveniencia.model.ItemCarrinho;
import java.util.List;

public class CarrinhoAdapter extends BaseAdapter {
    Context context;
    List<ItemCarrinho> itens;
    OnRemoverClick listener;

    public interface OnRemoverClick { void remover(ItemCarrinho item); }

    public CarrinhoAdapter(Context context, List<ItemCarrinho> itens, OnRemoverClick listener) {
        this.context = context; this.itens = itens; this.listener = listener;
    }

    public int getCount() { return itens.size(); }
    public Object getItem(int i) { return itens.get(i); }
    public long getItemId(int i) { return itens.get(i).getId(); }

    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) view = LayoutInflater.from(context).inflate(R.layout.item_carrinho, parent, false);
        ItemCarrinho item = itens.get(i);
        ((TextView)view.findViewById(R.id.txtItemNome)).setText(item.getNomeProduto());
        ((TextView)view.findViewById(R.id.txtItemQtd)).setText("Qtd: " + item.getQuantidade());
        ((TextView)view.findViewById(R.id.txtItemSubtotal)).setText(String.format("Subtotal: R$ %.2f", item.getSubtotal()));
        view.findViewById(R.id.btnRemover).setOnClickListener(v -> listener.remover(item));
        return view;
    }
}
