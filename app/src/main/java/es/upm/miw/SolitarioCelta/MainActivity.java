package es.upm.miw.SolitarioCelta;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Array;

import es.upm.miw.SolitarioCelta.model.FileHelper;
import es.upm.miw.SolitarioCelta.model.SCeltaViewModel;
import es.upm.miw.SolitarioCelta.model.SCeltaViewModelFactory;
import es.upm.miw.SolitarioCelta.model.Score;
import es.upm.miw.SolitarioCelta.model.ScoreViewModel;

public class MainActivity extends AppCompatActivity {

    protected final String LOG_TAG = "MiW";
    protected final Integer ID = 2021;
    private long timeWhenStopped = 0;
    protected SCeltaViewModel miJuegoVM;
    public FileHelper fileHelper;
    private String userName;
    ColorStateList colorStateList;
    private ScoreViewModel scoreViewModel;
    TextView tvTokenNumber;
    Chronometer chronometer;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);

        fileHelper = new FileHelper(getApplicationContext());

        miJuegoVM = new ViewModelProvider(
                    this,
                    new SCeltaViewModelFactory(getApplication(), ID)
                    )
                .get(SCeltaViewModel.class);
        tvTokenNumber = findViewById(R.id.tokens);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setTextSize(50);
        startChronometer();
        mostrarTablero();
        if(savedInstanceState != null) {
            setChronometer(savedInstanceState.getLong(getString(R.string.timeKey)));
            chronometer.start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setChronometer(timeWhenStopped);
        chronometer.start();
        userName = getUserName();
        colorStateList = getColor();
        mostrarTablero();
    }

    @Override
    public void onPause() {
        super.onPause();
        chronometer.stop();
        timeWhenStopped = Math.abs(SystemClock.elapsedRealtime() - chronometer.getBase());
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState (outState);
        timeWhenStopped = Math.abs(SystemClock.elapsedRealtime() - chronometer.getBase());
        outState.putLong(getString(R.string.timeKey), timeWhenStopped);
    }


    public String getUserName() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return SP.getString(getString(R.string.defaultUserKey), getString(R.string.defaultUser));
    }

    public ColorStateList getColor() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String index_color = SP.getString(getString(R.string.defaultColorKey), getString(R.string.defaultColor));
        int color = Color.BLACK;

        switch (index_color) {
            case "2":  color = Color.RED; break;
            case "3": color = Color.BLUE; break;
        }

        return new ColorStateList(
                new int[][]{
                        new int[]{ color }
                },
                new int[]{ color }
        );
    }

    /**
     * Se ejecuta al pulsar una ficha
     * Las coordenadas (i, j) se obtienen a partir del nombre del recurso, ya que el botón
     * tiene un identificador en formato pXY, donde X es la fila e Y la columna
     * @param v Vista de la ficha pulsada
     */
    public void fichaPulsada(@NotNull View v) {
        String resourceName = getResources().getResourceEntryName(v.getId());
        int i = resourceName.charAt(1) - '0';   // fila
        int j = resourceName.charAt(2) - '0';   // columna
        int tokens = miJuegoVM.numeroFichas();

        Log.i(LOG_TAG, "fichaPulsada(" + i + ", " + j + ") - " + resourceName);
        miJuegoVM.jugar(i, j);
        Log.i(LOG_TAG, "#fichas=" + tokens);
        mostrarTablero();
        if (miJuegoVM.juegoTerminado()) {
            Score score = new Score(
                    tokens,
                    userName,
                    millisecondsToTime(SystemClock.elapsedRealtime() - chronometer.getBase())
            );
            scoreViewModel.insert(score);
            new AlertDialogFragment().show(getSupportFragmentManager(), "ALERT_DIALOG");
        }
    }

    public void save() throws IOException {
        fileHelper.write(
                miJuegoVM.serializaTablero() + "," +
                        Math.abs(chronometer.getBase() - SystemClock.elapsedRealtime())
        );
    }

    public void recover() throws IOException {
        String content = fileHelper.read();
        String[] splitted = content.split(",");
        miJuegoVM.deserializaTablero(splitted[0]);
        setChronometer(Long.parseLong(splitted[1]));
        mostrarTablero();
    }

    /**
     * Visualiza el tablero
     */
    public void mostrarTablero() {
        RadioButton button;
        String strRId;
        String prefijoIdentificador = getPackageName() + ":id/p"; // formato: package:type/entry
        int idBoton;
        updateTokens(miJuegoVM.numeroFichas());

        for (int i = 0; i < SCeltaViewModel.TAMANIO; i++)
            for (int j = 0; j < SCeltaViewModel.TAMANIO; j++) {
                strRId = prefijoIdentificador + i + j;
                idBoton = getResources().getIdentifier(strRId, null, null);
                if (idBoton != 0) { // existe el recurso identificador del botón
                    button = findViewById(idBoton);
                    button.setChecked(miJuegoVM.obtenerFicha(i, j) == SCeltaViewModel.FICHA);
                    button.setButtonTintList(getColor());
                }
            }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcReiniciarPartida:
                new AlertDialogFragment().show(getSupportFragmentManager(), "ALERT_DIALOG");
                return true;
            case R.id.opcGuardarPartida:
                try {
                    save();
                    _notify("Guardado");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.opcRecuperarPartida:
                try {
                    recover();
                    _notify("Recuperada");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.opcMejoresResultados:
                Intent intent = new Intent(this, ScoreListActivity.class);
                startActivity(intent);
                return true;
            case R.id.opcAjustes:
                startActivity(new Intent(this, SCeltaPrefs.class));
                return true;
            case R.id.opcAcercaDe:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.aboutTitle)
                        .setMessage(R.string.aboutMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return true;

            default:
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtSinImplementar),
                        Snackbar.LENGTH_LONG
                        )
                        .show();
        }
        return true;
    }

    public void updateTokens(int tokens) {
        tvTokenNumber.setText(String.valueOf(tokens));
    }

    public void _notify(String textToShow) {
        Snackbar.make(
                findViewById(android.R.id.content),
                textToShow,
                Snackbar.LENGTH_LONG
        )
                .show();
    }

    public void startChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chronometer.start();
    }

    public void stopChronometer() {
        chronometer.stop();
        timeWhenStopped = Math.abs(chronometer.getBase() - SystemClock.elapsedRealtime());
    }

    public void resetChronometer() {
        stopChronometer();
        chronometer.setBase(SystemClock.elapsedRealtime());
        timeWhenStopped = 0;
        startChronometer();
    }

    public void setChronometer(long time) {
        timeWhenStopped = time;
        chronometer.setBase(SystemClock.elapsedRealtime() - time);
    }

    public String millisecondsToTime(Long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);

        return minutes + ":" + seconds;
    }
}
