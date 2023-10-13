package hansung.ac.serieson;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainPage extends AppCompatActivity {
    private List<String> originalTitles;
    private final String [] genre = {"all", "Comedy", "Fantasy", "Animation", "Action", "Romance", "Thriller"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("Mini Project");

        String csvFile = "movies_metadata.csv";
        originalTitles = readTitleData(this, csvFile, 3, 8, 0);

        final GridView gv = (GridView) findViewById(R.id.gridView);
        MyGridAdapter gAdapter = new MyGridAdapter(this, 0);
        gv.setAdapter(gAdapter);

        Button voteBtn = findViewById(R.id.vote);
        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originalTitles = readTitleData(MainPage.this, "movies_metadata_voteCount.csv", 3, 8, 0);
                gAdapter.notifyDataSetChanged();
            }
        });

        Button allBtn = (Button) findViewById(R.id.all);
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originalTitles = readTitleData(MainPage.this, csvFile, 3, 8, 0);
                gAdapter.notifyDataSetChanged();
            }
        });

        Button comedyBtn = (Button) findViewById(R.id.comedy);
        comedyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originalTitles = readTitleData(MainPage.this, csvFile, 3, 8, 1);
                gAdapter.notifyDataSetChanged();
            }
        });

        Button fantasyBtn = (Button) findViewById(R.id.fantasy);
        fantasyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originalTitles = readTitleData(MainPage.this, csvFile, 3, 8, 2);
//                MyGridAdapter gAdapter = (MyGridAdapter) gv.getAdapter();
                gAdapter.notifyDataSetChanged();
            }
        });
        Button animationBtn = (Button) findViewById(R.id.animation);
        animationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originalTitles = readTitleData(MainPage.this, csvFile, 3, 8, 3);
//                MyGridAdapter gAdapter = (MyGridAdapter) gv.getAdapter();
                gAdapter.notifyDataSetChanged();
            }
        });
        Button actionBtn = (Button) findViewById(R.id.action);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originalTitles = readTitleData(MainPage.this, csvFile, 3, 8, 4);
//                MyGridAdapter gAdapter = (MyGridAdapter) gv.getAdapter();
                gAdapter.notifyDataSetChanged();
            }
        });
        Button romanceBtn = (Button) findViewById(R.id.romance);
        romanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originalTitles = readTitleData(MainPage.this, csvFile, 3, 8, 5);
//                MyGridAdapter gAdapter = (MyGridAdapter) gv.getAdapter();
                gAdapter.notifyDataSetChanged();
            }
        });
        Button thrillerBtn = (Button) findViewById(R.id.thriller);
        thrillerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originalTitles = readTitleData(MainPage.this, csvFile, 3, 8, 6);
//                MyGridAdapter gAdapter = (MyGridAdapter) gv.getAdapter();
                gAdapter.notifyDataSetChanged();
            }
        });
    }

    public class MyGridAdapter extends BaseAdapter {
        Context context;
        public MyGridAdapter(Context context, int count){ this.context = context; }
        public int getCount(){ return originalTitles.size();}
        public Object getItem(int arg0){ return null; }
        public long getItemId(int arg0){ return 0; }
        Integer posterID = R.drawable.poster_sample;

        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout layout = new LinearLayout(context);
            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.setHorizontalGravity(Gravity.CENTER_VERTICAL);
            layout.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 500);
            imageParams.gravity = Gravity.CENTER_HORIZONTAL;
            imageView.setLayoutParams(imageParams);
            imageView.setScaleType((ImageView.ScaleType.FIT_CENTER));
            imageView.setPadding(5, 5, 5, 5);
            imageView.setImageResource(posterID);

            TextView titleView = new TextView(context);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 70);
            titleParams.gravity = Gravity.CENTER_HORIZONTAL;
            titleView.setLayoutParams(titleParams);
            titleView.setPadding(5,5,5,5);
            titleView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            titleView.setSingleLine(true);
            titleView.setFocusable(true);
            titleView.setSelected(true);
            titleView.setFocusableInTouchMode(true);
            titleView.setMarqueeRepeatLimit(-1);


            final int pos = position;
            titleView.setText(originalTitles.get(pos));

            layout.addView(imageView);
            layout.addView(titleView);

            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String id = getID(MainPage.this, "movies_metadata.csv", 8, originalTitles.get(pos));
                    Intent intent = new Intent(MainPage.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("targetTab", "DETAIL"); // 이동할 탭의 ID를 전달
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

            View borderView = new View(context);
            LinearLayout.LayoutParams borderParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
            borderView.setLayoutParams(borderParams);
            borderView.setBackgroundResource(R.drawable.bottom_border);
            layout.addView(borderView);
            return layout;
        }
    }

    private List<String> readTitleData(Context context, String csvFile, int genreColumn, int titleColumn, int genreIndex) {
        List<String> columnData = new ArrayList<>();

        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open(csvFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int i=0;
            while(i < 20) {
                if((line = reader.readLine()) == null)
                    break;
                String[] columns = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1); // CSV 파일에서 각 열의 데이터 추출
                if(genreIndex == 0){ //all 일 때
                    columnData.add(columns[titleColumn].trim());
                    i++;
                }
                else{
                    List<String> names = new ArrayList<>();
                    Pattern pattern = Pattern.compile("'name':\\s*'([^']*)'");
                    Matcher matcher = pattern.matcher(columns[genreColumn]);
                    while (matcher.find()) {
                        String name = matcher.group(1);
                        names.add(name);
                    }
                    boolean hasGenre = false;
                    for(String name : names){
                        if(name.equals(genre[genreIndex])) {
                            hasGenre = true;
                            break;
                        }
                    }
                    if (hasGenre) {
                        columnData.add(columns[titleColumn].trim());
                        i++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return columnData;
    }
    private String getID(Context context, String csvFile, int titleColumn, String title) {
//        List<String> columnData = new ArrayList<>();
        String id="100";

        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open(csvFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int idColumn = 5;
            while(true) {
                if((line = reader.readLine()) == null)
                    break;
                String[] columns = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1); // CSV 파일에서 각 열의 데이터 추출
                if(columns[titleColumn].equals(title)){
                    id = columns[idColumn].trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }
}

