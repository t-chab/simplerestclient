package com.acme.frontend;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class SimpleRestClient {
    public static void main(String[] args) {
        final List<Object> providers = new ArrayList<>();
        providers.add(new JacksonJsonProvider());
        final String address = "http://localhost:13000/jaxrs-service/sign/sign";
        final WebClient client =
                WebClient.create(address, providers);

        client.path("signJson").type("application/json")
                .accept(MediaType.APPLICATION_JSON_TYPE);

        final ClientConfiguration config = WebClient.getConfig(client);
        config.getInInterceptors().add(new LoggingInInterceptor());
        config.getOutInterceptors().add(new LoggingOutInterceptor());

        final JsonBean data = new JsonBean();
        data.setVal1("1");
        data.setVal2("deux");

        Response signResponse = client.post(data);
        final String signResult = signResponse.readEntity(String.class);

        final WebClient clientVerify =
                WebClient.create("http://localhost:13000/jaxrs-service/verify/verify", providers);

        clientVerify.path("check").type("application/json")
                .accept(MediaType.APPLICATION_JSON_TYPE);

        WebClient.getConfig(clientVerify).getInInterceptors().add(new LoggingInInterceptor());
        WebClient.getConfig(clientVerify).getOutInterceptors().add(new LoggingOutInterceptor());

        final Response verifyResponse = client.post(signResult);
        final String verifyResult = verifyResponse.readEntity(String.class);
    }
}
