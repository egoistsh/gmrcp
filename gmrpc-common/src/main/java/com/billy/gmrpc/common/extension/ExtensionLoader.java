package com.billy.gmrpc.common.extension;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ExtensionLoader<T> {
    private static final String GMRPC_DIRECTORY = "META-INF/gmrpc/";
    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();
    private final Class<?> type;
    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    private ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type should not be null");
        }
        //扩展点类型只能是接口
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type must be an interface");
        }
        if (type.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("Extension type must be annotated by @SPI");
        }
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (extensionLoader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
            extensionLoader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return extensionLoader;
    }

    public T getExtension(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Extension name == null");
        }
//        if ("true".equals(name)) {
//            //获取默认的拓展实现类
//            return getDefaultExtension();
//        }
        //Holder,用于持有目标对象
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<Object>());
            holder = cachedInstances.get(name);
        }
        Object instance = holder.get();
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createExtension(name);
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }

    //创建拓展对象
    private T createExtension(String name) {
        // 从配置文件中加载所有的拓展类，可得到"配置向名称"到"配置类"的映射表
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new RuntimeException("No such extension of name " + name);
        }
        T instance = (T) EXTENSION_INSTANCES.get(clazz);
        if (instance == null) {
            try {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return instance;
    }

    //获取默认拓展点
//    private T getDefaultExtension() {
//        getExtensionClasses();
//    }

    //获取所有拓展类
    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    //加载拓展类
//                    classes = loadExtensionClasses();
                    classes = new HashMap<>();
                    //加载指定目录下的配置文件
                    loadDirectory(classes);
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }

    private void loadDirectory(Map<String, Class<?>> classes) {
        String fileName = ExtensionLoader.GMRPC_DIRECTORY + type.getName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            urls = classLoader.getResources(fileName);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourceUrl = urls.nextElement();
                    loadResource(classes, classLoader, resourceUrl);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void loadResource(Map<String, Class<?>> classes, ClassLoader classLoader, URL resourceUrl) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceUrl.openStream(), "UTF-8"))) {
            String line;
            //按行读取配置内容
            while ((line = reader.readLine()) != null) {
                //定位 # 字符
                final int ci = line.indexOf('#');
                if (ci >= 0) {
                    //截取#之前的字符串，#之后的内容为注释，需忽略
                    line = line.substring(0, ci);
                }
                line = line.trim();
                if (line.length() > 0) {
                    final int ei = line.indexOf('=');
                    String name = line.substring(0, ei).trim();
                    String clazzName = line.substring(ei + 1).trim();
                    if (name.length() > 0 && clazzName.length() > 0) {
                        Class<?> clazz = classLoader.loadClass(clazzName);
                        classes.put(name, clazz);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    //TODO 默认值没实现
    /*private Map<String, Class<?>> loadExtensionClasses() {
        // 获取SPI注解，这里的type是调用getExtensionLoader时传入的
        final SPI defaultAnnotation = type.getAnnotation(SPI.class);
        if (defaultAnnotation != null) {
            String value = defaultAnnotation.value();
            if ((value = value.trim()).length() > 0) {
                //对 SPI 注解内容进行切分
            }
        }
    }*/


}
