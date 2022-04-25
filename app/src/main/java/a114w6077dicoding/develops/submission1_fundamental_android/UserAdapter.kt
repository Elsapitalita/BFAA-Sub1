package a114w6077dicoding.develops.submission1_fundamental_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.siyamed.shapeimageview.RoundedImageView
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter (private val users: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.RecyclerViewHolder>(), Filterable {

    private lateinit var onItemClickDetail: OnItemClickCallBack
    private lateinit var onItemClickShare: OnItemClickCallBack
    private var filterUsers: ArrayList<User> = users

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickDetail = onItemClickCallBack
        this.onItemClickShare = onItemClickCallBack
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val itemSearch = constraint.toString()
                filterUsers = if (itemSearch.isEmpty()) users else {
                    val itemlist = ArrayList<User>()
                    for (item in users) {
                        val name = item.name?.lowercase(Locale.ROOT)?.contains(
                            itemSearch.lowercase(
                                Locale.ROOT
                            ))
                        val userName = item.username?.lowercase(Locale.ROOT)?.contains(itemSearch.lowercase(
                                Locale.ROOT
                            )                        )
                        if (name!! || userName!!) {
                            itemlist.add(item)
                        }
                    }
                    itemlist
                }
                val filterResult = FilterResults()
                filterResult.values = filterUsers
                return filterResult
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterUsers = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, Position: Int): RecyclerViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int = filterUsers.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val user = filterUsers[position]
        Glide.with(holder.itemView).load(user.avatar)
            .apply(RequestOptions().override(300, 300))
            .into(holder.imageAvatar)
        with(holder){
            textName.text = user.name
            textUsername.text = user.username
            textCompany.text = user.company
            itemView.setOnClickListener{ onItemClickDetail.onItemClicked(filterUsers[holder.bindingAdapterPosition])}
            btnShare.setOnClickListener { onItemClickShare.onItemShared(filterUsers[holder.bindingAdapterPosition]) }
        }
    }

    inner class RecyclerViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        var imageAvatar: RoundedImageView = itemView.findViewById(R.id.item_avatar)
        var textName : TextView = itemView.findViewById(R.id.item_name)
        var textUsername : TextView = itemView.findViewById(R.id.text_item_username)
        var textCompany : TextView = itemView.findViewById(R.id.text_item_company)
        var btnShare : Button = itemView.findViewById(R.id.button_share)
    }

    interface OnItemClickCallBack{
        fun onItemClicked(data: User)
        fun onItemShared(data: User)
    }
}
