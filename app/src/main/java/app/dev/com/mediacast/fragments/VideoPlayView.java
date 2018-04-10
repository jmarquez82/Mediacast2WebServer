package app.dev.com.mediacast.fragments;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import app.dev.com.mediacast.includes.AppVars;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import app.dev.com.mediacast.R;


/**
 * Created by oso on 10-11-17.
 */
public class VideoPlayView extends Fragment implements MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {


    SurfaceHolder holder;
    @BindView(R.id.surface)
    VideoView surface;
    Unbinder unbinder;
    @BindView(R.id.layoutImg)
    LinearLayout llImage;
    @BindView(R.id.layoutVideo)
    LinearLayout llVideo;
    @BindView(R.id.imgs)
    android.widget.ImageView imgs;

    private MediaPlayer player;

    private int url = 0;
    private ArrayList<String> videoList = new ArrayList<String>();
    boolean mRestored = false;

    public VideoPlayView() {
        // Required empty public constructor
        Log.e("Construct:","VideoPlayView()");
    }

    @SuppressLint("ValidFragment")
    public VideoPlayView(String resource) {
        Log.e("Construct:","VideoPlayView(String resource)");
        try {
            video(resource);
        }catch(Exception ex){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_play_view, container, false);
        unbinder = ButterKnife.bind(this, view);

        mRestored = savedInstanceState != null;

        String videopath = "1.mp4";
        String videopath2 = "2.mp4";
        String videopath3 = "3.mp4";


        videoList.add(videopath);
        videoList.add(videopath2);
        videoList.add(videopath3);

        holder = surface.getHolder();
        holder.addCallback(this);
        //holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        player = new MediaPlayer();
        player.setOnPreparedListener(this);

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("ERROR Reproductor", "setOnErrorListener");
                return false;
            }
        });

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                url++;
                if (surface != null) {
                }

                if (url > videoList.size() - 1) {
                    url = 0;
                }

                try {
                    video(videoList.get(url));
                } catch (Exception ex) {

                }
            }
        });

        try {
            video(videoList.get(0));
        } catch (Exception ex) {
            Log.i("No carga video:", ex.toString());
        }

        return view;
    }

    public void setVideo(String path) {

        try {
            String pathFile = path;
            Log.i("Path Video Socket: ", path);
            video(pathFile);

        } catch (Exception ex) {
            Log.i("Error", "No se pudo recibir el video"+ ex.getMessage());
        }

    }

    public void video(String url) throws IOException {

        String video =  AppVars.pathHomeStatic + url.substring(url.lastIndexOf("/")+1,url.length());
        Log.i("Video: ", video);
        File file = new File(video);
        if (file.exists()) {
            if (player != null) {
                player.stop();
                player.reset();
                player.setDataSource(video);
                player.prepareAsync();
            }
        } else {
            Log.e("Path: ", "No Existe");
            Toast.makeText(getActivity(), " No se puede reproducir o no existe el video " + video, Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        player.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        player.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.e("METHOD: ", "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.e("METHOD: ", "surfaceDestroyed");
    }

    @Override
    public void onDestroyView() {
        Log.e("METHOD: ", "onDestroyView");
        player.stop();
        player.reset();
        super.onDestroyView();
        unbinder.unbind();
    }
}
