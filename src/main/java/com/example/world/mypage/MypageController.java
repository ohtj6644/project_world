package com.example.world.mypage;


import com.example.world.order.OrderService;
import com.example.world.order.ProductOrder;
import com.example.world.qna.Question;
import com.example.world.qna.QuestionService;
import com.example.world.review.Review;
import com.example.world.review.ReviewService;

import com.example.world.review.Review;
import com.example.world.review.ReviewService;
import com.example.world.user.SiteUser;
import com.example.world.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final UserService userService;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final QuestionService questionService;



    @GetMapping("")
    public String mypageMain(Model model , Principal principal){
        SiteUser siteUser = this.userService.getUser(principal.getName());
        List<ProductOrder> articles = this.orderService.getAuthor(siteUser);
        List<Review> reviews = this.reviewService.getAuthor(siteUser);
        List<Question> questions = this.questionService.getAuthor(siteUser);

        int orderCount = articles.size();
        int reviewCount = reviews.size();
        int questionCount=questions.size();

        model.addAttribute("questionCount",questionCount);
        model.addAttribute("reviewCount",reviewCount);
        model.addAttribute("orderCount",orderCount);
        model.addAttribute("user",siteUser);
        return "/mypage/Mypage_main";
    }

    @GetMapping("/order")
    public String myOrder(){
        return "/mypage/Mypage_order";
    }
    @GetMapping("/qna")
    public String myqna(){
        return "/mypage/Mypage_qna";
    }

    @GetMapping("/review")
    public String myReview(Model model, Principal principal,@RequestParam Long productOrderId,@RequestParam(value = "page", defaultValue = "0")int page){
        SiteUser siteUser = this.userService.getUser(principal.getName());
        Page<Review> reviewPage = this.reviewService.myReview(page);
        ProductOrder productOrder = this.orderService.getOrder(productOrderId);
        model.addAttribute("user",siteUser);
        model.addAttribute("review",reviewPage);
        model.addAttribute("productOrder",productOrderId);

        return "/mypage/Mypage_review";
    }

    @GetMapping("/user")
    public String myStatus(Model model , Principal principal){
        SiteUser siteUser = this.userService.getUser(principal.getName());
        List<ProductOrder> articles = this.orderService.getAuthor(siteUser);
        List<Review> reviews = this.reviewService.getAuthor(siteUser);
        List<Question> questions = this.questionService.getAuthor(siteUser);

        int orderCount = articles.size();
        int reviewCount = reviews.size();
        int questionCount=questions.size();

        model.addAttribute("questionCount",questionCount);
        model.addAttribute("reviewCount",reviewCount);
        model.addAttribute("orderCount",orderCount);
        model.addAttribute("user",siteUser);
        return "/mypage/Mypage_usr";
    }
}
