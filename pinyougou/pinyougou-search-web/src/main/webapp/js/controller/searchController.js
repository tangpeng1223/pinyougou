app.controller("searchController",function ($scope,$location,searchService) {

    $scope.searchMap={"keywords":"","category":"","brand":"","spec":{},"price":"","pageNo":"1","pageSize":"20","sortFiled":"","sortOrder":""};
    //根据关键字搜索
    $scope.search=function () {
       searchService.search($scope.searchMap).success(function (response) {
           $scope.resultMap=response;

           //构建分页条信息
           buildPageInfo();
       });
    };


    //添加查询条件
    $scope.addSearchItem=function (key,value) {
        if( "category"==key || "brand"==key || "price"==key ){
            $scope.searchMap[key]=value;
        }else{
            $scope.searchMap.spec[key]=value;
        }
        //重新搜索时需要将页号设置为1
        $scope.searchMap.pageNo=1;
        //重新查询
        $scope.search();
    }

    //删除查询标签
    $scope.removeSearchItem=function (key) {
        if("category"==key || "brand"==key ||"price"==key){
           $scope.searchMap[key]="";

        }else{
            //删除
            delete  $scope.searchMap.spec[key];
        }
        //重新搜索时需要将页号设置为1
        $scope.searchMap.pageNo=1;
        $scope.search();
    }

    buildPageInfo =function () {
        //页面显示的页号数组 如1 2 3 4 5
        $scope.pageNoList=[];
        //在导航条中总显示的页号数 设置为5
        var showPageNoTotal=5;
        //页号的起始页号
        var  startPageNo=1;
        //结束页号
        var endPageNo=$scope.resultMap.totalPages;

        //如果总页数大于要显示的页号数
        if($scope.resultMap.totalPages>showPageNoTotal){
            //当前页号的左右间隔 对显示页号/2向下求整
            var  interval=Math.floor(showPageNoTotal/2);
            startPageNo=parseInt($scope.searchMap.pageNo)-interval;
            endPageNo=parseInt($scope.searchMap.pageNo)+interval;


            if(startPageNo>=1){
                if(endPageNo>$scope.resultMap.totalPages){
                    startPageNo=$scope.resultMap.totalPages-showPageNoTotal+1;
                    endPageNo=$scope.resultMap.totalPages;
                }
            }else{
                //如果起始页号小于1的则设置为1 结束页号为要显示的总页号
                startPageNo=1;
                endPageNo=showPageNoTotal;
            }
        }
        //将要显示的页号存入到页号数组中
        for (var i=startPageNo; i<=endPageNo;i++){
            $scope.pageNoList.push(i);
        }

        $scope.frontDot=false;
        //前面的三个点 如果起始页号大于1就该出现
        if(startPageNo>1){
            $scope.frontDot=true;
        }
        //后面的三个点  如果结束页号小与总页数则该出现
        $scope.backDot=false;
        if(endPageNo<$scope.resultMap.totalPages){
            $scope.backDot=true;
        }
    };
    //判断是否为当前的页号
    $scope.isCurrentPage=function (pageNo) {
        return $scope.searchMap.pageNo==pageNo;
    }
    //跳转到某个页面
    $scope.queryByPageNo=function (pageNo) {
        pageNo=parseInt(pageNo);
        if(pageNo>0 && pageNo<=$scope.resultMap.totalPages){
              $scope.searchMap.pageNo=pageNo;
              $scope.search();
        }
    };

    //排序
    $scope.sortSearch=function (sortFiled,sortOrder) {
        $scope.searchMap.sortFiled=sortFiled;
        $scope.searchMap.sortOrder=sortOrder;
        //调用搜索方法
        $scope.search();
    }
    //加载搜索关键字
    $scope.loadKeywords=function () {
        $scope.searchMap.keywords=$location.search()["keywords"];
        $scope.search();
    }
});
