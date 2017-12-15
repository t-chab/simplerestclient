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
        List<Object> providers = new ArrayList<>();
        providers.add(new JacksonJsonProvider());
        String address = "http://localhost:13000/jaxrs-service/hello";
        WebClient client =
                WebClient.create(address, providers);

        client.path("jsonBean").type("application/json").accept(MediaType.APPLICATION_JSON_TYPE);

        ClientConfiguration config = WebClient.getConfig(client);
        config.getInInterceptors().add(new LoggingInInterceptor());
        config.getOutInterceptors().add(new LoggingOutInterceptor());

        final JsonBean data = new JsonBean();
        data.setVal1("1");
        data.setVal2("deux");

        Response message = client.post(data);
        System.out.println("Message received : " + message.readEntity(String.class));
    }
}
