package com.example.notehub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notehub.data.Note
import com.example.notehub.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// callbacky pro editaci a mazání
class NoteAdapter(
    private val onEditClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    // Seznam poznámek, které se mají zobrazit
    private var notes: List<Note> = emptyList()

    //textové názvy priorit; index v poli = hodnota priority v databázi
    private val priorityNames = arrayOf("Nízká", "Střední", "Vysoká")

    private val dateFormat = SimpleDateFormat("d. M. yyyy HH:mm", Locale.getDefault())

    // ViewHolder drží binding jednoho itemu (jedné poznámky)
    class NoteViewHolder(val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Vytvoří nový ViewHolder – tedy grafiku jedné položky v seznamu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NoteViewHolder(binding)
    }

    // Naplní jednu položku daty z konkrétní poznámky (title + content)
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = notes[position]

        holder.binding.tvNoteTitle.text = currentNote.title
        holder.binding.tvNoteContent.text = currentNote.content

        // převedeme číslo priority na text
        // getOrElse ochrání před neplatnou hodnotou (mimo 0-2), místo pádu zobrazí "?"
        val priorityText = priorityNames.getOrElse(currentNote.priority) { "?" }
        holder.binding.tvNotePriority.text = "Priorita: $priorityText"

        val dateText = if (currentNote.createdAt == 0L) {
            "datum neznámé"
        } else {
            dateFormat.format(Date(currentNote.createdAt))
        }
        holder.binding.tvNoteMeta.text = "${currentNote.category} • $dateText"


        // Kliknutí na EDIT ikonu
        holder.binding.ivEdit.setOnClickListener {
            onEditClick(currentNote)
        }

        // Kliknutí na DELETE ikonu
        holder.binding.ivDelete.setOnClickListener {
            onDeleteClick(currentNote)
        }
    }

    // Vrátí počet poznámek → RecyclerView ví, kolik řádků má vykreslit
    override fun getItemCount(): Int = notes.size

    // Aktualizuje seznam poznámek a obnoví zobrazení
    fun submitList(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged() // oznámí RecyclerView, že má překreslit data
    }
}