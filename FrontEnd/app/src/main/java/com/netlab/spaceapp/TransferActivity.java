package com.netlab.spaceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netlab.spaceapp.model.Item;
import com.netlab.spaceapp.model.User;
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
 * Activity yang digunakan untuk melakukan transfer item
 */
public class TransferActivity extends AppCompatActivity {
    private EditText quantity;
    private TextView receiver;
    private Button transferButton;

    private ArrayAdapter<User> userAdapter;
    private ArrayList<User> userList = new ArrayList<>();
    private Item itemTrans = ItemDetails.itemParser;
    ;
    private UserService mApiService;
    private Context Content;
    private String receiverName = MainActivity.usernamePars;

    private ListView usersLv;
    public static User selectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        Content = this;
        mApiService = ApiUtils.getAPIService();
        quantity = (EditText) findViewById(R.id.itemTransQuant);
        receiver = (TextView) findViewById(R.id.receiver);
        usersLv = (ListView) findViewById(R.id.userList);
        transferButton = (Button) findViewById(R.id.buttonUpdate);
        getUserRequest();
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity.getText().toString().isEmpty() || selectedUser.getUsername() == receiverName|| selectedUser == null || itemTrans.getQuantity() < Integer.parseInt(quantity.getText().toString())) {
                    Toast.makeText(Content, "Please Fill/Check all the forms or Check again the Quantity", Toast.LENGTH_SHORT).show();
                } else {
                    requesttransfer();
                }
            }
        });
        usersLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUser = userList.get(i);
                if (selectedUser != null) {
                    receiver.setText(selectedUser.getUsername());
                }
            }
        });
    }


    void requesttransfer() {
        mApiService.transferitem(String.valueOf(itemTrans.getId()), String.valueOf(selectedUser.getId()), quantity.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("message").equals("Item transferred successfully")) {
                                    Toast.makeText(Content, "Transfer Success", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Content, "Failed to Transfer", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    void getUserRequest() {
        Gson gson = new Gson();
        mApiService.getUser()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                JSONArray jsonArray = jsonRESULTS.getJSONArray("list");
                                if (jsonRESULTS.getString("message").equals("Item added successfully")) {
                                    userList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<User>>() {
                                    }.getType());
                                    userAdapter = new ArrayAdapter<User>(getApplicationContext(),
                                            android.R.layout.simple_list_item_1, userList);
                                    usersLv.setAdapter(userAdapter);
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
                            Toast.makeText(Content, "Failed to Load User", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

}
