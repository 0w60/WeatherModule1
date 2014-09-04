package cn.weathermodule1;

import android.app.Activity;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;


public class ShowResult extends Activity implements View.OnClickListener {

    static ArrayList<Weather> weatherList;
    static ListView showResultListView;
    static MatrixCursor cursor;
    static SimpleCursorAdapter adapter;

    static final String[] FROM_COLUMNS = {
            "City",
            "Day",
            "Conditions",
            "Temperature"};

    static final int[] TO_VIEWS = {
            R.id.cityTxtView,
            R.id.dayTxtView,
            R.id.condtnTxtView,
            R.id.tempTxtView};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        showResult();
    }


    void showResult() {
        weatherList = new ArrayList<>();
        showResultListView = (ListView) findViewById(R.id.listViewResult);
        cursor = getCursor();
        adapter = new SimpleCursorAdapter(this, R.layout.activity_views_for_list, cursor,
                FROM_COLUMNS, TO_VIEWS, 0);
        showResultListView.setAdapter(adapter);
    }

    static MatrixCursor getCursor() {
        String[] columns = {
                "_id",
                "City",
                "Day",
                "Conditions",
                "Temperature"};
        int id = 0;
        MatrixCursor matrixCursor = new MatrixCursor(columns);
        for (Weather w : weatherList) {
            matrixCursor.addRow(new Object[]{id++, w.city, w.day, w.weathrCondtns, w.temperature});
        }
        return matrixCursor;
    }

    static void refreshView(ArrayList<Weather> list) {
        weatherList.addAll(list);
        adapter.notifyDataSetChanged();
        adapter.changeCursor(getCursor());
        showResultListView.invalidateViews();
        showResultListView.refreshDrawableState();
    }

    @Override
    public void onClick(View backBttn) {
        Intent mainIntent = new Intent();
        mainIntent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
