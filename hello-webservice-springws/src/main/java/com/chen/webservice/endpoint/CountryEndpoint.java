package com.chen.webservice.endpoint;

import com.chen.webservice.repository.CountryRepository;
import com.chen.webservice.schema.GetCountryRequest;
import com.chen.webservice.schema.GetCountryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * CountryEndpoint
 * <p>
 * @Author LeifChen
 * @Date 2019-05-15
 */
@Endpoint
public class CountryEndpoint {

    private static final String NAMESPACE_URI = "http://chen.com/webservice/schema";

    @Autowired
    private CountryRepository countryRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
    @ResponsePayload
    public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
        GetCountryResponse response = new GetCountryResponse();
        response.setCountry(countryRepository.findCountry(request.getName()));

        return response;
    }
}
