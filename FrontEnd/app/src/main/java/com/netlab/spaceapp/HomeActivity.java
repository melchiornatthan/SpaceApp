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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netlab.spaceapp.model.Item;
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
 * Activity untuk menampilkan item yang dimiliki oleh User
 */
public class HomeActivity extends AppCompatActivity {

    private ArrayAdapter<Item> itemsAdapter;
    private ArrayList<Item> itemList = new ArrayList<>();
    private ListView itemLv;
    private EditText key;
    private Button addButton;
    private ImageButton searchButton;
    Context Content;
    UserService userService;
    public static Item selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Content = this;
        userService = ApiUtils.getAPIService();
        setContentView(R.layout.activity_home);
        key = (EditText) findViewById(R.id.searchInput);
        addButton = (Button) findViewById(R.id.buttonAdd);
        itemLv = (ListView) findViewById(R.id.itemView);
        searchButton = (ImageButton) findViewById(R.id.searchButton);
        getListRequest();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Content, AddItems.class));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.getText().toString().isEmpty()) {
                    Toast.makeText(Content, "Please Fill The Form", Toast.LENGTH_SHORT).show();
                } else {
                    searchRequest();
                }
            }
        });

        itemLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = itemList.get(i);
                Intent intent = new Intent(Content, ItemDetails.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Content, MainActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.invoice) {
            Toast.makeText(this, "Invoice Selected", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Content, InvoiceActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.account) {
            Toast.makeText(this, "Account Selected", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Content, AccountInfo.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    void searchRequest() {
        Gson gson = new Gson();
        userService.searchitem(key.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("message").equals("Not Found")) {
                                    Toast.makeText(Content, "No Result Found", Toast.LENGTH_SHORT).show();
                                } else if (jsonRESULTS.getString("message").equals("Item added successfully")) {
                                    JSONArray jsonArray = jsonRESULTS.getJSONArray("list");
                                    itemList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<Item>>() {
                                    }.getType());

                                    itemsAdapter = new ArrayAdapter<Item>(getApplicationContext(),
                                            android.R.layout.simple_list_item_1, itemList);
                                    itemLv.setAdapter(itemsAdapter);
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
                            Toast.makeText(Content, "Failed to Add items", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    void getListRequest() {
        Gson gson = new Gson();
        userService.getitems()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                JSONArray jsonArray = jsonRESULTS.getJSONArray("list");
                                if (jsonRESULTS.getString("message").equals("Item added successfully")) {

                                    itemList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<Item>>() {
                                    }.getType());

                                    itemsAdapter = new ArrayAdapter<Item>(getApplicationContext(),
                                            android.R.layout.simple_list_item_1, itemList);
                                    itemLv.setAdapter(itemsAdapter);
                                    Toast.makeText(Content, "Welcome to Your Space", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(Content, "Failed to Add items", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}