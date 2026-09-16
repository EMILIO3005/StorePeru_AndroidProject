package com.example.storeperu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.storeperu.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrarFragment extends Fragment {

    EditText edtRegNombre, edtRegCategoria, edtRegDescripcion, edtRegGarantia, edtRegPrecio, edtRegStock;

    Button btnRegGuardar;

    RequestQueue requestQueue;

    private final String URL = "http://192.168.1.72:3000/productos";

    private void loadUI(View view){
        edtRegNombre = view.findViewById(R.id.edtRegNombre);
        edtRegCategoria = view.findViewById(R.id.edtRegCategoria);
        edtRegDescripcion = view.findViewById(R.id.edtRegDescripcion);
        edtRegGarantia = view.findViewById(R.id.edtRegGarantia);
        edtRegPrecio = view.findViewById(R.id.edtRegPrecio);
        edtRegStock = view.findViewById(R.id.edtRegStock);
        btnRegGuardar = view.findViewById(R.id.btnRegGuardar);
    }
    public RegistrarFragment() {
        // Constructor vacío
    }

    private void guardarProductoWS() {

        String nombre = edtRegNombre.getText().toString().trim();
        String categoria = edtRegCategoria.getText().toString().trim();
        String descripcion = edtRegDescripcion.getText().toString().trim();
        String garantia = edtRegGarantia.getText().toString().trim();
        String precioStr = edtRegPrecio.getText().toString().trim();
        String stockStr = edtRegStock.getText().toString().trim();

        if (nombre.isEmpty()) { edtRegNombre.setError("Requerido"); edtRegNombre.requestFocus(); return; }
        if (categoria.isEmpty()) { edtRegCategoria.setError("Requerido"); edtRegCategoria.requestFocus(); return; }
        if (precioStr.isEmpty()) { edtRegPrecio.setError("Requerido"); edtRegPrecio.requestFocus(); return; }
        if (stockStr.isEmpty()) { edtRegStock.setError("Requerido"); edtRegStock.requestFocus(); return; }


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nombre", nombre);
            jsonBody.put("categoria", categoria);
            jsonBody.put("descripcion", descripcion);
            jsonBody.put("garantia", garantia);
            jsonBody.put("precio", Double.parseDouble(precioStr));
            jsonBody.put("stock", Integer.parseInt(stockStr));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Verifique los valores numéricos", Toast.LENGTH_SHORT).show();
            return;
        }


        requestQueue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String mensaje = response.getString("message");
                            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show();


                            limpiarFormulario();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // Manejo de errores controlados (400, 500) del backend
                        NetworkResponse response = volleyError.networkResponse;
                        if (response != null && response.data != null) {
                            try {
                                String errorJSON = new String(response.data);
                                JSONObject jsonObject = new JSONObject(errorJSON);
                                String mensajeError = jsonObject.optString("error", "Error en el servidor");
                                Toast.makeText(requireContext(), mensajeError, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(requireContext(), "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void limpiarFormulario() {
        edtRegNombre.setText("");
        edtRegCategoria.setText("");
        edtRegDescripcion.setText("");
        edtRegGarantia.setText("");
        edtRegPrecio.setText("");
        edtRegStock.setText("");
        edtRegNombre.requestFocus();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_registrar,
                container,
                false
        );

        this.loadUI(view);

        btnRegGuardar.setOnClickListener(v ->{this.guardarProductoWS();});
        return  view;
    }
}
