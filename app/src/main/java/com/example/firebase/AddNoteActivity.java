package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;


/**
 * 10120054
 * Fajru Falah
 * IF-2
 */

public class AddNoteActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn, deleteBtn;
    TextView pageTitleTextView;
    String title, content, docId;
    boolean isEditMode = false;
    private String noteKey;
    private String storedDocId;
    DatabaseReference notesReference; // Reference to the Realtime Database

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);


        setupUser();

        notesReference = FirebaseDatabase.getInstance().getReference("notes");

        saveNoteBtn.setOnClickListener(v -> addNoteToDatabase());
    }

    private void addNoteToDatabase() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
           String title = titleEditText.getText().toString().trim();
           String content = contentEditText.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                Toast.makeText(this, "Judul Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Isi Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            } else {
                String userId = currentUser.getUid();
                DatabaseReference userNotesRef = notesReference.child(userId);

                long currentTimestamp = System.currentTimeMillis();
                String formattedTimestamp = Utility.timestampToString(currentTimestamp);
                String docId = generateShortId();

                Note notes = new Note(title, content, formattedTimestamp,docId);

                userNotesRef.push().setValue(notes, (error, ref) -> {
                    if (error != null) {
                        Toast.makeText(this, "Gagal upload" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Berhasil upload", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                });
            }
        }
    }

    private void setupUser() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, Login.class));
            this.finish();
        }
    }

    private String generateShortId() {
        UUID uuid = UUID.randomUUID();
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();
        byte[] bytes = ByteBuffer.allocate(16)
                .putLong(mostSigBits)
                .putLong(leastSigBits)
                .array();
        String base64Uuid = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            base64Uuid = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        }
        return base64Uuid;
    }
}