package restTemplateAndRedisNotBD.service.impl;

import org.springframework.stereotype.Service;
import restTemplateAndRedisNotBD.aggregates.constants.Constants;
import restTemplateAndRedisNotBD.aggregates.response.ResponseSunat;
import restTemplateAndRedisNotBD.redis.RedisService;
import restTemplateAndRedisNotBD.service.InfoSunatService;
import restTemplateAndRedisNotBD.util.Util;

import java.io.IOException;
import java.util.Objects;

@Service
public class InfoSunatServiceImpl implements InfoSunatService {

    private final RedisService redisService;

    public InfoSunatServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }


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

        }
        return null;
    }


}
