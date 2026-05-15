package com.programacion.distribuida.books.rest;

import com.programacion.distribuida.books.clients.AuthorRestClient;
import com.programacion.distribuida.books.db.Book;
import com.programacion.distribuida.books.dtos.BookDTO;
import com.programacion.distribuida.books.repo.BookRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
@Transactional
public class BookRest {
    final BookRepository bookRepository;


    @GET
    public List<BookDTO> findAll() {
        return bookRepository.streamAll()
                .map(it -> BookDTO.builder()
                        .isbn(it.getIsbn())
                        .title(it.getTitle())
                        .price(it.getPrice())
//                        .inventorySold(it.getInventory().getSold())
//                        .inventorySupplied(it.getInventory().getSupplied())
                        .build())
                .toList();
    }

    @GET
    @Path("/{isbn}")
    public Response findByIsbn(@PathParam("isbn") String isbn) {

        AuthorRestClient client =  RestClientBuilder.newBuilder()
                .baseUri("http://127.0.0.1:8070")
                        .build(AuthorRestClient.class);


        return bookRepository.findByIdOptional(isbn)
                .map(book -> {
//                    Consultar los autores en http://127.0.0.1:8070
//                    List<AuthorDto> authors = List.of(
//                            AuthorDto.builder()
//                                    .id(1)
//                                    .name("Jason Mormoa")
//                                    .build()
//                    );
                    var authors =  client.findByBook(isbn);
                    return BookDTO.builder()
                            .isbn(book.getIsbn())
                            .title(book.getTitle())
                            .price(book.getPrice())
                            .authors(authors) // Esta variable 'authors' debe venir de una consulta previa
//                            .inventorySold(book.getInventory().getSold())
//                            .inventorySupplied(book.getInventory().getSupplied())
                            .build();
                })



//                .map(it -> BookDTO.builder()
//                        .isbn(it.getIsbn())
//                        .title(it.getTitle())
//                        .price(it.getPrice())
//                        .inventorySold(it.getInventory().getSold())
//                        .inventorySupplied(it.getInventory().getSupplied())
//                        .build())
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @PUT
    @Path("/{isbn}")
    public Response update(@PathParam("isbn") String isbn, Book book) {
        bookRepository.update(isbn, book);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{isbn}")
    public Response delete(@PathParam("isbn") String isbn) {
        bookRepository.deleteById(isbn);
        return Response.ok().build();
    }

    @POST
    @Path("/{isbn}")
    public Response post(Book book) {
        bookRepository.persist(book);

        var uri = UriBuilder.
                fromUri("/books/{isbn}").
                build(book.getIsbn());
        return Response.created(uri).build();
    }

}
