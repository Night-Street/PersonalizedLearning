function doClick(o, total) {
    o.className = "nav_current";
    var j;
    var id;
    var e;
    for (var i = 0; i < total; i++) {
        id = "nav" + i;
        j = document.getElementById(id);
        e = document.getElementById("sub" + i);
        if (id !== o.id) {
            if (null !== j)
                j.className = "nav_link";
            if (null !== e)
                e.style.display = "none";
        } else {
            e.style.display = "block";
        }
    }
}// * www.divcss5.com */