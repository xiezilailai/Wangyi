package com.example.xiezilailai.wangyi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by 蝎子莱莱123 on 2016/1/18.
 */
public class AddFragment extends android.app.Fragment {
    private Button pic;
    private View rootView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView=LayoutInflater.from(getActivity()).inflate(R.layout.add_fragment_view,null);
        pic=(Button)rootView.findViewById(R.id.pic_btn);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TakePicOrChoosePicDialog.dialog(getActivity(),getActivity());
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        return rootView;

    }

}
