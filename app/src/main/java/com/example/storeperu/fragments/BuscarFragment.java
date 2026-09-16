package com.example.storeperu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.storeperu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuscarFragment extends Fragment {
    EditText edtBuscarId;

    EditText txtNombreB, txtCategoriaB, txtDescripcionB, txtGarantiaB, txtPrecioB, txtStockB;
    Button btnBuscarWS, btnEliminar, btnActualizar, btnReiniciar;

    LinearLayout layoutResultado;

    RequestQueue requestQueue;
    private final String URL = "http://192.168.1.72:3000/productos";


    public BuscarFragment() {
        // Constructor vacío
    }


    private void loadUI(View view){
        edtBuscarId = view.findViewById(R.id.edtBuscarId);
        btnBuscarWS = view.findViewById(R.id.btnBuscarWS);

        layoutResultado = view.findViewById(R.id.layoutResultado);

        txtNombreB = view.findViewById(R.id.txtNombreB);
        txtCategoriaB = view.findViewById(R.id.txtCategoriaB);
        txtDescripcionB = view.findViewById(R.id.txtDescripcionB);
        txtGarantiaB = view.findViewById(R.id.txtGarantiaB);
        txtPrecioB = view.findViewById(R.id.txtPrecioB);
        txtStockB = view.findViewById(R.id.txtStockB);
    }
    private void validarError(int statusCode, String errorJSON){
        //404 No encontrado
        if (statusCode == 404) {
            try {
                JSONObject jsonObject = new JSONObject(errorJSON);
                String mensajeError = jsonObject.optString("error", "Error en la solicitud");
                this.resetUI();
                Toast.makeText(requireContext(), mensajeError, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void buscarProductos() {
        if (edtBuscarId.getText().toString().isEmpty()){
            edtBuscarId.setError("Campo requerido");
            edtBuscarId.requestFocus();
            return;
        }
        requestQueue = Volley.newRequestQueue(requireContext());
        String endPoind = URL + "/" + edtBuscarId.getText().toString();

        JsonArrayRequest jsonArrayRequest  = new JsonArrayRequest(
                Request.Method.GET,
                endPoind,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray  response) {
                        try {
                            if (response.length() > 0){
                                JSONObject jsonObject = response.getJSONObject(0);
                                txtNombreB.setText(jsonObject.getString("nombre"));
                                txtCategoriaB.setText(jsonObject.getString("categoria"));
                                txtDescripcionB.setText(jsonObject.getString("descripcion"));
                                txtGarantiaB.setText(jsonObject.getString("garantia"));
                                txtPrecioB.setText(jsonObject.getString("precio"));
                                txtStockB.setText(jsonObject.getString("stock"));

                                layoutResultado.setVisibility(View.VISIBLE);
                                btnActualizar.setEnabled(true);
                                btnEliminar.setEnabled(true);
                            }else{
                                resetUI();
                                Toast.makeText(requireContext(), "No se encontró el producto", Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException e){
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Manejo de errores
                        //Si el servidor retorna un codigo 40x (es un error)
                        NetworkResponse response = volleyError.networkResponse;

                        //Validar si existe un codigo de error
                        if (response != null && response.data != null){
                            //Mas Importante -> Saber el codigo de error
                            int statusCode = response.statusCode;
                            String errorJSON = new String(response.data);

                            validarError(statusCode, errorJSON);
                        } else{
                            layoutResultado.setVisibility(View.GONE);
                            Toast.makeText(requireContext(), "Error de red o servidor inaccesible", Toast.LENGTH_SHORT).show();
                        }
                    }//Volley error
                }//ErrorListener

        );//JsonObjectRequest

        requestQueue.add(jsonArrayRequest);
    }



    private void actualizarProducto(){

        // 1. Validar que el código no esté vacío
        if (edtBuscarId.getText().toString().isEmpty()){
            edtBuscarId.setError("Campo requerido");
            edtBuscarId.requestFocus();
            return;
        }

        requestQueue = Volley.newRequestQueue(requireContext());
        // Definimos la misma ruta apuntando al código del productos específico
        String endPoint = URL + "/" + edtBuscarId.getText().toString();

        // 2. Creamos el objeto JSON con los nuevos datos capturados de la interfaz
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre", txtNombreB.getText().toString());
            jsonObject.put("categoria", txtCategoriaB.getText().toString());
            jsonObject.put("descripcion", txtDescripcionB.getText().toString());
            jsonObject.put("garantia", txtGarantiaB.getText().toString());
            jsonObject.put("precio", Double.parseDouble(txtPrecioB.getText().toString()));
            jsonObject.put("stock", Integer.parseInt(txtStockB.getText().toString()));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // 3. Crear la petición HTTP usando el metodo PUT
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                endPoint,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String mensaje = response.getString("message");
                            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();


                            btnActualizar.setEnabled(false);
                            btnEliminar.setEnabled(false);


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        NetworkResponse response = volleyError.networkResponse;
                        if (response != null && response.data != null){
                            int statusCode = response.statusCode;
                            String errorJSON = new String(response.data);
                            validarError(statusCode, errorJSON);
                        } else {
                            Toast.makeText(requireContext(), "No se pudo actualizar el alumno", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
    private void resetUI(){
        edtBuscarId.setText(null);
        txtNombreB.setText(null);
        txtCategoriaB.setText(null);
        txtDescripcionB.setText(null);
        txtGarantiaB.setText(null);
        txtPrecioB.setText(null);
        txtStockB.setText(null);

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
        edtBuscarId.requestFocus();

    }

    private void validarAccion(String accion){
        requestQueue = Volley.newRequestQueue(requireContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Senati");
        builder.setMessage("¿Estas seguro de" + accion + "?");
        builder.setPositiveButton("Si", (a, b) ->{
            if (accion.equalsIgnoreCase("Eliminar")) this.eliminarProducto();
            if (accion.equalsIgnoreCase("Actualizar")) this.actualizarProducto();
        });
        builder.setNegativeButton("No", null);


        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void eliminarProducto(){
        requestQueue = Volley.newRequestQueue(requireContext());
        String endPoind = URL + "/" + edtBuscarId.getText().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                endPoind,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            String message = jsonObject.getString("message");
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            resetUI();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        NetworkResponse response = volleyError.networkResponse;
                        if (response != null && response.data != null) {
                            int statusCode = response.statusCode;
                            String errorJSON = new String(response.data);
                            validarError(statusCode, errorJSON);
                        } else {
                            Toast.makeText(requireContext(), "Error al intentar eliminar el producto", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_buscar,
                container,
                false
        );

        loadUI(view);

        btnEliminar = view.findViewById(R.id.btnEliminar);
        btnActualizar = view.findViewById(R.id.btnActualizar);
        btnReiniciar = view.findViewById(R.id.btnReiniciar);

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);


        btnBuscarWS.setOnClickListener(v -> buscarProductos());
        btnActualizar.setOnClickListener(v -> validarAccion("Actualizar"));
        btnReiniciar.setOnClickListener(v -> resetUI());
        btnEliminar.setOnClickListener(v -> {validarAccion("Eliminar");});

        return view;
    }
}
