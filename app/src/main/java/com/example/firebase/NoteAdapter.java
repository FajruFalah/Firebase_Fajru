package com.example.firebase;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase.databinding.RecyclerNoteItemBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

/**
 * 10120054
 * Fajru Falah
 * IF-2
 */

public class NoteAdapter extends FirebaseRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {

    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Note item, String noteKey);
    }

    public NoteAdapter(FirebaseRecyclerOptions<Note> options, OnItemClickListener listener) {
        super(options);
        this.itemClickListener = listener;
    }

    @Override
    protected void onBindViewHolder(NoteViewHolder holder, int position, Note model) {
        holder.bind(model);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerNoteItemBinding binding = RecyclerNoteItemBinding.inflate(inflater, parent, false);
        return new NoteViewHolder(binding);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerNoteItemBinding binding;

        public NoteViewHolder(RecyclerNoteItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(getItem(position), getRef(position).getKey()); // Pass the noteKey to the click listener
                }
            });
        }

        public void bind(Note item) {
            binding.noteTitleTextView.setText(item.getTitle());
            binding.noteContentTextView.setText(item.getContent());
            binding.noteTimestampTextView.setText(item.getTimestamp());
            binding.docIdTextView.setText(item.getDocId());
        }
    }

    public void onDataChanged() {
        super.onDataChanged();
        notifyDataSetChanged();
    }
}

