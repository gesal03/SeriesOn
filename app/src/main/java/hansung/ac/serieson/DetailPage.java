package hansung.ac.serieson;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailPage extends AppCompatActivity {
//    String id="278";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        String movie = "movies_metadata.csv";
        String credit = "credits.csv";

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
//        Log.d("id", id);

        // 제목
        TextView titleView = findViewById(R.id.title);
        titleView.setText(getData(this,movie, 8,5,id));

        // 평점
        TextView ratingView = findViewById(R.id.rating);
        ratingView.setText("⭐️" + getData(this,movie,22,5,id));

        // 년도
        TextView releaseView = findViewById(R.id.release);
        releaseView.setText("🗓️" + getData(this, movie, 14, 5,id));

        // 재생 시간
        TextView runTimeView = findViewById(R.id.runTime);
        runTimeView.setText("🕐" + getData(this, movie, 16, 5, id) + "Min");

        // 감독
        TextView directorView = findViewById(R.id.director);
        directorView.setText(getDirector(this, id));

        // 출연진
        TextView castView1 = findViewById(R.id.cast1);
        TextView castView2 = findViewById(R.id.cast2);
        TextView castView3 = findViewById(R.id.cast3);
        setCastView(this, castView1, castView2, castView3, id);

        // overView
        TextView overviewView = findViewById(R.id.overview);
        overviewView.setText(getData(this, movie, 9, 5, id));




    }
    private String getData(Context context, String csvFile, int targetColumn,int idColumn, String id) {
        String result="";

        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open(csvFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while(true) {
                if((line = reader.readLine()) == null)
                    break;
                String[] columns = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1); // CSV 파일에서 각 열의 데이터 추출
                if(columns[idColumn].equals(id)){
                    result = columns[targetColumn].trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getDirector(Context context,String id){
        String name="";

        String csvFile = "credits.csv";
        int directorColumn = 1;
        int idColumn = 2;
        boolean flag=false;

        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open(csvFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while(true) {
                if((line = reader.readLine()) == null)
                    break;
                if(flag)
                    break;
                String[] columns = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1); // CSV 파일에서 각 열의 데이터 추출
                if(columns[idColumn].equals(id)){
                    Pattern patternJob = Pattern.compile("'job':\\s*'([^']*)'");
                    Pattern patternName = Pattern.compile("'name':\\s*'([^']*)'");
                    Matcher matcherJob = patternJob.matcher(columns[directorColumn]);
                    Matcher matcherName = patternName.matcher(columns[directorColumn]);
                    int index = -1;
                    while (matcherJob.find()) {
                        String job = matcherJob.group(1);
                        if (job.equals("Director")) {
                            index = matcherJob.start();  // 'job'이 'Director'인 데이터의 시작 인덱스 저장
                            break;
                        }
                    }
                    if (index != -1) {
                        // 'job'이 'Director'인 데이터의 'Name' 값을 추출
                        if (matcherName.find(index)) {
                            name = matcherName.group(1);
                            flag=true;
                            Log.d("director", name);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }
    private void setCastView(Context context, TextView castView1, TextView castView2, TextView castView3, String id){
        String csvFile = "credits.csv";
        List<String> names = new ArrayList<>();
        int castColumn = 0;
        int idColumn = 2;
        int i=0;
        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open(csvFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while(true) {
                if((line = reader.readLine()) == null)
                    break;
                String[] columns = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)",-1); // CSV 파일에서 각 열의 데이터 추출
                if(columns[idColumn].equals(id)){
                    Pattern pattern = Pattern.compile("'name':\\s*'([^']*)'");
                    Matcher matcher = pattern.matcher(columns[castColumn]);
                    while (matcher.find()) {
                        if(i ==3)
                            break;
                        String name = matcher.group(1);
                        names.add(name);
                        i++;
                    }
                    castView1.setText(names.get(0));
                    castView2.setText(names.get(1));
                    castView3.setText(names.get(2));
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

