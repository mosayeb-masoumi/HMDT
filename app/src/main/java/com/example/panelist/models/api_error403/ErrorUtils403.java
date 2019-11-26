package com.example.panelist.models.api_error403;

import com.example.panelist.network.ErrorProvider;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils403 {

    public static APIError403 parseError(Response<?> response){
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
