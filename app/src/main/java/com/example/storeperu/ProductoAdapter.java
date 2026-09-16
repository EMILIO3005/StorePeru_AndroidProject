package com.example.storeperu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.storeperu.model.Producto;

import java.util.ArrayList;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    ArrayList<Producto> listaProductos;
    public ProductoAdapter(ArrayList<Producto> listaProductos){
        this.listaProductos = listaProductos;
    }
    @NonNull
    @Override
    public ProductoAdapter.ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.ProductoViewHolder holder, int position) {
        holder.asignarDatos(listaProductos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }


    public class ProductoViewHolder extends RecyclerView.ViewHolder{

        TextView txtNombre, txtCategoria, txtPrecio, txtStock;
        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            txtStock = itemView.findViewById(R.id.txtStock);
        }

        public void asignarDatos(Producto producto){
            txtNombre.setText(producto.getNombre());
            txtCategoria.setText("Categoria: " +producto.getCategoria());
            txtPrecio.setText("Precio: S/ " + producto.getPrecio());
            txtStock.setText("Stock: " + producto.getStock());

        }
    }
}
