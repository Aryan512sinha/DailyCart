package eu.tutorials.shopping.entity
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "grocery_items")
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    var quanty: Int,
    var isEdit: Boolean = false,
)



