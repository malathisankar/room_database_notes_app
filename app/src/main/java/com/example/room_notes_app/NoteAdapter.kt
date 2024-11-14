package com.example.room_notes_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private val onEditClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var notes = listOf<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noteTitle: TextView = itemView.findViewById(R.id.noteTitle)
        private val noteDescription: TextView = itemView.findViewById(R.id.noteDescription)
        private val editNoteButton: Button = itemView.findViewById(R.id.editNoteButton)
        private val deleteNoteButton: Button = itemView.findViewById(R.id.deleteNoteButton)

        fun bind(note: Note) {
            noteTitle.text = note.title
            noteDescription.text = note.description

            // Edit button click listener
            editNoteButton.setOnClickListener {
                onEditClick(note)
            }

            // Delete button click listener
            deleteNoteButton.setOnClickListener {
                onDeleteClick(note)
            }
        }
    }
}
