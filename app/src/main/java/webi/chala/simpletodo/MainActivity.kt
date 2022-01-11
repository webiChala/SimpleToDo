package webi.chala.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                listOfTasks.removeAt(position)
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        //1. Let's detect when a user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            Log.i("Webi", "User clicked on button")
//        }

        loadItems()

        //Look up recycler view by id
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager  = LinearLayoutManager(this)
        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        findViewById<Button>(R.id.button).setOnClickListener {
            //1.Grab the text the user has inputted into @id/addTaslField
            val userInputtedTask = inputTextField.text.toString()

            //2. Add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            //Notify the adapter the data has been updatted.
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //3.Reset text Field
            inputTextField.setText("")
            saveItems()
        }
    }
    //Save the data that the user has inputted.
    //Save the data by writing and reading from a file

    //Get the file we need
    fun getDataFile() : File {
        //Every Line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")

    }

    //Load the items by reading every line in the data file
    fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())

        }catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    //Save items by writing them into our data file
    fun saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)

        }catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }


}