package com.example.inventory.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.inventory.R;
import com.example.inventory.helper.AppController;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.inventory.helper.Constans.LOGIN;
import static com.example.inventory.helper.Constans.TAG_ID_USER;
import static com.example.inventory.helper.Constans.TAG_MESSAGE;
import static com.example.inventory.helper.Constans.TAG_SUCCESS;
import static com.example.inventory.helper.Constans.TAG_USERNAME;
import static com.example.inventory.helper.Constans.my_shared_preferences;
import static com.example.inventory.helper.Constans.session_status;
import static com.example.inventory.helper.Constans.success;
import static com.example.inventory.helper.Constans.tag_json_obj;

public class MainActivity extends AppCompatActivity {
    String id, username;
    ConnectivityManager conMgr;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    private static final String TAG = MainActivity.class.getSimpleName();
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextInputEditText Username = findViewById(R.id.username);
        TextInputEditText Password = findViewById(R.id.password);
        Button Login = findViewById(R.id.login);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
                Log.d(TAG, "onCreate: ");
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }

            sharedpreferences   = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
            session             = sharedpreferences.getBoolean(session_status, false);
            id                  = sharedpreferences.getString(TAG_ID_USER, null);
            username            = sharedpreferences.getString(TAG_USERNAME, null);

            if (session) {
                Intent intent = new Intent(MainActivity.this, Home.class);
                intent.putExtra(TAG_ID_USER, id);
                intent.putExtra(TAG_USERNAME, username);
                finish();
                startActivity(intent);
            }
        }
        Login.setOnClickListener(view -> {
            String userName = Objects.requireNonNull(Username.getText()).toString();
            String passWord = Objects.requireNonNull(Password.getText()).toString();
                pDialog = new ProgressDialog(this);
                pDialog.setCancelable(false);
                pDialog.setMessage("Logging in ...");
                showDialog();

                StringRequest strReq = new StringRequest(Request.Method.POST, LOGIN, response -> {
                    Log.e(TAG, "Login Response: " + response);
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        success = jObj.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            String username1 = jObj.getString(TAG_USERNAME);
                            String id        = jObj.getString(TAG_ID_USER);

                            Log.e("Successfully Login!", jObj.toString());
                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            // menyimpan login ke session
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(session_status, true);
                            editor.putString(TAG_ID_USER, id);
                            editor.putString(TAG_USERNAME, username1);
                            editor.apply();

                            // Memanggil main activity
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            intent.putExtra(TAG_ID_USER, id);
                            intent.putExtra(TAG_USERNAME, username1);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();

                    hideDialog();

                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", userName);
                        params.put("password", passWord);

                        return params;
                    }

                };

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
            });

        }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}