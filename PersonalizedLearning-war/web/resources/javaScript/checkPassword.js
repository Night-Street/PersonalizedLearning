function checkPassword() {
    if (document.getElementById("password").value.equals(document.getElementById("secondPassword").value)) {
        return;
    } else {
        alert("两次输入的密码不一致");
        document.getElementById("secondPassword").focus();
    }
}