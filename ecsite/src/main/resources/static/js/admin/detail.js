'use strict'

var urlhostname = "https://ecsite-sample-kumiko.net/ecsite";

$(function() {

	/** 更新ボタンを押したら*/
	$('#btn-update').click(function(event) {
		$(".entrynull").remove();
		let entry_date1 = $('input[name="userName"]').val();
		let entry_date2 = $('input[name="password"]').val();

		var bl = true;
		if(entry_date1 == null || entry_date1 == '' ){
			$("#userName").parent("td").append('<p class="entrynull">※氏名を入力してください。</p>');
			bl = false;
		};

		if(entry_date2 == null || entry_date2 == '' ){
			$("#password").parent("td").append('<p class="entrynull">※新しいパスワードを入力してください。</p>');
			bl = false;
		};

		if(bl == true){
			updateAdminUser();
		};
	});

	/** 削除ボタンを押したら*/
	$('#btn-delete').click(function(event) {
		deleteAdminUser();
	});
});


/** 管理者の更新処理*/
function updateAdminUser() {

	//formの値を取得
	var formData = $('#user-detail-form').serializeArray();

	/*console.log(formData);*/

	//ajax通信
	$.ajax({
		type : "PUT",
		cache : false,
		url : urlhostname + '/admin/update',
		data: formData,
		dataType : 'json',
	}).done(function(data) {
		//ajax成功時の処理
		//console.log(data);
		alert("管理者ユーザーを更新しました。");
		window.location.href= urlhostname + '/admin/admin';
	}).fail(function(jqHR, textStatus, errorThrown) {
		//ajax失敗時の処理
		alert('管理者ユーザーの更新に失敗しました。');
	}).always(function() {
		//常に実行する処理（特になし）
	});
};


/** 管理者の削除処理*/
function deleteAdminUser() {
	//formの値を取得
	var formData = $('#user-detail-form').serializeArray();

	console.log(formData);
	//ajax通信
	$.ajax({
		type : "DELETE",
		cache : false,
		url : urlhostname + '/admin/delete',
		data: formData,
		dataType : 'json',
	}).done(function(data) {
		//ajax成功時の処理
		//console.log(data);
		alert("管理者ユーザーを削除しました。");
		window.location.href= urlhostname + "/admin/admin";
	}).fail(function(jqHR, textStatus, errorThrown) {
		//ajax失敗時の処理
		alert('管理者ユーザーの削除に失敗しました。');
	}).always(function() {
		//常に実行する処理（特になし）
	});
};