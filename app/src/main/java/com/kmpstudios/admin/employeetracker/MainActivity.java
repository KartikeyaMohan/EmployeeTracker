package com.kmpstudios.admin.employeetracker;

import android.content.Intent;
import java.io.*;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText username,password;
    CheckBox employee,admin;
    Button sign,register;
    double j,k;
    String type="";
    String ans="";
    String name="",pw="";
    public static final String Key_name="Name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        employee=(CheckBox)findViewById(R.id.emp);
        admin=(CheckBox)findViewById(R.id.admin);
        sign=(Button)findViewById(R.id.signin);
        register=(Button)findViewById(R.id.register);
        employee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {   @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {   if (isChecked)
                {   admin.setChecked(false);
                    type="Employee";
                }
            }
        });
        admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {   @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {   if (isChecked)
                {   employee.setChecked(false);
                    type="Admin";
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {   startActivity(new Intent(MainActivity.this, Registeration.class));
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {   name=username.getText().toString();
                nameprocess();
                getJSON("http://well-balanced-petro.000webhostapp.com/retrieve-db.php");
            }
        });
    }
    public void nameprocess()
    {   StringRequest sr = new StringRequest(Request.Method.POST, "http://well-balanced-petro.000webhostapp.com/retrieve-db.php", new Response.Listener<String>() {
        @Override
        public void onResponse(String response)
        {   ans=response;
            try {
                check(ans);
            }
            catch(JSONException e)
            {
            }
            //Toast.makeText(MainActivity.this, response + " = StringRequest response", Toast.LENGTH_SHORT).show();
        }
        }, new Response.ErrorListener()
            {   @Override
                public void onErrorResponse(VolleyError error)
                {   Toast.makeText(MainActivity.this, error.toString()+" = StringRequest error", Toast.LENGTH_SHORT).show();
                }
            })
        {
            @Override
            protected Map<String, String> getParams()
            {   Map<String, String> params = new HashMap<>();
                params.put(Key_name, name);
                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(sr);
    }
    public void getJSON(final String urlWebService)
    {   class GetJSON extends AsyncTask<Void, Void, String> {
            String geturl=urlWebService;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s+" = JSON Array", Toast.LENGTH_SHORT).show();
                try {
                    //check(ans);
                }
                catch(Exception e)
                {   Toast.makeText(getApplicationContext(),e.toString()+" = JSONException",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(geturl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                }
                catch (Exception e)
                {   return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }
    public void check(String s)throws JSONException {
        JSONArray a = new JSONArray(s);
        String pass[] = new String[a.length()];
        String type1[] = new String[a.length()];
        for (int i = 0; i < a.length(); i++) {
            JSONObject obj = a.getJSONObject(i);
            pass[i] = obj.getString("Password");
            type1[i] = obj.getString("Type");
        }
        pw = password.getText().toString();
        try
        {   if (pw.compareTo(pass[0]) == 0 && type1[0].equals("Employee"))
            {   if (type.compareTo(type1[0]) == 0)
                {   Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, LoggedIn.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("n", name);
                    i.putExtras(bundle);
                    startActivity(i);
                }
                else
                {   Toast.makeText(getApplicationContext(), "Sign in as Employee", Toast.LENGTH_SHORT).show();
                }
            }
            else if (pw.compareTo(pass[0]) == 0 && type1[0].equals("Admin"))
            {   if (type.compareTo(type1[0]) == 0)
                {   Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, LoggedInAdmin.class);
                   //Bundle bundle = new Bundle();
                    //bundle.putString("n", name);
                    //i.putExtras(bundle);
                    startActivity(i);
                }
                else
                {   Toast.makeText(getApplicationContext(), "Sign in as Admin", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {   Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {   Toast.makeText(getApplicationContext(), "Name not found", Toast.LENGTH_SHORT).show();
        }
    }
}
