package com.example.world.order;

import com.example.world.product.Product;
import com.example.world.product.ProductService;
import com.example.world.user.SiteUser;
import com.example.world.user.UserService;
import jakarta.persistence.Lob;
import jakarta.persistence.criteria.Order;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/order")
@RequiredArgsConstructor
@Controller
public class OrderController {

    private final ProductService productService;
    private final UserService userService;
    private final SiteUser siteUser;
    private final OrderService orderService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/{id}")
    public String orderCreate(OrderForm orderForm, @PathVariable("id") Long id, Model model, BindingResult bindingResult, Principal principal) {
        Product product = this.productService.getProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("user", siteUser);
        return "Order_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}") // order/create/product.id
    public String orderCreate(@Valid OrderForm orderForm, Model model, BindingResult bindingResult, @PathVariable("id") Long id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "Order_form";
        }
        Product product = this.productService.getProduct(id);
        orderForm.setProduct(product);
        orderForm.setUser(siteUser);
        orderForm.setEmail(orderForm.getEmail());
        orderForm.setPayment(orderForm.getPayment());
        this.orderService.create(orderForm);

        return "redirect:/order/create/" + id;
    }

    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable Long id, BindingResult bindingResult, Model model, Principal principal) {
        Product product = this.productService.getProduct(id);
        SiteUser user = this.userService.getUser(principal.getName());
        OrderForm orderForm = new OrderForm();
        orderForm.setEmail("");
        model.addAttribute("orderProduct", product);
        model.addAttribute("username", user);
        model.addAttribute("orderForm", orderForm);
        model.addAttribute("customerName", "");

        return "Order_detail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        ProductOrder order = this.orderService.getOrder(id);
        this.orderService.delete(order);
        return "redirect:/";
    }

}
