package activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rest_myapi.R;

import API.ConfigRetrofit;
import API.DtoUsuario;
import API.TblCursoApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Button BTNlogin;
    TextView TXVcadastro,TXValerta;
    EditText EDTloginEmail, EDTloginPass;
    String email, senha;
    Boolean adm;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(getString(R.string.sharedPrefs),MODE_PRIVATE);
        Long loginSalvo = sharedPreferences.getLong(getString(R.string.sharedId),0);

        if(loginSalvo != 0){
            Intent intent = new Intent(MainActivity.this, ListaCursosActivity.class);
            startActivity(intent);
        }
        associaJavaXml();

        BTNlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(consistenciaDeDados())
                    login();
            }
        });
        TXVcadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });
}

    private void login() {

        Retrofit retrofit = ConfigRetrofit.getRetrofit();
        TblCursoApi tblCursoApi = retrofit.create(TblCursoApi.class);

        Call<DtoUsuario> call = tblCursoApi.loginUsuario(email,senha);
        call.enqueue(new Callback<DtoUsuario>() {
             @Override
             public void onResponse(Call<DtoUsuario> call, Response<DtoUsuario> response) {
                 DtoUsuario request = response.body();
                 String status = request.getStatus();

                 switch(status){
                     case "Usuario nao cadastrado no sistema":
                         TXValerta.setText(status);
                         TXValerta.setVisibility(View.VISIBLE);
                         break;
                     case "logon":
                         id = request.getId();
                         adm = request.getAcesso();
                         SharedPreferences.Editor editor = sharedPreferences.edit();
                         editor.putLong(getString(R.string.sharedId), id);
                         editor.putBoolean(getString(R.string.sharedAdm), adm);
                         editor.apply();
                         Intent intent = new Intent(MainActivity.this, ListaCursosActivity.class);
                         startActivity(intent);
                         break;
                     case "Usuario ou senha incorretos":
                         TXValerta.setText(status);
                         TXValerta.setVisibility(View.VISIBLE);
                         break;
                 };
             }

             @Override
             public void onFailure(Call<DtoUsuario> call, Throwable t) {
                 Log.i(TAG, "onFailure: " + t.toString());
             }
         });
    }

    private boolean consistenciaDeDados() {
        Boolean pass = true;
        email = EDTloginEmail.getText().toString();
        senha = EDTloginPass.getText().toString();
        if(email.isEmpty()) {
            EDTloginEmail.setError("Digite o seu login (O seu email cadastrado)");
            pass = false;
        }
        if(senha.isEmpty()) {
            EDTloginPass.setError("Digite sua senha");
            pass = false;
        }
        return pass;
    }

    private void associaJavaXml() {
        BTNlogin = findViewById(R.id.BTNlogin);
        TXVcadastro = findViewById(R.id.TXVcadastro);
        EDTloginEmail = findViewById(R.id.EDTloginEmail);
        EDTloginPass = findViewById(R.id.EDTloginPass);
        TXValerta = findViewById(R.id.TXValerta);
    }
}