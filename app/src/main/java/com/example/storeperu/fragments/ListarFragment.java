package com.example.storeperu.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.storeperu.ProductoAdapter;
import com.example.storeperu.R;
import com.example.storeperu.model.Producto;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListarFragment extends Fragment {


    ArrayList<Producto> listaProductos = new ArrayList<>();
    ProductoAdapter productoAdapter;
    RecyclerView recyclerProductos;

    RequestQueue requestQueue;


    private final String URL = "http://192.168.1.72:3000/productos";


    private void loadUI(View view) {
        recyclerProductos = view.findViewById(R.id.recyclerProductos);
    }

    private void obtenerDatosWS(){
        requestQueue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.i("Datos obtenidos", jsonArray.toString());
                        listaProductos.clear();
                        for(int i = 0; i < jsonArray.length(); i++){
                            try {
                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                Producto producto = new Producto();
                                producto.setId(jsonObject.getInt("id"));
                                producto.setNombre(jsonObject.getString("nombre"));
                                producto.setCategoria(jsonObject.getString("categoria"));
                                producto.setDescripcion(jsonObject.optString("descripcion"));
                                producto.setGarantia(jsonObject.optString("garantia"));
                                producto.setPrecio(jsonObject.getDouble("precio"));
                                producto.setStock(jsonObject.getInt("stock"));

                                listaProductos.add(producto);

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        productoAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("Error_WS", volleyError.toString());
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }



    public ListarFragment(){

    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_listar,
                container,
                false
        );
        this.loadUI(view);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(requireContext()));
        productoAdapter = new ProductoAdapter(listaProductos);
        recyclerProductos.setAdapter(productoAdapter);
        this.obtenerDatosWS();
        return view;
    }
}
