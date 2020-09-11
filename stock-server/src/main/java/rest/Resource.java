package rest;

import exceptions.TickerNotValidException;
import json.BuyShare;
import model.repository.Repository;
import model.stock.Stock;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/stocks")
public class Resource {

    @Inject
    Repository repository;

    @Path("/allStocks")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllStocks() {
        List<Stock> list = repository.getAllStocks();
        GenericEntity<List<Stock>> listWrapper = new GenericEntity<>(list){};
        return Response
                .status(Response.Status.OK)
                .entity(listWrapper)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("/stock/{ticker}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStock(@PathParam("ticker") String ticker) throws TickerNotValidException {
        try {
            return Response
                    .status(Response.Status.OK)
                    .entity(repository.getStock(ticker))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (IllegalArgumentException e) {
            throw new TickerNotValidException(e.getMessage());
        }
    }

    @POST
    @Path("/createStock")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStock(final String ticker) throws TickerNotValidException {
        try {
            Stock stock = repository.createStock(ticker);
            return Response
                    .status(Response.Status.OK)
                    .entity(stock)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (IllegalArgumentException e) {
            throw new TickerNotValidException(e.getMessage());
        }
    }

    @DELETE
    @Path("/deleteStock/{ticker}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStock(@PathParam("ticker") String ticker) throws TickerNotValidException {
        try {
            Stock stock = repository.deleteStock(ticker);
            return Response
                    .status(Response.Status.OK)
                    .entity(stock)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (IllegalArgumentException e) {
            throw new TickerNotValidException(e.getMessage());
        }
    }

    @PUT
    @Path("/buyShares")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response buyShares(@Valid final BuyShare buyShare) throws TickerNotValidException {
        try {
            Stock stock = repository.buyStock(buyShare);
            return Response
                    .status(Response.Status.OK)
                    .entity(stock)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (IllegalArgumentException e) {
            throw new TickerNotValidException(e.getMessage());
        }
    }

    @GET
    @Path("/getStocksPrices")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStockValue() {
        List<Map<String, List<Map<String, String>>>> res = repository.getStocksPrice();
        GenericEntity<List<Map<String, List<Map<String, String>>>>> listWrapper = new GenericEntity<>(res){};
        return Response
                .status(Response.Status.OK)
                .entity(listWrapper)
                .type(MediaType.APPLICATION_JSON)
                .build();

    }

}
