function searchKeyword(keyword) {
    alert(keyword);
    if(""==keyword||""==keyword.trim()){
        return;
    }
    window.location.href = "/result?keyword="+keyword;
}