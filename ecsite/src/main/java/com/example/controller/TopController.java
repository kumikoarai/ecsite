package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.service.CategoryService;
import com.example.domain.service.ProductCategoryService;
import com.example.domain.service.ProductOptionService;
import com.example.domain.service.ProductService;
import com.example.entity.CartProduct;
import com.example.entity.CartSession;
import com.example.entity.Product;
import com.example.entity.ProductCategory;
import com.example.form.CartForm;
import com.example.form.TopProductListForm;

@Controller
public class TopController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private Categories categories;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private LoginCheck loginCheck;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ProductOptionService productOptionService;

	@Autowired
	private Cart cart;

	@Autowired
	private HttpSession session;


	/** 商品を全件取得*/
	@GetMapping("/top")
	public String getProductsList(@ModelAttribute TopProductListForm form, Model model, HttpServletRequest request) {

		//セッション生成
		session = request.getSession();

		//ログイン中か判断
		String url = loginCheck.getLoginuser("top/index");

		//formをProductに変換
		Product product = modelMapper.map(form, Product.class);

		//商品一覧取得
		List<Product> productList = productService.getProducts(product);

		model.addAttribute("productList", productList);
		categories.getCategoryAll(model);

		return url;
	}



	/** カテゴリ別、商品取得*/
	@GetMapping("/top/{categoryName}")
	public String getProductsList(@PathVariable("categoryName") String categoryName, @ModelAttribute TopProductListForm form, Model model) {
		//ログイン中か判断
		String url = loginCheck.getLoginuser("top/index");

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

		return url;
	}



	/**商品詳細ページ*/
	@GetMapping("/top/product/detail/{productId}")
	public String getProductDetailPage(@PathVariable("productId") Integer productId, Model model, @ModelAttribute CartForm form) {
		//ログイン中か判断
		String url = loginCheck.getLoginuser("top/product/detail");

		/* 商品取得（1件） */
		Product product = productService.getProductsOne(productId);

		//商品の購入数量上限の取得
		Integer maxQuantity = productOptionService.getProductMaxQuantity(productId);
		if(maxQuantity == 0) {
			maxQuantity = 10;
		}
		List<Integer> quantity = new ArrayList<Integer> ();
		for(Integer i=0; i<maxQuantity; i++) {
			quantity.add(i + 1);
		}

		//商品の色の取得
		List<String> colors = productOptionService.getProductOptionColor(productId);
		Integer noColors = null;
		if(colors.size() == 0) {
			noColors = 0;
		}

		//モデルに登録
		model.addAttribute("product", product);
		categories.getCategoryAll(model);
		model.addAttribute("quantity", quantity);
		model.addAttribute("colors", colors);
		model.addAttribute("noColors", noColors);
		model.addAttribute("productId", productId);

		return url;
	}



	@PostMapping("/top/cart/add-to-cart/{productId}")
	public String postAddToCart(@PathVariable("productId") Integer productId, HttpServletRequest request, Model model, @ModelAttribute CartForm form) {

		//ショッピングカートのセッションを追加
		cart.setToSessionCart(productId, request, form);

		return "redirect:/top/cart/add-to-cart";
	}



	@GetMapping("/top/cart/add-to-cart")
	public String getAddToCart(Model model, HttpServletRequest request) {
		//セッション生成
		session = request.getSession();

		categories.getCategoryAll(model);

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

		return "top/cart/shoppinglist";
	}




	@PostMapping("/top/cart/remove")
	public String postCartItemRemove(@RequestParam("mapListKey")Integer mapListKey) {

		//セッションデータ取得
		@SuppressWarnings("unchecked")
		Map<Integer, CartSession> cartSessionList = (Map<Integer, CartSession>)session.getAttribute("cartSessionList");

		cartSessionList.remove(mapListKey);

		return "redirect:/top/cart/add-to-cart";
	}
}
