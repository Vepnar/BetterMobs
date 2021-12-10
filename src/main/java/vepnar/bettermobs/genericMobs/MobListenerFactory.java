package vepnar.bettermobs.genericMobs;

import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MobListenerFactory implements InvocationHandler {

    private final IMobListener mobListener;

    public MobListenerFactory(IMobListener listener) {
        this.mobListener = listener;
    }

    public static IMobListener createMobListener(JavaPlugin core, Class<IMobListener> classIMobListener) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // Create an instance of mobListener
        Object[] object = new Object[]{core};
        IMobListener mobListener = classIMobListener.getDeclaredConstructor(JavaPlugin.class).newInstance(object);

        return (IMobListener) Proxy.newProxyInstance(
                MobListenerFactory.class.getClassLoader(),
                new Class[]{IMobListener.class},
                new MobListenerFactory(mobListener));
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Method realMethod = mobListener.getClass().getMethod(
                method.getName(),
                method.getParameterTypes());
        MobHandler audit = realMethod.getAnnotation(MobHandler.class);

        if (audit != null && !mobListener.isEnabled()) {

            // Not tested what happens
            return new Object[]{};
        }

        return method.invoke(mobListener, args);
    }

}
