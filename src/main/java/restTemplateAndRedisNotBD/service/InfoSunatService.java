package restTemplateAndRedisNotBD.service;

import restTemplateAndRedisNotBD.aggregates.response.ResponseSunat;

import java.io.IOException;


public interface InfoSunatService {

    ResponseSunat getInfoSunat(String ruc) throws IOException;
}
