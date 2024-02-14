package activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.rest_myapi.R;
import com.example.rest_myapi.databinding.ActivityMainBinding;

import API.ConfigRetrofit;
import API.TblCursoApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AlteracaoActivity extends AppCompatActivity {

    EditText EDTnome, EDTduracao, EDTvalor;
    Button BTNalterar;
    ImageView IMGvoltar,IMGexcluir;
    Retrofit retrofit;
    TblCursoApi tblCursoApi;
    SharedPreferences sharedPreferences;
    long id;
    Boolean adm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alteracao);
        associaJavaXml();

        Bundle bundle = getIntent().getExtras();
        EDTnome.setText(bundle.getString("nome"));
        EDTvalor.setText(Double.toString(bundle.getDouble("valor")));
        EDTduracao.setText(Integer.toString((bundle.getInt("duracao"))));
        id = bundle.getLong("id");

        sharedPreferences = getSharedPreferences(getString(R.string.sharedPrefs),MODE_PRIVATE);
        adm = sharedPreferences.getBoolean(getString(R.string.sharedAdm),false);

        if(!adm){
            EDTnome.setFocusable(false);
            EDTduracao.setFocusable(false);
            EDTvalor.setFocusable(false);
            EDTnome.setClickable(false);
            EDTduracao.setClickable(false);
            EDTvalor.setClickable(false);
            EDTnome.setTextColor(getResources().getColor(R.color.gray));
            EDTduracao.setTextColor(getResources().getColor(R.color.gray));
            EDTvalor.setTextColor(getResources().getColor(R.color.gray));
            BTNalterar.setVisibility(View.GONE);
            IMGexcluir.setVisibility(View.GONE);
        }


        IMGvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlteracaoActivity.this, ListaCursosActivity.class);
                startActivity(intent);
            }
        });
        IMGexcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder msg = new AlertDialog.Builder(AlteracaoActivity.this);
                msg.setMessage("Deseja realmente excluir?");
                msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletarCurso(id);
                        Intent intent = new Intent(AlteracaoActivity.this, ListaCursosActivity.class);
                        startActivity(intent);
                    }
                });
                msg.setNegativeButton("Não", null);
                msg.show();
            }
        });


        BTNalterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(consitenciaDeDados()) {
                    updateCurso(EDTnome.getText().toString(),
                            Integer.parseInt(EDTduracao.getText().toString()),
                            Double.parseDouble(EDTvalor.getText().toString()));

                    Intent intent = new Intent(AlteracaoActivity.this, ListaCursosActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean consitenciaDeDados() {
        if(EDTnome.getText().toString().isEmpty() || EDTnome.getText().toString().length() == 1){
            EDTnome.setError("Digite o nome do curso");
            return false;
        }
        else if(EDTduracao.getText().toString().isEmpty()){
            EDTduracao.setError("Digite os meses");
            return false;
        }
        else if(EDTvalor.getText().toString().isEmpty()){
            EDTvalor.setError("Digite o valor do curso");
            return false;
        }
        return true;
    }

    private void updateCurso(String nomeCurso, int duracaoCurso, double valorCurso) {
        retrofit = ConfigRetrofit.getRetrofit();
        tblCursoApi = retrofit.create(TblCursoApi.class);
        Call<Boolean> call = tblCursoApi.updateCurso(id,nomeCurso,duracaoCurso,valorCurso);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    Toast.makeText(AlteracaoActivity.this, "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AlteracaoActivity.this, "Falha ao atualizar o curso, tente novamente", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                Toast.makeText(AlteracaoActivity.this, "Erro " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deletarCurso(long idCurso) {
        retrofit = ConfigRetrofit.getRetrofit();
        tblCursoApi = retrofit.create(TblCursoApi.class);
        Call<Boolean> call = tblCursoApi.deleteCurso(idCurso);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    Toast.makeText(AlteracaoActivity.this, "Excluido com sucesso", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AlteracaoActivity.this, "Falha ao excluir o curso, tente novamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                Toast.makeText(AlteracaoActivity.this, "Erro " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void associaJavaXml() {
        EDTduracao = findViewById(R.id.EDTduracaoDoCursoAlt);
        EDTnome = findViewById(R.id.EDTnomeDoCursoAlt);
        EDTvalor = findViewById(R.id.EDTvalorDoCursoAlt);
        BTNalterar = findViewById(R.id.BTNalterarCurso);
        IMGvoltar = findViewById(R.id.IMGvoltar);
        IMGexcluir = findViewById(R.id.IMGexcluir);
    }
}