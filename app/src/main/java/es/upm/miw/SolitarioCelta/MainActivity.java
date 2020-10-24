package es.upm.miw.SolitarioCelta;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import es.upm.miw.SolitarioCelta.model.AppDatabase;
import es.upm.miw.SolitarioCelta.model.FileHelper;
import es.upm.miw.SolitarioCelta.model.SCeltaViewModel;
import es.upm.miw.SolitarioCelta.model.SCeltaViewModelFactory;
import es.upm.miw.SolitarioCelta.model.Score;
import es.upm.miw.SolitarioCelta.model.ScoreViewModel;
import es.upm.miw.SolitarioCelta.view.ScoreListAdapter;

public class MainActivity extends AppCompatActivity {

    protected final String LOG_TAG = "MiW";
    protected final Integer ID = 2021;
    protected SCeltaViewModel miJuegoVM;
    public FileHelper fileHelper;
    private String userName;
    private ScoreViewModel scoreViewModel;

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
        mostrarTablero();
    }

    @Override
    public void onResume() {
        super.onResume();
        userName = getUserName();
    }

    public String getUserName() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return SP.getString("player_name", "Random Player");
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

        Log.i(LOG_TAG, "fichaPulsada(" + i + ", " + j + ") - " + resourceName);
        miJuegoVM.jugar(i, j);
        Log.i(LOG_TAG, "#fichas=" + miJuegoVM.numeroFichas());

        mostrarTablero();
        if (miJuegoVM.juegoTerminado()) {
            Score score = new Score(miJuegoVM.numeroFichas(), userName);
            scoreViewModel.insert(score);
            new AlertDialogFragment().show(getSupportFragmentManager(), "ALERT_DIALOG");
        }
    }

    public void save() throws IOException {
        fileHelper.write(miJuegoVM.serializaTablero());
    }

    public void recover() throws IOException {
        String content = fileHelper.read();
        miJuegoVM.deserializaTablero(content);
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

        for (int i = 0; i < SCeltaViewModel.TAMANIO; i++)
            for (int j = 0; j < SCeltaViewModel.TAMANIO; j++) {
                strRId = prefijoIdentificador + i + j;
                idBoton = getResources().getIdentifier(strRId, null, null);
                if (idBoton != 0) { // existe el recurso identificador del botón
                    button = findViewById(idBoton);
                    button.setChecked(miJuegoVM.obtenerFicha(i, j) == SCeltaViewModel.FICHA);
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

            // TODO!!! resto opciones

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

    public void _notify(String textToShow) {
        Snackbar.make(
                findViewById(android.R.id.content),
                textToShow,
                Snackbar.LENGTH_LONG
        )
                .show();
    }
}
