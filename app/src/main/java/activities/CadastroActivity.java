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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.rest_myapi.R;

import java.util.BitSet;

import API.ConfigRetrofit;
import API.DtoUsuario;
import API.TblCursoApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CadastroActivity extends AppCompatActivity {

    Button BTNenviar, BTNvoltar;
    EditText EDTusuarioNome, EDTusuarioSenha, EDTusuarioEmail;
    ToggleButton TGBusuarioAcesso;
    String nome,email,senha;
    Boolean adm;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        associaJavaXml();

        sharedPreferences = getSharedPreferences(getString(R.string.sharedPrefs),MODE_PRIVATE);


        BTNenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(consistenciaDeDados())
                    cadastrarUsuario();
            }
        });
        BTNvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cadastrarUsuario() {
        Retrofit retrofit = ConfigRetrofit.getRetrofit();
        TblCursoApi tblCursoApi = retrofit.create(TblCursoApi.class);

        Call<DtoUsuario> call = tblCursoApi.insertUsuario(nome,email,senha,adm);
        call.enqueue(new Callback<DtoUsuario>() {
            @Override
            public void onResponse(Call<DtoUsuario> call, Response<DtoUsuario> response) {
               DtoUsuario  request = response.body();
               String stauts = request.getStatus();
               adm = request.getAcesso();
               Long id = request.getId();
                switch (stauts) {
                    case "Cadastrado":
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(getString(R.string.sharedAdm), adm);
                        editor.putLong(getString(R.string.sharedId), id);
                        editor.apply();
                        EDTusuarioEmail.setText("");
                        EDTusuarioSenha.setText("");
                        EDTusuarioNome.setText("");
                        Toast.makeText(CadastroActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CadastroActivity.this, ListaCursosActivity.class);
                        startActivity(intent);
                        break;
                    case "Nao foi possivel cadastar":
                        Toast.makeText(CadastroActivity.this, "Tente novamente, mais tarde", Toast.LENGTH_SHORT).show();
                        break;
                    case "Email ja cadastrado":
                        EDTusuarioSenha.setText("");
                        EDTusuarioEmail.requestFocus();
                        EDTusuarioEmail.setError("ATENÇÃO: Esse email já está sendo utilizado");
                        break;
                }
            }
            @Override
            public void onFailure(Call<DtoUsuario> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private boolean consistenciaDeDados() {
        EDTusuarioEmail.setError(null);

        Boolean result = true;
         String nomeTemp = EDTusuarioNome.getText().toString();
         String senhaTemp = EDTusuarioSenha.getText().toString();
         String emailTemp = EDTusuarioEmail.getText().toString();

        if(nomeTemp.isEmpty() || nomeTemp.length() > 50) {
            EDTusuarioNome.setError("Digite um nome entre 1 e 50 caracteres");
            result = false;
        }
        if (senhaTemp.isEmpty() || senhaTemp.length() >20){
            EDTusuarioSenha.setError("Digite uma senha entre 1 e 20 caracteres");
            result = false;
        }
        if (emailTemp.isEmpty() || emailTemp.length() > 200) {
            EDTusuarioEmail.setError("Digite um email entre 1 e 200 caracteres");
            result = false;
        }
        if(result){
            nome = nomeTemp;
            senha = senhaTemp;
            email = emailTemp;
            adm = (TGBusuarioAcesso.isChecked()) ?  true : false;
        }
        return result;


    }
    private void associaJavaXml() {
        BTNenviar = findViewById(R.id.BTNenviar);
        BTNvoltar = findViewById(R.id.BTNvoltar);
        EDTusuarioNome = findViewById(R.id.EDTusuarioNome);
        EDTusuarioSenha = findViewById(R.id.EDTusuarioSenha);
        EDTusuarioEmail = findViewById(R.id.EDTusuarioEmail);
        TGBusuarioAcesso = findViewById(R.id.TGBusuarioAcesso);
    }

}