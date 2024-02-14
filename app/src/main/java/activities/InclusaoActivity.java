package activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rest_myapi.R;

import API.ConfigRetrofit;
import API.TblCursoApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InclusaoActivity extends AppCompatActivity {

    EditText EDTnome, EDTduracao, EDTvalor;
    Button BTNincluir;
    Retrofit retrofit;
    TblCursoApi tblCursoApi;
    ImageView IMGvoltar2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inclusao);
        associaJavaXml();

        IMGvoltar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InclusaoActivity.this, ListaCursosActivity.class);
                startActivity(intent);
            }
        });

        BTNincluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(consistenciaDeDados()) {
                    salvarCurso(EDTnome.getText().toString(),
                            Integer.parseInt(EDTduracao.getText().toString()),
                            Double.parseDouble(EDTvalor.getText().toString()));

                    Intent intent = new Intent(InclusaoActivity.this, ListaCursosActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean consistenciaDeDados() {
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

    private void salvarCurso(String nome, int duracao, double valor) {
        retrofit = ConfigRetrofit.getRetrofit();
        tblCursoApi = retrofit.create(TblCursoApi.class);
        Call<Boolean> call = tblCursoApi.insertCurso(nome,duracao,valor);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    Toast.makeText(InclusaoActivity.this, "Incluido com sucesso", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(InclusaoActivity.this, "Falha ao salvar o curso, tente novamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                Toast.makeText(InclusaoActivity.this, "Erro " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void associaJavaXml() {
        EDTduracao = findViewById(R.id.EDTduracaoDoCurso);
        EDTnome = findViewById(R.id.EDTnomeDoCurso);
        EDTvalor = findViewById(R.id.EDTvalorDoCurso);
        BTNincluir = findViewById(R.id.BTNincluirCurso);
        IMGvoltar2 = findViewById(R.id.IMGvoltar2);
    }
}