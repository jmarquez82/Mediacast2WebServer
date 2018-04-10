package app.dev.com.mediacast;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.dev.com.mediacast.includes.AppVars;
import app.dev.com.mediacast.includes.Download;
import app.dev.com.mediacast.includes.Functions;
import app.dev.com.mediacast.includes.Permissions;
import app.dev.com.mediacast.server.ClientApi;
import retrofit2.Retrofit;

/**
 * Created by oso on 10-11-17.
 */

public class SplashScreen extends AppCompatActivity {
    SplashScreen act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        Fresco.initialize(this);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //Toast.makeText(SplashScreen.this, "onPreExecute ", Toast.LENGTH_SHORT).show();

                Permissions permissions = new Permissions(act);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("https://manager.rinnolab.cl/ra/api/project/");
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            String result = null;
                            StringBuffer sb = new StringBuffer();
                            InputStream is = null;

                            try {
                                is = new BufferedInputStream(urlConnection.getInputStream());
                                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                                String inputLine = "";
                                while ((inputLine = br.readLine()) != null) {
                                    sb.append(inputLine);
                                    showLinks(inputLine);
                                }

                            }
                            catch (Exception e) {
                                Log.i("GETAPI", "Error reading InputStream");
                                result = null;
                            }

                        } catch (Exception e) {
                            Log.e("Error", "Exception: " + e.getMessage());
                        }
                    }
                });


                try{
                    Thread. sleep(3000);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }



            @Override
            protected Void doInBackground(Void... params) {
                //Crear archivo base
                Functions.createFileAndDir(AppVars.pathHome, AppVars.fileDefault);
                //Crear directorios y subdirectorios necesarios
                if(!Functions.createDirs(AppVars.mediaDirs)){
                    Log.e("Error","Al crear directorios base");
                    Toast.makeText(SplashScreen.this, "Warning: No se pudo crear directorios bases para la aplicación. ", Toast.LENGTH_SHORT).show();

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(SplashScreen.this, "Run ", Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }.execute();

    }

    private static void showLinks(String content) {
        Log.i("LINK", "Entra!" + content);
        String regex = "(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?.(jpg|mp4|png|jpeg)";
        final Matcher m = Pattern.compile(regex).matcher(content);
        final List<String> matches = new ArrayList<>();
        int cont = 0;
        while (m.find()) {
            cont++;
            matches.add(m.group(0));

            try {

                if(m.group(0).contains(".mp4")) {
                    Download down = new Download(m.group(0));
                    down.downloadFile();
                }

            } catch (Exception ex) {
                Log.e("DOWN", "Error:" + ex);
            }
            Log.e("MATCHES","Match: " + cont + " .- " + m.group(0));
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int i=0; i < grantResults.length;i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SplashScreen.this, "Permiso " + permissions[i] + " was " + grantResults[0], Toast.LENGTH_SHORT).show();
                Intent ip = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(ip);
                finish();
            }
        }
    }
}

