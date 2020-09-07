package rest;

import model.manager.StockManager;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

@Path("/stocks")
public class Resource {

    @Inject
    private StockManager stockmanager;

    @Path("StockValid/{ticker}")
    @GET
    public Response isStockValid(@PathParam("ticker") String ticker) {
        return Response.ok(stockmanager.isCached(ticker)).build();
    }

    @Path("StockValue/{ticker}/{date}")
    @GET
    public Response getStockValue(@PathParam("ticker") String ticker, @PathParam("date") String date) {
        return Response.ok(stockmanager.searchStockValue(ticker, LocalDate.parse(date))).build();
    }

}
