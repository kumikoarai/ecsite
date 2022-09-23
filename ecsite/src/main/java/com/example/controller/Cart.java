package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.entity.CartSession;
import com.example.form.CartForm;

@Component
public class Cart {

	@Autowired
	private HttpSession session;



	/**ショッピングカートのセッションを追加*/
	public void setToSessionCart(Integer productId, HttpServletRequest request, CartForm form) {
		//セッション生成
		session = request.getSession();

		//セッションデータ取得(存在確認)
		@SuppressWarnings("unchecked")
		Map<Integer, CartSession> cartSession = (Map<Integer, CartSession>)session.getAttribute("cartSessionList");

		//List<CartSession> cartSessionList = new ArrayList<CartSession> ();

		Map<Integer, CartSession> cartSessionList = new HashMap<>();
		if(cartSession == null) {
			//セッション削除
			session.removeAttribute("cartSessionList");

			Integer num = 1;
			CartSession cartItem = new CartSession();
			cartItem.setProductId(productId);
			cartItem.setQuantity(form.getQuantity());
			cartItem.setProduct_color(form.getProduct_color());

			cartSessionList.put(num, cartItem);

		    //セッションデータ設定
		    session.setAttribute("cartSessionList", cartSessionList);
		} else {
			List<Integer> keys = new ArrayList<Integer>(cartSession.keySet());
			Integer num = 0;
			if(keys.size() != 0) {
				num = keys.get(keys.size() - 1);
			} else {
				num = keys.size();
			}

			num = num + 1;
			CartSession cartItem = new CartSession();
			cartItem.setProductId(productId);
			cartItem.setQuantity(form.getQuantity());
			cartItem.setProduct_color(form.getProduct_color());

			cartSession.put(num, cartItem);

			//セッションの有効時間設定 3600は30分
			session.setMaxInactiveInterval(3600);

			//セッションデータ設定
		    session.setAttribute("cartSessionList", cartSession);
		}
	}
}
