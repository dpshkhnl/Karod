package deepesh.info.np.karodpati.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Dpshkhnl on 2017-02-19.
 */

public interface RestInterface {

    @GET("get_all_questions.php")
    Call<RestModel> loadQuestions();


}
