package mx.ecommerce.controllers;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import mx.ecommerce.models.Stock;
import mx.ecommerce.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller    // This means that this class is a Controller
@RequestMapping(value = "/stocks")
public class StockController {
    private final StockRepository stockRepository;

    public StockController(@Autowired StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @GetMapping(path = "/list")
    @Operation(summary = "List products", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody Iterable<Stock> getAllStocks(@RequestParam(required = false) String searchPhrase, Authentication authentication) {
        Iterable<Stock> result;
        if (searchPhrase == null || searchPhrase.isBlank()) {
            result = stockRepository.allWithoutImage();
        } else {
            result = stockRepository.findByName(searchPhrase);
        }
        return result;
    }

    @GetMapping(value = "/{code}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "Return picture", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<byte[]> getImage(@PathVariable int code) {
        Optional<byte[]> imageByCode = stockRepository.findImageByCode(code);

        return imageByCode.map(bytes -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(bytes))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Add stock product", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody Stock createStock(@RequestPart String description,
                                           @RequestPart(required = false) String color,
                                           @RequestPart String category,
                                           @RequestPart String price,
                                           @RequestPart String quantity,
                                           @RequestPart String status,
                                           @RequestPart(required = false) MultipartFile image,
                                           Authentication auth) {
        Integer clientId = ((Claims) auth.getDetails()).get("userId", Integer.class);
        Stock k = new Stock();
        k.setCategory(category);
        k.setColor(color);
        k.setPrice(Double.parseDouble(price));
        k.setDescription(description);
        k.setQuantity(Integer.parseInt(quantity));
        k.setStatus(status);
        k.setUpdated_by(clientId);
        k.setCreated_by(clientId);

        if (image != null) {
            try {
                k.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Stock saved = stockRepository.save(k);
        return saved;
    }

    @PutMapping(path = "/{code}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Update stock product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Stock> updateStock(@PathVariable int code,
                                             @RequestPart String description,
                                             @RequestPart(required = false) String color,
                                             @RequestPart String category,
                                             @RequestPart String price,
                                             @RequestPart String quantity,
                                             @RequestPart String status,
                                             @RequestPart(required = false) MultipartFile image,
                                             Authentication auth) {

        Integer clientId = ((Claims) auth.getDetails()).get("userId", Integer.class);

        return
                stockRepository.findById(code).map(stock -> {
                    stock.setCategory(category);
                    stock.setColor(color);
                    stock.setPrice(Double.parseDouble(price));
                    stock.setDescription(description);
                    stock.setQuantity(Integer.parseInt(quantity));
                    stock.setStatus(status);
                    stock.setUpdated_by(clientId);
                    stockRepository.save(stock);
                    return stock;
                }).map(result -> ResponseEntity.ok().body(result)
                ).orElse(ResponseEntity.notFound().build());

    }

    @GetMapping(path = "/{code}")
    @Operation(summary = "Get stock product", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody Optional<Stock> getStock(@PathVariable int code) {
        Optional<Stock> stockOpt = stockRepository.findById(code);
        return stockOpt;
    }

    @DeleteMapping(path = "/{code}")
    @Operation(summary = "Delete stock product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteStock(@PathVariable int code) {
        try {
            stockRepository.deleteById(code);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}
