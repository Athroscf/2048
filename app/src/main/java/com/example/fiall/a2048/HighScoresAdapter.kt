package com.example.fiall.a2048

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.fiall.a2048.dataBase.tablesDB.Scores

class HighScoresAdapter(var listOfScores: List<Scores> = ArrayList<Scores>()): RecyclerView.Adapter<HighScoresAdapter.ViewHolder>() {

    private var onNoteItemClickListener: OnNoteItemClickListener? = null

    override fun getItemCount(): Int {
        return listOfScores.count()
    }

    override fun onBindViewHolder(holder: HighScoresAdapter.ViewHolder, position: Int) {
        // Obtener la posición del item seleccionado
        holder.view.setOnClickListener {
            onNoteItemClickListener?.onNoteItemClickListener(listOfScores[position])
        }
        holder.view.setOnLongClickListener {
            onNoteItemClickListener?.onNoteItemLongClickListener(listOfScores[position])
            true
        }
        holder.onBindViews(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoresAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_high_scores, parent, false)

        return RecyclerView.ViewHolder(view, listOfScores) as ViewHolder
    }
// probar este codigo
    class ViewHolder(val view: View, val listOfScores: List<Scores>: List<Scores>) : RecyclerView.ViewHolder(view) {
        fun onBindViews(position: Int) {
            view.findViewById<TextView>(R.id.tvTitulo).text = listOfScores[position].titulo
            view.findViewById<TextView>(R.id.).text =
                listOfScores[position].titulo.first().toUpperCase().toString()
        }
    }

}