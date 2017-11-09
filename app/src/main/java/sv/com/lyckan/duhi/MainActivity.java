package sv.com.lyckan.duhi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.common.util.concurrent.ExecutionError;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sv.com.lyckan.duhi.adapter.HistoriesAdapter;
import sv.com.lyckan.duhi.pojos.Histories;
import sv.com.lyckan.duhi.webservice.ApiController;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private ApiController api;
    private String syncConnPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        GridLayoutManager lManager = new GridLayoutManager(getApplicationContext(), 2);
        recycler.setLayoutManager(lManager);
        api = new ApiController(this, recycler);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        syncConnPref = sharedPref.getString("key_gallery_name", null);
        Toast.makeText(this, syncConnPref, Toast.LENGTH_SHORT).show();
        DataInfo.URL = syncConnPref;

        try {
            api.getHistories(syncConnPref);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint("");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this,"Buscando: "+ query, Toast.LENGTH_SHORT).show();
                 ApiController.Controller c = api.searchHistories(syncConnPref);

                 c.searchHistory(query).enqueue(new Callback<Histories>() {
                     @Override
                     public void onResponse(Call<Histories> call, Response<Histories> response) {
                         try {
                             HistoriesAdapter adapter = new HistoriesAdapter(response.body().getHistories());
                             recycler.setAdapter(adapter);
                             if (response.body().getHistories().isEmpty()){
                                 Toast.makeText(getBaseContext(), "No se encontro la busqueda", Toast.LENGTH_LONG).show();
                             }

                         }catch (ExecutionError e){
                             e.printStackTrace();
                         }
                     }

                     @Override
                     public void onFailure(Call<Histories> call, Throwable t) {

                     }
                 });





                //se oculta el EditText
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);


    }

}
