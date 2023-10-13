package hansung.ac.serieson;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalActivityManager localActivityManager = new LocalActivityManager(this, false);
        localActivityManager.dispatchCreate(savedInstanceState);

        TabHost tabHost = findViewById(R.id.tabhost);
        tabHost.setup(localActivityManager);

        TabHost.TabSpec tabSpecMain = tabHost.newTabSpec("MAIN").setIndicator("Main");
        Intent intentMain = new Intent(this, MainPage.class);
        tabSpecMain.setContent(intentMain);
        tabHost.addTab(tabSpecMain);

        TabHost.TabSpec tabSpecSearch = tabHost.newTabSpec("SEARCH").setIndicator("Search");
        Intent intentSearch = new Intent(this, SearchPage.class);
        tabSpecSearch.setContent(intentSearch);
        tabHost.addTab(tabSpecSearch);

        TabHost.TabSpec tabSpecDetail = tabHost.newTabSpec("DETAIL").setIndicator("Detail");
        Intent intentDetail = new Intent(this, DetailPage.class);
        intentDetail.putExtra("id", "278");
        tabSpecDetail.setContent(intentDetail);
        tabHost.addTab(tabSpecDetail);

        String targetTab = getIntent().getStringExtra("targetTab");
        String id = getIntent().getStringExtra("id");
        intentDetail.putExtra("id", id);
        if (targetTab != null) {
            tabHost.setCurrentTabByTag(targetTab);
        }
    }
}
