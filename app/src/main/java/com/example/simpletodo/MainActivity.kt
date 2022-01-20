package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import org.apache.commons.io.FileUtils
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object  : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //remove item from list
                listOfTasks.removeAt(position)
                //notify the adapter that something has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        loadItems()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter =  TaskItemAdapter(listOfTasks, onLongClickListener)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        findViewById<Button>(R.id.Button).setOnClickListener {
            val userInputtedTask = inputTextField.text.toString()

            listOfTasks.add(userInputtedTask)

            adapter.notifyItemInserted(listOfTasks.size - 1)

            inputTextField.setText("")

            saveItems()
        }


    }

    //Save data that the user has inputted
    //Save data by writing and reading a file

    //create a method to get the file we need
    fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }
    //Load the items by reading evry line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())

        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
    //Save items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}
