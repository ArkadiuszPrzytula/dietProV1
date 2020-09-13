package com.pl.arkadiusz.diet_pro.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfiguration implements WebMvcConfigurer {


   @Override
   public void addResourceHandlers(final ResourceHandlerRegistry registry) {
       WebMvcConfigurer.super.addResourceHandlers(registry);
       registry.addResourceHandler("/images/**").addResourceLocations("/images/");
       registry.addResourceHandler("/css/**").addResourceLocations("/css/");
       registry.addResourceHandler("/js/**").addResourceLocations("/js/");
   }
}
