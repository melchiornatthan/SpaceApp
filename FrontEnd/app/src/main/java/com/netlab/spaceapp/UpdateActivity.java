package com.netlab.spaceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netlab.spaceapp.model.Item;
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
 * Activity yang dilakukan untuk mengupdate item yang dimiliki user
 */
public class UpdateActivity extends AppCompatActivity {
    EditText name, description, quantity;
    TextView title;
    Button updateButton, deleteButton;
    private Item itemUpt;
    Context Content;
    UserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Content = this;
        userService = ApiUtils.getAPIService();
        name = (EditText) findViewById(R.id.itemUptName);
        description = (EditText) findViewById(R.id.itemUptDescription);
        quantity = (EditText) findViewById(R.id.itemUptQuant);
        updateButton = (Button) findViewById(R.id.buttonUpdate);
        deleteButton = (Button) findViewById(R.id.buttonDelete);
        title = (TextView) findViewById(R.id.textView5);

        itemUpt = ItemDetails.itemParser;
        if (itemUpt != null) {
            title.setText("Update\n" + itemUpt.getName());
            name.setText(itemUpt.getName());
            description.setText(itemUpt.getDescription());
            quantity.setText(String.valueOf(itemUpt.getQuantity()));
        }
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRequest();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRequest();
            }
        });
    }

    void updateRequest() {
        userService.updateitem(String.valueOf(itemUpt.getId()), name.getText().toString(), quantity.getText().toString(), description.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("message").equals("Item updated successfully")) {
                                    Toast.makeText(Content, "Update Success", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Content, "Failed to Update", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    void deleteRequest() {
        userService.deleteitem(String.valueOf(itemUpt.getId()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("message").equals("Item deleted successfully")) {
                                    Toast.makeText(Content, "Delete Success", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Content, "Failed to Delete", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}