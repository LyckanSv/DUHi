package sv.com.lyckan.duhi;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.com.lyckan.duhi.adapter.ReadPageAdapter;
import sv.com.lyckan.duhi.pojos.Chapter;
import sv.com.lyckan.duhi.pojos.History;
import sv.com.lyckan.duhi.pojos.SelectHistory;
import sv.com.lyckan.duhi.webservice.ApiController;


public class ReadFragmentActivity extends FragmentActivity {

    ReadPageAdapter pageAdapter;

    @BindView(R.id.startHistory)
    Button btnLogin;

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.containerP)
    ConstraintLayout constraintLayout;

    @BindView(R.id.imageHistory)
    ImageView imageView;

    @BindView(R.id.titleHistory)
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_fragment);
        History articulo = getIntent().getParcelableExtra("parametro");
        ButterKnife.bind(this);
        getData(articulo.getIdHistory());
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setAdapter(pageAdapter);
                constraintLayout.setVisibility(View.GONE);
            }
        });

        Picasso.with(this).load(DataInfo.URL + articulo.getImage()).into(imageView);
        textView.setText(articulo.getTitle());

    }

    private List<Fragment> getFragments(List<Chapter> chapters) {
        List<Fragment> fList = new ArrayList<Fragment>();
        int pos = 1;
        for(Chapter chapter: chapters){
            String txt = pos+"/"+ chapters.size() ;
            fList.add(ScreenSlidePageFragment.newInstance(chapter.getImage_url(), chapter.getBody(), txt));
            pos++;
        }

        return fList;
    }

    private void getData(String key){

        ApiController ctr = new ApiController();
        ApiController.Controller controller = ctr.getHistorySelected(DataInfo.URL);

        controller.getHistory(key).enqueue(new Callback<SelectHistory>() {
            @Override
            public void onResponse(Call<SelectHistory> call, Response<SelectHistory> response) {
                List<Fragment> fragments = getFragments(response.body().getChapters());
                pageAdapter = new ReadPageAdapter(getSupportFragmentManager(), fragments);
                pager.setPageTransformer(true, new ZoomOutPageTransformer());

            }

            @Override
            public void onFailure(Call<SelectHistory> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
