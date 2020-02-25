package sg.edu.smu.cs461.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> taskList;
    private ArrayAdapter adapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskList = new ArrayList<>();
        lv = findViewById(R.id.tasks_list);
        readFromFile();
        adapter = new ArrayAdapter(
          this,
          android.R.layout.simple_list_item_1,
          taskList
        );
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteItem(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    public void addThisTask(View view) {
        EditText et = findViewById(R.id.new_task);
        String newTask = et.getText().toString();

        try{
            PrintStream ps = new PrintStream(openFileOutput("new_tasks_list.txt", MODE_PRIVATE|MODE_APPEND));
            ps.println(newTask);
            ps.close();
        }catch(IOException ioe){


        }
        taskList.add(newTask);
        adapter.notifyDataSetChanged();
        et.setText("");
    }

    private void readFromFile() {
        Scanner scan = null;
        try {
            scan = new Scanner(openFileInput("new_tasks_list.txt"));
            readHelper(scan);
        } catch (Exception e) {

        }
    }

    private void readHelper(Scanner scan) {
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] pieces = line.split(",");
            taskList.add(pieces[0]);
        }
    }

    private void deleteItem(int index){
        String itemToDel = taskList.get(index);
        taskList.remove(itemToDel);

        try {
            PrintStream ps = new PrintStream(openFileOutput("new_tasks_list.txt", MODE_PRIVATE));
            for (String listItem : taskList) {
                ps.println(listItem);
            }
        } catch (FileNotFoundException e) {
        }
        Toast.makeText(this, "Item " + itemToDel + " is marked as completed!", Toast.LENGTH_SHORT).show();
    }
}
