function showsubmenu(sid) {
    whichEl = eval("submenu" + sid);
    imgmenu = eval("imgmenu" + sid);
    if (whichEl.style.display == "none")
    {
        eval("submenu" + sid + ".style.display=\"\";");
        imgmenu.background = "../images/leftbg.gif";
    } else
    {
        eval("submenu" + sid + ".style.display=\"none\";");
        imgmenu.background = "../images/leftbg.gif";
    }
}