package org.izv.ad.psp1920chat;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.izv.ad.psp1920chat.apibot.ChatterBot;
import org.izv.ad.psp1920chat.apibot.ChatterBotFactory;
import org.izv.ad.psp1920chat.apibot.ChatterBotSession;
import org.izv.ad.psp1920chat.apibot.ChatterBotType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "xyz";

    private ChatterBotSession botsession = null;
    private EditText etTexto;
    private TextView tvTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etTexto = findViewById(R.id.etTexto);
        tvTexto = findViewById(R.id.tvTexto);

        initChat();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etTexto.getText().toString();
                etTexto.setText("");
                new Chat().execute(text);
            }
        });
    }

    private void initChat() {
        try {
            ChatterBotFactory factory = new ChatterBotFactory();
            ChatterBot bot = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            botsession = bot.createSession();
        } catch(Exception e) {
            Log.v(TAG, e.toString());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private String chat(String text) {
        String response;
        try{
            response = botsession.think(text);
        } catch (Exception e) {
            response = e.toString();
        }
        return response;
    }

    public static String getTextFromUrl(String src) {
        StringBuffer out = new StringBuffer();
        try {
            URL url = new URL(src);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                out.append(line + "\n");
            }
            in.close();
        } catch (IOException e) {
        }
        return out.toString();
    }

    private class Chat extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... s) {
            String[] r = {s[0], chat(s[0])};
            return r;
        }

        @Override
        protected void onPostExecute(String[] response) {
            super.onPostExecute(response);
            tvTexto.append("you> " + response[0] + "\n");
            tvTexto.append("bot> " + response[1] + "\n");
        }
    }

    //https://www.bing.com/ttranslatev3?isVertical=1&&IG=395A7BB88DA9425492B43B92CFB8DCFC&IID=translator.5026.1
    //https://www.bing.com/ttranslatev3?IID=translator.5026.1
    //POST
    //fromLang=es
    //text=soy programador
    //to=en
    //Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36
}