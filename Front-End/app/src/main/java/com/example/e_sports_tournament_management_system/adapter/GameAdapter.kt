import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_sports_tournament_management_system.R
import com.example.e_sports_tournament_management_system.model.Game

class GameAdapter(
    private val gameList: List<Game>,
    private val onEdit: (Game) -> Unit,
    private val onDelete: (Game) -> Unit
) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textGameName: TextView = itemView.findViewById(R.id.textGameName)
        val textPlatform: TextView = itemView.findViewById(R.id.textPlatform)
        val imageGame: ImageView = itemView.findViewById(R.id.imageGame)  // ajout ici
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gameList[position]
        holder.textGameName.text = game.name
        holder.textPlatform.text = game.platform

        Glide.with(holder.itemView.context)
            .load(game.imageUrl)
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.holo_red_dark)
            .into(holder.imageGame)

        holder.btnEdit.setOnClickListener { onEdit(game) }
        holder.btnDelete.setOnClickListener { onDelete(game) }
    }

    override fun getItemCount(): Int = gameList.size
}
