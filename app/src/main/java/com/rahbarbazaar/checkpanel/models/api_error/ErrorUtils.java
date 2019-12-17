package com.rahbarbazaar.checkpanel.models.api_error;

import com.rahbarbazaar.checkpanel.models.api_error403.APIError403;
import com.rahbarbazaar.checkpanel.network.ErrorProvider;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

    public static APIError422 parseError422(Response<?> response){
        Converter<ResponseBody, APIError422> converter=
                ErrorProvider
                        .retrofit.
                        responseBodyConverter(APIError422.class,new Annotation[0]);
        APIError422 error;

        try{
            error=converter.convert(response.errorBody());

        }catch (IOException e){
            return  new APIError422();
        }
        return error;
    }

    public static APIError403 parseError403(Response<?> response){
        Converter<ResponseBody, APIError403> converter=
                ErrorProvider
                        .retrofit.
                        responseBodyConverter(APIError403.class,new Annotation[0]);
        APIError403 error;

        try{
            error=converter.convert(response.errorBody());

        }catch (IOException e){
            return  new APIError403();
        }
        return error;
    }
}
