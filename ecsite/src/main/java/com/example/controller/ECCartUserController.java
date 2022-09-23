package com.example.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.service.OrderService;
import com.example.domain.service.ProductService;
import com.example.domain.service.UserService;
import com.example.entity.CartProduct;
import com.example.entity.CartSession;
import com.example.entity.OrderDetail;
import com.example.entity.Product;
import com.example.entity.ProductOrder;
import com.example.entity.Users;
import com.example.form.CartForm;

@Controller
@RequestMapping("/user/cart")
public class ECCartUserController {

	@Autowired
	private Categories categories;

	@Autowired
	private HttpSession session;

	@Autowired
	private UserController userController;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private Cart cart;


	//ログイン中のユーザー名（メールアドレス）を取得後、ユーザーID取得
	public Integer getLoginUserId() {
		String name = AuthSecurityContextHolder.getName();
		Users users = userService.getUsersOne(name);
		return users.getUserId();
	}



	@GetMapping("/add-to-cart")
	public String getAddToCart(Model model, HttpServletRequest request) {
		//セッション生成
		session = request.getSession();

		categories.getCategoryAll(model);

		//ログイン中のユーザー名をModelに追加
		userController.setModelUserName(model);

		//セッションデータ取得
		@SuppressWarnings("unchecked")
		Map<Integer, CartSession> cartSessionList = (Map<Integer, CartSession>)session.getAttribute("cartSessionList");

		List<CartProduct> cartProductList = new ArrayList<CartProduct> ();
		Integer total = 0;
		Integer cartSessionListOr = null;

		//セッションがあるなら
		if(cartSessionList != null) {
			//セッションがある上で、Map内に要素があるなら
			if(cartSessionList.size() != 0) {
				cartSessionListOr = 1;
				for(Map.Entry<Integer, CartSession> cartSession : cartSessionList.entrySet()) {
					CartProduct cartProduct = new CartProduct();
					// 商品取得（1件）
					Product product = productService.getProductsOne(cartSession.getValue().getProductId());
					Integer quantity = cartSession.getValue().getQuantity();
					Integer price = product.getPrice();
					cartProduct.setProductId(cartSession.getValue().getProductId());
					cartProduct.setProductImage(product.getProductImage());
					cartProduct.setProductName(product.getProductName());
					cartProduct.setPrice(price);
					cartProduct.setQuantity(quantity);
					cartProduct.setProduct_color(cartSession.getValue().getProduct_color());
					cartProduct.setMapListKey(cartSession.getKey());

					cartProductList.add(cartProduct);
					total = total + (price * quantity);
				}
			} else {
				cartSessionListOr = 0;
			}
		} else {
			cartSessionListOr = 0;
		}

		model.addAttribute("cartProductList", cartProductList);
		model.addAttribute("total", total);
		model.addAttribute("cartSessionListOr", cartSessionListOr);

		return "user/cart/shoppinglist";
	}



	@PostMapping("/add-to-cart/{productId}")
	public String postAddToCart(@PathVariable("productId") Integer productId, HttpServletRequest request, Model model, @ModelAttribute CartForm form) {

		//ショッピングカートのセッションを追加
		cart.setToSessionCart(productId, request, form);

		return "redirect:/user/cart/add-to-cart";
	}



	@PostMapping("/remove")
	public String postCartItemRemove(@RequestParam("mapListKey")Integer mapListKey) {

		//セッションデータ取得
		@SuppressWarnings("unchecked")
		Map<Integer, CartSession> cartSessionList = (Map<Integer, CartSession>)session.getAttribute("cartSessionList");

		cartSessionList.remove(mapListKey);

		return "redirect:/user/cart/add-to-cart";
	}



	@PostMapping("/payment")
	public String postPayment(@RequestParam("total")Integer total, Model model) {
		categories.getCategoryAll(model);

		//ログイン中のユーザー名をModelに追加
		userController.setModelUserName(model);

		//ログイン中のユーザー名（メールアドレス）を取得後、ユーザーID取得
		Integer userId = getLoginUserId();

		// 現在日時情報で初期化されたインスタンスの生成
		Date dateObj = new Date();

		ProductOrder productOrder = new ProductOrder();
		productOrder.setUserId(userId);
		productOrder.setTotalPrice(total);
		productOrder.setOrderDate(dateObj);

		// オーダーの登録
		Integer orderId = orderService.postProductOrder(productOrder);

		//セッションデータ取得
		@SuppressWarnings("unchecked")
		Map<Integer, CartSession> cartSessionList = (Map<Integer, CartSession>)session.getAttribute("cartSessionList");

		//セッションがあるなら
		if(cartSessionList != null) {
			//セッションがある上で、Map内に要素があるなら
			if(cartSessionList.size() != 0) {
				for(Map.Entry<Integer, CartSession> cartSession : cartSessionList.entrySet()) {
					OrderDetail orderDetail = new OrderDetail();
					orderDetail.setOrderId(orderId);
					orderDetail.setProductId(cartSession.getValue().getProductId());
					orderDetail.setQuantity(cartSession.getValue().getQuantity());
					orderDetail.setProductColor(cartSession.getValue().getProduct_color());

					//オーダーの詳細の登録
					orderService.postOrderDetail(orderDetail);
				}
			}
		}

		//セッション削除
		session.removeAttribute("cartSessionList");

		return "user/cart/payment";
	}
}
