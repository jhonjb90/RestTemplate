package restTemplateAndRedisNotBD.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import restTemplateAndRedisNotBD.aggregates.constants.Constants;
import restTemplateAndRedisNotBD.aggregates.response.ResponseSunat;
import restTemplateAndRedisNotBD.redis.RedisService;
import restTemplateAndRedisNotBD.service.InfoSunatService;
import org.springframework.http.*;
import restTemplateAndRedisNotBD.util.Util;

import java.io.IOException;
import java.util.Objects;

@Service
public class InfoSunatServiceImpl implements InfoSunatService {

    private final RedisService redisService;
    private final RestTemplate restTemplate;

    public InfoSunatServiceImpl(RedisService redisService, RestTemplate restTemplate) {
        this.redisService = redisService;
        this.restTemplate = restTemplate;
    }

    @Value("${token.api}")
    private String token;


    @Override
    public ResponseSunat getInfoSunat(String ruc) throws IOException {
        ResponseSunat responseSunat = new ResponseSunat();
        //logica del diagrama
        //recuperamos la informacion de redis
        String sunatRedisInfo = redisService.getDataFromRedis(
                Constants.REDIS_KEY_API_SUNAT+ruc);
        //validando info de redis
        if(Objects.nonNull(sunatRedisInfo)){
            //si existe info en redis
            responseSunat = Util.convertirDesdeString(sunatRedisInfo,
                    ResponseSunat.class);
        } else {
            //no existe info en redis, por ende vamos a golpear a cliente externo
            responseSunat = executeRestTemplate(ruc);
            if(Objects.nonNull(responseSunat)){
                String dataParaRedis =
                        Util.convertirAString(responseSunat);
                redisService.saveInRedis(Constants.REDIS_KEY_API_SUNAT+ruc,
                        dataParaRedis,Constants.REDIS_TTL);
                return responseSunat;
            }
        }
        return null;
    }

    private ResponseSunat executeRestTemplate(String ruc){
        String urlComplet = Constants.BASE_URL+"/v2/reniec/dni?numero="+ruc;

            ResponseEntity<ResponseSunat> execute =
                    restTemplate.exchange(
                            urlComplet, HttpMethod.GET,
                            new HttpEntity<>(createHeaders()),
                            ResponseSunat.class
                    );
            if (execute.getStatusCode().equals(HttpStatus.OK)){
                return execute.getBody();
            }
            return  null;
    }

    private HttpHeaders createHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",Constants.BEABER+token);
        return headers;
    }
}
