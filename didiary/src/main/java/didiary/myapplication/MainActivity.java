package didiary.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends Activity {

    TextView dateText;
    EditText diary;
    Button save;
    DatePickerDialog datePickerDialog;
    int cYear, cMonth, cDay;
    String fileName, filePath;
    File myFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("일기장 앱");

        dateText = (TextView) findViewById(R.id.date);
        diary = (EditText) findViewById(R.id.diary);
        save = (Button) findViewById(R.id.save);

        Calendar cal = Calendar.getInstance();
        cYear = cal.get(Calendar.YEAR);
        cMonth = cal.get(Calendar.MONTH) + 1;
        cDay = cal.get(Calendar.DAY_OF_MONTH);


        fileName = cYear + "년_" + cMonth + "월_" + cDay + "일.txt";
        myFile = getExternalFilesDir("mydiary");
        filePath = myFile.getAbsolutePath();
        diary.setText(readDiary());

        dateText.setText(Integer.toString(cYear) + "년 " + Integer.toString(cMonth) + "월 " + Integer.toString(cDay) + "일");
        dateText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            cYear = year;
                            cMonth = monthOfYear + 1;
                            cDay = dayOfMonth;
                            dateText.setText(Integer.toString(cYear) + "년 " + Integer.toString(cMonth) + "월 " + Integer.toString(cDay) + "일");
                            fileName = cYear + "년_" + cMonth + "월_" + cDay + "일.txt";
                            diary.setText(readDiary());
                        }
                    }, cYear, cMonth - 1, cDay);
                    datePickerDialog.show();
                }
                return false;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(filePath + "/" + fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file, true);
                    String diaryContext = diary.getText().toString();
                    fos.write(diaryContext.getBytes());
                    fos.close();
                } catch (IOException e) {

                }
                Toast.makeText(getApplicationContext(), fileName + "저장", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        final float textSize = 36;

        if (id == R.id.reread) {
            diary.setText(readDiary());
            return true;
        } else if(id == R.id.del) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(dateText.getText() + "\n 일기를 삭제?");
            dlg.setPositiveButton("확인!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    File file = new File(filePath + "/" + fileName);
                    file.delete();
                    diary.setText(readDiary());
                    Toast.makeText(getApplicationContext(), fileName + "삭제", Toast.LENGTH_SHORT).show();
                }
            });
            dlg.setNegativeButton("취소!",null);
            dlg.show();
        } else if(id == R.id.big) {
            diary.setTextSize(textSize * 4 / 3);
        } else if(id == R.id.small) {
            diary.setTextSize(textSize * 2 / 3);
        } else {
            diary.setTextSize(textSize);
        }
        return super.onOptionsItemSelected(item);
    }

    String readDiary() {
        String diaryContext = null;
        FileInputStream fis;
        File file = new File(filePath + "/" + fileName);
        try {
            fis = new FileInputStream(file);
            byte[] txt = new byte[500];
            fis.read(txt);
            fis.close();
            diaryContext = (new String(txt)).trim();
        } catch (IOException e) {

        }
        return diaryContext;
    }
}
