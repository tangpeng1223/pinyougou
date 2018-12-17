app.service("loginService",function ($http) {
    //显示用户名
    this.getUsername=function () {
        return $http.get("/login/getUsername.do");
    }
})