package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.TransactionRequest;
import co.edu.javeriana.spacetrader.model.PlanetaryStock;
import co.edu.javeriana.spacetrader.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    // Endpoint to list planetary stock for a given planet
    @GetMapping("/planetary-stock/{planetId}")
    public ResponseEntity<?> listPlanetaryStock(@PathVariable Long planetId) {
        try {
            List<PlanetaryStock> stocks = tradeService.listPlanetaryStock(planetId);
            return ResponseEntity.ok(stocks);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error); // This will return a JSON body with the error message
        }
    }


    // Endpoint to process a buy transaction
    @PostMapping("/buy")
    public ResponseEntity<?> buyProduct(@RequestBody TransactionRequest transaction) {
        try {
            tradeService.buyProduct(transaction.getSpaceshipId(), transaction.getPlanetaryStockId(), transaction.getQuantity());
            Map<String, String> success = new HashMap<>();
            success.put("message", "Purchase successful.");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Endpoint to process a sell transaction
    @PostMapping("/sell")
    public ResponseEntity<?> sellProduct(@RequestBody TransactionRequest transaction) {
        try {
            tradeService.sellProduct(transaction.getSpaceshipId(), transaction.getPlanetaryStockId(), transaction.getQuantity());
            Map<String, String> success = new HashMap<>();
            success.put("message", "Sale successful.");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
