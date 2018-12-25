app.controller("searchController",function ($scope,searchService) {

    $scope.searchMap={"keywords":"","category":"","brand":"","spec":{},"price":""};
    //根据关键字搜索
    $scope.search=function () {
       searchService.search($scope.searchMap).success(function (response) {
           $scope.resultMap=response;
       });
    };


    //添加查询条件
    $scope.addSearchItem=function (key,value) {
        if( "category"==key || "brand"==key || "price"==key ){
            $scope.searchMap[key]=value;
        }else{
            $scope.searchMap.spec[key]=value;
        }
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
         $scope.search();
    }

});
