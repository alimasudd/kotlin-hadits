package id.alimasudd.hadits.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.alimasudd.hadits.R
import id.alimasudd.hadits.model.ModelDoa
import kotlinx.android.synthetic.main.list_item_doa.view.*
import java.util.*

/**
 * Created by @alimasudd on 02-12-2022
 */

class AdapterDoa(context: Context, private val modelBacaan: MutableList<ModelDoa>) :
        RecyclerView.Adapter<AdapterDoa.ListViewHolder>(), Filterable {

    private val modelBacaanListFull: List<ModelDoa> = ArrayList(modelBacaan)
    private val context: Context

    init {
        this.context = context
    }

    override fun getFilter(): Filter {
        return modelBacaanFilter
    }

    private val modelBacaanFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<ModelDoa> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(modelBacaanListFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase()
                for (modelDoaFilter in modelBacaanListFull) {
                    if (modelDoaFilter.strTitle!!.toLowerCase().contains(filterPattern)) {
                        filteredList.add(modelDoaFilter)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            modelBacaan.clear()
            modelBacaan.addAll(results.values as List<ModelDoa>)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_doa, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(listViewHolder: ListViewHolder, i: Int) {
        val dataModel = modelBacaan[i]
        listViewHolder.tvId.text = dataModel.strId
        listViewHolder.tvTitle.text = dataModel.strTitle
        listViewHolder.tvArabic.text = dataModel.strArabic
        listViewHolder.tvLatin.text = dataModel.strLatin
        listViewHolder.tvTerjemahan.text = dataModel.strTranslation
    }

    override fun getItemCount(): Int {
        return modelBacaan.size
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvId: TextView
        var tvTitle: TextView
        var tvArabic: TextView
        var tvLatin: TextView
        var tvTerjemahan: TextView

        init {
            tvId = itemView.tvId
            tvTitle = itemView.tvTitle
            tvArabic = itemView.tvArabic
            tvLatin = itemView.tvLatin
            tvTerjemahan = itemView.tvTerjemahan
        }
    }

}