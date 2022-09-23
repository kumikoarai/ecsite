'use strict'

$(function() {
	/** 新規登録ボタンを押したら*/
	$('#btn-signup1').click(function(event) {
		//console.log("新規登録ボタンを押した");
		karitouroku();
	});


});


/**新規の仮登録の処理*/
function karitouroku() {
	//formの値を取得
	var formData = $('#user-sigup-form').serializeArray();
	//console.log(formData);

	//ajax通信
	$.ajax({
		type : "POST",
		cache : false,
		url : '/user/signup_edit',
		data: formData,
		dataType : 'json',
	}).done(function(data) {
		//ajax成功時の処理
		console.log(data);
		alert("新規の仮登録をしました。");
		window.location.href='/user/signup_edit';
	}).fail(function(jqHR, textStatus, errorThrown) {
		//ajax失敗時の処理
		console.log(jqHR);
		console.log(textStatus);
		console.log(errorThrown);
		alert('新規の仮登録に失敗しました。');
	}).always(function() {
		//常に実行する処理（特になし）
	});
};

