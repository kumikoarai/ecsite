$(function() {
	$("#hamburger_menu").click(function() {
		console.log("#hamburger_menuをクリックしたよ。aaa");
		var thismenu = $(this);
		thismenu.toggleClass("horizontal_line");
		thismenu.toggleClass('cross_line');

		var navdiv = $("nav").find("div");
		navdiv.toggleClass("show");

		var login_btn_box = $(".login_btn_box");
		login_btn_box.toggleClass("show");
	});
});