package com.easy.ls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by a on 2018/5/3.
 */
@Configuration
public class StaticConfig extends WebMvcConfigurerAdapter {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludePath=new String[]{"/asset/v1.0/user/login","/static/**","/public/**","/swagger-ui.html"};

//        registry.addInterceptor(myInterceptor).addPathPatterns("/*").excludePathPatterns(excludePath);

        super.addInterceptors(registry);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        super.addViewControllers(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true).allowedHeaders("Origin, X-Requested-With, Content-Type, Accept")
                .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
                .maxAge(3600);
    }
//    @Bean
//    public FilterRegistrationBean timeFile(){
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        MyFilter timeFilter = new MyFilter();
//        registrationBean.setFilter(timeFilter);
//        List<String> url = new ArrayList<>();
//        url.add("/*");  //对所有路径起作用。对项目来说看其所需
//        registrationBean.setUrlPatterns(url);
//
//        return registrationBean;
//    }

//    @Bean
//    public FilterRegistrationBean filterRegist() {
//        FilterRegistrationBean frBean = new FilterRegistrationBean();
//        frBean.setFilter(new MyFilter());
//        frBean.addUrlPatterns("/*");
//        frBean.setOrder(1);
//        System.out.println("filter");
//        return frBean;
//    }


  /*  @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*")
                        .allowedMethods("*").allowedHeaders("*")
                        .allowCredentials(true)
                        .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L);
            }
        };
    }*/
}
