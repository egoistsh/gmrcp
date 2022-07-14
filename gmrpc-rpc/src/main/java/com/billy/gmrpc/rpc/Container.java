package com.billy.gmrpc.rpc;

import com.billy.gmrpc.registry.IRegistry;
import com.billy.gmrpc.registry.RegistryFactory;
import com.billy.gmrpc.rpc.annotation.Provider;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//处理注解
@Slf4j
public class Container {

    private static IRegistry registry = RegistryFactory.getRegistry();
    private static Map<String, Object> providers = new HashMap<>();

    static {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.billy"))
                .setScanners(new TypeAnnotationsScanner()));
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Provider.class, true);
        for (Class<?> clazz : classes) {
            try {
                Provider annotation = clazz.getAnnotation(Provider.class);
                Object provider = clazz.newInstance();
                String canonicalName = annotation.interfaceClazz().getCanonicalName();
                providers.put(canonicalName, provider);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void registerSelf(String selfAddress) {
        for (String service : providers.keySet()) {
            registry.register(selfAddress, service);
        }
    }

    public static Map<String, Object> getProviders() {
        return providers;
    }
}
