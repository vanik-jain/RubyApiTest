package com.vapps.rubytest;

import androidx.appcompat.app.AppCompatActivity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{

    private EditText mUserEmail;
    private EditText mUserPassword;
   int resp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);

        mUserEmail=(EditText)findViewById(R.id.username);
        mUserPassword=(EditText)findViewById(R.id.password);
        //=(Button)findViewById(R.id.login);


    }

    public String getResponse(HttpURLConnection connection)
    {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {

                sb.append(line);
                break;
            }

            in.close();
            return sb.toString();
        }catch(Exception e)
        {
            return e.getMessage();
        }
    }

    public void login(View button) {
        JSONObject loginDetails=new JSONObject();
        JSONObject holder = new JSONObject();
        try {
            if(!(mUserEmail.getText().toString().equals("")||mUserPassword.getText().toString().equals(""))) {
                loginDetails.put("email", mUserEmail.getText().toString());
                loginDetails.put("password", mUserPassword.getText().toString());
                AddressDetailsLoader task=new AddressDetailsLoader();
               holder.put("user",loginDetails);
                task.execute("http://www.amxdp.fun/api/sessions",holder.toString());
            //    progressBar.setVisibility(View.VISIBLE);
            }
            else
            {
                Toast.makeText(this, "Please Enter something!", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e)
        {

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    class AddressDetailsLoader extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params)
        {

            try {
                URL url = new URL(params[0]);

                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.addRequestProperty("Accept","application/json");
            connection.addRequestProperty("Content-Type","application/json");

                connection.setRequestMethod("POST");

            connection.setDoOutput(true);
            connection.connect();
            DataOutputStream outputStream=new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(params[1]);
            Log.i("LOGIN JSON DATA",params[1]);
            resp=connection.getResponseCode();
                String response=getResponse(connection);
            if(resp!=200) {


                Log.i("LOGIN RESPONSE CODE",String.valueOf(resp));
                return String.valueOf(resp);
            }
            else
            {
            Log.i("RESPONSE CODE",String.valueOf(resp));
            Log.i("RESPONSE",response);
            }

            }
            catch (ProtocolException e)
            {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }


        @Override
        protected void onPostExecute(String response)
        {
            if (response != null)
            {
                if (response.equals("200"))
                {
                    Toast.makeText(getApplicationContext(), "Logged In!", Toast.LENGTH_SHORT).show();

                } else
                    {
                    Toast.makeText(getApplicationContext(), "Check Your Credentials And Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {

                Toast.makeText(MainActivity.this, "Please Check Your Internet Connection and Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }





}