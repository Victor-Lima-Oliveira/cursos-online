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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rest_myapi.R;

import java.util.ArrayList;

import API.ConfigRetrofit;
import API.TblCursoApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListaCursosActivity extends AppCompatActivity {

    ListView listViewCurso;
    Button BTNNovoCurso;
    ImageView IMGfiltroValorMax, IMGlogout;
    EditText EDTvalorMax;
    ArrayList<DtoCurso> cursoArrayList = new ArrayList<DtoCurso>();
    Retrofit retrofit;
    TblCursoApi tblCursoApi;
    SharedPreferences sharedPreferences;
    Boolean adm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listadecursos);
        associaJavaXml();
        consultarTodosCursos();
        sharedPreferences = getSharedPreferences(getString(R.string.sharedPrefs),MODE_PRIVATE);
        adm = sharedPreferences.getBoolean(getString(R.string.sharedAdm),false);

        BTNNovoCurso.setVisibility(adm ? View.VISIBLE : View.GONE);

        IMGlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder msg = new AlertDialog.Builder(ListaCursosActivity.this);
                msg.setMessage("Tem certeza que deseja sair da conta?");
                msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        Toast.makeText(ListaCursosActivity.this, "Logout realizado com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                msg.setNegativeButton("Não",null);
                msg.show();
            }
        });
        BTNNovoCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaCursosActivity.this, InclusaoActivity.class);
                startActivity(intent);

            }
        });

        IMGfiltroValorMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!EDTvalorMax.getText().toString().isEmpty())
                   filtoCurso(Double.parseDouble(EDTvalorMax.getText().toString()));
                consultarTodosCursos();
            }
        });

        listViewCurso.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                    AlertDialog.Builder msg = new AlertDialog.Builder(ListaCursosActivity.this);
                    msg.setMessage("Deseja realmente excluir?");
                    msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deletarCurso(cursoArrayList.get(position).getIdCurso());
                        }
                    });
                    msg.setNegativeButton("Não", null);
                    if(adm)
                        msg.show();

                    return true;

            }
        });

        listViewCurso.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ListaCursosActivity.this, AlteracaoActivity.class);
                intent.putExtra("id", cursoArrayList.get(position).getIdCurso());
                intent.putExtra("nome", cursoArrayList.get(position).getNomeCurso());
                intent.putExtra("valor", cursoArrayList.get(position).getValorCurso());
                intent.putExtra("duracao", cursoArrayList.get(position).getDuracaoCurso());

                startActivity(intent);
            }
        });
    }

    private void consultarTodosCursos() {
        retrofit = ConfigRetrofit.getRetrofit();
        TblCursoApi tblCursoApi1 = retrofit.create(TblCursoApi.class);

        Call <ArrayList<DtoCurso>> call = tblCursoApi1.consultarTodosCursos();
        call.enqueue(new Callback<ArrayList<DtoCurso>>() {
            @Override
            public void onResponse(Call<ArrayList<DtoCurso>> call, Response<ArrayList<DtoCurso>> response) {
                cursoArrayList = response.body();
                ArrayAdapter adapter = new ArrayAdapter(ListaCursosActivity.this, android.R.layout.simple_list_item_1, cursoArrayList);
                listViewCurso.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<DtoCurso>> call, Throwable t) {
                Toast.makeText(ListaCursosActivity.this, "Não foi possível retornar a lista de cursos", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onFailure: "+t.toString());
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
                    Toast.makeText(ListaCursosActivity.this, "Excluido com sucesso", Toast.LENGTH_SHORT).show();
                    consultarTodosCursos();
                }
                else{
                    Toast.makeText(ListaCursosActivity.this, "Falha ao excluir o curso, tente novamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
                Toast.makeText(ListaCursosActivity.this, "Erro " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void filtoCurso(double valorMax) {
        retrofit = ConfigRetrofit.getRetrofit();
        TblCursoApi tblCursoApi1 = retrofit.create(TblCursoApi.class);

        Call <ArrayList<DtoCurso>> call = tblCursoApi1.consultarCursoValorMax(valorMax);
        call.enqueue(new Callback<ArrayList<DtoCurso>>() {
            @Override
            public void onResponse(Call<ArrayList<DtoCurso>> call, Response<ArrayList<DtoCurso>> response) {
                cursoArrayList = response.body();
                ArrayAdapter adapter = new ArrayAdapter(ListaCursosActivity.this, android.R.layout.simple_list_item_1, cursoArrayList);
                listViewCurso.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<DtoCurso>> call, Throwable t) {

            }
        });
    }

    private void associaJavaXml() {
        BTNNovoCurso = findViewById(R.id.buttonNovoCurso);
        listViewCurso = findViewById(R.id.listViewCurso);
        IMGfiltroValorMax = findViewById(R.id.IMGfiltroValorMax);
        EDTvalorMax = findViewById(R.id.EDTvalorMax);
        IMGlogout = findViewById(R.id.IMGlogout);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}