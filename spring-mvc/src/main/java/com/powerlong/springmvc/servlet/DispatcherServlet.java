package com.powerlong.springmvc.servlet;

import com.powerlong.springmvc.init.handlerAdapter.HandlerAdapter;
import com.powerlong.springmvc.init.handlerMapping.HandlerMapping;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    static Collection<HandlerAdapter> handlerAdapters ;
    static Collection<HandlerMapping> handlerMappings ;

    private AnnotationConfigApplicationContext applicationContext;

    public DispatcherServlet() {
        //组件初始化
        Map<String, HandlerMapping> handlerMappingMaps = applicationContext.getBeansOfType(HandlerMapping.class);
        handlerMappings = handlerMappingMaps.values();

        Map<String, HandlerAdapter> handlerAdapterMaps = applicationContext.getBeansOfType(HandlerAdapter.class);
        handlerAdapters = handlerAdapterMaps.values();
    }

    public DispatcherServlet(AnnotationConfigApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object handlerMapping = getHandlerMapping(req);
        if(handlerMapping == null){
            System.out.println("未匹配到handlerMapping");
            return;
        }
        HandlerAdapter handlerAdapter = getHandlerAdapter(handlerMapping);
        if(handlerAdapter == null){
            System.out.println("未匹配到handlerAdapter");
            return;
        }
        Object result = handlerAdapter.handle(req,resp,handlerMapping);

        PrintWriter writer = resp.getWriter();
        writer.println(result);
        writer.flush();
        writer.close();
    }


    protected Object getHandlerMapping(HttpServletRequest request) {
        if (this.handlerMappings != null) {
            for (HandlerMapping mapping : this.handlerMappings) {
                Object handler = mapping.getHandlerMapping(request.getRequestURI());
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
    protected HandlerAdapter getHandlerAdapter(Object handlerMapping) {
        if (this.handlerAdapters != null) {
            for (HandlerAdapter adapter : this.handlerAdapters) {
                boolean flag = adapter.supports(handlerMapping);
                if (flag) {
                    return adapter;
                }
            }
        }
        return null;
    }
}
