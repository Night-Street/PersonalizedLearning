//下面用来限制文本框输入字符的长度，来自https://www.jb51.net/article/76462.htm
//获取字符串长度（汉字算两个字符，字母数字算一个）
function getByteLen(val) {
    var len = 0;
    for (var i = 0; i < val.length; i++) {
        var a = val.charAt(i);
        if (a.match(/[^\x00-\xff]/ig) != null) {
            len += 2;
        } else {
            len += 1;
        }
    }
    return len;
}
// 只要键盘一抬起就验证编辑框中的文字长度，最大字符长度可以根据需要设定
function checkLength(obj, maxChars, leftwords, controlObject, chartype) {//maxChars--最多字符数  
    //chartype字符类型：0表示汉字，1表示英文，2表示不限制
    var curr = maxChars - getByteLen(obj.value);
    if (curr > 0) {
        document.getElementById(leftwords).innerHTML = curr.toString();
    } else {
        document.getElementById(leftwords).innerHTML = '0';
        document.getElementById(controlObject).readOnly = true;
    }
}
//上面用来限制文本框输入字符的长度，来自https://www.jb51.net/article/76462.htm
//下面控制输入字符类型
function chekUp(textobj, chartype) {
    if (chartype === 1) {//只能输入中文
        textobj.value = textobj.value.replace(/[^\u4E00-\u9FA5]/g, '');
    } else if (chartype === 0) {//只能输入英文
        textobj.value = textobj.value.replace(/[^a-zA-Z]/g, '');
    }
}