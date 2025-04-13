package eu.tutorials.shopping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import eu.tutorials.shopping.entity.User
import eu.tutorials.shopping.entity.UserRepository
import eu.tutorials.shopping.entity.UserViewModel
import eu.tutorials.shopping.entity.UserViewModelFactory
import eu.tutorials.shopping.ui.theme.ShoppingTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var userRepo: UserRepository
    private var sItems by mutableStateOf(listOf<User>())


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userRepo = UserRepository(applicationContext)
        val viewModelFactory = UserViewModelFactory(userRepo)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)
        lifecycleScope.launch {
            sItems = userRepo.getUsersList()
        }
        setContent {
            ShoppingTheme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showdialog by remember{ mutableStateOf(false) }
                    var itemname by remember{ mutableStateOf("") }
                    var itemquantity by remember{ mutableStateOf("") }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ){
                       Button(
                           onClick = {showdialog = true},
                           modifier = Modifier.align(Alignment.CenterHorizontally)
                       ) {
                           Text(text = "grocessary")
                       }
                           LazyColumn(
                               modifier = Modifier
                                   .fillMaxSize()
                                   .padding(16.dp)
                           ){
                               items(sItems){
                                   item ->
                               if(item.isEdit) {
                                   grocessaryitemEditor(
                                       item = item,
                                       viewModel = viewModel,
                                       onEditComplete = { editedName,editedQuantity  ->
                                           sItems = sItems.map { it.copy(isEdit = false) }
                                           val editeditem = sItems.find { it.id == item.id }
                                           editeditem?.let {
                                               it.name = editedName
                                               it.quanty = editedQuantity
                                           }
                                           viewModel.updateItem(item.copy(name = editedName, quanty = editedQuantity.toString().toIntOrNull() ?: 0
                                           ))
                                       })
                               }
                               else{
                                       grocessaryitemlist(item = item, onEditClick = {
                                           sItems =
                                               sItems.map { it.copy(isEdit = it.id == item.id) }
                                       }, onDeleteClick = {
                                           lifecycleScope.launch {
                                               userRepo.delete(item)
                                               sItems = userRepo.getUsersList()

                                           }})

                                   }




                                   }
                               }

                           }


                        if(showdialog){
                            AlertDialog(
                                onDismissRequest = {showdialog = false},
                                confirmButton = {
                                                Row(modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(8.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween)
                                                {
                                                    Button(onClick = {
                                                        if (itemname.isNotBlank()){
                                                            val newitem =User(
                                                                id=sItems.size+1 ,
                                                                    name= itemname,
                                                                quanty = itemquantity.toIntOrNull()?:0
                                                            )

                                                            lifecycleScope.launch {
                                                                userRepo.insert(newitem)  // ✅ Insert into database
                                                                sItems = userRepo.getUsersList()}
                                                                showdialog=false
                                                            itemname=""
                                                            itemquantity=""
                                                        }
                                                    }) {
                                                        Text(text = "add")
                                                    }
                                                    Button(onClick = {showdialog=false}) {
                                                        Text(text = "cancel")
                                                    }

                                                }
                                },
                                title = { Text(text = "add items")},
                                text ={
                                    Column {
                                        OutlinedTextField(value = itemname , onValueChange ={itemname = it},
                                            singleLine = true,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp))
                                        OutlinedTextField(value = itemquantity, onValueChange ={itemquantity = it},
                                            singleLine = true,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(10.dp))
                                    }

                                }
                            )

                            }
                        }
                    }
                }
        
            }
        }





@Composable
fun grocessaryitemEditor(item: User,viewModel: UserViewModel, onEditComplete:(String,Int)->Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quanty.toString()) }


    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        Column {
            BasicTextField(
                value =editedName,
                onValueChange ={editedName = it},
                singleLine= true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
            BasicTextField(
                value =editedQuantity,
                onValueChange ={editedQuantity = it},
                singleLine= true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
        }

        Button(onClick = {
            val updatedItem = item.copy(
                name = editedName,
                quanty = editedQuantity.toIntOrNull() ?: 1,
                isEdit=true
            )
            viewModel.updateItem(updatedItem) // Call ViewModel function to update item
            onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
            }
    ) {
           Text(text = "save")
        }
    }

}


@Composable
fun grocessaryitemlist(
    item:User,
    onEditClick: ()-> Unit,
    onDeleteClick: ()-> Unit,

) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(3.dp, Color.Black),
                shape = RoundedCornerShape(30)
            )
    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "qty :${item.quanty}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)

            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }

        }
    }
}


