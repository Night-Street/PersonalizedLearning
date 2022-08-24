function myRichTextf(indexString, content) {
    $(indexString).html(marked(content));
}