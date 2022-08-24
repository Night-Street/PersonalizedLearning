var myWordsPre = document.getElementById("wordCloud").innerHTML.split("#");//字符串转换为数组
var myWords = new Array();
for (let tem of myWordsPre) {//循环用(int i=0;i<myWordsPre.length;i++)出错
    var temArray = tem.split(",");
    var wordCount = {
        word: temArray[0],
        size: parseInt(temArray[1])
    };
    myWords.push(wordCount);
}