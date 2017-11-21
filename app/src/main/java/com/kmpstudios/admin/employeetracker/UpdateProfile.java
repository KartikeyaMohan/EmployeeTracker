package com.kmpstudios.admin.employeetracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {
    EditText email,phone,pass;
    String name;
    Button update;
    public static final String Key_name="Name";
    public static final String Key_email="Email";
    public static final String Key_phone="Phone";
    public static final String Key_password="Password";
    private static final String url="http://well-balanced-petro.000webhostapp.com/updateprofile.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        email=(EditText)findViewById(R.id.email);
        phone=(EditText)findViewById(R.id.phone);
        pass=(EditText)findViewById(R.id.password);
        update=(Button)findViewById(R.id.update);
        getInfo();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update();
            }
        });
    }
    public void getInfo()
    {   Bundle bundle=getIntent().getExtras();
        name=bundle.getString("Name");
    }
    public void Update()
    {   final String em=email.getText().toString();
        final String ph=phone.getText().toString();
        final String pa=pass.getText().toString();
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response)
        {   Toast.makeText(UpdateProfile.this, response, Toast.LENGTH_SHORT).show();
        }
    }, new Response.ErrorListener()
    {   @Override
    public void onErrorResponse(VolleyError error)
    {   Toast.makeText(UpdateProfile.this, error.toString(), Toast.LENGTH_SHORT).show();
    }
    })
    {
        @Override
        protected Map<String, String> getParams()
        {   Map<String, String> params = new HashMap<>();
            params.put(Key_name, name);
            params.put(Key_email, em);
            params.put(Key_phone, ph);
            params.put(Key_password, pa);
            return params;
        }
    };
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(sr);
    }
}
