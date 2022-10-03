'use strict'

var urlhostname = "https://ecsite-sample-kumiko.net/ecsite";

$(function() {
	/** 更新ボタンを押したら*/
	$('#btn-update').click(function(event) {
		//console.log("クリックした：");
		$(".entrynull").remove();
		let entry_date1 = $('input[name="userName"]').val();
		let entry_date2 = $('input[name="password"]').val();
		//let entry_date3 = $('input[name="userEmail"]').val();
		let entry_date4 = $('input[name="birthday"]').val();
		let entry_date5 = $('input[name="address"]').val();
		let entry_date6 = $('input[name="phoneNumber"]').val();

		var bl = true;
		if(entry_date1 == null || entry_date1 == '' ){
			$("#userName").parent("div").append('<p class="entrynull">※氏名を入力してください。</p>');
			bl = false;
		};

		if(entry_date2 == null || entry_date2 == '' ){
			$("#password").parent("div").append('<p class="entrynull">※新しいパスワードを入力してください。</p>');
			bl = false;
		};

		/*if(entry_date3 == null || entry_date3 == '' ){
			$("#userEmail").parent("div").append('<p class="entrynull">※メールアドレスを入力してください。</p>');
			bl = false;
		};*/

		if(entry_date4 == null || entry_date4 == '' ){
			$("#birthday").parent("div").append('<p class="entrynull">※誕生日を入力してください。</p>');
			bl = false;
		};

		if(entry_date5 == null || entry_date5 == '' ){
			$("#address").parent("div").append('<p class="entrynull">※住所を入力してください。</p>');
			bl = false;
		};

		if(entry_date6 == null || entry_date6 == '' ){
			$("#phoneNumber").parent("div").append('<p class="entrynull">※電話番号を入力してください。</p>');
			bl = false;
		};

		if(bl == true){
			userUpdate();
		};
	});



	/** [メールアドレスを変更はこちら]を押したら*/
	$('#email_change').click(function() {
		$('#email_change_form').toggle(100);
		$('#email_change').toggleClass('color');
		$('#email_change').text("メールアドレスを変更はこちら。");
		$('.color').text("新しいメールアドレスを入力してください。");
	});



	/** メールアドレスを変更ボタンを押したら*/
	$('#btn-email-change').click(function() {
		$(".entrynull").remove();
		let entry_date1 = $('#email_change_form').find('input[name="userEmail"]').val();
		if(entry_date1 == null || entry_date1 == '' ){
			$("#email_change_form").find("#userEmail").parent("div").append('<p class="entrynull">※新しいメールアドレスを入力してください。</p>');
		} else {
			emailUpdate();
		};
	});
});




/**ユーザー情報の更新処理*/
function userUpdate() {
	var formData = $('#user-form').serializeArray();
	console.log("中：" + formData);

	//ajax通信
	$.ajax({
		type : "PUT",
		cache : false,
		url : urlhostname + '/user/update',
		data: formData,
		dataType : 'json',
	}).done(function(data) {
		//ajax成功時の処理
		//console.log(data);
		alert("アカウント情報の更新をしました。");
		window.location.href= urlhostname + '/user/account';
	}).fail(function(jqHR, textStatus, errorThrown) {
		//ajax失敗時の処理
		//console.log(jqHR);
		//console.log(textStatus);
		//console.log(errorThrown);
		alert('新規の仮登録に失敗しました。');
	}).always(function() {
		//常に実行する処理（特になし）
	});
};




/**メールアドレスの更新処理*/
function emailUpdate() {
	console.log("btn-email-changeをクリックした");
	var formData = $('#email_change_form').serializeArray();

	//ajax通信
	$.ajax({
		type : "PUT",
		cache : false,
		url : urlhostname + '/user/updateEmail',
		data: formData,
		dataType : 'json',
	}).done(function(data) {
		//ajax成功時の処理
		//console.log(data);
		alert("メールアドレスを変更しました。再度ログインしてください。");
		window.location.href= urlhostname + '/user/logout';
	}).fail(function(jqHR, textStatus, errorThrown) {
		//ajax失敗時の処理
		alert('メールアドレスの変更に失敗しました。');
	}).always(function() {
		//常に実行する処理（特になし）
	});
};

