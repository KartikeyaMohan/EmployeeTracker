package com.kmpstudios.admin.employeetracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Registeration extends AppCompatActivity {
    private static final String url="http://well-balanced-petro.000webhostapp.com/insert-db.php";
    public static final String Key_name="Name";
    public static final String Key_email="Email";
    public static final String Key_phone="Phone";
    public static final String Key_password="Password";
    public static final String Key_type="Type";
    EditText name,email,phone,password,cp;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        phone=(EditText)findViewById(R.id.phone);
        password=(EditText)findViewById(R.id.password);
        cp=(EditText)findViewById(R.id.cp);
        register=(Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Confirmation();
            }
        });
    }
    private void Confirmation() {
        final String n = name.getText().toString().trim();
        final String e = email.getText().toString().trim();
        final String p = phone.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        final String confirmp = cp.getText().toString().trim();
        final String type="Employee";
        if (n.length() == 0 || e.length() == 0 || p.length() == 0 || pass.length() == 0 || confirmp.length() == 0)
        {   Toast.makeText(getApplication(), "Empty text", Toast.LENGTH_SHORT).show();
        }
        else if (p.length() != 10)
        {   Toast.makeText(getApplication(), "Phone number wrong", Toast.LENGTH_SHORT).show();
        }
        else if(pass.compareTo(confirmp)!=0)
        {   Toast.makeText(getApplicationContext(),"Mismatching passwords",Toast.LENGTH_SHORT).show();
        }
        else
        {   StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {   Toast.makeText(Registeration.this, response, Toast.LENGTH_SHORT).show();
            }
            }, new Response.ErrorListener()
            {   @Override
                public void onErrorResponse(VolleyError error)
                {   Toast.makeText(Registeration.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {   Map<String, String> params = new HashMap<>();
                params.put(Key_name, n);
                params.put(Key_email, e);
                params.put(Key_phone, p);
                params.put(Key_password, pass);
                params.put(Key_type, type);
                return params;
            }
        };
            RequestQueue rq = Volley.newRequestQueue(this);
            rq.add(sr);
        }
    }
}
