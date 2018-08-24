function searchKeyword(keyword,startDate,endDate) {
    if(""==keyword||""==keyword.trim()){
        return;
    }
    if(!startDate || startDate==""){
        startDate=null;
    }
    if(!endDate || endDate==""){
        endDate=null;
    }
    window.location.href = "/result?keyword="+keyword+"&startDate="+startDate+"&endDate="+endDate;
}

