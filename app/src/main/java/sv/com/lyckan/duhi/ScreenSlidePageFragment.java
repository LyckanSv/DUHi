package sv.com.lyckan.duhi;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

public class ScreenSlidePageFragment extends Fragment {

    SpeakRequest speakRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String image = getArguments().getString("image");
        final String body = getArguments().getString("body");
        String num = getArguments().getString("pos");
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        speakRequest = new SpeakRequest(getContext());

        ImageView img = (ImageView) rootView.findViewById(R.id.imageChapter);
        TextView bodyTxt = (TextView) rootView.findViewById(R.id.body);
        TextView numPage = (TextView) rootView.findViewById(R.id.numPage);
        final ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.imageButton2);
        View adContainer = rootView.findViewById(R.id.adMobView);

        try {
            Picasso.with(getContext()).load(DataInfo.URL + image).into(img);
            Log.d("DATAINFO:", image);
            bodyTxt.setText(body);
            numPage.setText(num);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (speakRequest.isSpeaking()){
                        speakRequest.stopSpeak();
                    }else{
                        speakRequest.speak(body);
                    }
                }
            });
        }catch (Exception e){
            bodyTxt.setText(getResources().getString(R.string.error_load));
        }

        MobileAds.initialize(rootView.getContext(), "ca-app-pub-7173840215052053~3259543889");
        AdView mAdView = new AdView(rootView.getContext());
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId("ca-app-pub-7173840215052053/6884708528");
        ((RelativeLayout)adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("DA4E140B5B05CE69E1C10C46B64D5EEA")
                .build();

        mAdView.loadAd(adRequest);


        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {
            if (!isVisibleToUser) {
                Log.d("MyFragment", "Not visible anymore.  Stopping audio.");
                if (speakRequest.isSpeaking()){
                    speakRequest.stopSpeak();
                }
            }
        }
    }


    public static final ScreenSlidePageFragment newInstance(String image, String body, String pos)
    {
        ScreenSlidePageFragment f = new ScreenSlidePageFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString("image", image);
        bdl.putString("body", body);
        bdl.putString("pos", pos);
        f.setArguments(bdl);
        return f;
    }


}
