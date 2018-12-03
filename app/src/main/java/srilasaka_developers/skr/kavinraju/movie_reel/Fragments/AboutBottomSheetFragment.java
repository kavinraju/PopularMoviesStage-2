package srilasaka_developers.skr.kavinraju.movie_reel.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import srilasaka_developers.skr.kavinraju.movie_reel.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    AboutBottomSheetFragment aboutBottomSheetFragment;

    public AboutBottomSheetFragment() {
    }

    @BindView(R.id.linkedIn)
    ImageButton linkedIn;
    @BindView(R.id.twitter)
    ImageButton twitter;
    @BindView(R.id.github)
    ImageButton github;
    @BindView(R.id.gmail)
    ImageButton gmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_about,container,false);;
        ButterKnife.bind(this,view);

        linkedIn.setOnClickListener(this);
        twitter.setOnClickListener(this);
        github.setOnClickListener(this);
        gmail.setOnClickListener(this);

        return view;
    }

    @OnClick(R.id.img_btn_close)
    public void onClickCloseAbout(View view){
        if(aboutBottomSheetFragment != null) {
            aboutBottomSheetFragment.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url;
        switch (view.getId()){
            case R.id.linkedIn:
                url = "https://www.linkedin.com/in/kavinraju";
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.twitter:
                url = "https://twitter.com/KavinRaju3/";
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.github:
                url = "https://github.com/kavinraju";
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.gmail:
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:skr.appdeveloper@gmail.com"));
                startActivity(intent);
                break;
        }
    }

    public void setAboutBottomSheetFragment(AboutBottomSheetFragment aboutBottomSheetFragment) {
        this.aboutBottomSheetFragment = aboutBottomSheetFragment;
    }
}
