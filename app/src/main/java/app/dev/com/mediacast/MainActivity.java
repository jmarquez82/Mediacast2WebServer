package app.dev.com.mediacast;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import app.dev.com.mediacast.fragments.ImageView;
import app.dev.com.mediacast.fragments.MapsView;
import app.dev.com.mediacast.fragments.VideoPlayView;
import app.dev.com.mediacast.includes.Functions;
import app.dev.com.mediacast.server.SimpleWebServer;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    private SimpleWebServer mWebServer;
    FragmentManager fgManager;
    FragmentTransaction fragmentTransaction;
    static VideoPlayView vp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.showFullScreen(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        final int port = 8080;
        mWebServer = new SimpleWebServer(this,port, getResources().getAssets());
        mWebServer.start();
    }

    @Override
    public void onPause() {
        //mWebServer.stop();
        super.onPause();
    }

    public void setContent(String type,String value) {

        fgManager = getSupportFragmentManager();
        fragmentTransaction = fgManager.beginTransaction();

        if(type.equals("image")){
            Log.i("Image Ok","OK");
            vp = null;
            ImageView iv = new ImageView(value);
            fragmentTransaction.replace(R.id.fragment_content, iv);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.detach(iv).attach(iv).commit();

        }else if(type.equals("video")){

            //VideoPlayView vp = (value.equals(""))? new VideoPlayView(): new VideoPlayView(value);
            Log.i("Video","OK");
            if(vp == null){
                vp = new VideoPlayView();
                fragmentTransaction.replace(R.id.fragment_content, vp);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.detach(vp).attach(vp).commit();

            }else{
                // Si el reproductor ya esta corriendo, reutiliza el mismo
                if(!value.equals("")){
                    vp.setVideo(value);
                }
            }

        }else if(type.equals("map")){
            Log.i("Map","OK");
            vp = null;
            MapsView mv = new MapsView(value);
            fragmentTransaction.replace(R.id.fragment_content, mv);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.detach(mv).attach(mv).commit();
        }
    }


}
