package dk.bollsjen.wantedcats.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dk.bollsjen.wantedcats.R
import dk.bollsjen.wantedcats.models.*
import java.text.SimpleDateFormat

public open class MyAdapter(
    private val items: List<Cat>,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            //.inflate(R.layout.list_item_simple, viewGroup, false)
            .inflate(R.layout.cat_card, viewGroup, false)
        return MyViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val item: Cat = items[position]
        viewHolder.catNameText.text = item.name
        viewHolder.catRewardText.text = item.reward.toString()
        viewHolder.catPlaceText.text = item.place
        val simpleDate = SimpleDateFormat("dd/M/yyyy")
        val currentDate = simpleDate.format(item.date * 1000)
        viewHolder.catDateText.text = currentDate
    }

    class MyViewHolder(itemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val catNameText: TextView = itemView.findViewById(R.id.item_card_cat_name)
        val catRewardText: TextView = itemView.findViewById(R.id.item_card_cat_reward)
        val catPlaceText: TextView = itemView.findViewById(R.id.item_card_cat_place)
        val catDateText: TextView = itemView.findViewById(R.id.item_card_cat_date)


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = bindingAdapterPosition
            // gradle     implementation "androidx.recyclerview:recyclerview:1.2.1"
            onItemClicked(position)
        }
    }
}