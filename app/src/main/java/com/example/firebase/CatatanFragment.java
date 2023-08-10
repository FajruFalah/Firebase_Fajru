package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.firebase.databinding.ActivityCatatanFragmentBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * 10120054
 * Fajru Falah
 * IF-2
 */

public class CatatanFragment extends Fragment {

    private NoteAdapter noteAdapter;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ActivityCatatanFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityCatatanFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("notes");

        setupUser();
        loadData();

        binding.addNoteBtn.setOnClickListener(v -> startActivity(new Intent(requireActivity(), AddNoteActivity.class)));

    }


    private void loadData(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userNotesRef = databaseReference.child(userId);

            LinearLayoutManager manager = new LinearLayoutManager(requireContext());
            binding.rvNote.setLayoutManager(manager);

            FirebaseRecyclerOptions<Note> options =
                    new FirebaseRecyclerOptions.Builder<Note>()
                            .setQuery(userNotesRef, Note.class)
                            .build();
            noteAdapter = new NoteAdapter(options, (item, noteKey) -> {
                showDetailActivity(item, noteKey);
            });
            binding.rvNote.setAdapter(noteAdapter);

        }
    }

    private void showDetailActivity(Note item, String noteKey) {
        Intent intent = new Intent(requireContext(), NoteDetailsActivity.class);
        intent.putExtra("notes",item);
        intent.putExtra("note_key", noteKey);
        intent.putExtra("is_edit", true);
        startActivity(intent);
    }

    private void setupUser() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(requireContext(), Login.class));
            requireActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        noteAdapter.startListening();

    }

    @Override
    public void onPause() {
        super.onPause();
        noteAdapter.stopListening();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
