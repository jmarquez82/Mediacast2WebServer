package app.dev.com.mediacast.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import app.dev.com.mediacast.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by oso on 10-11-17.
 */

public class ImageView  extends Fragment {

    @BindView(R.id.img)
    SimpleDraweeView img;
    Unbinder unbinder;
    String image;

    public ImageView() {
        this.image = "https://s3-sa-east-1.amazonaws.com/rankingc3-wp/wp-content/uploads/2016/09/08094315/Logo-Fundamenta-sin-fondo.png";
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ImageView(String image) {
        this.image = image;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_view, container, false);
        unbinder = ButterKnife.bind(this, view);

        img.setImageURI(image);

        return view;
    }

    public void setImage(String imgUrl){
        img.setImageURI(imgUrl);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
