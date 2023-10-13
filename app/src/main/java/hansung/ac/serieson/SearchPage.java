package hansung.ac.serieson;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AppCompatActivity {
    private List<String> originalTitles;
    private List<String> ids;
    private List<String> overviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        String csvFile = "movies_metadata.csv";


        final GridView gv = findViewById(R.id.gridView);

        Button searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchName = findViewById(R.id.searchName);
                String name = searchName.getText().toString();

                originalTitles = readTitleData(SearchPage.this, csvFile, 3, 8, name);
                ids = readIDData(SearchPage.this, csvFile, 3, 8, name);
                overviews = readOverViewData(SearchPage.this, csvFile, 3, 8, name);

                SearchPage.MyGridAdapter gAdapter = new SearchPage.MyGridAdapter(SearchPage.this);
                gv.setAdapter(gAdapter);
            }
        });
    }

    public class MyGridAdapter extends BaseAdapter {
        Context context;

        public MyGridAdapter(Context context) {
            this.context = context;
        }

        public int getCount() {
            return originalTitles.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout layout = new LinearLayout(context);
            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.HORIZONTAL);

            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(400, 600);
            imageView.setLayoutParams(imageParams);
            imageView.setImageResource(R.drawable.poster_sample);
            imageView.setContentDescription("Sample");

            LinearLayout childLayout = new LinearLayout(context);
            childLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            childLayout.setOrientation(LinearLayout.VERTICAL);

            TextView titleTextView = new TextView(context);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            titleTextView.setLayoutParams(titleParams);
            titleTextView.setTextSize(25);
            titleTextView.setPadding(30,5,5,10);
            titleTextView.setText("제목");
            titleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            titleTextView.setSingleLine(true);
            titleTextView.setFocusable(true);
            titleTextView.setSelected(true);
            titleTextView.setFocusableInTouchMode(true);
            titleTextView.setMarqueeRepeatLimit(-1);

            TextView idTextView = new TextView(context);
            LinearLayout.LayoutParams idParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            idTextView.setLayoutParams(idParams);
            idTextView.setTextSize(20);
            idTextView.setPadding(30,5,5,10);
            idTextView.setText("아이디");

            TextView overviewTextView = new TextView(context);
            LinearLayout.LayoutParams overviewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            overviewTextView.setLayoutParams(overviewParams);
            overviewTextView.setTextSize(20);
            overviewTextView.setPadding(30,5,5,10);
            overviewTextView.setText("OverView");
            overviewTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            overviewTextView.setSingleLine(true);
            overviewTextView.setFocusable(true);
            overviewTextView.setSelected(true);
            overviewTextView.setFocusableInTouchMode(true);
            overviewTextView.setMarqueeRepeatLimit(-1);

            childLayout.addView(titleTextView);
            childLayout.addView(idTextView);
            childLayout.addView(overviewTextView);

            layout.addView(imageView);
            layout.addView(childLayout);

            titleTextView.setText(originalTitles.get(position));
            idTextView.setText(ids.get(position));
            overviewTextView.setText(overviews.get(position));

            View borderView = new View(context);
            LinearLayout.LayoutParams borderParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
            borderView.setLayoutParams(borderParams);
            borderView.setBackgroundResource(R.drawable.bottom_border);
            layout.addView(borderView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = ids.get(position);
                    Intent intent = new Intent(SearchPage.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("targetTab", "DETAIL"); // 이동할 탭의 ID를 전달
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
            return layout;
        }
    }
    private List<String> readTitleData(Context context, String csvFile, int actorColumn, int titleColumn, String title) {
        List<String> columnData = new ArrayList<>();

        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open(csvFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int i=0;
            while(i < 5) {
                if((line = reader.readLine()) == null)
                    break;
                String[] columns = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1); // CSV 파일에서 각 열의 데이터 추출
                if(columns[titleColumn].contains(title)){
                    columnData.add(columns[titleColumn].trim());
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return columnData;
    }
    private List<String> readIDData(Context context, String csvFile, int actorColumn, int titleColumn, String title) {
        List<String> columnData = new ArrayList<>();

        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open(csvFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int idColumn = 5;
            int i=0;
            while(i < 5) {
                if((line = reader.readLine()) == null)
                    break;
                String[] columns = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1); // CSV 파일에서 각 열의 데이터 추출
                if(columns[titleColumn].contains(title)){
                    columnData.add(columns[idColumn].trim());
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return columnData;
    }private List<String> readOverViewData(Context context, String csvFile, int actorColumn, int titleColumn, String title) {
        List<String> columnData = new ArrayList<>();

        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open(csvFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int overViewColumn = 9;
            int i=0;
            while(i < 5) {
                if((line = reader.readLine()) == null)
                    break;
                String[] columns = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1); // CSV 파일에서 각 열의 데이터 추출
                if(columns[titleColumn].contains(title)){
                    String overview[] = columns[overViewColumn].trim().split("\\.", -1);
                    columnData.add(overview[0]);
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return columnData;
    }

}


