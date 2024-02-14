package API;

import java.util.ArrayList;

import activities.DtoCurso;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TblCursoApi {
    @FormUrlEncoded
    @POST("curso_insert.php")
    Call<Boolean> insertCurso(
            @Field("nomeCurso") String nomeCurso,
            @Field("duracaoCurso") int duracaoCurso,
            @Field("valorCurso") double valorCurso
    );
    @FormUrlEncoded
    @POST("curso_update.php")
    Call<Boolean> updateCurso(
            @Field("idCurso") long idCurso,
            @Field("nomeCurso") String nomeCurso,
            @Field("duracaoCurso") int duracaoCurso,
            @Field("valorCurso") double valorCurso
    );

    @FormUrlEncoded
    @POST("curso_delete.php")
    Call<Boolean> deleteCurso(
            @Field("idCurso") long nomeCurso
    );
    @FormUrlEncoded
    @POST("usuario_insert.php")
    Call<DtoUsuario> insertUsuario(
            @Field("usuarioNome") String usuarioNome,
            @Field("usuarioEmail") String usuarioEmail,
            @Field("usuarioSenha") String usuarioSenha,
            @Field("usuarioAcesso") Boolean usuarioAcesso
    );
    @FormUrlEncoded
    @POST("usuario_login.php")
    Call<DtoUsuario> loginUsuario(
            @Field("usuarioEmail") String usuarioEmail,
            @Field("usuarioSenha") String usuarioSenha
    );

    @GET("curso_select.php")
    Call<ArrayList<DtoCurso>> consultarTodosCursos();

    @GET("curso_select_valorMax.php")
    Call<ArrayList<DtoCurso>> consultarCursoValorMax(
            @Query("valorMaximo") double valorMaximo
    );
}
