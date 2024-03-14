package co.edu.javeriana.spacetrader.controller;

import co.edu.javeriana.spacetrader.model.Product;
import co.edu.javeriana.spacetrader.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class WebProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public String listProducts(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/detail/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.findProductById(id);
        model.addAttribute("product", product);
        return "product-detail";
    }

    @GetMapping("/edit-form/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product;
        if (id == 0) {
            product = new Product(); // Create a new Product object
        } else {
            product = productService.findProductById(id);
        }
        model.addAttribute("product", product);
        return "product-edit";
    }

    @PostMapping("/save")
    public String saveOrUpdateProduct(@ModelAttribute Product product) {
        productService.saveOrUpdateProduct(product);
        return "redirect:/product/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/product/list";
    }

}
