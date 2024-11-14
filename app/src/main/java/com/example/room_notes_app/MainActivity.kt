package com.example.room_notes_app

import NoteViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        noteAdapter = NoteAdapter({ note ->
            // Edit Note (Open Dialog)
            showAddNoteDialog(note)
        }, { note ->
            // Delete Note
            noteViewModel.delete(note)
        })
        recyclerView.adapter = noteAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer { notes ->
            notes?.let { noteAdapter.setNotes(it) }
        })

        // Add new note button click listener
        findViewById<Button>(R.id.addNoteButton).setOnClickListener {
            // Open dialog to add a new note
            showAddNoteDialog(null)
        }
    }

    // Function to show the Add/Edit Note dialog
    private fun showAddNoteDialog(note: Note?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_note, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.noteTitleEditText)
        val contentEditText = dialogView.findViewById<EditText>(R.id.noteContentEditText)

        // If it's an edit, populate the fields with existing data
        note?.let {
            titleEditText.setText(it.title)
            contentEditText.setText(it.description)
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle(if (note == null) "Add New Note" else "Edit Note")
            .setView(dialogView)
            .setPositiveButton(if (note == null) "Save" else "Update") { _, _ ->
                val title = titleEditText.text.toString()
                val content = contentEditText.text.toString()
                if (title.isNotEmpty() && content.isNotEmpty()) {
                    if (note == null) {
                        // Add new note
                        val newNote = Note(title = title, description = content)
                        noteViewModel.insert(newNote)
                        Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show()
                    } else {
                        // Update existing note
                        val updatedNote = note.copy(title = title, description = content)
                        noteViewModel.update(updatedNote)
                        Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }
}
