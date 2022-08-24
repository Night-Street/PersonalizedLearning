"use strict";
var ColorHSL = /** @class */ (function () {
    function ColorHSL() {
        this.hue = _.random(360);
        this.saturation = _.random(100);
        this.lightness = _.random(100);
    }
    ColorHSL.prototype.init = function (h, s, l) {
        this.hue = h;
        this.saturation = s;
        this.lightness = l;
    };
    ColorHSL.prototype.getValue = function () {
        return "hsl(" + this.hue + "," + this.saturation + "%," + this.lightness + "%)";
    };
    ColorHSL.prototype.singleChange = function () {
        var hE = _.random(-5, 5);
        var sE = _.random(-5, 5);
        var lE = _.random(-5, 5);
        this.hue += hE;
        if (this.hue < 0) {
            this.hue = 0;
        }
        else if (this.hue > 360) {
            this.hue = 360;
        }
        this.saturation += sE;
        if (this.saturation < 0) {
            this.saturation = 0;
        }
        else if (this.saturation > 100) {
            this.saturation = 100;
        }
        this.lightness += lE;
        if (this.lightness < 0) {
            this.lightness = 0;
        }
        else if (this.lightness > 100) {
            this.lightness = 100;
        }
    };
    ColorHSL.prototype.setHue = function (H) {
        this.hue = H;
    };
    ColorHSL.prototype.getHue = function () {
        return this.hue;
    };
    ColorHSL.prototype.setSaturation = function (S) {
        this.saturation = S;
    };
    ColorHSL.prototype.getSaturation = function () {
        return this.saturation;
    };
    ColorHSL.prototype.setLightness = function (L) {
        this.lightness = L;
    };
    ColorHSL.prototype.getLightness = function () {
        return this.lightness;
    };
    ColorHSL.prototype.equal = function (that) {
        return (this.getHue() === that.getHue() &&
            this.getLightness() === that.getLightness() &&
            this.getSaturation() === that.getSaturation());
    };
    return ColorHSL;
}());
var ColorIndividual = /** @class */ (function () {
    function ColorIndividual(count) {
        this.colorList = new Array();
        ColorIndividual.COLOR_COUNT = count;
        for (var i = 0; i < count; i++) {
            this.colorList.push(new ColorHSL());
        }
    }
    ColorIndividual.prototype.init = function (color) {
        var _this = this;
        this.colorList = new Array();
        color.map(function (item) {
            _this.colorList.push(item);
        });
    };
    ColorIndividual.prototype.setColor = function (color) {
        this.colorList = color.concat();
    };
    ColorIndividual.prototype.getColor = function () {
        return this.colorList.concat();
    };
    ColorIndividual.prototype.getColorValue = function () {
        var value = new Array();
        for (var i = 0; i < ColorIndividual.COLOR_COUNT; i++) {
            value.push(this.colorList[i].getValue());
        }
        return value;
    };
    ColorIndividual.prototype.singleChange = function (rate) {
        var minRate = 5;
        var index = 0;
        for (var i = 0; i < rate.length; i++) {
            if (minRate < rate[i]) {
                minRate = rate[i];
                index = i;
            }
        }
        this.colorList[index].singleChange();
    };
    ColorIndividual.prototype.sinCross = function (that) {
        var selectIndex = _.random(ColorIndividual.COLOR_COUNT - 1);
        var temp1 = this.getColor();
        var temp2 = that.getColor();
        this.setColor(_.concat(_.slice(temp1, 0, selectIndex), _.slice(temp2, selectIndex)));
        that.setColor(_.concat(_.slice(temp2, 0, selectIndex), _.slice(temp1, selectIndex)));
        return selectIndex;
    };
    return ColorIndividual;
}());
var ColorPopulation = /** @class */ (function () {
    function ColorPopulation(colorCount, individualCount) {
        ColorPopulation.INDIVIDUAL_COUNT = individualCount;
        ColorPopulation.COLOR_COUNT = colorCount;
        this.colorPopulation = new Array();
        for (var i = 0; i < individualCount; i++) {
            this.colorPopulation.push(new ColorIndividual(colorCount));
        }
    }
    ColorPopulation.prototype.getPopu = function () {
        return this.colorPopulation;
    };
    ColorPopulation.prototype.setPopu = function (popu) {
        this.colorPopulation = popu.concat();
    };
    ColorPopulation.prototype.getColorValue = function () {
        var value = new Array();
        for (var i = 0; i < ColorPopulation.INDIVIDUAL_COUNT; i++) {
            value.push(this.colorPopulation[i].getColorValue());
        }
        return value;
    };
    ColorPopulation.prototype.singleChange = function (colorRate) {
        for (var i = 0; i < colorRate.length; i++) {
            this.colorPopulation[i].singleChange(colorRate[i]);
        }
    };
    ColorPopulation.prototype.sinCross = function () {
        var firstIndex = 0;
        var secondIndex = 0;
        var selectIndex = 0;
        firstIndex = _.random(ColorPopulation.INDIVIDUAL_COUNT - 1);
        do {
            secondIndex = _.random(ColorPopulation.INDIVIDUAL_COUNT - 1);
        } while (firstIndex === secondIndex);
        var firstIndividual = this.colorPopulation[firstIndex];
        var secondIndividual = this.colorPopulation[secondIndex];
        selectIndex = firstIndividual.sinCross(secondIndividual);
        return [firstIndex, secondIndex, selectIndex];
    };
    ColorPopulation.prototype.choice = function (colorRate) {
        var index = new Array();
        var offspringPopulation = new Array();
        var disk = new Array(ColorPopulation.INDIVIDUAL_COUNT + 1);
        disk[0] = 0;
        for (var i = 0; i < ColorPopulation.INDIVIDUAL_COUNT; i++) {
            disk[i + 1] = disk[i] + colorRate[i];
        }
        for (var i = 0; i < ColorPopulation.INDIVIDUAL_COUNT; i++) {
            var r = _.random(disk[ColorPopulation.INDIVIDUAL_COUNT]);
            for (var j = 0; j < ColorPopulation.INDIVIDUAL_COUNT; j++) {
                if (disk[j] <= r && r <= disk[j + 1]) {
                    index.push(j);
                    offspringPopulation.push(this.colorPopulation[j]);
                    break;
                }
            }
        }
        this.setPopu(offspringPopulation);
        return index;
    };
    return ColorPopulation;
}());
var ColorGA = /** @class */ (function () {
    function ColorGA(colorCount, individualCount) {
        this.colorPopulation = new ColorPopulation(colorCount, individualCount);
    }
    ColorGA.prototype.evolution = function (individualRate, colorRate) {
        var choiceIndex;
        var newColorRate = new Array();
        choiceIndex = this.colorPopulation.choice(individualRate);
        for (var i = 0; i < colorRate.length; i++) {
            newColorRate.push(colorRate[choiceIndex[i]]);
        }
        var sinCrossIndex;
        sinCrossIndex = this.colorPopulation.sinCross();
        var temp1 = newColorRate[sinCrossIndex[0]].concat();
        var temp2 = newColorRate[sinCrossIndex[1]].concat();
        newColorRate[sinCrossIndex[0]] = _.concat(_.slice(temp1, 0, sinCrossIndex[2]), _.slice(temp2, sinCrossIndex[2]));
        newColorRate[sinCrossIndex[1]] = _.concat(_.slice(temp2, 0, sinCrossIndex[2]), _.slice(temp1, sinCrossIndex[2]));
        this.colorPopulation.singleChange(newColorRate);
    };
    ColorGA.prototype.setColor = function (color) {
        var colrIndividuals = new Array();
        color.map(function (item) {
            var colrIndividual = new ColorIndividual(4);
            var colors = new Array();
            var fontColor = new ColorHSL();
            var titleColor = new ColorHSL();
            var backgroundColor = new ColorHSL();
            var asideBackgroundColor = new ColorHSL();
            var font_color = item.font_color, title_color = item.title_color, background_color = item.background_color, aside_background_color = item.aside_background_color;
            fontColor.init(font_color.hue, font_color.saturation, font_color.lightness);
            titleColor.init(title_color.hue, title_color.saturation, title_color.lightness);
            backgroundColor.init(background_color.hue, background_color.saturation, background_color.lightness);
            asideBackgroundColor.init(aside_background_color.hue, aside_background_color.saturation, aside_background_color.lightness);
            colors.push(fontColor, titleColor, backgroundColor, asideBackgroundColor);
            colrIndividual.setColor(colors);
            colrIndividuals.push(colrIndividual);
        });
        this.colorPopulation.setPopu(colrIndividuals);
    };
    ColorGA.prototype.getColor = function () {
        return this.colorPopulation.getColorValue();
    };
    return ColorGA;
}());