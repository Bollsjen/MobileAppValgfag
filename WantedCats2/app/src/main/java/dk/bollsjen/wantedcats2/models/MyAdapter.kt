package dk.bollsjen.wantedcats2.models

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import dk.bollsjen.wantedcats2.R

class MyAdapter(private val newList: ArrayList<Cat>) : RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = newList[position]
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleImage : ShapeableImageView = itemView.findViewById(R.id.textView_list_item)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View){
            val position = bindingAdapterPosition

            onItemClicked(position)
        }
    }

}