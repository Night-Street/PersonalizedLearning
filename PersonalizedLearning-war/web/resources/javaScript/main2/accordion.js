$(document).ready(function() {
$('div.accordion> ul ul').hide();
$('div.accordion> ul ul.active').show();
$('div.accordion> ul li a').click(function() {
$(this).next('ul').slideToggle('fast').parent('li').siblings('li').children('ul:visible').slideUp('fast');
});
});