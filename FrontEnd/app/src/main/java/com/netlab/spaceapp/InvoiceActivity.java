package com.netlab.spaceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netlab.spaceapp.model.Transaction;
import com.netlab.spaceapp.request.ApiUtils;
import com.netlab.spaceapp.request.UserService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity yang digunakan untuk menampilkan transaksi yang terkait dengan user
 */
public class InvoiceActivity extends AppCompatActivity {

    private ArrayAdapter<Transaction> transAdapter;
    private ArrayList<Transaction> transactionList = new ArrayList<>();
    private String namePars = MainActivity.usernamePars;
    private ListView invoiceLv;
    private TextView title;
    Context Content;
    UserService userService;
    public static Transaction selectedInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Content = this;
        userService = ApiUtils.getAPIService();
        setContentView(R.layout.activity_invoice);
        getListRequest();
        invoiceLv = (ListView) findViewById(R.id.itemView);
        title = (TextView) findViewById(R.id.titleInv);
        title.setText( namePars + "\nInvoice");

        invoiceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedInvoice = transactionList.get(i);
                Intent intent = new Intent(Content, InvoiceDetails.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.invoice_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Content, HomeActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    public void getListRequest() {
        Gson gson = new Gson();
        userService.gettransaction()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                JSONArray jsonArray = jsonRESULTS.getJSONArray("list");
                                if (jsonRESULTS.getString("message").equals("Item added successfully")) {
                                    transactionList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<Transaction>>() {
                                    }.getType());
                                    transAdapter = new ArrayAdapter<Transaction>(getApplicationContext(),
                                            android.R.layout.simple_list_item_1, transactionList);
                                    invoiceLv.setAdapter(transAdapter);
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
                            Toast.makeText(Content, "Failed to add Invoice", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}