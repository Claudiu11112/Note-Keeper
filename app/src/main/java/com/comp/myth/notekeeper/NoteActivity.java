package com.comp.myth.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_POSITION = "com.myth.NOTE_POSITION";
    private NoteInfo mNote;
    private boolean isNewNote;
    private Spinner sp;
    private EditText mTitle;
    private EditText mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = findViewById(R.id.spinner_courses);
        List<CourseInfo> list = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(arrayAdapter);
        readDisplayStateValues();
        mTitle = findViewById(R.id.text_note_title);
        mText = findViewById(R.id.text_note_text);

        if (!isNewNote)
            displayNote(sp, mTitle, mText);

    }

    private void displayNote(Spinner sp, EditText title, EditText text) {
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int index = courses.indexOf(mNote.getCourse());
        sp.setSelection(index);
        title.setText(mNote.getTitle());
        text.setText(mNote.getText());
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, -1);
        isNewNote = position == -1;
        if (!isNewNote)
            mNote = DataManager.getInstance().getNotes().get(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {
        CourseInfo course = (CourseInfo) sp.getSelectedItem();
        String subject = mTitle.getText().toString();
        String text = "Course \"" + course.getTitle() + "\"\n" + mText.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }
}













