package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.exception.InsufficientCreditsException;
import co.edu.javeriana.spacetrader.model.PlanetaryStock;
import co.edu.javeriana.spacetrader.model.TransactionRequest;
import co.edu.javeriana.spacetrader.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    private static final Logger logger = Logger.getLogger(TradeController.class.getName());

    @Autowired
    private TradeService tradeService;

    @GetMapping("/planetary-stock/{planetId}")
    public ResponseEntity<?> listPlanetaryStock(@PathVariable Long planetId) {
        try {
            List<PlanetaryStock> stocks = tradeService.listPlanetaryStock(planetId);
            return ResponseEntity.ok(stocks);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error listing planetary stock", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/buy")
    @Transactional
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

    @PostMapping("/sell")
    @Transactional
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
