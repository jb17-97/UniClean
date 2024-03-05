package com.app.rest;

import com.app.dto.CalculationDTO;
import com.app.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataController {

    private final Logger log = LoggerFactory.getLogger(DataController.class);

    @PostMapping("/calculate-price")
    public ResponseEntity<?> calculatePrice(@RequestBody CalculationDTO calculationDTO) {
        log.info("Entering calculatePrice() method");
        log.info("calculationDTO : "+calculationDTO);
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        try {
            data.put("totalPrice", CommonUtil.calculateTotalCleaningPrice(calculationDTO));
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exiting calculatePrice() method");
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
