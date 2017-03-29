# Commons2

最基本的一套应用的外壳，修改一下包名什么的就可以直接开发了。最基本的一些工具都放在utlis包下
基本框架网络请求格式
Map<String, String> map = new HashMap<String, String>();
        map.put("key", "valus");
        RxRetrofitClient.getInstence()
                .create(HttpService.class)
                .login(map)
                .compose(RxHelper.<BaseHttpResult>io_main())
                .subscribe(new CommonsSubscriber<BaseHttpResult>() {
                    @Override
                    public void onSuccess(BaseHttpResult baseHttpResult) {

                    }
                    @Override
                    public void onError(String errorMsg) {

                    }
                });
                
