package com.example.panelist.models.api_error;

import com.example.panelist.network.ErrorProvider;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils422 {
    public static APIError422 parseError(Response<?> response){
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
}
