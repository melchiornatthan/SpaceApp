package com.netlab.spaceapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.spaceapp.model.Transaction;
import com.netlab.spaceapp.request.ApiUtils;
import com.netlab.spaceapp.request.UserService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity yang digunakan untuk melihat detail transaksi yang telah dilakukan
 */
public class InvoiceDetails extends AppCompatActivity {
    private Transaction invoice = InvoiceActivity.selectedInvoice;
    TextView name, quantity, receiver;
    Button updateButton;
    Context Content;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);
        Content = this;
        receiver = (TextView) findViewById(R.id.idInv);
        quantity = (TextView) findViewById(R.id.quantInv);
        name = (TextView) findViewById(R.id.nameInv);
        updateButton = (Button) findViewById(R.id.updateInv);
        userService = ApiUtils.getAPIService();
        if (invoice != null) {
            name.setText(invoice.getName());
            quantity.setText(String.valueOf(invoice.getQuant_sent()));
            receiver.setText(invoice.getUsername());
        }
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestUpdateInvoice();
            }
        });
    }

    void requestUpdateInvoice() {
        userService.updatetransaction(String.valueOf(invoice.getId())).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("message").equals("Transaction updated successfully")) {
                            Toast.makeText(Content, "Update Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Content, InvoiceActivity.class);
                            startActivity(intent);
                        } else if (jsonRESULTS.getString("message").equals("Not Authorized")) {
                            Toast.makeText(Content, "Not Authorized", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Content, InvoiceActivity.class);
                            startActivity(intent);
                        } else {
                            String error_message = jsonRESULTS.getString("error_msg");
                            Toast.makeText(Content, error_message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Content, "Failed to Update", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }
}