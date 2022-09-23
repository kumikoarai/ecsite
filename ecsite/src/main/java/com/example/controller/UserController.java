package com.example.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.service.CategoryService;
import com.example.domain.service.EndUserService;
import com.example.domain.service.OrderService;
import com.example.domain.service.ProductCategoryService;
import com.example.domain.service.ProductService;
import com.example.domain.service.UserService;
import com.example.entity.EndUser;
import com.example.entity.EndUserSignup;
import com.example.entity.KariUser;
import com.example.entity.OrderDetail;
import com.example.entity.OrderProductDetail;
import com.example.entity.Product;
import com.example.entity.ProductCategory;
import com.example.entity.ProductOrder;
import com.example.entity.Users;
import com.example.form.EndUserForm;
import com.example.form.TopProductListForm;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private Categories categories;

	@Autowired
	private HttpSession session;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private EndUserService endUserService;

	@Autowired
	private OrderService orderService;



	//ログイン中のユーザー名（メールアドレス）を取得
	public String getAuthSecurityContextHolder() {
		String name = AuthSecurityContextHolder.getName();
		return name;
	}


	//ログイン中のユーザー名をModelに追加
	public void setModelUserName(Model model) {
		String name = getAuthSecurityContextHolder();
		Users users = userService.getUsersOne(name);
		model.addAttribute("usersName", users.getUserName());
	}



	//ログイン中のユーザー情報を取得
	public EndUser getEndUser(String email) {
		Users users = userService.getUsersOne(email);
		EndUserSignup endUserjoho = endUserService.getEndUserSignupjoho(users.getUserId());
		EndUser endUser = new EndUser();
		endUser.setUserId(endUserjoho.getUserId());
		endUser.setUserName(users.getUserName());
		endUser.setUserEmail(email);
		endUser.setPassword(null);
		endUser.setAddress(endUserjoho.getAddress());
		endUser.setBirthday(endUserjoho.getBirthday());
		endUser.setGender(endUserjoho.getGender());
		endUser.setPhoneNumber(endUserjoho.getPhoneNumber());
		endUser.setUsersId(endUserjoho.getUsersId());

		return endUser;
	}



	/**ユーザーのトップページ*/
	@GetMapping("/index")
	public String getUserPage(@ModelAttribute TopProductListForm form, Model model) {

		//formをProductに変換
		Product product = modelMapper.map(form, Product.class);

		//商品一覧取得
		List<Product> productList = productService.getProducts(product);

		model.addAttribute("productList", productList);
		categories.getCategoryAll(model);

		//ログイン中のユーザー名をModelに追加
		setModelUserName(model);

		return "user/index";
	}



	/** カテゴリ別、商品取得*/
	@GetMapping("/{categoryName}")
	public String getProductsList(@PathVariable("categoryName") String categoryName, @ModelAttribute TopProductListForm form, Model model) {

		//カテゴリIDの取得
		Integer categoryId = categoryService.getCategoryId(categoryName);

		//商品とカテゴリの取得（カテゴリ別）
		List<Product> productList2 = new ArrayList<>();
		List<ProductCategory> productCategories = productCategoryService.getProductCategoriesCategory(categoryId);
		for(ProductCategory pc : productCategories) {
			Integer productId = pc.getProductId();
			Product getProduct = productService.getProductsOne(productId);
			productList2.add(getProduct);
		}

		model.addAttribute("productList", productList2);
		categories.getCategoryAll(model);
		model.addAttribute("categoryName", categoryName);

		//ログイン中のユーザー名をModelに追加
		setModelUserName(model);

		return "user/index";
	}



	/**ユーザー仮登録取得*/
	@GetMapping("/signup_edit")
	public String getSignuoEdit(Locale locale,  @ModelAttribute EndUserForm form, Model model) {
		//セッションデータ取得
	    Integer userId = (Integer)session.getAttribute("userId");


		KariUser kariUser = userService.getKariUser(userId);
		//form.setUserId(userId);
		form.setUserEmail(kariUser.getUserEmail());
		form.setUserName(kariUser.getUserName());
		form.setPassword(kariUser.getPassword());
		//model.addAttribute("kariUser", kariUser);
		return "user/signup_edit";
	}



	/**ユーザー登録確認*/
	@PostMapping("/confirm")
	public String postConfirm(Locale locale, @ModelAttribute @Validated EndUserForm form, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			//NG:管理者追加画面に戻ります
			System.out.println("エラーがあります" + bindingResult);
			return getSignuoEdit(locale, form, model);
		}
		//セッションデータ取得
	    Integer userId = (Integer)session.getAttribute("userId");
		KariUser kariUser = userService.getKariUser(userId);
		model.addAttribute("kariUser", kariUser);

		return "user/confirm";
	}



	/**ユーザー登録*/
	@PostMapping("/signup")
	public String postSignupEndUser(@ModelAttribute @Validated EndUserForm form) {
		//formをProductに変換
		EndUser endUser = modelMapper.map(form, EndUser.class);
		//セッションデータ取得
	    Integer userId = (Integer)session.getAttribute("userId");
		userService.postEndUser(endUser, userId);
		//セッションの破棄
		session.removeAttribute("userId");
		return "redirect:/login";
	}



	/**アカウント情報編集*/
	@GetMapping("/account")
	public String getAccount(Model model, @ModelAttribute EndUserForm form) {
		categories.getCategoryAll(model);

		//ログイン中のユーザー名をModelに追加
		setModelUserName(model);

		//ログイン中のユーザー名（メールアドレス）を取得
		String email = getAuthSecurityContextHolder();

		//ログイン中のユーザー情報を取得
		EndUser endUser = getEndUser(email);

		//EndUserをformに変換
		form = modelMapper.map(endUser, EndUserForm.class);

		model.addAttribute("endUserForm", form);

		return "user/account";
	}



	/**購入履歴*/
	@GetMapping("/order-list")
	public String getOrderList(Model model) {
		categories.getCategoryAll(model);
		//ログイン中のユーザー名をModelに追加
		setModelUserName(model);

		//ログイン中のユーザー名（メールアドレス）を取得
		String userEmail = getAuthSecurityContextHolder();

		//ログイン中のユーザー情報を取得
		EndUser endUser = getEndUser(userEmail);

		// オーダーの取得 ユーザーID別
		List<ProductOrder> productOrderList = orderService.getProductOrderByUserId(endUser.getUsersId());

		//TreeMap：Mapの宣言・初期化
		Map<Integer, List<OrderProductDetail>> orderListMap = new TreeMap<Integer, List<OrderProductDetail>>(Comparator.reverseOrder());

		for(ProductOrder productOrder : productOrderList) {

			List<OrderProductDetail> orderProductDetailList = new ArrayList<> ();
			List<OrderDetail> orderDetailList = orderService.getOrderDetailByOrderId(productOrder.getOrderId());

			for(OrderDetail orderDetail : orderDetailList) {
				OrderProductDetail orderProductDetail = new OrderProductDetail();
				//商品取得(1件)
				Product product = productService.getProductsOne(orderDetail.getProductId());
				//情報をセット
				orderProductDetail.setProductId(orderDetail.getProductId());
				orderProductDetail.setProductImage(product.getProductImage());
				orderProductDetail.setProductName(product.getProductName());
				orderProductDetail.setPrice(product.getPrice());
				orderProductDetail.setOrderDetailId(orderDetail.getOrderDetailId());
				orderProductDetail.setQuantity(orderDetail.getQuantity());
				orderProductDetail.setProductColor(orderDetail.getProductColor());
				//リストに追加
				orderProductDetailList.add(orderProductDetail);
			}
			orderListMap.put(productOrder.getOrderId(), orderProductDetailList);
		}
		model.addAttribute("orderListMap", orderListMap);
		model.addAttribute("productOrderList", productOrderList);

		return "user/order_list";
	}

}
