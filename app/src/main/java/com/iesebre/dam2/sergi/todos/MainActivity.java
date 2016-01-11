package com.iesebre.dam2.sergi.todos;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import static android.widget.CompoundButton.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SHARED_PREFERENCES_TODOS = "SP_TODOS";
    private static final String TODO_LIST = "todo_list";

    private Gson gson;
    private String taskName;
    private  int taskPriority;
    private boolean taskDone;
    public TodoArrayList tasks;
    private CustomListAdapter adapter;
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences todos = getSharedPreferences(SHARED_PREFERENCES_TODOS, 0);
        String todoList = todos.getString(TODO_LIST, null);

        /* JSON EXAMPLE

        [
         {"name":"Comprar llet", "done": true, "priority": 2},
         {"name":"Comprar pa", "done": true, "priority": 1},
         {"name":"Fer exercisi", "done": false, "priority": 3}
        ]
         */
        if (todoList == null) {
            String initial_json = "[\n" +
                    "         {\"name\":\"Comprar llet\", \"done\": true, \"priority\": 2},\n" +
                    "         {\"name\":\"Comprar pa\", \"done\": true, \"priority\": 1},\n" +
                    "         {\"name\":\"Fer exercici\", \"done\": false, \"priority\": 3}\n" + "]" ;
            SharedPreferences.Editor editor = todos.edit();
            editor.putString(TODO_LIST,initial_json);
            editor.commit();
            todoList = todos.getString(TODO_LIST, null);
        }

        Log.d("TAG PROVA", "**********************************");
        Log.d("TAG PROVA",todoList);
        Log.d("TAG PROVA", "**********************************");



        /* JSON EXAMPLE

        [
         {"name":"Comprar llet", "done": true, "priority": 2},
         {"name":"Comprar pa", "done": true, "priority": 1},
         {"name":"Fer exercisi", "done": false, "priority": 3}
        ]

        */


        Type arrayTodoList = new TypeToken<TodoArrayList>() {}.getType();
        this.gson = new Gson();
        TodoArrayList temp = gson.fromJson(todoList,arrayTodoList);

        if (temp != null) {
            tasks = temp;
        } else {
            //ErrorTODO
        }




        ListView todoslv =(ListView) findViewById(R.id.todolistview);

        //We bind our arraylist os task the adapter
        adapter = new CustomListAdapter(this, tasks);
        todoslv.setAdapter(adapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fabRemove = (FloatingActionButton) findViewById(R.id.fabremove);
        //FloatingActionButton fabRemove = (FloatingActionButton) findViewById(R.id.fab_remove);
        //fab.setOnClickListener((view)   {
       //           @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
//                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }
    public void showAddTaskForm(View view) {
        taskDone = false;
        EditText taskNameText;
        EditText taskPriorityText;
        CheckBox taskDoneText;
        MaterialDialog dialog = new MaterialDialog.Builder(this).
                title("Add new Task").
                customView(R.layout.form_add_task, true).
                negativeText("Cancel").
                positiveText("Add").
                negativeColor(Color.parseColor("#ff3333")).
                positiveColor(Color.parseColor("#2196F3")).
                onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        final TodoItem todoItem = new TodoItem();
                        todoItem.setName(taskName);
                        todoItem.setPriority(taskPriority);
                        todoItem.setDone(taskDone);
                        tasks.add(todoItem);

                        adapter.notifyDataSetChanged();
                    }
                }).
                build();
        dialog.show();

        taskNameText = (EditText) dialog.getCustomView().findViewById(R.id.task_title);
        taskNameText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        taskPriorityText = (EditText) dialog.getCustomView().findViewById(R.id.task_Priority);
        taskPriorityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskPriority = Integer.parseInt(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        taskDoneText = (CheckBox) dialog.getCustomView().findViewById(R.id.task_Done);
        taskDoneText.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    taskDone = true;
                } else {
                    taskDone = false;
                }
            }

        });

    }
    public void removeTask(View view){

        ListView lvItems = (ListView) findViewById(R.id.todolistview);

        for (int i = tasks.size() -1; i >= 0; i--)
        {
            LinearLayout vwParentRow = (LinearLayout) lvItems.getChildAt(i);
            CheckBox btnChild = (CheckBox)vwParentRow.getChildAt(0);
            if ( btnChild.isChecked()) {
                tasks.remove(i); }
        }

        adapter.notifyDataSetChanged();
    }
    public void editTask(View view){

        ListView lvItems = (ListView) findViewById(R.id.todolistview);

        

        adapter.notifyDataSetChanged();
    }

}

