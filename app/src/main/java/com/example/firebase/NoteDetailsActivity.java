package com.example.firebase;

import android.annotation.SuppressLint;
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

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn, deleteBtn;
    TextView pageTitleTextView;
    String title, content, docId;
    boolean isEditMode = false;
    private String noteKey;
    private String storedDocId;
    DatabaseReference notesReference; // Reference to the Realtime Database

    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteBtn = findViewById(R.id.delete_note_btn);

        setupUser();

        notesReference = FirebaseDatabase.getInstance().getReference("notes"); // Reference to "notes" node in the

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("notes") && intent.hasExtra("note_key")) {
            Note notes = intent.getParcelableExtra("notes");
            noteKey = intent.getStringExtra("note_key");

            titleEditText.setText(notes.getTitle());
            contentEditText.setText(notes.getContent());
            storedDocId = notes.getDocId(); // Initialize storedDocId here

        }



        saveNoteBtn.setOnClickListener(v -> addOrUpdateNoteToDatabase());

        deleteBtn.setOnClickListener(v -> deleteData());
    }

    private void addOrUpdateNoteToDatabase() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String title = titleEditText.getText().toString().trim();
            String content = contentEditText.getText().toString().trim();
            String userId = currentUser.getUid();
            DatabaseReference userNotesRef = notesReference.child(userId);

            if (TextUtils.isEmpty(title)) {
                Toast.makeText(this, "Judul Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Isi Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            } else {
                long currentTimestamp = System.currentTimeMillis();
                String formattedTimestamp = Utility.timestampToString(currentTimestamp);

                Note note = new Note(title, content, formattedTimestamp, storedDocId);

                    userNotesRef.child(noteKey).setValue(note, (error, ref) -> {
                        if (error != null) {
                            Toast.makeText(this, "Gagal update" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "berhasil update", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    });

            }
        }
    }

    private void deleteData() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null && noteKey != null) {
            String userId = currentUser.getUid();
            DatabaseReference userNotesRef = notesReference.child(userId);

            userNotesRef.child(noteKey).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(NoteDetailsActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(NoteDetailsActivity.this, "Failed to delete data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
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
