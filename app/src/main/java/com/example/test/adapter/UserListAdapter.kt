import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.R
import com.example.test.model.User
import com.example.test.activity.UserDetailsActivity

class UserListAdapter(private val userList: List<User>) :
    RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // Inflate your list item layout here

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return UserViewHolder(view)

       // return TODO("Provide the return value")
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]

        holder.textView.text = user.title.toString()


        Glide.with(holder.imageView.context)
            .load(Uri.parse(user.image.toString()))
            .into(holder.imageView);

        holder.textView.setOnClickListener {


            val intent = Intent(holder.textView.context, UserDetailsActivity::class.java)
            intent.putExtra("title", user.title.toString())
            intent.putExtra("category", user.category.toString())
            intent.putExtra("price", user.price.toString())
            intent.putExtra("description", user.description.toString())
            intent.putExtra("image", user.image.toString())
            intent.putExtra("rate", user.rate.toString())
            intent.putExtra("count", user.count.toString())



            holder.textView.context.startActivity(intent)



        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.productImageView)
        val textView: TextView = itemView.findViewById(R.id.textView)

    }
    
}

