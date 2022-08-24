/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
let initColor=JSON.parse(document.querySelector("#init_color").innerText);
let colorGA = new ColorGA(5, 5);
let individualRate = new Array();
let colorRate = new Array();
Vue.component('verte', Verte);
Vue.component('star-rating', VueStarRating.default);
let app = new Vue({
    el: "#app",
    data:function() {
        return {
            fontColor: "hsl(0,0%,0%)",
            titleColor: "hsl(0,0%,0%)",
            backgroundColor: "hsl(0,0%,0%)",
            asideGroundColor: "hsl(0,0%,0%)",
            contentGroundColor:"hsl(0,0%,0%)",
            pageColorRate: 3,
            fontColorRate: 3,
            titleColorRate: 3,
            backgroundColorRate: 3,
            asideGroundColorRate: 3,
            contentGroundColorRate:3,
            index: 0
        };
    },
    methods: {
        next:function() {
            if (this.index === 5) {
                colorGA.evolution(individualRate, colorRate);
                this.index = 0;
            }
            individualRate.push(this.pageColorRate);
            colorRate.push([
                this.fontColorRate,
                this.titleColorRate,
                this.contentGroundColorRate,
                this.backgroundColorRate,
                this.asideGroundColorRate
            ]);
            this.renderColor(this.index);
            this.resetRate();
        },
        resetRate:function() {
            this.pageColorRate = 3;
            this.fontColorRate = 3;
            this.titleColorRate = 3;
            this.backgroundColorRate = 3;
            this.asideGroundColorRate = 3;
        },
        renderColor:function(index) {
            let color = colorGA.getColor();
            this.fontColor = color[index][0];
            this.titleColor = color[index][1];
            this.contentGroundColor=color[index][2];
            this.backgroundColor = color[index][3];
            this.asideGroundColor = color[index][4];
            this.index = this.index + 1;
        },
    },
    mounted:function(){
//        if(initColor.font_color.length===5){
//        colorGA.setColor(initColor);
//        this.renderColor(0);    
//        }else{
            this.renderColor(0); 
//        }
    }
});
function ajaxClick(event) {
    if (event.status === "success") {
        app.next();
    }
}
function customer(event){
    if (event.status === "success") {
        alert("提交成功，已保存自定义数据");
        history.back();
    }
}

