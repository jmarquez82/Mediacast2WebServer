package app.dev.com.mediacast.includes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import app.dev.com.mediacast.SplashScreen;

/**
 * Created by Dev21 on 02-10-17.
 */

public class Permissions {

    SplashScreen activity;

    public Permissions(SplashScreen activity){
        this.activity = activity;
    }

    public boolean isStoragePermission(){
        if(Build.VERSION.SDK_INT >=23){
            if(activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(SplashScreen.this,"Permiso activado.",Toast.LENGTH_SHORT).show();
                return true;
            }else{
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                //Toast.makeText(SplashScreen.this,"Permiso resuelto",Toast.LENGTH_SHORT).show();
                return false;
            }

        }else{
            //Toast.makeText(SplashScreen.this,"Permiso activado..",Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public boolean isStoragePermissionRead(){
        if(Build.VERSION.SDK_INT >=23){
            if(activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(SplashScreen.this,"Permiso activado.",Toast.LENGTH_SHORT).show();
                return true;
            }else{
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                //Toast.makeText(SplashScreen.this,"Permiso resuelto",Toast.LENGTH_SHORT).show();
                return false;
            }

        }else{
            //Toast.makeText(SplashScreen.this,"Permiso activado..",Toast.LENGTH_SHORT).show();
            return true;
        }
    }


}

