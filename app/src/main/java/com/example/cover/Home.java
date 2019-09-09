package com.example.cover;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {
    ViewFlipper viewFlipper;
    ImageView best1, best2, best3, best4, best5;
    RequestOptions options;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        int images[] = {R.drawable.s0, R.drawable.s1};
        viewFlipper = (ViewFlipper) v.findViewById(R.id.v_flipper);

        best1 = (ImageView) v.findViewById(R.id.best1);
        best2 = (ImageView) v.findViewById(R.id.best2);
        best3 = (ImageView) v.findViewById(R.id.best3);
        best4 = (ImageView) v.findViewById(R.id.best4);
        best5 = (ImageView) v.findViewById(R.id.best5);

        options = new RequestOptions()
                .dontAnimate()
                .centerCrop()
                .placeholder(R.drawable.ic_picture)
                .error(R.drawable.ic_picture);

        Glide.with(v)
                .load("https://firebasestorage.googleapis.com/v0/b/covertocover-c12ab.appspot.com/o/cover%2F2d11eab2ca7550568a0ca1fd1cbcb257.jpg?alt=media&token=5716b215-99d6-4d4d-bf0e-d374a451158a")
                .apply(options)
                .into(best1);
        Glide.with(v)
                .load("https://firebasestorage.googleapis.com/v0/b/covertocover-c12ab.appspot.com/o/cover%2Fthesubtleartof.jpg?alt=media&token=bfbf5354-4b9f-4997-b28c-243af1719347")
                .apply(options)
                .into(best2);
        Glide.with(v)
                .load("https://firebasestorage.googleapis.com/v0/b/covertocover-c12ab.appspot.com/o/cover%2Felonmusk.jpg?alt=media&token=456ec511-6435-4ed4-a670-8c8aba50ce33")
                .apply(options)
                .into(best3);
        Glide.with(v)
                .load("https://firebasestorage.googleapis.com/v0/b/covertocover-c12ab.appspot.com/o/cover%2Froom105.jpg?alt=media&token=48921a2c-f271-406a-a9bc-c8b71b46a50e")
                .apply(options)
                .into(best4);
        Glide.with(v)
                .load("https://firebasestorage.googleapis.com/v0/b/covertocover-c12ab.appspot.com/o/cover%2Fthinkstraight.jpg?alt=media&token=ed114167-7c60-478e-b50d-382a26489743")
                .apply(options)
                .into(best5);

        //loop
        for(int image: images){
            flipperImage(image);
        }
        return v;
    }

    // image slider
    private void flipperImage(int image) {
        ImageView imageView = new ImageView(getActivity());
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
    }

}
