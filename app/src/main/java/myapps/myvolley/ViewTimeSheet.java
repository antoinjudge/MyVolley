package myapps.myvolley;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ViewTimeSheet extends AppCompatActivity implements View.OnClickListener {

    private TextView editTEmpID;
    private TextView editTBasic;
    private TextView editTMeals;
    private TextView editTMileage;
    private TextView editTOT;
    private TextView editDate;
    private Button btnPrev;
    private Button btnNext;
    private Button btnSave;
    private Button btnDelete;

    private static final String SELECT_SQL = "SELECT * FROM times";
    private SQLiteDatabase db;

    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_time_sheet);

        openDatabase();

        editTEmpID = (TextView) findViewById(R.id.textViewId);
        editTBasic = (TextView) findViewById(R.id.textViewBasic);
        editTOT = (TextView) findViewById(R.id.textViewOverTime);
        editTMeals = (TextView) findViewById(R.id.textViewMeals);
        editTMileage = (TextView) findViewById(R.id.textViewMileage);
        editDate =(TextView) findViewById(R.id.textViewDate);

        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        c = db.rawQuery(SELECT_SQL, null);
        c.moveToFirst();
        showRecords();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    protected void openDatabase() {
        db = openOrCreateDatabase("MyDailyTS", Context.MODE_PRIVATE, null);
    }

    protected void showRecords() {
        String emplid = c.getString(0);
        String basic = c.getString(1);
        String overtime = c.getString(2);
        String meals = c.getString(3);
        String mileage = c.getString(4);
        String date =c.getString(5);

        editTEmpID.setText(emplid);
        editTBasic.setText("Basic Hours : "+basic);
        editTOT.setText("Overtime Hours : "+overtime);
        editTMeals.setText("Meals : "+meals);
        editTMileage.setText("Mileage : "+ mileage);
        editDate.setText("Date : "+ date);
    }

    protected void moveNext() {
        if (!c.isLast())
            c.moveToNext();

        showRecords();
    }

    protected void movePrev() {
        if (!c.isFirst())
            c.moveToPrevious();

        showRecords();

    }
    protected void saveRecord() {
        String empid = editTEmpID.getText().toString().trim();
        String basic = editTBasic.getText().toString().trim();
        String overtime = editTOT.getText().toString().trim();

        String sql = "UPDATE times SET empid='" + empid + "', basic='" + basic + "' WHERE empid=" + empid + "';";

        if (basic.equals("") || overtime.equals("") || empid.equals("")) {
            Toast.makeText(getApplicationContext(), "You cannot save blank values", Toast.LENGTH_LONG).show();
            return;
        }

        db.execSQL(sql);
        Toast.makeText(getApplicationContext(), "Records Saved Successfully", Toast.LENGTH_LONG).show();
        c = db.rawQuery(SELECT_SQL, null);
        c.moveToPosition(Integer.parseInt(empid));
    }

    protected void deleteRecord() {
        String empid = editTEmpID.getText().toString().trim();
        String thisemp = empid.replaceFirst(".*?(\\d+).*", "$1");
        int myempid = Integer.parseInt(thisemp);
        String date = editDate.getText().toString().trim();
        String overtime = editTOT.getText().toString().trim();

        String sql = "DELETE FROM  times WHERE  empid='" + myempid
                + "'";

      //  if (basic.equals("") || overtime.equals("") || empid.equals("")) {
           // Toast.makeText(getApplicationContext(), "You cannot save blank values", Toast.LENGTH_LONG).show();
           // return;
       // }

        db.execSQL(sql);
        Toast.makeText(getApplicationContext(), "Records Deleted Successfully", Toast.LENGTH_LONG).show();
        c = db.rawQuery(SELECT_SQL, null);
        c.moveToPosition(Integer.parseInt(empid));
    }

    @Override
    public void onClick(View v) {
        if (v == btnNext) {
            moveNext();
        }

        if (v == btnPrev) {
            movePrev();
        }

        if (v == btnSave) {
            saveRecord();
        }
        if (v == btnDelete) {
            deleteRecord();
        }


    }
}
