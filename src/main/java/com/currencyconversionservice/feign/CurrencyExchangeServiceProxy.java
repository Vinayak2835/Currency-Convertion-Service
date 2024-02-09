package com.currencyconversionservice.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.currencyconversionservice.entity.CurrencyConversion;

@FeignClient(name = "currency-exchange-service")
//@FeignClient(name = "currency-exchange-service")
public interface CurrencyExchangeServiceProxy {

	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversion retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);

}
