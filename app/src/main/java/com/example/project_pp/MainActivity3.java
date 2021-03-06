package com.example.project_pp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity3 extends AppCompatActivity {

    //de database
    Database database;

    //id van de website
    int id;

    //intent om de putextra op te vangen
    Intent intent;

    //tittel opvragen om kleur te geven
    TextView textView1,textView4,textView1dialog1 ,textView2dialog1;

    //de listvieuw
    ListView listView1, listView2;

    //Dialog
    Dialog dialog;

    //imageview
    ImageView imageView1dialog1 ;

    //editText dialog
    EditText editTextdialog1;

    //dit zijn de namen voor de listview
    String emails[] = {"yago.engels@gmail.com"};
    String passwords[] = {"u$nAO!ELs&r$"};

    //de adapter voor de eerste listview
    MainActivity3List mainActivity3List;
    MainActivity3List1 mainActivity3List1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //dit zorgt ervoor dat deze app word gedraaid in een fullscreen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        //deze gaat de topbar omzetten in een kleur , die doet dit ook met de bot bar
        getWindow().setNavigationBarColor(Color.parseColor("#ffffff"));
        getWindow().setStatusBarColor(Color.parseColor("#ffffff"));

        //de database connectere
        database = new Database(this);

        //dialogt koppelen
        dialog = new Dialog(this);

        //intent koppelen aan een object
        intent = getIntent();

        //id is nu de id die mee is gegeven met de intent
        id = intent.getIntExtra("webId",-1);

        //texvieuw aan een object vast hangen
        textView1 = findViewById(R.id.text1);
        textView4 = findViewById(R.id.text4);

        //textvieuw kleuren
        setColorToGradiant(textView1);

        //listview aan een object vast hangen
        listView1 = findViewById(R.id.list_view_1);
        listView2 = findViewById(R.id.list_view_2);

        comboMaker();
        //de adapters een object geven
        mainActivity3List = new MainActivity3List(this,emails);
        mainActivity3List1 = new MainActivity3List1(this,passwords);
        listView1.setAdapter(mainActivity3List);
        listView2.setAdapter(mainActivity3List1);

        //functies
        setText1();
        clickOnAdd();
        clickOnHome();
        clickOnListview1();
        clickOnListview2();
    }


    //de functie van de verf lol
    private void setColorToGradiant(TextView textView){
        TextPaint textPaint = textView.getPaint();
        float width = textPaint.measureText(textView.getText().toString());

        Shader shader = new LinearGradient(width/2,0,width/2,textView.getTextSize(),
                new int[]{
                        Color.parseColor("#00BF8F"),
                        Color.parseColor("#001510")
                },null,Shader.TileMode.CLAMP);
        textView.getPaint().setShader(shader);
    }

    //de functie click on add
    public void clickOnAdd(){
        textView4.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity5.class);
                intent.putExtra("webId",id);

                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View,String>(textView4,"1");
                pairs[1] = new Pair<View,String>(textView4,"3");
                pairs[2] = new Pair<View,String>(textView4,"5");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity3.this,
                        pairs);

                startActivity(intent,options.toBundle());
            }
        });
    }

    //de functie click on home
    public void clickOnHome(){
        textView1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(textView1,"2");
                pairs[1] = new Pair<View,String>(textView1,"1");
                pairs[1] = new Pair<View,String>(textView1,"3");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity3.this,
                        pairs);

                startActivity(intent,options.toBundle());
            }
        });
    }

    //de functie set text one
    public void setText1(){
        textView1.setText(database.getTable_1_col_2(id));
    }

    //fun make combo voor listview
    public void comboMaker(){
        String[] uit = database.emails(id).clone();
        String[] uit2 = database.codes(id).clone();
        emails = uit;
        passwords = uit2;
    }

    //click functie voor de eerste listview
    public void clickOnListview1(){
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password",database.getTable_2_col_4(database.idsTableTwo(id)[i]));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),database.getTable_2_col_4(database.idsTableTwo(id)[i]) + "\n is gecopieerd", Toast.LENGTH_LONG).show();
            }
        });

        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                dialog.setContentView(R.layout.dialoge1);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                imageView1dialog1 = dialog.findViewById(R.id.imageViewPopup);
                textView1dialog1 = dialog.findViewById(R.id.textView_edit);
                textView2dialog1 = dialog.findViewById(R.id.textView_verwijderen);
                editTextdialog1 = dialog.findViewById(R.id.editTextDialog);
                editTextdialog1.setText(database.getTable_2_col_3(database.idsTableTwo(id)[i]));

                //button om de popup te closen
                imageView1dialog1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                //button voor een edit
                textView1dialog1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                        database.rename2(database.idsTableTwo(id)[i],id,editTextdialog1.getText().toString(),database.getTable_2_col_4(database.idsTableTwo(id)[i]));
                        startActivity(intent);
                    }
                });

                //button voor de verwijdering
                textView2dialog1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                        database.deleteTable2Row(database.idsTableTwo(id)[i]);
                        startActivity(intent);
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    public void clickOnListview2(){
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password",database.getTable_2_col_4(database.idsTableTwo(id)[i]));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(),database.getTable_2_col_4(database.idsTableTwo(id)[i]) + "\n is gecopieerd", Toast.LENGTH_LONG).show();
            }
        });

        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                dialog.setContentView(R.layout.dialoge1);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                imageView1dialog1 = dialog.findViewById(R.id.imageViewPopup);
                textView1dialog1 = dialog.findViewById(R.id.textView_edit);
                textView2dialog1 = dialog.findViewById(R.id.textView_verwijderen);
                editTextdialog1 = dialog.findViewById(R.id.editTextDialog);
                editTextdialog1.setText(database.getTable_2_col_4(database.idsTableTwo(id)[i]));

                //button om de popup te closen
                imageView1dialog1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                //button voor een edit
                textView1dialog1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                        database.rename2(database.idsTableTwo(id)[i],id,database.getTable_2_col_3(database.idsTableTwo(id)[i]),editTextdialog1.getText().toString());
                        startActivity(intent);
                    }
                });

                //button voor de verwijdering
                textView2dialog1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                        database.deleteTable2Row(database.idsTableTwo(id)[i]);
                        startActivity(intent);
                    }
                });
                dialog.show();
                return true;
            }
        });
    }
}