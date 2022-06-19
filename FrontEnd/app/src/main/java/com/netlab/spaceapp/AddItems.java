package com.netlab.spaceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
 * Activity yang berfungsi untuk menambahkan item
 */
public class AddItems extends AppCompatActivity {
    EditText name, description, quantity;
    Button addbutton;
    UserService userService;
    Context Content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        Content = this;
        userService = ApiUtils.getAPIService();
        name = (EditText) findViewById(R.id.itemRegName);
        description = (EditText) findViewById(R.id.itemRegDescription);
        quantity = (EditText) findViewById(R.id.itemRegQuant);
        addbutton = (Button) findViewById(R.id.buttonUpdate);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty() || description.getText().toString().isEmpty() || quantity.getText().toString().isEmpty()) {
                    Toast.makeText(Content, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    requestadditem();
                }
            }
        });
    }

    void requestadditem() {
        userService.additem(name.getText().toString(), quantity.getText().toString(), description.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("message").equals("item added successfully")) {
                                    Toast.makeText(Content, "Item Added", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Content, HomeActivity.class);
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
                            Toast.makeText(Content, "Failed to Add item", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}
