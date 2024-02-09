package com.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.currencyconversionservice.entity.CurrencyConversion;
import com.currencyconversionservice.feign.CurrencyExchangeServiceProxy;

import lombok.RequiredArgsConstructor;

@RestController
public class CurrencyConversionController {
    
	@Autowired
	private CurrencyExchangeServiceProxy currencyExchangeServiceProxy;

	@GetMapping("/currency-convertor/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion convertCurrency(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		Map<String, String> uriVariables = new HashMap<>();

		ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8081/currency-exchange/from/USD/to/INR", CurrencyConversion.class, uriVariables);

		CurrencyConversion responseBody = responseEntity.getBody();

		return new CurrencyConversion(responseBody.getId(), from, to, quantity, responseBody.getConversionMultiple(),
				quantity.multiply(responseBody.getConversionMultiple()), responseBody.getEnvironment());
	}

	@GetMapping("/currency-convertor-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion convertCurrencyFeigh(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		CurrencyConversion currencyConversion = currencyExchangeServiceProxy.retrieveExchangeValue(from, to);

		return new CurrencyConversion(currencyConversion.getId(), from, to, quantity,
				currencyConversion.getConversionMultiple(),
				quantity.multiply(currencyConversion.getConversionMultiple()), 
				currencyConversion.getEnvironment()+" "+"PORT");
	}

}
