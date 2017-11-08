package sv.com.lyckan.duhi;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import sv.com.lyckan.duhi.login.CreateUserFragment;
import sv.com.lyckan.duhi.login.LogInFragment;

public class LoginActivity extends AppCompatActivity implements
        CreateUserFragment.OnFragmentInteractionListener,
        LogInFragment.OnFragmentInteractionListener
{

    private static final String TAG = "MainActivity";
    private AdView mAdView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        View adContainer = findViewById(R.id.adMobView);

//        AdView mAdView = new AdView(this);
//        mAdView.setAdSize(AdSize.BANNER);
//        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
//        ((RelativeLayout)adContainer).addView(mAdView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        logIn();
    }


    private void logIn(){

            LogInFragment fragment = new LogInFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            //transaction.setCustomAnimations(R.animator.enter_from_right, R.animator.enter_from_right);
            transaction.add(R.id.fragment_container, fragment);
            transaction.commit();

    }




    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
