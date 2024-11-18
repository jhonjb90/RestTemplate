package restTemplateAndRedisNotBD.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import restTemplateAndRedisNotBD.aggregates.response.ResponseSunat;
import restTemplateAndRedisNotBD.service.InfoSunatService;

import java.io.IOException;

@RestController
@RequestMapping("/v1/restTemplate")
@RequiredArgsConstructor
public class InfoSunatController {
    private final InfoSunatService infoSunatService;

    @GetMapping("/infoSunat")
    public ResponseEntity<ResponseSunat> getInfoSunat(
            @RequestParam("ruc") String ruc) throws IOException{
        return new ResponseEntity<>(infoSunatService.getInfoSunat(ruc), HttpStatus.OK);
    }

}
