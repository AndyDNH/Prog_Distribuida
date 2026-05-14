package com.programacion.distribuida.books.rest;

import com.programacion.distribuida.books.db.Book;
import com.programacion.distribuida.books.dtos.BookDTO;
import com.programacion.distribuida.books.repo.BookRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;

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
        return bookRepository.findByIdOptional(isbn)
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
