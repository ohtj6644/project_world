package com.example.world.admin;

import com.example.world.notice.Notice;
import com.example.world.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.example.world.DataNotFoundException;
import com.example.world.order.OrderService;
import com.example.world.order.ProductOrder;
import com.example.world.product.Product;
import com.example.world.product.ProductService;
import com.example.world.review.Review;
import com.example.world.user.SiteUser;
import com.example.world.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final NoticeService noticeService;

    private final AdminService adminService;
    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;


    @GetMapping("/ad")
    public String adminMain() {
        return "admin/admin_main";
    }


    @GetMapping("/ad/order")
    public String adminOrder(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<ProductOrder> paging = this.adminService.getList(page, size);
        List<ProductOrder> orderProductList = this.orderService.getOrderList();
        model.addAttribute("paging", paging);
        model.addAttribute("orderProductList", orderProductList);

        return "admin/admin_order";
    }


    @PostMapping("/ad/code/{id}")
    public String adminSendCode(@PathVariable("id") Long id,
                                @RequestParam(value = "sendCode", required = false) String sendCode) {
        ProductOrder productOrder = this.orderService.getOrder(id);

        if (id != null && sendCode != null && !sendCode.isEmpty()) {
            orderService.updateOrderSendCode(productOrder);
            return "redirect:/ad/order";
        } else {
            return "redirect:/ad/order";
        }
    }

    @PostMapping("/ad/confirm/{id}")
    public String adminConfirmOrder(@PathVariable Long id) {
        orderService.updateOrderStatus(id, "결제완료");
        return "redirect:/ad/order";
    }


    @PostMapping("/ad/cancel/{id}")
    public String adminCancelOrder(@PathVariable Long id) {
        orderService.updateOrderStatus(id, "취소완료");
        return "redirect:/ad/order";
    }


    @GetMapping("/ad/product")
    public String adminProduct() {
        return "admin/admin_product";
    }


    @GetMapping("/ad/user")
    public String adminUser() {
        return "admin/admin_user";
    }


    @GetMapping("/ad/review")
    public String adminReview() {
        return "admin/admin_review";
    }


    @GetMapping("/ad/qna")
    public String adminQna() {
        return "admin/admin_qna";
    }

    @GetMapping("ad/notice")
    public String adminNotice(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
        Page<Notice> paging = this.noticeService.allNotice(page);
        model.addAttribute("paging",paging);
        return "admin/admin_notice";
    }
}
