package com.example.smartattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    StudentAdapter studentAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<StudentItem> studentItems = new ArrayList<>();
    Toolbar toolbar;

    EditText id1;
    EditText name1;
    private DbHelper dbHelper;
    private MyCalendar calendar;
    private TextView Sectitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = new MyCalendar();

        dbHelper = new DbHelper(this);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(v -> showDialog());

        setToolbar();
        loadData();

        recyclerView = findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        studentAdapter = new StudentAdapter(this, studentItems);
        recyclerView.setAdapter(studentAdapter);
        studentAdapter.setOnItemClickListener(position -> gotoItemActivity(position));
        studentAdapter.setOnItemClickListener(position -> changeStatus(position));
        loadStatusData();

    }

    private void loadData() {
        Cursor cursor = dbHelper.getStudentTable();
        studentItems.clear();

        int columnIndexSid = cursor.getColumnIndex(DbHelper.S_ID);
        int columnIndexStudentId = cursor.getColumnIndex(DbHelper.STUDENT_ROLL_KEY);
        int columnIndexStudentName = cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY);

        while (cursor.moveToNext()) {
            long sid = cursor.getLong(columnIndexSid);
            int studentId = cursor.getInt(columnIndexStudentId);
            String studentName = cursor.getString(columnIndexStudentName);
            studentItems.add(new StudentItem(sid, studentId, studentName));
        }

        cursor.close();
    }


    private void changeStatus(int position) {
        String status = studentItems.get(position).getStatus();
        if (status.equals("P")) status = "A";
        else status = "P";
        studentItems.get(position).setStatus(status);
        studentAdapter.notifyItemChanged(position);
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        Sectitle = toolbar.findViewById(R.id.Sec_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);
        save.setOnClickListener(v -> saveStatus());
        title.setText("Attendance App");
        Sectitle.setText(calendar.getDate());
        back.setVisibility(View.INVISIBLE);

        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem -> onMenuItemClick(menuItem));

    }

    private void saveStatus() {
        for (StudentItem studentItem : studentItems) {
            String status = studentItem.getStatus();
            if (status != "P") status = "A";
            long value = dbHelper.addStatus(studentItem.getSid(), calendar.getDate(), status);
            if (value == -1)
                dbHelper.updateStatus(studentItem.getSid(), calendar.getDate(), status);

        }
    }

    private void loadStatusData() {
        for (StudentItem studentItem : studentItems) {
            String status = dbHelper.getStatus(studentItem.getSid(),calendar.getDate());
            if(status!=null) studentItem.setStatus(status);
            else studentItem.setStatus("");

        }
        studentAdapter.notifyDataSetChanged();
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.show_Calendar) {
            showCalendar();
        }
        if (menuItem.getItemId() == R.id.show_attendance_sheet) {
            openSheetList();
        }

        return true;
    }

    private void openSheetList() {
        long[] idArray = new long[studentItems.size()];
        String[] nameArray = new String[studentItems.size()];
        int[] rollArray = new int[studentItems.size()];

        for(int i=0;i<idArray.length;i++)
            idArray[i]=studentItems.get(i).getSid();
        for(int i=0;i<rollArray.length;i++)
            rollArray[i]=studentItems.get(i).getStudentId();
        for(int i=0;i<nameArray.length;i++)
            nameArray[i]=studentItems.get(i).getStudentName();


        Intent intent=new Intent(this,SheetListActivity.class);
        intent.putExtra("idArray", idArray);
        intent.putExtra("rollArray", rollArray);
        intent.putExtra("nameArray", nameArray);
        startActivity(intent);
    }

    private void showCalendar() {
        MyCalendar calendar = new MyCalendar();
        calendar.show(getSupportFragmentManager(), "");
        calendar.setOnCalendarOkClickListener(this::onCalendarOkClicked);

    }

    private void onCalendarOkClicked(int year, int month, int day) {
        calendar.setDate(year, month, day);
        Sectitle.setText(calendar.getDate());
        loadStatusData();
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("className", studentItems.get(position).getStudentId());
        intent.putExtra("Section", studentItems.get(position).getStudentName());
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.student_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        id1 = view.findViewById(R.id.id);
        name1 = view.findViewById(R.id.name);
        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(v -> dialog.dismiss());
        add.setOnClickListener(v -> {
            addClass();
            dialog.dismiss();
        });

    }

    private void addClass() {
        String studentId = id1.getText().toString();
        String StudentName = name1.getText().toString();
        int StudentId = Integer.parseInt(studentId);
        long sid = dbHelper.addStudent(StudentId, StudentName);
        StudentItem studentItem = new StudentItem(sid, StudentId, StudentName);
        studentItems.add(studentItem);
        studentAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                break;
            case 1:
                deleteStudent(item.getGroupId());

        }
        return super.onContextItemSelected(item);
    }

    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getSid());
        studentItems.remove(position);
        studentAdapter.notifyItemRemoved(position);
    }
}