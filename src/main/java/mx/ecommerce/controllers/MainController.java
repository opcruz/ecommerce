package mx.ecommerce.controllers;

import io.swagger.v3.oas.annotations.Operation;
import mx.ecommerce.models.Stock;
import mx.ecommerce.repositories.StockRepository;
import mx.ecommerce.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@Controller    // This means that this class is a Controller
@RequestMapping(value = "/ecommerce") // This means URL's start with /demo (after Application path)
public class MainController {
    private final StockRepository stockRepository;
    private final OrderService orderService;

    public MainController(@Autowired StockRepository stockRepository,
                          @Autowired OrderService orderService) {
        this.stockRepository = stockRepository;
        this.orderService = orderService;
    }

    @GetMapping(path = "/stock/list")
    public @ResponseBody Iterable<Stock> getAllStocks() {
        // This returns a JSON or XML with the users
        return stockRepository.findAll();
    }

    @GetMapping(path = "/transanction")
    public @ResponseBody String transanction() {
        // This returns a JSON or XML with the users
        try {
            orderService.transaction();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Hello";
    }

    @PostMapping(path = "/createOrder")
    public @ResponseBody String createOrder() {
        // This returns a JSON or XML with the users
        try {
            orderService.createOrder();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Hello";
    }

    @PostMapping(path = "/test")
    public @ResponseBody String test() {
        // This returns a JSON or XML with the users
        orderService.test(1);
        return "Hello";
    }


    @PostMapping(path = "/stock", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Add stock product")
    public @ResponseBody Stock createStock(@RequestPart String description,
                                           @RequestPart(required = false) String color,
                                           @RequestPart String category,
                                           @RequestPart String price,
                                           @RequestPart String quantity,
                                           @RequestPart String status,
                                           @RequestPart(required = false) MultipartFile image) {
        Stock k = new Stock();
        k.setCategory(category);
        k.setColor(color);
        k.setPrice(Double.parseDouble(price));
        k.setDescription(description);
        k.setQuantity(Integer.parseInt(quantity));
        k.setStatus(status);

        if (image != null) {
            try {
//            try (FileOutputStream fos = new FileOutputStream("test.png")) {
//                fos.write(document.getBytes());
//                //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
//            }
                k.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Stock saved = stockRepository.save(k);
        return saved;
    }

    @PutMapping(path = "/stock/{code}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Update stock product")
    public @ResponseBody Stock updateStock(@PathVariable int code,
                                           @RequestPart String description,
                                           @RequestPart(required = false) String color,
                                           @RequestPart String category,
                                           @RequestPart String price,
                                           @RequestPart String quantity,
                                           @RequestPart String status,
                                           @RequestPart(required = false) MultipartFile image) {
        Stock k = new Stock();
        k.setCode(code);
        k.setCategory(category);
        k.setColor(color);
        k.setPrice(Double.parseDouble(price));
        k.setDescription(description);
        k.setQuantity(Integer.parseInt(quantity));
        k.setStatus(status);

        if (image != null) {
            try {
                k.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Stock updated = stockRepository.save(k);
        return updated;
    }

    @GetMapping(path = "/stock/{code}")
    @Operation(summary = "Get stock product")
    public @ResponseBody Optional<Stock> getStock(@PathVariable int code) {
        Optional<Stock> stockOpt = stockRepository.findById(code);
        return stockOpt;
    }
}
