package com.programacion.distribuida.books.clients;


import com.programacion.distribuida.books.dtos.AuthorDto;
import io.vertx.ext.auth.authorization.Authorization;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
//@RegisterRestClient(configKey = "AuthorRestClient")
@RegisterRestClient(baseUri = "stork://authors-api")
public interface AuthorRestClient  {

    @GET
    @Path("/find/{isbn}")
//    @Retry(maxRetries = 2, delay = 100)
    @Timeout(100)
    @Fallback(fallbackMethod = "fallbackFindByBook")
    List<AuthorDto> findByBook(@PathParam("isbn") String isbn);


    default List<AuthorDto> fallbackFindByBook(String isbn) {
        AuthorDto author = AuthorDto.builder().
                name("Desconocido")
                .id(0)
                .build();

        return List.of(author);
    }
}
