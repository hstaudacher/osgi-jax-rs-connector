package com.eclipsesource.jaxrs.connector.example.ds.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@Produces("text/html")
public class ExampleProvider implements MessageBodyWriter<Product> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                    Annotation[] annotations, MediaType mediaType) {
            return Product.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Product t, Class<?> type, Type genericType,
                    Annotation[] annotations, MediaType mediaType) {
            return -1;
    }

    @Override
    public void writeTo(Product t, Class<?> type, Type genericType,
                    Annotation[] annotations, MediaType mediaType,
                    MultivaluedMap<String, Object> httpHeaders,
                    OutputStream entityStream)
                    throws IOException, WebApplicationException {
            PrintWriter writer = new PrintWriter(entityStream);
            writer.println("<html>");
            writer.println("<body>");
            writer.println("Product name: "+t.getName());
            writer.println("<br/>");
            writer.println("Product Description :<br/>"+t.getDescription());
            writer.println("</body>");
            writer.println("</html>");
            writer.flush();
    }

}
