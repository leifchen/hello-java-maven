package com.chen.servlet;

import com.chen.annotation.Autowired;
import com.chen.annotation.Controller;
import com.chen.annotation.RequestMapping;
import com.chen.annotation.Service;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * 自定义DispatcherServlet
 * <p>
 * @Author LeifChen
 * @Date 2019-06-20
 */
@Slf4j
public class DispatcherServlet extends HttpServlet {

    private static final String LOCATION = "contextConfigLocation";

    private Properties properties = new Properties();

    private List<String> classNames = new ArrayList<>();

    private Map<String, Object> ioc = new HashMap<>();

    private Map<String, Method> handlerMapping = new HashMap<>();

    public DispatcherServlet() {
        super();
    }

    /**
     * 初始化，加载配置文件
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) {
        // 1.加载配置文件
        doLoadConfig(config.getInitParameter(LOCATION));
        // 2.扫描所有相关的类
        doScanner(properties.getProperty("scanPackage"));
        // 3.初始化所有相关类的实例，并保存到IOC容器中
        doInstance();
        // 4.依赖注入
        doAutowired();
        // 5.构造HandlerMapping
        initHandlerMapping();
        // 6.等待请求，匹配URL，定位方法，反射调用执行
        // 调用doGet或doPost方法
        log.info("hello-mvc is init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (this.handlerMapping.isEmpty()) {
            return;
        }

        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        if (!this.handlerMapping.containsKey(url)) {
            resp.getWriter().write("404 Not Found!");
            return;
        }

        Method method = this.handlerMapping.get(url);
        // 获取方法的参数列表
        Class<?>[] parameterTypes = method.getParameterTypes();
        // 获取请求的参数
        Map<String, String[]> parameterMap = req.getParameterMap();
        // 保存参数值
        Object[] paramValues = new Object[parameterTypes.length];
        // 方法的参数列表
        for (int i = 0; i < parameterTypes.length; i++) {
            // 根据参数名称，做某些处理
            Class parameterType = parameterTypes[i];
            if (parameterType == HttpServletRequest.class) {
                paramValues[i] = req;
                continue;
            } else if (parameterType == HttpServletResponse.class) {
                paramValues[i] = resp;
                continue;
            } else if (parameterType == String.class) {
                for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
                    String value = Arrays.toString(param.getValue())
                            .replaceAll("\\[|\\]", "")
                            .replaceAll(",\\s", ",");
                    paramValues[i] = value;
                }
            }
        }

        String beanName = lowerFirstCase(method.getDeclaringClass().getSimpleName());
        try {
            method.invoke(ioc.get(beanName), paramValues);
        } catch (Exception e) {
            log.error("exception", e);
        }
    }

    private void doLoadConfig(String location) {
        InputStream input = null;

        try {
            input = this.getClass().getClassLoader().getResourceAsStream(location);
            properties.load(input);
        } catch (IOException e) {
            log.error("exception", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.error("exception", e);
                }
            }
        }
    }

    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                doScanner(packageName + "." + file.getName());
            } else {
                classNames.add(packageName + "." + file.getName().replace(".class", "").trim());
            }
        }
    }

    private void doInstance() {
        if (classNames.size() == 0) {
            return;
        }

        for (String className : classNames) {
            Class<?> clazz;
            try {
                clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    String beanName = lowerFirstCase(clazz.getSimpleName());
                    ioc.put(beanName, clazz.newInstance());
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = clazz.getAnnotation(Service.class);
                    String beanName = service.value();
                    // 如果用户设置了名字，就使用用户的自定义名字
                    if (!"".equals(beanName.trim())) {
                        ioc.put(beanName, clazz.newInstance());
                        continue;
                    }
                    // 如果自己没设置，就按接口类型创建一个实例
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> i : interfaces) {
                        ioc.put(i.getName(), clazz.newInstance());
                    }
                }
            } catch (Exception e) {
                log.error("exception", e);
            }
        }
    }

    /**
     * 首字母小写
     * @param str
     * @return
     */
    private String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }

                Autowired autowired = field.getAnnotation(Autowired.class);
                String beanName = autowired.value().trim();
                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    log.error("exception", e);
                }
            }
        }
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }

            String baseUrl = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                baseUrl = requestMapping.value();
            }

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }

                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String url = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");
                handlerMapping.put(url, method);
                log.info("mapping: {}, method: {}", url, method);
            }
        }
    }
}
